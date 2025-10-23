package com.mislbd.report_manager.configuration.aopConfig.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mislbd.report_manager.configuration.aopConfig.domain.CommandResponse;
import com.mislbd.report_manager.configuration.aopConfig.entity.CommandEntity;
import com.mislbd.report_manager.configuration.aopConfig.repository.CommandRepository;
import com.mislbd.report_manager.exception.CommandValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.Map;
import java.util.function.Consumer;

@Component
public class ApprovalTaskProcessor {


    private final CommandHandlerAnnotationProcessor commandHandlerAnnotationProcessor;
    private final CommandListenerProcessor commandListenerProcessor;
    private final CommandValidatorAnnotationProcessor commandValidator;
    private final CommandRepository commandRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();  // Jackson for JSON → Object

    public ApprovalTaskProcessor(CommandHandlerAnnotationProcessor commandHandlerAnnotationProcessor, CommandListenerProcessor commandListenerProcessor, CommandValidatorAnnotationProcessor commandValidator, CommandRepository commandRepository) {
        this.commandHandlerAnnotationProcessor = commandHandlerAnnotationProcessor;
        this.commandListenerProcessor = commandListenerProcessor;
        this.commandValidator = commandValidator;
        this.commandRepository = commandRepository;
    }



    public ResponseEntity<?> verifyOperation(String commandName, String payload, String action) {
        CommandEntity command=commandRepository.findByCommandName(commandName);
        CommandResponse<?> response = null;
            if(action.equals("APPROVE")){
                 response = executeApproveCommand(
                        command.getCommandPackageName(),
                        command.getEntityPackageName(),
                        payload,
                         r -> {
                             // publish command listener on APPROVE
                             commandListenerProcessor.publishCommandListener(commandName,payload,action);
                         }
                );
            }else{
                // publish command listener on REJECTION and CORRECTION
                commandListenerProcessor.publishCommandListener(commandName,payload,action);
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



    public  CommandResponse<?>  executeApproveCommand(String commandPackage, String entityPackage, Object payload, Consumer<CommandResponse<?>> callback) {

        try {
            // 1️⃣ Load entity class dynamically
            Class<?> entityClass = Class.forName(entityPackage);

            Object payloadObject;

            if (payload instanceof String payloadString) {
                // parse JSON string into Map
                payloadObject = objectMapper.readValue(payloadString, Map.class);
            } else {
                payloadObject = payload;
            }
            // 2️⃣ Convert payload map -> entity instance
            Object entity = objectMapper.convertValue(payloadObject, entityClass);

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

            // ✅ 8️⃣ Call callback if provided (on success)
            if (callback != null && response != null) {
                callback.accept(response);
            }
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



    private RuntimeException unwrapException(Throwable e) {
        if (e instanceof InvocationTargetException ite && ite.getCause() != null) {
            return unwrapException(ite.getCause());
        }
        if (e instanceof CommandValidationException) {
            return (CommandValidationException) e;
        }
        return new RuntimeException(e);
    }


}
