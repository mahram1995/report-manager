package com.mislbd.report_manager.controller.admin;
import com.mislbd.report_manager.command.CreateNewUserCommand;
import com.mislbd.report_manager.command.UserModificationCommand;
import com.mislbd.report_manager.configuration.aopConfig.service.CommandProcessor;
import com.mislbd.report_manager.criteria.UserSearchCriteria;
import com.mislbd.report_manager.domain.admin.AuthRequestDomain;
import com.mislbd.report_manager.domain.admin.ChangePasswordDomain;
import com.mislbd.report_manager.domain.admin.UserDomain;
import com.mislbd.report_manager.entity.admin.UserEntity;
import com.mislbd.report_manager.service.admin.AuthService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.UnknownHostException;


@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    CommandProcessor commandProcessor;
    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntity data) {
        return ResponseEntity.ok(commandProcessor.executeCommand(new CreateNewUserCommand(data)));
    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody UserEntity data) {
        return ResponseEntity.ok(commandProcessor.executeCommand(new UserModificationCommand(data)));
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

