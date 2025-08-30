package com.responses;

public class SignupResponse {
    private boolean status;
    private String message;
    private Data data;  // <-- add this

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    // Nested class for user data
    public static class Data {
        private int id;
        private String name;
        private String email;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}
