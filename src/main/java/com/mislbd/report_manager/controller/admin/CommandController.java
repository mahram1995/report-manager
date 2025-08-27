package com.mislbd.report_manager.controller.admin;
import com.mislbd.report_manager.configuration.aopConfig.entity.CommandEntity;
import com.mislbd.report_manager.configuration.aopConfig.service.CommandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/commands")
public class CommandController {

    private final CommandService commandService;

    public CommandController(CommandService commandService) {
        this.commandService = commandService;
    }

    // Create a new CommandEntity
    @PostMapping("save-command")
    public ResponseEntity<?> createCommandEntity(@RequestBody CommandEntity CommandEntity) {
        return  commandService.saveCommand(CommandEntity);
    }

    // Get all CommandEntity
    @GetMapping("/get-commands")
    public Object getCommands(
            @RequestParam(name = "commandName", required = false) String commandName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "asPage", defaultValue = "true") boolean asPage
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommandEntity> pageResult = commandService.getAllCommands(commandName, pageable);

        if (asPage) {
            // return full Page object (with metadata: totalElements, totalPages, etc.)
            return pageResult;
        } else {
            // return only the list of entities
            return pageResult.getContent();
        }
    }

    // Get CommandEntity by ID
    @GetMapping("get-commandById/{id}")
    public ResponseEntity<CommandEntity> getCommandEntityById(@PathVariable Long id) {
        return commandService.getCommandById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update CommandEntity
    @PutMapping("update-command")
    public ResponseEntity<?> updateCommandEntity( @RequestBody List<CommandEntity> commandEntities) {
                   return commandService.updateCommand(commandEntities);
    }

    // Delete CommandEntity
    @DeleteMapping("delete-command/{id}")
    public ResponseEntity<Void> deleteCommandEntity(@PathVariable Long id) {
        commandService.deleteCommand(id);
        return ResponseEntity.noContent().build();
    }
}
