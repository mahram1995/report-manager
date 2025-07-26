package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.domain.admin.AuthRequestDomain;
import com.mislbd.report_manager.domain.admin.ChangePasswordDomain;
import com.mislbd.report_manager.entity.admin.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
public interface AuthService {

    public ResponseEntity<?> saveUser(UserEntity user);
    public ResponseEntity<?> login(AuthRequestDomain request) throws UnknownHostException;
    public ResponseEntity<String> logout( String username, String logoutType);
    public ResponseEntity<?> changePassword(ChangePasswordDomain req);
    public boolean existByUserName(String userName);
}
