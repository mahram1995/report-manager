package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.criteria.UserSearchCriteria;
import com.mislbd.report_manager.domain.admin.AuthRequestDomain;
import com.mislbd.report_manager.domain.admin.ChangePasswordDomain;
import com.mislbd.report_manager.domain.admin.UserDomain;
import com.mislbd.report_manager.entity.admin.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
public interface AuthService {

    public ResponseEntity<?> saveUser(UserEntity user);
    public ResponseEntity<?> updateUser(UserEntity user);
    public ResponseEntity<?> login(AuthRequestDomain request) throws UnknownHostException;
    public ResponseEntity<String> logout( String username, String logoutType);
    public ResponseEntity<?> changePassword(ChangePasswordDomain req);
    public Page<UserDomain> getUsers(UserSearchCriteria criteria, Pageable pageable);
    public boolean existByUserName(String userName);
}
