package com.mislbd.report_manager.configuration.aopConfig.service;

import com.mislbd.report_manager.configuration.aopConfig.entity.CommandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CommandService {

    ResponseEntity<?> saveCommand(CommandEntity command);
    ResponseEntity<?> updateCommand(List<CommandEntity> command);
    CommandEntity getCommandByCommandName(String commandName);

    Page<CommandEntity> getAllCommands(String commandName, Pageable pageable);

    Optional<CommandEntity> getCommandById(Long id);

    void deleteCommand(Long id);
}
