package com.mislbd.report_manager.configuration.aopConfig.serviceImpl;

import com.mislbd.report_manager.configuration.aopConfig.entity.CommandEntity;
import com.mislbd.report_manager.configuration.aopConfig.repository.CommandRepository;
import com.mislbd.report_manager.configuration.aopConfig.service.CommandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommandServiceImpl implements CommandService {

    private final CommandRepository commandRepository;

    public CommandServiceImpl(CommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }


    @Override
    public ResponseEntity<?> saveCommand(CommandEntity command) {
        commandRepository.save(command);
        return ResponseEntity.ok().body(Map.of("message", "Save Successfully "));


    };

    @Override
    public ResponseEntity<?> updateCommand(List<CommandEntity> command) {
        commandRepository.saveAll(command);
        return ResponseEntity.ok().body(Map.of("message", "Save Successfully "));

    }

    @Override
    public CommandEntity getCommandByCommandName(String commandName) {
        return commandRepository.findByCommandName(commandName);
    }

    @Override
    public Page<CommandEntity> getAllCommands(String commandName, Pageable pageable) {
        return  commandRepository.getCommandsByName(commandName, pageable);
    }

    @Override
    public Optional<CommandEntity> getCommandById(Long id) {
        return commandRepository.findById(id);
    }

    @Override
    public void deleteCommand(Long id) {
        commandRepository.deleteById(id);
    }
}

