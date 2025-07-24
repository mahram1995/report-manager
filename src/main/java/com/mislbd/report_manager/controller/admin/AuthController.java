package com.mislbd.report_manager.controller.admin;

import com.mislbd.report_manager.configuration.annotation.Command;
import com.mislbd.report_manager.configuration.annotation.ValidateAttribute;
import com.mislbd.report_manager.domain.admin.AuthRequestDomain;
import com.mislbd.report_manager.domain.admin.ChangePasswordDomain;
import com.mislbd.report_manager.entity.admin.UserEntity;
import com.mislbd.report_manager.service.admin.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

@RestController
@RequestMapping("/admin/auth")
public class AuthController {


    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ValidateAttribute(operation = "CREATE_NEW_USER")
    @Command("CREATE_NEW_USER")
    public ResponseEntity<?> register(@RequestBody UserEntity request) {
        try {
            String result = authService.saveUser(request);
            return ResponseEntity.ok().body(Map.of("message", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
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

