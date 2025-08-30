package com.responses;

public class LoginResponse {
    private boolean status;
    private String message;
    private User data;     // response data object

    public boolean isStatus() { return status; }
    public String getMessage() { return message; }
    public User getData() { return data; }

    public class User {
        private String id;
        private String name;
        private String email;

        public String getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
    }
}
