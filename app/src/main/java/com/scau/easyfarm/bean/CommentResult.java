package com.scau.easyfarm.bean;


import com.alibaba.fastjson.annotation.JSONField;

/**
 * 操作结果实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月14日 下午2:59:27
 * 
 */
@SuppressWarnings("serial")
public class CommentResult extends Base {

    private Result result;

    private Notice notice;

    @JSONField(name = "obj")
    private Comment comment;

    public Result getResult() {
	return result;
    }

    public void setResult(Result result) {
	this.result = result;
    }

    public Notice getNotice() {
	return notice;
    }

    public void setNotice(Notice notice) {
	this.notice = notice;
    }

    public Comment getComment() {
	return comment;
    }

    public void setComment(Comment comment) {
	this.comment = comment;
    }

//    public MessageDetail getMessage() {
//        //现在pub_message接口返回的是comment对象。所以要转成message
//        message = new MessageDetail();
//        if(comment!=null) {
//            message.setId(comment.getId());
//            message.setPortrait(comment.getPortrait());
//            message.setAuthor(comment.getAuthor());
//            message.setAuthorId(comment.getId());
//            message.setContent(comment.getContent());
//            message.setCreateDate(comment.getCreateDate());
//        }
//        return message;
//    }
//
//    public void setMessage(MessageDetail message) {
//        this.message = message;
//    }
}
