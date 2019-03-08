package com.example.ibra.oxp.models;

public class Category {
    private String Name;
    private String Description;

    Category()
    { }

    public Category(  String Name,String Description)
    {
        this.Name=Name;
        this.Description=Description;
    }


    public String get_name() { return Name;}
    public String get_description() { return Description;}
    public void set_name(String Name) {this.Name=Name; }
    public void set_description(String Description) { this.Description=Description;}
}
