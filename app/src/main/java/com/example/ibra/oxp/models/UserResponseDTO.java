package com.example.ibra.oxp.models;

public class UserResponseDTO {


    private String f_name;
    private String l_name;
    private int id;
    private String email;

    public UserResponseDTO(String f_name, String l_name, int id, String email) {
        this.f_name = f_name;
        this.l_name = l_name;
        this.id = id;
        this.email = email;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
