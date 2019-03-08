package com.example.ibra.oxp.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.zip.DataFormatException;

public class MyProduct implements Serializable {
    public String DateToStr;
    private int Id;
    private String Name;
    private String Description;
    private String Price;
    private String Quantity;
    private String Image;
    private String Category;
    private boolean isLoading = false;


    public MyProduct(MyProduct p) {
        this.Name = p.Name;
        this.Description = p.Description;
        this.Price = p.Price;
        this.Quantity = p.Quantity;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date CurrentDate = new Date();
        DateToStr = dateFormat.format(CurrentDate);
        System.out.println(DateToStr);
    }
    public MyProduct( String name, String description, String price, String quantity, String Image,String category) {
        this.Name = name;
        this.Description = description;
        this.Price = price;
        this.Quantity = quantity;
        this.Image = Image;
        this.Category=category;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date CurrentDate = new Date();
        DateToStr = dateFormat.format(CurrentDate);
        System.out.println(DateToStr);
    }
    public MyProduct(int id, String name, String description, String price, String quantity, String Image,String category) {
        this.Id = id;
        this.Name = name;
        this.Description = description;
        this.Price = price;
        this.Quantity = quantity;
        this.Image = Image;
        this.Category=category;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date CurrentDate = new Date();
        DateToStr = dateFormat.format(CurrentDate);
        System.out.println(DateToStr);
    }

    public MyProduct() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date CurrentDate = new Date();
        DateToStr = dateFormat.format(CurrentDate);
        System.out.println(DateToStr);
    }

    public String getDateToStr() {
        return DateToStr;
    }

    public void setDateToStr(String dateToStr) {
        DateToStr = dateToStr;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) { Category = category; }

}
