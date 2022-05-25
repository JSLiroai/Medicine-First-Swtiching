package com.example.medicinefirstswitching.Searching;

public class SearchItem {
    private String item;
    private String explain;

    public SearchItem(String item, String explain) {
        this.item = item;
        this.explain = explain;
    }

    public String getItem() {
        return item;
    }
    public String getExplain() { return explain; }
}
