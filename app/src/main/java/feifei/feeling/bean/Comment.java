package feifei.feeling.bean;

import cn.bmob.v3.BmobObject;

//一条评论对应一个用户
public class Comment extends BmobObject {

    private User   user;
    private String commentContent;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

}
