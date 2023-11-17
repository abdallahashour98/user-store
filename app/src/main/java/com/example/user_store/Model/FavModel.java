package com.example.user_store.Model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class FavModel  implements Serializable {
    private boolean fav;
    private String ImageProduct;
    private String Name;
    private Object Price;
    @Exclude
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public FavModel() {
    }

    public FavModel(boolean fav, String imageProduct, String name, Object price) {
        this.fav = fav;
        ImageProduct = imageProduct;
        Name = name;
        Price = price;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
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
