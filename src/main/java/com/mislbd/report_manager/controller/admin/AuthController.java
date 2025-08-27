package com.mislbd.report_manager.controller.admin;
import com.mislbd.report_manager.configuration.annotation.CommandAttribute;
import com.mislbd.report_manager.criteria.UserSearchCriteria;
import com.mislbd.report_manager.domain.admin.AuthRequestDomain;
import com.mislbd.report_manager.domain.admin.ChangePasswordDomain;
import com.mislbd.report_manager.domain.admin.UserDomain;
import com.mislbd.report_manager.entity.admin.UserEntity;
import com.mislbd.report_manager.service.admin.AuthService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.UnknownHostException;


@RestController
@RequestMapping("/admin/auth")
public class AuthController {


    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @CommandAttribute("CREATE_NEW_USER")
    public ResponseEntity<?> register(@RequestBody UserEntity request) {
        return authService.saveUser(request);
    }

    @PutMapping("/update-user")
    @CommandAttribute("MODIFICATION_USER")
    public ResponseEntity<?> updateUser(@RequestBody UserEntity request) {
        return  authService.updateUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDomain request) {
        try {
            return authService.login(request);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam("userName") String userName,
                                         @RequestParam("logoutType") String logoutType) {
        return authService.logout(userName, logoutType);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDomain req) {
        return authService.changePassword(req);
    }

    @GetMapping(path = {"get-users"})
    public Page<UserDomain> getUsers(
            @ParameterObject Pageable pageable,
            @ParameterObject UserSearchCriteria criteria
    ) {
        return authService.getUsers(criteria, pageable);
    }
}

