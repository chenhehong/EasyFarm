package com.scau.easyfarm.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import com.scau.easyfarm.R;
import com.scau.easyfarm.api.OperationResponseHandler;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.bean.Result;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.bean.Tweet;
import com.scau.easyfarm.bean.VillageProofResource;
import com.scau.easyfarm.bean.VillageService;
import com.scau.easyfarm.fragment.VillageServiceProofResourPubFragment;
import com.scau.easyfarm.ui.SimpleBackActivity;
import com.scau.easyfarm.util.JsonUtils;

import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * Created by ChenHehong on 2016/10/6.
 */
public class ProofResourceService extends BaseService{

    private static final String MYKEY = "proofResource_";
    public static final String BUNDLE_PUB_RESOURCE = "BUNDLE_PUB_RESOURCE";

    class PublicResourceResponseHandler extends OperationResponseHandler {

        public PublicResourceResponseHandler(Looper looper, Object... args) {
            super(looper, args);
        }

        @Override
        public void onSuccess(int code, ByteArrayInputStream is, Object[] args)
                throws Exception {
            VillageProofResource villageResource = (VillageProofResource) args[0];
            final int id = villageResource.getId();
            Result res = JsonUtils.toBean(ResultBean.class, is).getResult();
            if (res.OK()) {
                notifySimpleNotifycation(id,"上传佐证成功","上传佐证成功","上传佐证成功",false, true, new Intent(),false,false);
//				new Handler().postDelayed(new Runnable() {
//				    @Override
//				    public void run() {
//						cancellNotification(id);
//				    }
//				}, 3000);
                removePenddingTask(MYKEY + id);
                if (villageResource.getImageFilePath() != null) {
                    File imgFile = new File(villageResource.getImageFilePath());
                    if (imgFile.exists()) {
                        imgFile.delete();
                    }
                }
            } else {
                onFailure(res.getErrorCode(), res.getErrorMessage(), args);
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            VillageProofResource villageResource = (VillageProofResource) args[0];
            int id = villageResource.getId();
            Intent intent = new Intent(ProofResourceService.this, SimpleBackActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(VillageServiceProofResourPubFragment.BUNDLE_PUB_PROOFRESOURCE, villageResource);
            intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, bundle);
            intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.VILLAGE_SERVICE_PROOF_RESOURCE_ADD.getValue());
            notifySimpleNotifycation(id, "上传佐证失败", "上传佐证失败:"+errorMessage,"点击进入重新上传界面",true, true, intent,false,false);
            removePenddingTask(MYKEY + id);
        }

        @Override
        public void onFinish() {
            tryToStopServie();
        }
    };

    @Override
    public void onMyHandleIntent(Intent intent) {
        VillageProofResource villageResource = (VillageProofResource) intent.getExtras().getSerializable(BUNDLE_PUB_RESOURCE);
        villageResource.setId((int) System.currentTimeMillis());
        int id = villageResource.getId();
        addPenddingTask(MYKEY + id);
        notifySimpleNotifycation(id, "上传佐证材料中","上传佐证材料中","上传佐证材料中", true, false,new Intent(),false,false);
        EasyFarmServerApi.pubVillageServiceProof(villageResource, new PublicResourceResponseHandler(getMainLooper(),villageResource));
    }
}
