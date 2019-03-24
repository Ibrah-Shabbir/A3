package com.example.ibra.oxp.models;

import java.io.Serializable;

public class MyComment extends MyPost {
    private int Id;
    private String Name;
    private String Description;
    private int Likes;
    private boolean isLoading = false;
    MyPost post=new MyPost();


    public MyComment(MyComment p) {
        this.Id=p.Id;
        this.Name = p.Name;
        this.Description = p.Description;
    }
    public MyComment(String name, String description) {
        this.Name = name;
        this.Description = description;
    }
    public MyComment(int Id,String description) {
      //  this.Name = name;
        int id=post.getId();
        id=Id;
        this.Description = description;
    }
    public MyComment(int id, String name, String description) {
        this.Id = id;
        this.Name = name;
        this.Description = description;
    }

    public MyComment() {
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

