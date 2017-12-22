package com.scau.easyfarm.bean;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.alibaba.fastjson.annotation.JSONField;
import com.scau.easyfarm.AppContext;
import com.scau.easyfarm.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题实体类
 *
 */
@SuppressWarnings("serial")
//用Parcelable代替Serializable实现序列化，可以减少内存的占用
public class Tweet extends Entity implements Parcelable {

    @JSONField(name = "imagePath")
    private String portrait="";
    private String author;
    private int authorid;
    private  String title;
    private String content;
    private String commentCount;
    private int readCount;
    private String createDate;
    private String imgSmall;
    private String imgBig;
    private String attach;
    private int manualCategoryID;
    private int expertPersonalID;
    private String expertName;

    private int likeCount;

    private int isLike;

    private List<User> likeUser = new ArrayList<User>();

    @JSONField(name = "resourceMetaList")
    private ArrayList<FileResource> imageFiles = new ArrayList<FileResource>();

    public Tweet() {
    }

    public Tweet(Parcel dest) {
        id = dest.readInt();
        author = dest.readString();
        authorid = dest.readInt();
        content = dest.readString();
        commentCount = dest.readString();
        createDate = dest.readString();
        imgSmall = dest.readString();
        imgBig = dest.readString();
        attach = dest.readString();
        imageFiles = dest.readArrayList(FileResource.class.getClassLoader());
        isLike = dest.readInt();
        title = dest.readString();
        readCount = dest.readInt();
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getImgSmall() {
        return imgSmall;
    }

    public void setImgSmall(String imgSmall) {
        this.imgSmall = imgSmall;
    }

    public String getImgBig() {
        return imgBig;
    }

    public void setImgBig(String imgBig) {
        this.imgBig = imgBig;
    }

    public List<User> getLikeUser() {
        return likeUser;
    }

    public void setLikeUser(List<User> likeUser) {
        this.likeUser = likeUser;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getExpertName() {
        return expertName;
    }

    public void setExpertName(String expertName) {
        this.expertName = expertName;
    }

    public int getExpertPersonalID() {
        return expertPersonalID;
    }

    public void setExpertPersonalID(int expertPersonalID) {
        this.expertPersonalID = expertPersonalID;
    }

    public int getManualCategoryID() {
        return manualCategoryID;
    }

    public void setManualCategoryID(int manualCategoryID) {
        this.manualCategoryID = manualCategoryID;
    }

    public ArrayList<FileResource> getImageFiles() {
        return imageFiles;
    }

    public void setImageFiles(ArrayList<FileResource> imageFiles) {
        this.imageFiles = imageFiles;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(author);
        dest.writeInt(authorid);
        dest.writeString(content);
        dest.writeString(commentCount);
        dest.writeString(createDate);
        dest.writeString(imgSmall);
        dest.writeString(imgBig);
        dest.writeString(attach);
        dest.writeList(imageFiles);
        dest.writeInt(isLike);
        dest.writeString(title);
        dest.writeInt(readCount);
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }

        @Override
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }
    };

    public void setLikeUsers(Context contet, TextView likeUser, boolean limit) {
        // 构造多个超链接的html, 通过选中的位置来获取用户名
        if (getLikeCount() > 0 && getLikeUser() != null
                && !getLikeUser().isEmpty()) {
            likeUser.setVisibility(View.VISIBLE);
            likeUser.setMovementMethod(LinkMovementMethod.getInstance());
            likeUser.setFocusable(false);
            likeUser.setLongClickable(false);
            likeUser.setText(addClickablePart(contet, limit), BufferType.SPANNABLE);
        } else {
            likeUser.setVisibility(View.GONE);
            likeUser.setText("");
        }
    }

    /**
     * 对每条问题的点赞人显示进行样式的处理
     * @return
     */
    private SpannableStringBuilder addClickablePart(final Context context,
                                                    boolean limit) {

        StringBuilder sbBuilder = new StringBuilder();
        int showCunt = getLikeUser().size();
        if (limit && showCunt > 4) {
            showCunt = 4;
        }

        // 如果已经点赞，始终让该用户在首位
        if (getIsLike() == 1) {

            for (int i = 0; i < getLikeUser().size(); i++) {
                if (getLikeUser().get(i).getId() == AppContext.getInstance()
                        .getLoginUid()) {
                    getLikeUser().remove(i);
                }
            }
            getLikeUser().add(0, AppContext.getInstance().getLoginUser());
        }

        for (int i = 0; i < showCunt; i++) {
            sbBuilder.append(getLikeUser().get(i).getRealName()).append("、");
        }

        String likeUsersStr = sbBuilder.substring(0, sbBuilder.lastIndexOf("、"));

        // 第一个赞图标
        // ImageSpan span = new ImageSpan(AppContext.getInstance(),
        // R.drawable.ic_unlike_small);
        SpannableString spanStr = new SpannableString("");
        // spanStr.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);
        ssb.append(likeUsersStr);

        String[] likeUsers = likeUsersStr.split("、");

        if (likeUsers.length > 0) {
            // 最后一个
            for (int i = 0; i < likeUsers.length; i++) {
                final String name = likeUsers[i];
                final int start = likeUsersStr.indexOf(name) + spanStr.length();
                final int index = i;
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        User user = getLikeUser().get(index);
                        UIHelper.showUserCenter(context, user.getId(),
                                user.getLoginName());
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        // ds.setColor(R.color.link_color); // 设置文本颜色
                        // 去掉下划线
                        ds.setUnderlineText(false);
                    }
                }, start, start + name.length(), 0);
            }
        }
        if (likeUsers.length < getLikeCount()) {
            ssb.append("等");
            int start = ssb.length();
            String more = getLikeCount() + "人";
            ssb.append(more);
            return ssb.append("觉得很赞");
        } else {
            return ssb.append("觉得很赞");
        }
    }
}
