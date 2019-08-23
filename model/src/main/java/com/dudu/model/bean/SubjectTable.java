package com.dudu.model.bean;

public class SubjectTable {

    /**
     * id : 21
     * title : 彩虹鱼系列
     * guide : 有着闪亮的七彩鳞片的彩虹鱼是大海中最美丽的鱼。
     * picture : s20.png
     * num_stories : 6
     * subject_id : 21
     * type : subject
     */

    private int id;
    private String title;
    private String guide;
    private String picture;
    private int num_stories;
    private int subject_id;
    private String type;

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

    public int getNum_stories() {
        return num_stories;
    }

    public void setNum_stories(int num_stories) {
        this.num_stories = num_stories;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
