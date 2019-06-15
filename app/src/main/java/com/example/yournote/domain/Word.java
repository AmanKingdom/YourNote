package com.example.yournote.domain;

import java.util.List;

public class Word {
    private int id;
    private String word;
    private String wordTranslation;
    private int chapter;    //是chapter， 不是chaper的id
    private List<Example> exampleList;

    public Word(){}

    public Word(int id, String word, String wordTranslation, int chapter, List<Example> exampleList){
        this.id = id;
        this.word = word;
        this.wordTranslation = wordTranslation;
        this.chapter = chapter;
        this.exampleList = exampleList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordTranslation() {
        return wordTranslation;
    }

    public void setWordTranslation(String wordTranslation) {
        this.wordTranslation = wordTranslation;
    }

    public int getChapter() {
        return this.chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public List<Example> getExampleList() {
        return exampleList;
    }

    public void setExampleList(List<Example> exampleList) {
        this.exampleList = exampleList;
    }

    public String showExamples(){
        StringBuilder examples = new StringBuilder();
        for(Example e:this.exampleList){
            examples.append(e.showExample());
        }
        return examples.toString();
    }
}
