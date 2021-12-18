package org.voronov.boot.bot.model.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.voronov.boot.bot.model.dto.Operation;

import java.util.List;

public interface OperationRepository extends CrudRepository<Operation, Long> {

    @Modifying
    @Query(value = "update Operations o " +
            "set o.IsDeleted = 1 " +
            "where o.ID = :id", nativeQuery = true)
    void removeById(@Param("id") Long id);

    @Modifying
    @Query(value = "update Operations o " +
            "set o.IsDeleted = 1 " +
            "where o.ID in :ids", nativeQuery = true)
    void removeById(@Param("ids") List<Long> ids);
}
