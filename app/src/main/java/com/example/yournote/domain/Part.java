package com.example.yournote.domain;

public class Part {
    private int id;
    private int part;
    private String partName;

    public Part(){ }

    public Part(int id, int part, String partName){
        this.id = id;
        this.part = part;
        this.partName = partName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }
}
