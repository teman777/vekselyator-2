package org.voronov.boot.bot.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.voronov.boot.bot.model.dto.UserChat;

@Repository
public interface UserChatRepository extends CrudRepository<UserChat, Long> {
}
