package com.dataservicesperu.ssoma.auth_service.service;

import com.dataservicesperu.ssoma.auth_service.dto.LoginRequest;
import com.dataservicesperu.ssoma.auth_service.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}