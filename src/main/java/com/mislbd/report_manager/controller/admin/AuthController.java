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
import java.net.UnknownHostException;

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
    public ResponseEntity<?> login(@RequestBody  AuthRequestDomain request) {
        try {
            return authService.login(request);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam("userName") String userName ,
                                         @RequestParam("logoutType") String logoutType) {
       return  authService.logout(userName,logoutType);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDomain req) {
       return authService.changePassword(req);
    }
}

