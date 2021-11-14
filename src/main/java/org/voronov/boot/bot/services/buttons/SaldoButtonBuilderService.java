package org.voronov.boot.bot.services.buttons;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voronov.boot.bot.caches.saldo.SaldoEntity;
import org.voronov.boot.bot.commands.SaldoCommand;
import org.voronov.boot.bot.model.dto.Operation;
import org.voronov.boot.bot.services.MessageTextService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SaldoButtonBuilderService {

    @Autowired
    private MessageTextService textService;

    public InlineKeyboardMarkup buildButtons(SaldoEntity entity, SaldoCommand.Stage stage) {
        if (stage == SaldoCommand.Stage.SHOW) {
            return buildButtonsShow(entity);
        } else if (stage == SaldoCommand.Stage.CONFIRM) {
            return buildButtonsConfirm(entity);
        } else if (stage == SaldoCommand.Stage.YES) {
            return buildButtonsYes(entity);
        }
        return null;
    }

    private InlineKeyboardMarkup buildButtonsShow(SaldoEntity entity) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>(ListUtils.partition(buildSaldoList(entity), 1));
        buttons.add(buildBottomButtons(entity));
        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    private InlineKeyboardMarkup buildButtonsConfirm(SaldoEntity entity) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(buildClose(entity));
        buttons.add(buildConfirm(entity));
        return InlineKeyboardMarkup.builder()
                .keyboardRow(buttons)
                .build();
    }

    private InlineKeyboardMarkup buildButtonsYes(SaldoEntity entity) {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(Collections.singletonList(buildClose(entity)))
                .build();
    }

    private InlineKeyboardButton buildConfirm(SaldoEntity entity) {
        return InlineKeyboardButton.builder()
                .text("Принять")
                .callbackData("saldoPerform/" + entity.getId().toString())
                .build();
    }

    private List<InlineKeyboardButton> buildBottomButtons(SaldoEntity entity) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        buttons.add(buildClose(entity));
        if (!entity.getSelectedSaldo().isEmpty()) {
            buttons.add(buildNext(entity));
        }
        return buttons;
    }

    private List<InlineKeyboardButton> buildSaldoList(SaldoEntity entity) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        List<Long> selected = entity.getSelectedSaldo();
        for (Operation saldo : entity.getSaldoAll()) {
            String text = textService.buildTextForSaldo(saldo);
            String callback = "saldoSel/" + saldo.getId().toString() + "/" + entity.getId().toString();

            if (selected.contains(saldo.getId())) {
                text = text + " " + new String(Character.toChars(0x2705));
                callback = callback.replace("saldoSel/", "saldoDesel/");
            }

            buttons.add(InlineKeyboardButton.builder()
                    .text(text)
                    .callbackData(callback)
                    .build());
        }
        return buttons;
    }

    private InlineKeyboardButton buildNext(SaldoEntity entity) {
        return InlineKeyboardButton.builder()
                .callbackData("saldoNext/" + entity.getId().toString())
                .text("Погасить")
                .build();
    }

    private InlineKeyboardButton buildClose(SaldoEntity entity) {
        return InlineKeyboardButton.builder()
                .text("Закрыть")
                .callbackData("saldoClose/" + entity.getId().toString())
                .build();
    }
}
