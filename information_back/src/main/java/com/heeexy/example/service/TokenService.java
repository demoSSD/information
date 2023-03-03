package com.heeexy.example.service;

import com.heeexy.example.dto.session.SessionUserInfo;

public interface TokenService {
    String generateToken(String username);
    SessionUserInfo getUserInfo();
    void invalidateToken();
}
