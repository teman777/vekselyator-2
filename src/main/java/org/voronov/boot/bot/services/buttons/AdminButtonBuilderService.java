package org.voronov.boot.bot.services.buttons;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voronov.boot.bot.Bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AdminButtonBuilderService {

    @Autowired
    private Bot bot;

    public InlineKeyboardMarkup build() {
        return InlineKeyboardMarkup.builder()
                .keyboard(buildButtons())
                .build();
    }

    private List<List<InlineKeyboardButton>> buildButtons() {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(buildRetranslateAllButton());
        List<List<InlineKeyboardButton>> rows = new ArrayList<>(ListUtils.partition(buttons, 2));
        rows.add(Collections.singletonList(buildCancel()));
        return rows;
    }

    private InlineKeyboardButton buildRetranslateAllButton() {
        Boolean isOn = bot.getTranslateAllOn();
        return InlineKeyboardButton.builder()
                .text("Ретрансляция : " + (isOn ? "ON" : "OFF"))
                .callbackData("retranslateAll/" + (isOn ? "2" : "1"))
                .build();
    }

    private InlineKeyboardButton buildCancel() {
        return InlineKeyboardButton.builder()
                .text("Закрыть")
                .callbackData("adminClose/")
                .build();
    }
}
