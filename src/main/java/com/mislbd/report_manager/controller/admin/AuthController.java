package com.mislbd.report_manager.controller.admin;

import com.mislbd.report_manager.domain.admin.AuthRequestDomain;
import com.mislbd.report_manager.domain.admin.ChangePasswordDomain;
import com.mislbd.report_manager.entity.admin.UserEntity;
import com.mislbd.report_manager.service.admin.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/admin/auth")
public class AuthController {


    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute UserEntity request) throws IOException {
        return authService.saveUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(AuthRequestDomain request, HttpServletRequest httpRequest) {
        return authService.login(request,httpRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam("username") String username) {
       return  authService.logout(username);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDomain req) {
       return authService.changePassword(req);
    }
}

