package org.voronov.boot.bot.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.voronov.boot.bot.model.dto.Operation;

public interface OperationRepository extends CrudRepository<Operation, Long> {
}
