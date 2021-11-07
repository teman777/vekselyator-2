package org.voronov.boot.bot.caches.list;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.voronov.boot.bot.caches.core.Cache;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ListOperationsCache extends Cache<ListOperationsEntity> {
}
