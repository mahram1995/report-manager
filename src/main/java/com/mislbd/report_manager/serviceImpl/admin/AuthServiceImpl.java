package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.configuration.JwtUtil;
import com.mislbd.report_manager.configuration.SecurityConfig;
import com.mislbd.report_manager.domain.admin.AuthRequestDomain;
import com.mislbd.report_manager.domain.admin.ChangePasswordDomain;
import com.mislbd.report_manager.entity.admin.UserEntity;
import com.mislbd.report_manager.entity.admin.UserLoginInfoEntity;
import com.mislbd.report_manager.repository.admin.SecuUserRepository;
import com.mislbd.report_manager.repository.admin.UserLoginInfoRepository;
import com.mislbd.report_manager.service.admin.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Component
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired private SecuUserRepository userRepo;
    @Autowired private UserLoginInfoRepository loginRepo;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder encoder;

    @Override
    public ResponseEntity<?> saveUser(UserEntity data) {
        UserEntity user = new UserEntity();
        user.setUserName(data.getUserName());
        user.setPassword(encoder.encode(data.getPassword()));
        user.setEmail(data.getEmail());
        user.setUserPhoto(data.getUserPhoto() != null ? data.getUserPhoto() : null);
        userRepo.save(user);
        return ResponseEntity.ok("Registered");
    }

    @Override
    public ResponseEntity<?> login(AuthRequestDomain request, HttpServletRequest httpRequest) {
        Optional<UserEntity> user = userRepo.findByUserName(request.getUserName());

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User name is incorrect");
        }

        if (!securityConfig.passwordEncoder().matches(request.getPassword(), user.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Password is incorrect");
        }
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));


        UserLoginInfoEntity loginInfo = new UserLoginInfoEntity();
        loginInfo.setUserId(user.get().getId());
        loginInfo.setLoginTime(LocalDateTime.now());
        loginInfo.setLoginTerminal(httpRequest.getRemoteHost());
        loginInfo.setLoginDeviseName(httpRequest.getHeader("User-Agent"));
        loginRepo.save(loginInfo);

        String token = jwtUtil.generateToken(request.getUserName());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @Override
    public ResponseEntity<String> logout(String username) {
        Optional<UserEntity> user = userRepo.findByUserName(username);

        if (user.isPresent()) {
            UserLoginInfoEntity loginInfo = (UserLoginInfoEntity) loginRepo.findTopByUserIdOrderByLoginTimeDesc(user.get().getId())
                    .orElse(null);

            if (loginInfo != null && loginInfo.getLogoutTime() == null) {
                loginInfo.setLogoutTime(LocalDateTime.now());
                loginRepo.save(loginInfo);
            }

            return ResponseEntity.ok("User logged out successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user");
        }
    }

    @Override
    public ResponseEntity<?> changePassword(ChangePasswordDomain req) {
        UserEntity user = userRepo.findByUserName(req.getUserName()).orElseThrow();
        if (!encoder.matches(req.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect current password");
        }
        user.setPassword(encoder.encode(req.getNewPassword()));
        userRepo.save(user);
        return ResponseEntity.ok("Password updated");
    }
}
