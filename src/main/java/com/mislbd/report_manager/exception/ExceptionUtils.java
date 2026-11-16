package com.mislbd.report_manager.exception;


import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

    private ExceptionUtils() {
        // Prevent object creation
    }

    public static Throwable getRootCause(Throwable throwable) {

        if (throwable == null) {
            return null;
        }

        Throwable rootCause = throwable;

        while (rootCause.getCause() != null
                && rootCause.getCause() != rootCause) {

            rootCause = rootCause.getCause();
        }

        return rootCause;
    }

    public static String getRootCauseMessage(Throwable throwable) {

        Throwable rootCause = getRootCause(throwable);

        return rootCause != null
                ? rootCause.getMessage()
                : "Unknown error";
    }

    public static String getFullErrorMessage(Throwable exception) {

        if (exception == null) {
            return "Unknown error occurred";
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        exception.printStackTrace(pw);

        return sw.toString();
    }

    public static String getRootErrorMessage(Throwable exception) {

        if (exception == null) {
            return "Unknown error occurred";
        }

        Throwable rootCause = exception;

        // Find the deepest cause safely
        while (rootCause.getCause() != null
                && rootCause.getCause() != rootCause) {

            rootCause = rootCause.getCause();
        }

        String message = rootCause.getMessage();

        if (message == null || message.trim().isEmpty()) {
            message = rootCause.toString();
        }

        return message;
    }
}