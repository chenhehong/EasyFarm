package com.scau.easyfarm.api;

import android.os.Looper;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.api.remote.EasyFarmServerApi;
import com.scau.easyfarm.bean.Result;
import com.scau.easyfarm.bean.ResultBean;
import com.scau.easyfarm.util.JsonUtils;
import com.scau.easyfarm.util.TLog;

import java.io.ByteArrayInputStream;

import cz.msebera.android.httpclient.Header;

public abstract class OperationResponseHandler extends AsyncHttpResponseHandler {

	private Object[] args;

	public OperationResponseHandler(Looper looper, Object... args) {
		super(looper);
		this.args = args;
	}

	public OperationResponseHandler(Object... args) {
		this.args = args;
	}

	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		onFailure(arg0, "网络错误", args);
	}

	public abstract void onFailure(int code, String errorMessage, Object[] args);

	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		try {
//			验证是否AuthToken已经过期
			ResultBean resultBean = JsonUtils.toBean(ResultBean.class,arg2);
			Result result = resultBean.getResult();
		 	int errorCode = result.getErrorCode();
			if(!result.OK()&&(errorCode==AppContext.ACCESS_ERROR_CODE||errorCode==AppContext.ACCESS_INVALID_CODE||errorCode==AppContext.ACCESS_TIMEOUT_CODE)){
//				onFailure(result.getErrorCode(),result.getErrorMessage(),args);
				TLog.error(result.getErrorMessage()+result.getErrorCode());
				EasyFarmServerApi.getAccessToken();
				return;
			}
			onSuccess(arg0, new ByteArrayInputStream(arg2), args);
		} catch (Exception e) {
			e.printStackTrace();
			onFailure(arg0, e.getMessage(), args);
		}
	}

	public abstract void onSuccess(int code, ByteArrayInputStream is, Object[] args)
			throws Exception;

//	执行所有操作后必定执行onfinish
	@Override
	public void onFinish() {
		super.onFinish();
	}
}
