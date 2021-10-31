package org.voronov.boot.bot.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.voronov.boot.bot.model.dto.TgUser;

public interface UserRepository extends CrudRepository<TgUser, Long> {
}
