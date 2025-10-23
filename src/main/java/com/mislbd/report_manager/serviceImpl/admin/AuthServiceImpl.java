package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.configuration.aopConfig.domain.CommandResponse;
import com.mislbd.report_manager.configuration.jwtConfig.JwtUtil;
import com.mislbd.report_manager.configuration.security.SecurityConfig;
import com.mislbd.report_manager.criteria.UserSearchCriteria;
import com.mislbd.report_manager.domain.admin.AuthRequestDomain;
import com.mislbd.report_manager.domain.admin.ChangePasswordDomain;
import com.mislbd.report_manager.domain.admin.UserDomain;
import com.mislbd.report_manager.domain.admin.UserResponseDomain;
import com.mislbd.report_manager.enam.UserStatus;
import com.mislbd.report_manager.entity.admin.UserEntity;
import com.mislbd.report_manager.entity.admin.UserLoginInfoEntity;
import com.mislbd.report_manager.mapper.admin.UserMapper;
import com.mislbd.report_manager.repository.admin.UserRepository;
import com.mislbd.report_manager.repository.admin.UserLoginInfoRepository;
import com.mislbd.report_manager.service.admin.AuthService;
import com.mislbd.report_manager.specification.UserSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Transactional
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserLoginInfoRepository loginRepo;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public CommandResponse<?> saveUser(UserEntity data) {

        data.setPassword(encoder.encode(data.getPassword()));
        data.setUserPhoto(data.getUserPhoto() != null ? data.getUserPhoto() : null);
        data.setUserStatus(UserStatus.ACTIVE.name());


        return new CommandResponse<>(userRepo.save(data));
    }

    @Override
    public CommandResponse<?> updateUser(UserEntity data) {
        UserEntity entity = userRepo.findById(data.getId()).get();

        if(StringUtils.hasText(data.getPassword())){
            entity.setPassword(encoder.encode(data.getPassword()));
        }
        data.setPassword(entity.getPassword());
        return new CommandResponse<>(userRepo.save(data));
    }

    @Override
    public ResponseEntity<?> login(AuthRequestDomain request) {
        Optional<UserEntity> user = userRepo.findByUserName(request.getUserName());
        String token;

        if (user.isEmpty()) {
            throw new RuntimeException("User name is incorrect");
        } else if (!securityConfig.passwordEncoder().matches(request.getPassword(), user.get().getPassword())) {
            throw new RuntimeException("Password is incorrect");
        }
        if (user.get().getUserStatus().contains(UserStatus.BLOCKED.name())) {
            throw new RuntimeException("User is block. Please contact your administrator");
        }
        if (user.get().getUserStatus().contains(UserStatus.INACTIVE.name())) {
            throw new RuntimeException("User is inactive. Please contact your administrator for active the user");
        }
        if (user.get().getUserStatus().contains(UserStatus.DISABLED.name())) {
            throw new RuntimeException("User is disabled. you are not able to login in this system");
        }

        if (user.get().getIsLogin() != null && user.get().getIsLogin().contains("true")) {
            UserLoginInfoEntity loginInfo = (UserLoginInfoEntity) loginRepo.findTopByUserIdOrderByLoginTimeDesc(user.get().getId())
                    .orElse(null);
            if (loginInfo != null && !loginInfo.getLoginTerminal().equals(request.getLoginTerminal())) {
                throw new RuntimeException("User already login");
            } else {
                token = jwtUtil.generateToken(request.getUserName());
                return ResponseEntity.ok(entityToDomain(user.get(), token));
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

        return ResponseEntity.ok(entityToDomain(user.get(), token));
    }

    public UserResponseDomain entityToDomain(UserEntity user, String token) {
        UserResponseDomain domain = new UserResponseDomain();
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

        return domain;
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

    @Override
    public Page<UserDomain> getUsers(UserSearchCriteria criteria, Pageable pageable) {
        Specification<UserEntity> spec = UserSpecification.getUserSpecification(criteria);

        return userRepo.findAll(spec, pageable)
                .map(UserMapper::entityToDomain); // convert each entity to domain
    }

    @Override
    public boolean existByUserName(String userName) {
        return userRepo.existsByUserName(userName);
    }

}
