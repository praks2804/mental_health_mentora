package com.request;

public class UpdateProfileRequest {
    public int id;
    public String name;
    public String dob;     // dd-mm-yyyy
    public String gender;

    public UpdateProfileRequest(int id, String name, String dob, String gender) {
        this.id = id; this.name = name; this.dob = dob; this.gender = gender;
    }
}
