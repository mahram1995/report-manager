package com.mislbd.report_manager.configuration.aopConfig.processor;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandScannerRunner implements ApplicationRunner {

    private final CommandAnnotationScanner scanner;

    public CommandScannerRunner(CommandAnnotationScanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        scanner.scanAndSave();
    }
}
