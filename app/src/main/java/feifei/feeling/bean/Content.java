package feifei.feeling.bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class Content extends BmobObject implements Parcelable {

    private static final long serialVersionUID = -6280656428527540320L;

    private User         author;

    private String       title;
    private String       content;
    private BmobFile     Contentfigureurl;
    private int          love;
    private int          hate;
    private int          share;
    private int          comment;
    private boolean      isPass;
    private boolean      myFav;//收藏
    private boolean      myLove;//赞
    private BmobRelation relation;

    public BmobRelation getRelation() {
        return relation;
    }

    public void setRelation(BmobRelation relation) {
        this.relation = relation;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getContentfigureurl() {
        return Contentfigureurl;
    }

    public void setContentfigureurl(BmobFile contentfigureurl) {
        Contentfigureurl = contentfigureurl;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }

    public int getHate() {
        return hate;
    }

    public void setHate(int hate) {
        this.hate = hate;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean isPass) {
        this.isPass = isPass;
    }

    public boolean getMyFav() {
        return myFav;
    }

    public void setMyFav(boolean myFav) {
        this.myFav = myFav;
    }

    public boolean getMyLove() {
        return myLove;
    }

    public void setMyLove(boolean myLove) {
        this.myLove = myLove;
    }

    @Override
    public String toString() {
        return "QiangYu [author=" + author + ", content=" + content
                + ", Contentfigureurl=" + Contentfigureurl + ", love=" + love
                + ", hate=" + hate + ", share=" + share + ", comment="
                + comment + ", isPass=" + isPass + ", myFav=" + myFav
                + ", myLove=" + myLove + ", relation=" + relation + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.author);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeSerializable(this.Contentfigureurl);
        dest.writeInt(this.love);
        dest.writeInt(this.hate);
        dest.writeInt(this.share);
        dest.writeInt(this.comment);
        dest.writeByte(this.isPass ? (byte) 1 : (byte) 0);
        dest.writeByte(this.myFav ? (byte) 1 : (byte) 0);
        dest.writeByte(this.myLove ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.relation);
    }

    public Content() {
    }

    protected Content(Parcel in) {
        this.author = (User) in.readSerializable();
        this.title = in.readString();
        this.content = in.readString();
        this.Contentfigureurl = (BmobFile) in.readSerializable();
        this.love = in.readInt();
        this.hate = in.readInt();
        this.share = in.readInt();
        this.comment = in.readInt();
        this.isPass = in.readByte() != 0;
        this.myFav = in.readByte() != 0;
        this.myLove = in.readByte() != 0;
        this.relation = (BmobRelation) in.readSerializable();
    }

    public static final Parcelable.Creator<Content> CREATOR = new Parcelable.Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel source) {
            return new Content(source);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };
}
