package com.scau.easyfarm.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HuangWenwei
 *
 * @date 2014年10月10日
 */
@SuppressWarnings("serial")
//问答列表
public class TweetsList extends Entity implements ListEntity<Tweet> {
//	用户个人栏目的大于0
    public final static int CATALOG_LATEST = 0;
    public final static int CATALOG_HOT = -1;
    public final static int CATALOG_ME = 1;
	
	private int tweetCount;
	private int pagesize;
	private List<Tweet> tweetslist = new ArrayList<Tweet>();

	public int getTweetCount() {
		return tweetCount;
	}

	public void setTweetCount(int tweetCount) {
		this.tweetCount = tweetCount;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public List<Tweet> getTweetslist() {
		return tweetslist;
	}

	public void setTweetslist(List<Tweet> tweetslist) {
		this.tweetslist = tweetslist;
	}

	@Override
	public List<Tweet> getList() {
		return tweetslist;
	}

}
