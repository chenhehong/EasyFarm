package com.scau.easyfarm.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
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

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.base.BaseFragment;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.VillageProofResource;
import com.scau.easyfarm.service.LocationUtils;
import com.scau.easyfarm.service.ServerTaskUtils;
import com.scau.easyfarm.ui.ImageGalleryActivity;
import com.scau.easyfarm.ui.VideoPlayActivity;
import com.scau.easyfarm.util.DateTimeUtil;
import com.scau.easyfarm.util.DialogHelp;
import com.scau.easyfarm.util.FileUtil;
import com.scau.easyfarm.util.ImageUtils;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.SimpleTextWatcher;
import com.scau.easyfarm.util.StringUtils;
import com.scau.easyfarm.util.UIHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VillageServiceProofResourPubFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks{

    public static final String BUNDLE_PUB_PROOFRESOURCE = "bundle_pub_proofresource";

    public static final String BUNDLEKEY_PUB_PROOFMETHOD = "bundlekey_pub_proofmethod";
    public static final int TAKEPHOTO = 1;
    public static final int TAKEVIDEO = 2;
    int videoTimeLimit = 15;//视频拍摄限制的秒数

    public static final int REQUESTCODE_CHOOSE_VILLAGESERVICE = 2;

    int proofMethod = TAKEPHOTO;

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

    @InjectView(R.id.btn_description)
    ImageView mImvDescriptioon;

    private int selectedVillageServiceTypeId = 0;

    private MenuItem mSendMenu;

    private String proofFile;
    private File uploadFile;
    private int descriptionId;

    private final int RC_CAMERA_PERM = 241;
    private final int RC_LOCATION_PERM = 242;
    private final int RC_CAMERA_ALBUM_PERM = 243;

//  标示第一次是否是第一次进入activity，此字段用于拍照时返回需要自动退出该activity的情况
    private boolean firstStart = true;
//  指示是否拍照成功，此字段用于拍照时返回需要自动退出该activity的情况
    private boolean takeProofSuccess = false;

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
        public void onFailure(int code, String errorMessage, Object[] args) {
            AppContext.showToast("网络出错" + errorMessage);
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
        if (mVillageType.getText().length() > 0&&mResourceDescription.getText().length()>0) {
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
        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        VillageProofResource villageProofResource = new VillageProofResource();
        villageProofResource.setCreateDate(mResourceTime.getText().toString());
        villageProofResource.setAddress(mResourceAddress.getText().toString());
        villageProofResource.setVillageServiceId(selectedVillageServiceTypeId);
        villageProofResource.setDescription(mResourceDescription.getText().toString());
        villageProofResource.setVillageServiceDescription(mVillageType.getText().toString());
        villageProofResource.setDescriptionId(descriptionId);
        if (uploadFile != null && uploadFile.exists()) {
            villageProofResource.setUploadFilePath(uploadFile.getAbsolutePath());
        }
        ServerTaskUtils.uploadProofResource(getActivity(), villageProofResource);
        getActivity().finish();
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
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        if (bundle!=null){
            VillageProofResource villageProofResource = (VillageProofResource) bundle.getSerializable(BUNDLE_PUB_PROOFRESOURCE);
            proofMethod = bundle.getInt(BUNDLEKEY_PUB_PROOFMETHOD);
//      如果外界传入了对象
            if (villageProofResource!=null){
//              初始化参数
                String uploadFilePath = villageProofResource.getUploadFilePath();
                String suffix = uploadFilePath.substring(uploadFilePath.lastIndexOf(".") + 1);
                Bitmap bitmap=null;
                if(suffix.equalsIgnoreCase("jpg")||suffix.equalsIgnoreCase("jpeg")||suffix.equalsIgnoreCase("png")){
                    bitmap = ImageUtils.loadImgThumbnail(uploadFilePath,100,100);
                }else if(suffix.equalsIgnoreCase("mp4")||suffix.equalsIgnoreCase("3gp")){
                    InputStream is= null;
                    try {
                        is = getContext().getAssets().open("video_play.jpg");
                        bitmap = BitmapFactory.decodeStream(is);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Message msg = new Message();
                msg.what = 1;
                msg.obj = bitmap;
                handler.sendMessage(msg);
                mResourceTime.setText(villageProofResource.getCreateDate());
                mResourceAddress.setText(villageProofResource.getAddress());
                mVillageType.setText(villageProofResource.getVillageServiceDescription());
                mResourceDescription.setText(villageProofResource.getDescription());
                if (uploadFilePath!=null){
                    uploadFile = new File(uploadFilePath);
                }
                selectedVillageServiceTypeId = villageProofResource.getVillageServiceId();
//              表明已经拍照成功,防止resume方法finish
                takeProofSuccess = true;
            }else {
                useCamera();
            }
        }else{
            useCamera();
        }
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
        mResourceDescription.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                updateMenuState();
            }
        });
        mImvVillageType.setOnClickListener(this);
        mImvDescriptioon.setOnClickListener(this);

        mResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(proofMethod==TAKEPHOTO){
                    ImageGalleryActivity.show(getActivity(), proofFile, "");
                }else if(proofMethod==TAKEVIDEO){
                    Intent intent = new Intent(VillageServiceProofResourPubFragment.this.getContext(), VideoPlayActivity.class);
                    intent.putExtra(VideoPlayActivity.BUNDLEKEY_VIDEOSOURCE_TYPE, VideoPlayActivity.VIDEOTYPEFILE);
                    intent.putExtra(VideoPlayActivity.BUNDLEKEY_VIDEOSOURCE,proofFile);
                    VillageServiceProofResourPubFragment.this.getContext().startActivity(intent);
                }
            }
        });
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
        }else if (id==R.id.btn_description){
            UIHelper.chooseProofResourceDescription(this,ProofResourceDescriptionChooseFragment.REQUEST_CODE_DESCRIPTION_SELECT);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent imageReturnIntent) {
        Log.d("chh", "拍照返回");
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode==REQUESTCODE_CHOOSE_VILLAGESERVICE){
            selectedVillageServiceTypeId = imageReturnIntent.getIntExtra(VillageServiceProofChooseFragment.SELECTED_VILLAGESERVICE_ID,0);
            mVillageType.setText(imageReturnIntent.getStringExtra(VillageServiceProofChooseFragment.SELECTED_VILLAGESERVICE_DEC));
        }
        if (requestCode==ProofResourceDescriptionChooseFragment.REQUEST_CODE_DESCRIPTION_SELECT){
            String selectedDescription = imageReturnIntent.getStringExtra(ProofResourceDescriptionChooseFragment.BUNDLE_SELECT_DESCRIPTION_STR);
            descriptionId = imageReturnIntent.getIntExtra(ProofResourceDescriptionChooseFragment.BUNDLE_SELECT_DESCRIPTION_ID,0);
            if (selectedDescription.equals("其他")){
                mResourceDescription.setEnabled(true);
                mResourceDescription.setText("其他");
            }else {
                mResourceDescription.setEnabled(false);
                mResourceDescription.setText(selectedDescription);
            }
        }
        if (requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA) {
            takeProofSuccess = true;
            locate();
            mResourceTime.setText(DateTimeUtil.getCurrentDateStr("yyyy-MM-dd HH:mm:ss"));
            new Thread() {
                @Override
                public void run() {
                    Bitmap bitmap = null;
//                  界面显示的缩略图
                    if (proofMethod==TAKEPHOTO && !StringUtils.isEmpty(proofFile)) {
                        proofFile = imageReturnIntent.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0);
                        bitmap = ImageUtils.loadImgThumbnail(proofFile, 200, 200);
                        // 存放上传照片的文件夹
                        String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EasyFarm/Camera/";
                        File savedir = new File(savePath);
                        if (!savedir.exists()) {
                            savedir.mkdirs();
                        }
                        String proofFileName = FileUtil.getFileName(proofFile);
                        // 生成860的缩略图并作为上传的图片
                        String uploadFileName = "upload_" + proofFileName;
                        String uploadPhotoFile = savePath + uploadFileName;
                        try {
                            // 压缩上传的图片
                            ImageUtils.createImageThumbnail(getActivity(),
                                    proofFile, uploadPhotoFile, 860, 100);
                            uploadFile = new File(uploadPhotoFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if(proofMethod==TAKEVIDEO){
                        try {
                            InputStream is= VillageServiceProofResourPubFragment.this.getContext().getAssets().open("video_play.jpg");
                            bitmap = BitmapFactory.decodeStream(is);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        uploadFile = new File(proofFile);
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                };
            }.start();
        }
    }

    /**
     * 该方法用于实现定位
     */
    @AfterPermissionGranted(RC_LOCATION_PERM)
    public void locate(){
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            //异步处理地址
            Handler mhandler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0: {
                            //显示地址
                            mResourceAddress.setText((msg.obj).toString());
                            break;
                        }
                    }
                }
            };
            LocationUtils locationUtils = new LocationUtils(mhandler);
            locationUtils.start();
        } else {
            EasyPermissions.requestPermissions(this, "华南农技通请求网络定位和GPS定位权限", RC_LOCATION_PERM, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
//      如果没有拍照成功并且不是第一次进入activity，自动退出
        if (!firstStart&&!takeProofSuccess){
            getActivity().finish();
        }
        if (firstStart==true){
            firstStart = false;
        }
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    private void useCamera() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this.getContext(), perms)) {
            try{
                toCamera();
            }catch (Exception e){
                AppContext.showToast("华南农技通未能获取拍照和读取文件权限，请授权后操作！");
            }
        } else {
            EasyPermissions.requestPermissions(this, "请求获取拍照和读取文件权限", RC_CAMERA_PERM, perms);
        }
    }

    private  void toCamera(){
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

        if(proofMethod==TAKEPHOTO){
            String fileName = "easyfarm_" + timeStamp + ".jpg";// 照片命名
            File out = new File(savePath, fileName);
            Uri uri = Uri.fromFile(out);
            proofFile = savePath + fileName;// 该照片的绝对路径
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//            startActivityForResult(intent,
//                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
            handleAddFiles();
        }else {
            String fileName = "easyfarm_" + timeStamp + ".mp4";// 照片命名
            File out = new File(savePath, fileName);
            Uri uri = Uri.fromFile(out);
            proofFile = savePath + fileName;// 该照片的绝对路径
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,videoTimeLimit);
            startActivityForResult(intent,
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
        }

    }

    @AfterPermissionGranted(RC_CAMERA_ALBUM_PERM)
    private void handleAddFiles(){
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            MultiImageSelector.create(this.getContext())
                    .showCamera(true).count(1).single().start(this, ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
        } else {
            EasyPermissions.requestPermissions(this, "请求获取拍照和读取相册的权限", RC_CAMERA_ALBUM_PERM, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

//  检查权限是否被永久禁用的接口
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
        try{
            toCamera();
        }catch (Exception e){
            AppContext.showToast("华南农技通未能获取拍照和读取文件权限，请授权后操作！");
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        String tip = ">在设置-应用-华南农技通权限中允许拍摄照片，以正常使用佐证功能";
        if (perms.get(0).equals(Manifest.permission.CAMERA)) {
            tip = ">在设置-应用-华南农技通权限中允许拍摄照片，以正常使用佐证功能";
        }
        if (perms.get(0).equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            tip = ">在设置-应用-华南农技通权限中允许读取文件，以正常使用佐证功能";
        }
        if (perms.get(0).equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            tip = ">在设置-应用-华南农技通权限中允许定位，以正常使用佐证功能";
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
