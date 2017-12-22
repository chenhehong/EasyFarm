package com.scau.easyfarm.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.FileResource;
import com.scau.easyfarm.bean.ManualCategory;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.Tweet;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.ImageUtils;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.SimpleTextWatcher;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class TweetPubFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks{

    public static final String BUNDLEKEY_MANUAL_NAME = "bundlekey_manual_name";
    public static final String BUNDLEKEY_MANUAL_ID = "bundlekey_manual_id";
    public static final String BUNDLEKEY_TITLE = "bundlekey_title";
    private static final int MAX_TEXT_LENGTH = 500;

    @InjectView(R.id.ib_picture)
    ImageButton mIbPicture;

    @InjectView(R.id.tv_clear)
    TextView mTvClear;

    @InjectView(R.id.et_content)
    EditText mEtInput;

    @InjectView(R.id.et_tweet_type)
    EditText mEtType;

    @InjectView(R.id.btn_tweet_type)
    ImageView mImvType;

    @InjectView(R.id.et_tweet_choose_expert)
    EditText mEtChooseExpert;

    @InjectView(R.id.btn_tweet_choose_expert)
    ImageView mImvChooseExpert;

    @InjectView(R.id.et_tweet_title)
    EditText mEtTitle;

    @InjectView(R.id.rl_img1) View mlyImage1;
    @InjectView(R.id.rl_img2) View mlyImage2;
    @InjectView(R.id.rl_img3) View mlyImage3;
    @InjectView(R.id.rl_img4) View mlyImage4;
    @InjectView(R.id.iv_img1) ImageView mIvImage1;
    @InjectView(R.id.iv_img2) ImageView mIvImage2;
    @InjectView(R.id.iv_img3) ImageView mIvImage3;
    @InjectView(R.id.iv_img4) ImageView mIvImage4;
    @InjectView(R.id.iv_clear_img1) ImageView mIvClearImage1;
    @InjectView(R.id.iv_clear_img2) ImageView mIvClearImage2;
    @InjectView(R.id.iv_clear_img3) ImageView mIvClearImage3;
    @InjectView(R.id.iv_clear_img4) ImageView mIvClearImage4;
    private ArrayList<View> mlyImageList;
    private ArrayList<ImageView> mIvImageList;
    private ArrayList<ImageView> mIvClearImageList;
    private ArrayList<String> imagePathList = new ArrayList<String>();
    private ArrayList<Bitmap> imageBitmapList = new ArrayList<Bitmap>();

    private String selectedTweetTypeName = "";
    private int selectedTweetTypeId = 0;
    private String selectedExpertName = "";
    private int selectedExpertId = 0;

    private MenuItem mSendMenu;

    private boolean mIsKeyboardVisible;

    public static final int MAXPICTURENUM = 4;

    private final int RC_CAMERA_ALBUM_PERM = 91;

    private final OperationResponseHandler mHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
            ResultBean resultBean = JsonUtils.toBean(ResultBean.class, is);
            if (resultBean != null) {
                handleResultBean(resultBean);
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            hideWaitDialog();
            AppContext.showToast(errorMessage + code);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }
    };

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                refreshImages();
            }
        }
    };

    private void handleResultBean(ResultBean resultBean){
        if (resultBean.getResult().OK()){
            hideWaitDialog();
            AppContext.showToastShort("发布成功！");
            getActivity().finish();
        }else {
            hideWaitDialog();
            AppContext.showToast(resultBean.getResult().getErrorMessage());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      设置显示输入法区域（当用户进入窗口时），并且当输入法显示时，允许窗口重新计算尺寸，使内容不被输入法所覆盖
        int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        getActivity().getWindow().setSoftInputMode(mode);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.submit_menu,menu);
        mSendMenu = menu.findItem(R.id.public_menu_send);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.public_menu_send:
                handleSubmit();
                break;
            default:
                break;
        }
        return true;
    }

    private void handleSubmit() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_network_error);
            return;
        }
        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        String content = mEtInput.getText().toString().trim();
        String title = mEtTitle.getText().toString().trim();

        if (TextUtils.isEmpty(title)){
            mEtTitle.requestFocus();
            AppContext.showToastShort("标题不能为空");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            mEtInput.requestFocus();
            AppContext.showToastShort(R.string.tip_content_empty);
            return;
        }
        if (content.length() > MAX_TEXT_LENGTH) {
            AppContext.showToastShort(R.string.tip_content_too_long);
            return;
        }
        if (TextUtils.isEmpty(mEtType.getText().toString())){
            mEtType.requestFocus();
            AppContext.showToastShort("未选择问题类型！");
            return;
        }
        if (TextUtils.isEmpty(mEtChooseExpert.getText().toString())){
            mEtChooseExpert.requestFocus();
            AppContext.showToastShort("未选择提问的专家！");
            return;
        }

        Tweet tweet = new Tweet();
        tweet.setAuthorid(AppContext.getInstance().getLoginUid());
        tweet.setContent(content);
        tweet.setTitle(title);
        tweet.setExpertPersonalID(selectedExpertId);
        tweet.setExpertName(selectedExpertName);
        tweet.setManualCategoryID(selectedTweetTypeId);
        if (imagePathList!=null&&imagePathList.size()>0){
            ArrayList<FileResource> fileResourceArrayList = new ArrayList<FileResource>();
            for (int i=0;i<imagePathList.size();i++){
                FileResource fileResource = new FileResource();
                fileResource.setPath(imagePathList.get(i));
                fileResourceArrayList.add(fileResource);
            }
            tweet.setImageFiles(fileResourceArrayList);
        }
        if (mIsKeyboardVisible) {
            TDevice.hideSoftKeyboard(getActivity().getCurrentFocus());
        }
        showWaitDialog("发布问题中");
        EasyFarmServerApi.pubTweet(tweet, mHandler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_pub, container,
                false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle!=null){
            selectedTweetTypeId = bundle.getInt(BUNDLEKEY_MANUAL_ID);
            selectedTweetTypeName = bundle.getString(BUNDLEKEY_MANUAL_NAME,"");
            String title = bundle.getString(BUNDLEKEY_TITLE,"");
            mEtType.setText(selectedTweetTypeName);
            if (title.length()>0){
                mEtTitle.setText(title);
            }
        }
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);
        mIbPicture.setOnClickListener(this);
        mTvClear.setOnClickListener(this);
        mTvClear.setText(String.valueOf(MAX_TEXT_LENGTH));
        mImvType.setOnClickListener(this);
        mImvChooseExpert.setOnClickListener(this);
        mlyImageList = new ArrayList<View>(){{add(mlyImage1);add(mlyImage2);add(mlyImage3);add(mlyImage4);}} ;
        mIvImageList = new ArrayList<ImageView>(){{add(mIvImage1);add(mIvImage2);add(mIvImage3);add(mIvImage4);}};
        mIvClearImageList = new ArrayList<ImageView>(){{add(mIvClearImage1);add(mIvClearImage2);add(mIvClearImage3);add(mIvClearImage4);}};
        for (int i=0;i<mIvClearImageList.size();i++){
            mIvClearImageList.get(i).setOnClickListener(this);
        }

        mEtInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mTvClear.setText((MAX_TEXT_LENGTH - s.length()) + "");
            }
        });
        // 获取保存的tweet草稿
        mEtInput.setText(AppContext.getTweetDraftContent());
        mEtInput.setSelection(mEtInput.getText().toString().length());
        mEtTitle.setText(AppContext.getTweetDraftTitle());

        mEtInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mTvClear.setText((MAX_TEXT_LENGTH - s.length()) + "");
            }
        });

    }

    @Override
    public boolean onBackPressed() {
        final String tweet_content = mEtInput.getText().toString();
        final String tweet_title = mEtTitle.getText().toString();
        DialogHelp.getConfirmDialog(getActivity(), "是否退出问题发布界面?", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AppContext.setTweetDraft(tweet_content,tweet_title);
                getActivity().finish();
            }
        }, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).show();
        return true;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.ib_picture) {
            handleSelectPicture();
        }else if (id == R.id.tv_clear) {
            handleClearWords();
        }else if(id==R.id.btn_tweet_type){
            handlerSelectType();
        }else if(id==R.id.btn_tweet_choose_expert){
            handleSelectExpert();
        }else if (mIvClearImageList.contains(v)){
            int i = mIvClearImageList.indexOf(v);
            imagePathList.remove(i);
            imageBitmapList.remove(i);
            refreshImages();
        }
    }

    public void handlerSelectType(){
        ManualCategory industryManualCategory = new ManualCategory(0,0,false,"全部类型/",ManualCategory.EXPERT,"");
        UIHelper.showTweetTypeChoose(this, industryManualCategory, TweetChooseManualCategoryFragment.MANUAL_COTEGORY_LIST_REQUEST_CODE);
    }

    public void handleSelectExpert(){
        if (mEtType.getText().toString().length()==0){
            AppContext.showToastShort("请选择问答所属类别后再选择专家");
            return;
        }
        UIHelper.showTweetExpertChoose(this, selectedTweetTypeId, TweetExpertChooseFragment.TWEET_EXPERT_CHOOSE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent returnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;
//      如果是选择at专家的
        if (requestCode == TweetChooseManualCategoryFragment.MANUAL_COTEGORY_LIST_REQUEST_CODE) {
            selectedTweetTypeName = returnIntent.getStringExtra(TweetChooseManualCategoryFragment.SELECTED_MANUAL_COTEGORY_NAME);
            selectedTweetTypeId = returnIntent.getIntExtra(TweetChooseManualCategoryFragment.SELECTED_MANUAL_COTEGORY_ID, 0);
            mEtType.setText(selectedTweetTypeName);
            selectedExpertId=0;
            selectedExpertName="";
            mEtChooseExpert.setText("");
            return;
        }else if (requestCode==TweetExpertChooseFragment.TWEET_EXPERT_CHOOSE_REQUEST_CODE){
            selectedExpertName = returnIntent.getStringExtra(TweetExpertChooseFragment.SELECT_TWEET_EXPERT_NAME);
            selectedExpertId = returnIntent.getIntExtra(TweetExpertChooseFragment.SELECT_TWEET_EXPERT_ID, 0);
            mEtChooseExpert.setText(selectedExpertName);
            return;
        }else if (requestCode==ImageUtils.REQUEST_CODE_MULTISELECT_PICTURE){
            imagePathList =  returnIntent.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            new Thread() {
                @Override
                public void run() {
                    //  压缩成bitmap形式的缩略图
                    Bitmap bitmap = null;
                    imageBitmapList.clear();
                    for (int i=0;i<imagePathList.size();i++){
                        bitmap = ImageUtils.loadImgThumbnail(imagePathList.get(i),100,100);
                        imageBitmapList.add(bitmap);
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }.start();
        }
    }

    public void refreshImages(){
//      先隐藏掉所有的图片
        for (int i=0;i<mlyImageList.size();i++){
            mlyImageList.get(i).setVisibility(View.GONE);
        }
//      重新加载图片
        for (int i=0;i<imageBitmapList.size();i++){
            mIvImageList.get(i).setImageBitmap(imageBitmapList.get(i));
            mlyImageList.get(i).setVisibility(View.VISIBLE);
        }
    }

    private void handleClearWords() {
        if (TextUtils.isEmpty(mEtInput.getText().toString()))
            return;
        DialogHelp.getConfirmDialog(getActivity(), "是否清空内容?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mEtInput.getText().clear();
                if (mIsKeyboardVisible) {
                    TDevice.showSoftKeyboard(mEtInput);
                }
            }
        }).show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @AfterPermissionGranted(RC_CAMERA_ALBUM_PERM)
    private void handleSelectPicture() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)) {
             MultiImageSelector.create(this.getContext())
                    .showCamera(true).count(MAXPICTURENUM).multi().origin(imagePathList).start(this, ImageUtils.REQUEST_CODE_MULTISELECT_PICTURE);
        } else {
            EasyPermissions.requestPermissions(this, "请求获取拍照和读取相册的权限", RC_CAMERA_ALBUM_PERM, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void initData() {
    }


    @Override
    public boolean shouldShowRequestPermissionRationale(String permission) {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        String tip = ">在设置-应用-华南农技通权限中允许拍摄照片，以正常使用添加附件功能";
        if (perms.get(0).equals(Manifest.permission.CAMERA)) {
            tip = ">在设置-应用-华南农技通权限中允许拍摄照片，以正常使用添加附件功能";
        }
        if (perms.get(0).equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            tip = ">在设置-应用-华南农技通权限中允许读取文件，以正常使用添加附件功能";
        }
        // 权限被拒绝了
        DialogHelp.getConfirmDialog(getActivity(),
                "权限申请",
                tip,
                "去设置",
                "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                    }
                },
                null).show();
    }

}


