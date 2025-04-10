package com.example.hotel_booking_java.request;
//nên xóa package này để chung ở pack payload
public class UpdateUserRequest {

    private String email;
    private String phone;
    //private String[] address;
    //private List<String> address; dd
    //private List<Object> address;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
