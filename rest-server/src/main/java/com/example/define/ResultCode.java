package com.example.define;

public enum ResultCode {
    SUCCESS("00000", "Success"),
    NOT_SUPPORTED_METHOD("90001", "Not Supported Method"),
    INVALID_CLIENT_INFO("90002", "Required request header 'X-Client-Id' for method parameter type String is not present"),
    INVALID_CREDENTIALS("90003", "Invalid Credentials"),
    EXPIRED_TOKEN("90004", "Expired Token"),
    INVALID_TOKEN("90005", "Invalid Token"),
    SERVER_ERROR("99999", "Unknown Server Error");

    public final String code;
    public final String message;
    ResultCode(String code, String message){
        this.code = code;
        this.message = message;
    }
}
