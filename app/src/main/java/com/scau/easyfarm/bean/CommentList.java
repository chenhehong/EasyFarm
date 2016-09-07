package com.scau.easyfarm.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 评论列表实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月14日 下午3:32:39
 * 
 */
@SuppressWarnings("serial")
public class CommentList extends Entity implements ListEntity<Comment> {

//  评论的类别
    public final static int CATALOG_TWEET = 1;

    private int pageSize;
    private int allCount;
    private final List<Comment> commentlist = new ArrayList<Comment>();

    public int getPageSize() {
        return pageSize;
    }

    public int getAllCount() {
        return allCount;
    }

    public List<Comment> getCommentlist() {
        return commentlist;
    }

    @Override
    public List<Comment> getList() {
        return commentlist;
    }

    public void sortList() {
        Collections.sort(commentlist, new Comparator<Comment>() {

            @Override
            public int compare(Comment lhs, Comment rhs) {
                return lhs.getCommentDate().compareTo(rhs.getCommentDate());
            }
        });
    }
}
