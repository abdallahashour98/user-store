package com.example.user_store.Model;

public class ProductDetailsModel {
    private String ImageProduct;
    private String Name;
    private Object Price;
    private String collection;

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public ProductDetailsModel() {
    }

    public ProductDetailsModel(String imageProduct, String name, Object price) {
        ImageProduct = imageProduct;
        Name = name;
        Price = price;
    }

    public String getImageProduct() {
        return ImageProduct;
    }

    public void setImageProduct(String imageProduct) {
        ImageProduct = imageProduct;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Object getPrice() {
        return Price;
    }

    public void setPrice(Object price) {
        Price = price;
    }
}
