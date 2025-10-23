package com.mislbd.report_manager.controller.admin;

import com.mislbd.report_manager.command.CreateNewUserCommand;
import com.mislbd.report_manager.command.CreateReportGroupCommand;
import com.mislbd.report_manager.command.UpdateReportGroupCommand;
import com.mislbd.report_manager.configuration.aopConfig.service.CommandProcessor;
import com.mislbd.report_manager.entity.admin.ReportGroupEntity;
import com.mislbd.report_manager.service.admin.ReportGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/report-groups")
@RequiredArgsConstructor
public class ReportGroupController {
   @Autowired
   private CommandProcessor commandProcessor;
    @Autowired
    private ReportGroupService reportGroupService;
    @PostMapping("/createReportGroup")
    public ResponseEntity<?> createReportGroup(@RequestBody ReportGroupEntity data) {
        //
        return ResponseEntity.ok(commandProcessor.executeCommand(new CreateReportGroupCommand(data)));
    }

    @PutMapping("/updateReportGroup")
    public ResponseEntity<?> updateReportGroup(@RequestBody ReportGroupEntity data) {
        //
        return ResponseEntity.ok(commandProcessor.executeCommand(new UpdateReportGroupCommand(data)));
    }

}
