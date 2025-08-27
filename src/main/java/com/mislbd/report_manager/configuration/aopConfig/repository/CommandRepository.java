package com.mislbd.report_manager.configuration.aopConfig.repository;

import com.mislbd.report_manager.configuration.aopConfig.entity.CommandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandRepository extends JpaRepository<CommandEntity, Long> {
    CommandEntity findByCommandName(String commandName);

    @Query(value = "SELECT * FROM COMMAND C WHERE C.COMMAND_NAME= NVL( :name,COMMAND_NAME)", nativeQuery = true)
    Page<CommandEntity> getCommandsByName(@Param("name") String commandName, Pageable pageable);
}