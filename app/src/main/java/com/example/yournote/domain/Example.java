package com.example.yournote.domain;

public class Example {
    private int id;
    private String example;
    private String exampleTranslation;
    private int wordID;

    public Example(){}

    public Example(int id, String example, String exampleTranslation, int wordID){
        this.id = id;
        this.example = example;
        this.exampleTranslation = exampleTranslation;
        this.wordID = wordID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getExampleTranslation() {
        return exampleTranslation;
    }

    public void setExampleTranslation(String exampleTranslation) {
        this.exampleTranslation = exampleTranslation;
    }

    public int getWordID() {
        return wordID;
    }

    public void setWordID(int wordID) {
        this.wordID = wordID;
    }

    public String showExample(){
        return this.example+"\n"+this.exampleTranslation+"\n";
    }
}
