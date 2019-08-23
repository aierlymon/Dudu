package com.dudu.model.bean;

import java.util.List;

public class StoryTable{
    /**
     * id : 1631
     * title : 大灰狼才应该小心
     * guide : 故事最后大灰狼都没有好下场。
     * tags : [{"id":2,"name":"3岁+"},{"id":28,"name":"睡前故事"},{"id":35,"name":"动物故事"},{"id":48,"name":"智慧"},{"id":59,"name":"思考"},{"id":74,"name":"绘本故事"}]
     * picture : 1603.png
     * audio : 1603.mp3
     * like_num : 160
     */

    private int id;
    private String title;
    private String guide;
    private String picture;
    private String audio;
    private int like_num;
    private List<TagsBean> tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class TagsBean {
        /**
         * id : 2
         * name : 3岁+
         */

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
