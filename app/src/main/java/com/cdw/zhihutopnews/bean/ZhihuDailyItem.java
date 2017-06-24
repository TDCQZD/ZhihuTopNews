package com.cdw.zhihutopnews.bean;

import com.google.gson.annotations.SerializedName;


/**
 * 新闻条目类
 */
public class ZhihuDailyItem {

    @SerializedName("images")
    private String[] images;
    @SerializedName("type")
    private int type;
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;

    private String date;
    public boolean hasFadedIn = false;

    public void setImages(String[] images) {
        this.images = images;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getImages() {
        return images;
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }


}
