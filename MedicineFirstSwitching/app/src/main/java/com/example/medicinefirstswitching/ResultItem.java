package com.example.medicinefirstswitching;

public class ResultItem {
    private String product;
    private String company;
    private String form;
    private String ingredient;
    private String effectiveness;
    private String warning;
    private int index;

    public ResultItem(String product, String company, String form, String ingredient, String effectiveness, String warning) {
        this.product = product;
        this.company = company;
        this.form = form;
        this.ingredient = ingredient;
        this.effectiveness = effectiveness;
        this.warning = warning;

    }

    public void setIndex(int index){
        this.index = index;
    }

    public int getIndex() { return index; }



    public String getProduct() {
        return product;
    }

    public String getCompany() {
        return company;
    }

    public String getForm() {
        return form;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getEffectiveness() {return effectiveness;}

    public String getWarning() {
        return warning;
    }



    @Override
    public String toString() {
        return "ResultItem{" +
                "product='" + product + '\'' +
                ", company='" + company + '\'' +
                ", form='" + form + '\'' +
                ", ingredient='" + ingredient + '\'' +
                ", effectiveness='" + effectiveness + '\'' +
                ", warning='" + warning + '\'' +
                '}';
    }
}
