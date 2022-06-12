package com.example.medicinefirstswitching.Review;

public class ReviewItem {
    private String id;
    private String context;
    private int rate;

    public ReviewItem(String id, String context, int rate) {
        this.id = id;
        this.context = context;
        this.rate = rate;
    }

    public String getId() { return id; }
    public String getContext(){ return context; }
    public int getRate() { return rate; }

}
