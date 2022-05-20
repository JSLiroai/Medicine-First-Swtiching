package com.example.medicinefirstswitching.Searching;

public class RecentItem {
    private String item;
    private String date;

    public RecentItem(String item, String date) {
        this.item = item;
        this.date = date;
    }

    public String getItem() {
        return item;
    }
    public String getDate() { return date; }
}
