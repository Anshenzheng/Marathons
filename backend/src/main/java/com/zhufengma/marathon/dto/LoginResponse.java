package com.zhufengma.marathon.dto;

public class LoginResponse {
    private String token;
    private String username;
    private String userType;
    private Long userId;
    private String realName;

    public LoginResponse() {}

    public LoginResponse(String token, String username, String userType, Long userId, String realName) {
        this.token = token;
        this.username = username;
        this.userType = userType;
        this.userId = userId;
        this.realName = realName;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
}
