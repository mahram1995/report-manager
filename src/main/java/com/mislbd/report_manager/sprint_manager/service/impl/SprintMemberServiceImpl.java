package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.service.admin.EmailService;
import com.mislbd.report_manager.service.admin.GoogleChatService;
import com.mislbd.report_manager.sprint_manager.entity.SprintMemberEntity;
import com.mislbd.report_manager.sprint_manager.repository.SprintMemberRepository;
import com.mislbd.report_manager.sprint_manager.service.SprintMemberService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SprintMemberServiceImpl implements SprintMemberService {
    private final SprintMemberRepository repository;
    private  final EmailService emailService;
    private final GoogleChatService googleChatService;

    public SprintMemberServiceImpl(SprintMemberRepository repository, EmailService emailService, GoogleChatService googleChatService) {
        this.repository = repository;
        this.emailService = emailService;
        this.googleChatService = googleChatService;
    }

    @Override
    public SprintMemberEntity save(SprintMemberEntity entity) {
        SprintMemberEntity data;
        try {
            data=repository.save(entity);
            if(!data.getId().toString().isEmpty()){
                String  body="Hello " + data.getMemberName() + "\n"+
                              "Your are successfully added to sprint as member your designation is  " + data.getDesignation() +"\n"+
                              "Best wishes for your success"
                        ;

                emailService.sendEmail("mahram@mislbd.com","New Sprint Member Added Confirmation",body);

                googleChatService.sendMessage(body);
            }
        }catch (Exception e){
            throw new RuntimeException(e.getCause());
        }


        return repository.save(entity);
    }

    @Override
    public SprintMemberEntity update(Long id, SprintMemberEntity entity) {
        SprintMemberEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintMemberEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintMemberEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintMemberEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintMemberEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintMemberEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
