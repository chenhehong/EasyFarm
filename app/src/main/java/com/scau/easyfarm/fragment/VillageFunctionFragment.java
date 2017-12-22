package com.scau.easyfarm.fragment;

import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.R;
import com.scau.easyfarm.bean.Module;
import com.scau.easyfarm.bean.SimpleBackPage;
import com.scau.easyfarm.bean.User;

/**
 * Created by ChenHehong on 2016/6/11.
 */
public class VillageFunctionFragment extends BaseFunctionFragment{

    @Override
    public void initModuleList() {
        User u = AppContext.getInstance().getLoginUser();
        if (Module.existModule(u.getModuleList(), Module.MODULE_SERVICE_APPLY)){
            Module serviceApply = new Module(SimpleBackPage.VILLAGE_SERVICE_APPLY,"服务申请",R.drawable.func_village_service_apply_icon,null);
            moduleList.add(serviceApply);
        }
        if (Module.existModule(u.getModuleList(), Module.MODULE_SERVICE_PROOF)){
            Module serviceProof = new Module(SimpleBackPage.VILLAGE_SERVICE_PROOF,"服务佐证",R.drawable.func_village_service_proof_icon,null);
            moduleList.add(serviceProof);
        }
        if (Module.existModule(u.getModuleList(), Module.MODULE_SERVICE_VERIFY)){
            Module serviceVerify = new Module(SimpleBackPage.VILLAGE_SERVICE_VERIFY_VIEWPAGER,"服务审批",R.drawable.func_village_service_performance_icon,null);
            moduleList.add(serviceVerify);
        }
        if (Module.existModule(u.getModuleList(), Module.MODULE_SERVICE_STATISTICS_EXPERT)){
            Module serviceStatisticsExpert = new Module(SimpleBackPage.MYSERVICE_MONTH_STATISTICS,"服务统计",R.drawable.func_agriculture_knowledge_base_icon,null);
            moduleList.add(serviceStatisticsExpert);
        }
        if (Module.existModule(u.getModuleList(), Module.MODULE_SERVICE_STATISTICS_VERIFIER)){
            Module serviceStatisticsVerifier = new Module(SimpleBackPage.SERVICE_MONTH_STATISTICS,"服务统计",R.drawable.func_agriculture_knowledge_base_icon,null);
            moduleList.add(serviceStatisticsVerifier);
        }
    }

}
