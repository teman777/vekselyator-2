package org.voronov.boot.bot.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.voronov.boot.bot.model.dto.TgChat;

@Repository
public interface ChatRepository extends CrudRepository<TgChat, Long> {
}
