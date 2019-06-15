package com.example.yournote.domain;

public class Chapter {
    private int id;
    private int chapter;
    private String chapterName;
    private int partID;

    public Chapter(){}

    public Chapter(int id, int chapter, String chapterName, int partID){
        this.id = id;
        this.chapter = chapter;
        this.chapterName = chapterName;
        this.partID = partID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getPartID() {
        return partID;
    }

    public void setPartID(int partID) {
        this.partID = partID;
    }
}
