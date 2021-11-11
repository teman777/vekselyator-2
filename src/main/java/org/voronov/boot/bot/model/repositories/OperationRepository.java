package org.voronov.boot.bot.model.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.voronov.boot.bot.model.dto.Operation;

import java.util.List;

public interface OperationRepository extends CrudRepository<Operation, Long> {

    @Query("SELECT o " +
            " FROM UserChat uc " +
            " JOIN Operation o " +
            "   ON o.UTo = uc.ID or o.UFrom = uc.ID" +
            "WHERE uc.ChatID = ?1 and uc.UserID = ?2")
    List<Operation> findByChatIdAndUserId(Long chatId, Long userId);
}
