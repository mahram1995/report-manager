package com.mislbd.report_manager.configuration.aopConfig.auditListener;

import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import org.springframework.stereotype.Component;

@Component
public class AuditorContextHolder {
    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();
    private static final ThreadLocal<String> currentClientIp = new ThreadLocal<>();
    private static final ThreadLocal<Command> currentCommand = new ThreadLocal<>();


    public static void setCurrentUser(String username) {
        currentUser.set(username);
    }

    public static String getCurrentUser() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }

    // ✅ optional payload support
    public static void setCommand(Command command) {
        currentCommand.set(command);
    }

    public static Command getCommand() {
        return currentCommand.get();
    }

    public static void setClientIp(String clientIp){
        currentClientIp.set(clientIp);
    }
    public static String getClientIp(){
        return currentClientIp.get();
    }
}
