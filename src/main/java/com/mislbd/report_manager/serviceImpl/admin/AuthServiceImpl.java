package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.configuration.JwtUtil;
import com.mislbd.report_manager.configuration.SecurityConfig;
import com.mislbd.report_manager.domain.admin.AuthRequestDomain;
import com.mislbd.report_manager.domain.admin.ChangePasswordDomain;
import com.mislbd.report_manager.domain.admin.UserResponseDomain;
import com.mislbd.report_manager.entity.admin.UserEntity;
import com.mislbd.report_manager.entity.admin.UserLoginInfoEntity;
import com.mislbd.report_manager.exception.CommonRuntimeException;
import com.mislbd.report_manager.repository.admin.SecuUserRepository;
import com.mislbd.report_manager.repository.admin.UserLoginInfoRepository;
import com.mislbd.report_manager.service.admin.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Transactional
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
        data.setPassword(encoder.encode(data.getPassword()));
        data.setUserPhoto(data.getUserPhoto() != null ? data.getUserPhoto() : null);
        userRepo.save(data);
        return ResponseEntity.ok("Registered");
    }

    @Override
    public ResponseEntity<?> login(AuthRequestDomain request)  {
        Optional<UserEntity> user = userRepo.findByUserName(request.getUserName());
        String token;

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User name is incorrect");
        } else if (!securityConfig.passwordEncoder().matches(request.getPassword(), user.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password is incorrect");
        } else if(user.get().getIsLogin()!=null && user.get().getIsLogin().contains("true")){
            UserLoginInfoEntity loginInfo = (UserLoginInfoEntity) loginRepo.findTopByUserIdOrderByLoginTimeDesc(user.get().getId())
                    .orElse(null);
            if(!loginInfo.getLoginTerminal().equals(request.getLoginTerminal())){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User already login");
            }else{
                token = jwtUtil.generateToken(request.getUserName());
               return ResponseEntity.ok( entityToDomain(user.get(),token));
            }

        }

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));

        // update user as isLogin true
        user.get().setIsLogin("true");
        userRepo.save(user.get());


        // save user loginInfo log
        UserLoginInfoEntity loginInfo = new UserLoginInfoEntity();
        loginInfo.setUserId(user.get().getId());
        loginInfo.setLoginTime(LocalDateTime.now());
        loginInfo.setLoginTerminal(request.getLoginTerminal());
        loginInfo.setLoginDeviseName(request.getUserAgent());
        loginRepo.save(loginInfo);

         token = jwtUtil.generateToken(request.getUserName());

        return ResponseEntity.ok( entityToDomain(user.get(),token));
    }

   public UserResponseDomain entityToDomain(UserEntity user, String token){
        UserResponseDomain domain=new UserResponseDomain();
        domain.setEmail(user.getEmail());
        domain.setUserName(user.getUserName());
        domain.setDepartmentId(user.getDepartmentId());
        domain.setLastName(user.getLastName());
        domain.setFirstName(user.getFirstName());
        domain.setLastName(user.getLastName());
        domain.setMiddleName(user.getMiddleName());
        domain.setGroupId(user.getGroupId());
        domain.setToken(token);
        domain.setUserBranchId(user.getUserBranchId());

        return  domain;
    }

    @Override
    public ResponseEntity<String> logout(String username, String logoutType) {
        Optional<UserEntity> user = userRepo.findByUserName(username);

        if (user.isPresent()) {
            UserLoginInfoEntity loginInfo = (UserLoginInfoEntity) loginRepo.findTopByUserIdOrderByLoginTimeDesc(user.get().getId())
                    .orElse(null);

            if (loginInfo != null && loginInfo.getLogoutTime() == null) {
                loginInfo.setLogoutTime(LocalDateTime.now());
                loginInfo.setLogoutType(logoutType);
                loginRepo.save(loginInfo);
            }

            // update user isLogin
              user.get().setIsLogin("false");
              userRepo.save(user.get());

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
