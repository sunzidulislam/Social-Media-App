package com.example.instragram;

public class Add {
        private  String desc;
        private  String postid;
        private  String publiserId;
        private String url;

        public Add() {
        }

        public Add(String desc, String postid, String publiserId, String url) {
                this.desc = desc;
                this.postid = postid;
                this.publiserId = publiserId;
                this.url = url;
        }

        public String getDesc() {
                return desc;
        }

        public void setDesc(String desc) {
                this.desc = desc;
        }

        public String getPostid() {
                return postid;
        }

        public void setPostid(String postid) {
                this.postid = postid;
        }

        public String getPubliserId() {
                return publiserId;
        }

        public void setPubliserId(String publiserId) {
                this.publiserId = publiserId;
        }

        public String getUrl() {
                return url;
        }

        public void setUrl(String url) {
                this.url = url;
        }
}

