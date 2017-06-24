package com.cdw.zhihutopnews.bean;

/**
 * 小说
 * 章节类
 */

public class Chapter {
    String url, name;

    public Chapter(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
