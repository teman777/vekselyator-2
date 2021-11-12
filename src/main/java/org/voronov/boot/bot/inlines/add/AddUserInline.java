package org.voronov.boot.bot.inlines.add;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.voronov.boot.bot.caches.operations.AddOperationCache;
import org.voronov.boot.bot.inlines.core.AbstractInlineHandler;

@Component
public class AddUserInline extends AbstractInlineHandler {
    @Autowired
    private AddOperationCache cache;

    public AddUserInline() {
        super("adduser");
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {

    }
}
