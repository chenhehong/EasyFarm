package com.scau.easyfarm.bean;

import java.io.Serializable;

/**
 * 更新实体类
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年11月10日 下午12:56:27
 * 
 */
@SuppressWarnings("serial")
public class Update extends Entity {

	private AndroidBean androidBean;

	public AndroidBean getAndroidBean() {
		return androidBean;
	}

	public void setAndroidBean(AndroidBean androidBean) {
		this.androidBean = androidBean;
	}

	public class AndroidBean implements Serializable {
		private int versionCode;
		private String versionName;
		private String downloadUrl;
		private String updateLog;

		public int getVersionCode() {
			return versionCode;
		}

		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}

		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}

		public String getDownloadUrl() {
			return downloadUrl;
		}

		public void setDownloadUrl(String downloadUrl) {
			this.downloadUrl = downloadUrl;
		}

		public String getUpdateLog() {
			return updateLog;
		}

		public void setUpdateLog(String updateLog) {
			this.updateLog = updateLog;
		}

	}
}
