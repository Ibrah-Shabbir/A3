package com.example.ibra.oxp.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.zip.DataFormatException;

public class MyService implements Serializable {
    public String DateToStr;
    private int Id;
    private String Name;
    private String Description;
    private boolean isLoading = false;


    public MyService(MyService p) {
        this.Id=p.Id;
        this.Name = p.Name;
        this.Description = p.Description;
    }
    public MyService(String name, String description) {
        this.Name = name;
        this.Description = description;
    }
    public MyService(int id, String name, String description) {
        this.Id = id;
        this.Name = name;
        this.Description = description;
    }

    public MyService() {
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

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
