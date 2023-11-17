package com.example.user_store.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class RetrieveDataModel implements Serializable {
    private String ImageProduct;
    private String Name;
    private Object Price;
    @Exclude
    private String id;
    @Exclude
    private String Collection;

    public String getCollection() {
        return Collection;
    }

    public void setCollection(String collection) {
        Collection = collection;
    }

    public RetrieveDataModel() {
    }

    public String getId() {
        return id;
    }

    // setter method for our id
    public void setId(String id) {
        this.id = id;
    }


    public RetrieveDataModel(String imageProduct, String name, Double price) {
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

    public void setPrice(Double price) {
        Price = price;
    }

    public class Contract{
        public static final String COLLECTION = "CATEGORY";
        public static final String COLLECTION_phone = "phone";
        public static final String COLLECTION_electronics = "electronics";
        public static final String COLLECTION_headphones = "headphones";
        public static final String COLLECTION_hours = "hours";
        public static final String COLLECTION_clothes = "clothes";
        public static final String COLLECTION_shoes = "shoes";
    }

}
