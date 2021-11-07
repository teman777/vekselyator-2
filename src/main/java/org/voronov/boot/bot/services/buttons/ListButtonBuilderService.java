package org.voronov.boot.bot.services.buttons;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.caches.list.ListOperationsEntity;
import org.voronov.boot.bot.commands.ListCommand;

@Service
public class ListButtonBuilderService {

    public InlineKeyboardMarkup buildButtons(ListOperationsEntity entity, ListCommand.Stage stage) {
        return null;
    }

    private InlineKeyboardMarkup buildForSettingType(ListOperationsEntity entity) {
        return null;

    }

}
