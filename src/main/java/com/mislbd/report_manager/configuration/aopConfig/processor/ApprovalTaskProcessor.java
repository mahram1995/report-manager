package com.mislbd.report_manager.configuration.aopConfig.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mislbd.report_manager.configuration.aopConfig.auditListener.AuditorContextHolder;
import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import com.mislbd.report_manager.configuration.aopConfig.domain.CommandResponse;
import com.mislbd.report_manager.configuration.aopConfig.entity.CommandEntity;
import com.mislbd.report_manager.configuration.aopConfig.repository.CommandRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.Map;
import java.util.function.Consumer;

@Component
public class ApprovalTaskProcessor {


    private final CommandHandlerAnnotationProcessor commandHandlerAnnotationProcessor;
    private final AuditorContextHolder auditorContextHolder;
    private final CommandListenerProcessor commandListenerProcessor;
    private final CommandValidatorAnnotationProcessor commandValidator;
    private final CommandRepository commandRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();  // Jackson for JSON → Object

    public ApprovalTaskProcessor(CommandHandlerAnnotationProcessor commandHandlerAnnotationProcessor,
                                 AuditorContextHolder auditorContextHolder,
                                 CommandListenerProcessor commandListenerProcessor,
                                 CommandValidatorAnnotationProcessor commandValidator,
                                 CommandRepository commandRepository) {
        this.commandHandlerAnnotationProcessor = commandHandlerAnnotationProcessor;
        this.auditorContextHolder = auditorContextHolder;
        this.commandListenerProcessor = commandListenerProcessor;
        this.commandValidator = commandValidator;
        this.commandRepository = commandRepository;
    }



    public ResponseEntity<?> verifyOperation(String commandName, String payload, String action) {
        CommandEntity command=commandRepository.findByCommandName(commandName);
       Command baseCommand=  getBaseCommand(payload, command.getCommandPackageName());
        CommandResponse<?> response = null;
            if(action.equals("APPROVE")){
                 response = executeApproveCommand(
                        command.getCommandPackageName(),
                        command.getEntityPackageName(),
                        payload,
                         action,
                         baseCommand
                );
            }else{
                // publish command listener on REJECTION and CORRECTION
                commandListenerProcessor.publishCommandListener(commandName,baseCommand.getPayload(),action);
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Task Reject successfully: "
                ));
            }
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Task approve successfully: " +response.getContent()
        ));

    }



    public  CommandResponse<?>  executeApproveCommand(String commandPackage, String entityPackage, String payload, String action, Command baseCommand) {

        try {

            JsonNode rootNode = objectMapper.readTree(payload);
            JsonNode payloadNode = rootNode.get("payload");


            // 1️⃣ Load entity class dynamically
            Class<?> entityClass = Class.forName(entityPackage);

            Object payloadObject;

            // 2️⃣ Convert payload map -> entity instance
            Object entity = objectMapper.convertValue(payloadNode, entityClass);

            // 3️⃣ Load command class dynamically
            Class<?> commandClass = Class.forName(commandPackage);

            // 4️⃣ Find constructor with entity parameter
            Constructor<?> constructor = commandClass.getConstructor(entityClass);

            // 5️⃣ Create command instance
            Object command = constructor.newInstance(entity);

            // validate command before approve
            Boolean isCommandValidate=commandValidator.runCommandValidator(command);
            CommandResponse response=null;

            if(isCommandValidate){
                response = (CommandResponse<?>)  commandHandlerAnnotationProcessor.runCommand(command);
            }
            // call command handler process for doing the operation
            commandListenerProcessor.publishCommandListener(baseCommand.getClass().getSimpleName(), baseCommand.getPayload(),action);

            return response;



        } catch (InvocationTargetException ex) {
           return null;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }



    Command getBaseCommand(String payload, String commandClass){
        Command baseCommand;
        JsonNode rootNode = null;
        try {
            Class<?> clazz = Class.forName(commandClass);
            rootNode = objectMapper.readTree(payload);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            baseCommand = (Command<?>) objectMapper.convertValue(rootNode, clazz);

            // set verifier information
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes()).getRequest();
            String terminalIp = request.getHeader("terminalIp");
            baseCommand.setVerifier(AuditorContextHolder.getCurrentUser());
            baseCommand.setVerifierTerminal(terminalIp);
            auditorContextHolder.setCommand(baseCommand);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return baseCommand;
    }

}
