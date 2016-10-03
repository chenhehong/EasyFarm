package com.scau.easyfarm.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.VillageProofResource;
import com.scau.easyfarm.util.DateTimeUtil;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.FileUtil;
import com.scau.easyfarm.util.ImageUtils;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.SimpleTextWatcher;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.util.TDevice;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

public class VillageServiceProofResourPubFragment extends BaseFragment{

    public static final int REQUESTCODE_CHOOSE_VILLAGESERVICE = 2;
    public static final String FROM_IMAGEPAGE_KEY = "from_image_page";

    public static final String ACTION_TYPE = "action_type";

    @InjectView(R.id.img_resource)
    ImageView mResource;

    @InjectView(R.id.et_resource_time)
    EditText mResourceTime;

    @InjectView(R.id.et_resource_address)
    EditText mResourceAddress;

    @InjectView(R.id.et_village_type)
    EditText mVillageType;

    @InjectView(R.id.btn_village_type)
    ImageView mImvVillageType;

    @InjectView(R.id.et_description)
    EditText mResourceDescription;

    private int selectedVillageServiceTypeId = 0;

    private MenuItem mSendMenu;

    private boolean mIsKeyboardVisible;

    private String theLarge, theThumbnail;
    private File imgFile;

//  标示第一次是否是第一次进入activity，此字段用于拍照时返回需要自动退出该activity的情况
    private boolean firstStart = true;
//  指示是否拍照成功，此字段用于拍照时返回需要自动退出该activity的情况
    private boolean takePhotoSuccess = false;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && msg.obj != null) {
                // 显示图片
                mResource.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    private final OperationResponseHandler mHandler = new OperationResponseHandler() {

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args) {
            ResultBean resultBean = JsonUtils.toBean(ResultBean.class, is);
            if (resultBean != null) {
                handleResultBean(resultBean);
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            AppContext.showToast("网络出错" + arg0);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
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
        updateMenuState();
    }

    public void updateMenuState() {
        if (mSendMenu == null) {
            return;
        }
        if (mVillageType.getText().length() > 0) {
            mSendMenu.setEnabled(true);
            mSendMenu.setIcon(R.drawable.actionbar_send_icon);
        } else {
            mSendMenu.setEnabled(false);
            mSendMenu.setIcon(R.drawable.actionbar_unsend_icon);
        }
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
        VillageProofResource villageProofResource = new VillageProofResource();
        villageProofResource.setCreateDate(mResourceTime.getText().toString());
        villageProofResource.setAddress(mResourceAddress.getText().toString());
        villageProofResource.setVillageServiceId(selectedVillageServiceTypeId);
        villageProofResource.setDescription(mResourceDescription.toString());
        if (imgFile != null && imgFile.exists()) {
            villageProofResource.setImageFilePath(imgFile.getAbsolutePath());
        }

        showWaitDialog("上传佐证中");
        EasyFarmServerApi.pubVillageServiceProof(villageProofResource, mHandler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_villageservice_resource_pub, container,
                false);

        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        takePhoto();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);
        mVillageType.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                updateMenuState();
            }
        });
        mImvVillageType.setOnClickListener(this);

    }

    @Override
    public boolean onBackPressed() {
        DialogHelp.getConfirmDialog(getActivity(), "是否退出上传佐证?", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
            }
        }, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
        return true;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_village_type){
            UIHelper.showVillageServiceProofChoose(this, REQUESTCODE_CHOOSE_VILLAGESERVICE);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent imageReturnIntent) {
        Log.d("chh","拍照返回");
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode==REQUESTCODE_CHOOSE_VILLAGESERVICE){
            selectedVillageServiceTypeId = imageReturnIntent.getIntExtra(VillageServiceProofListFragment.SELECTED_VILLAGESERVICE_ID,0);
            mVillageType.setText(imageReturnIntent.getStringExtra(VillageServiceProofListFragment.SELECTED_VILLAGESERVICE_DEC));
        }
        if (requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA) {
            takePhotoSuccess = true;
            mResourceAddress.setText("广东省-广州市-天河区-华南农业大学");
            mResourceTime.setText(DateTimeUtil.getCurrentDateStr("yyyy-MM-dd HH:mm:ss"));
            new Thread() {
                @Override
                public void run() {
                    Bitmap bitmap = null;
                    // 拍摄图片
                    if (bitmap == null && !StringUtils.isEmpty(theLarge)) {
                        bitmap = ImageUtils
                                .loadImgThumbnail(theLarge, 100, 100);
                    }
                    if (bitmap != null) {// 存放照片的文件夹
                        String savePath = Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + "/EasyFarm/Camera/";
                        File savedir = new File(savePath);
                        if (!savedir.exists()) {
                            savedir.mkdirs();
                        }

                        String largeFileName = FileUtil.getFileName(theLarge);
                        String largeFilePath = savePath + largeFileName;
                        // 判断是否已存在缩略图
                        if (largeFileName.startsWith("thumb_")
                                && new File(largeFilePath).exists()) {
                            theThumbnail = largeFilePath;
                            imgFile = new File(theThumbnail);
                        } else {
                            // 生成上传的800宽度图片
                            String thumbFileName = "thumb_" + largeFileName;
                            theThumbnail = savePath + thumbFileName;
                            if (new File(theThumbnail).exists()) {
                                imgFile = new File(theThumbnail);
                            } else {
                                try {
                                    // 压缩上传的图片
                                    ImageUtils.createImageThumbnail(getActivity(),
                                            theLarge, theThumbnail, 860, 100);
                                    imgFile = new File(theThumbnail);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        // 保存动弹临时图片
                        // ((AppContext) getApplication()).setProperty(
                        // tempTweetImageKey, theThumbnail);

                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = bitmap;
                        handler.sendMessage(msg);
                    }
                };
            }.start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//      如果没有拍照成功并且不是第一次进入activity，自动退出
        if (!firstStart&&!takePhotoSuccess){
            getActivity().finish();
        }
        if (firstStart==true){
            firstStart = false;
        }
    }

    private void takePhoto() {
        // 判断是否挂载了SD卡
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/easyfarm/Camera/";
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (StringUtils.isEmpty(savePath)) {
            AppContext.showToastShort("无法保存照片，请检查SD卡是否挂载");
            return;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String fileName = "osc_" + timeStamp + ".jpg";// 照片命名
        File out = new File(savePath, fileName);
        Uri uri = Uri.fromFile(out);

        theLarge = savePath + fileName;// 该照片的绝对路径

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    @Override
    public void initData() {
    }

}
