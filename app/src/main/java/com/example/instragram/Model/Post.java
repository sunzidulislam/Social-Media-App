package com.example.instragram.Model;

public class Post {
    private String desc;
    private String postId;
    private String imgurl, profileUrl;
    private String publiserId;

    public Post() { }

    public Post(String desc, String postId, String imgurl, String profileUrl, String publiserId) {
        this.desc = desc;
        this.postId = postId;
        this.imgurl = imgurl;
        this.profileUrl = profileUrl;
        this.publiserId = publiserId;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getPubliserId() {
        return publiserId;
    }

    public void setPubliserId(String publiserId) {
        this.publiserId = publiserId;
    }
}
