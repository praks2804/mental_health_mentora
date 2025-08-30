package com.model;

import com.google.gson.annotations.SerializedName;

public class UserProfile {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("email")
    public String email;

    // backend returns dd-mm-yyyy for display
    @SerializedName("dob")
    public String dob;

    @SerializedName("gender")
    public String gender;
}
