package com.example.medicinefirstswitching.RecyclerView;

public class Item {
    public String getName() {
        return name;
    }

    public String name;

    public String getImageSrc() {
        return imageSrc;
    }

    public String imageSrc;

    public Item(String name, String imageSrc){
        this.name = name;
        this.imageSrc = imageSrc;
    }
}
