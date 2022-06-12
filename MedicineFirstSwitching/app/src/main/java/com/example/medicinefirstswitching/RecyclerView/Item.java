package com.example.medicinefirstswitching.RecyclerView;

public class Item {
    public String getName() {
        return name;
    }

    public String name;

    public int getImageSrc() {
        return imageSrc;
    }

    public int imageSrc;

    public Item(String name, int imageSrc){
        this.name = name;
        this.imageSrc = imageSrc;
    }
}
