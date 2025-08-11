package com.mislbd.report_manager.configuration.aopConfig.repository;

import com.mislbd.report_manager.configuration.aopConfig.entity.TaskInstanceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskInstanceRepo extends JpaRepository<TaskInstanceEntity, Long> {
    TaskInstanceEntity findByTaskId(Long taskId);

    void deleteByTaskId(Long taskId);
    @Query(value = "SELECT *\n" +
            "  FROM AF_TASK_INSTANCE a\n" +
            "  WHERE A.TASK_ID = NVL ( :taskId, A.TASK_ID)\n" +
            "  AND A.MAKER=NVL(:maker,A.MAKER)\n" +
            "  AND(( A.VERIFIER IS NULL\n" +
            "  AND A.MAKER <> :verifier) or \n" +
            "  (A.VERIFIER IS NOT NULL AND A.VERIFIER=:verifier)) order by task_id desc", nativeQuery = true)
    Page<TaskInstanceEntity> getTasks(@Param("taskId") Long taskId,
                                      @Param("verifier") String verifier,
                                      @Param("maker") String maker,
                                      Pageable pageable);
}
