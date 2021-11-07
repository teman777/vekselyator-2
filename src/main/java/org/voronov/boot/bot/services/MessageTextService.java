package org.voronov.boot.bot.services;

import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;
import org.voronov.boot.bot.model.dto.Operation;

@Service
public class MessageTextService {

    private static final String REGISTER_TEMPLATE = "Зарегал %s";
    private static final String ADD_OPERATION_TEMPLATE = "Вексель добавлен.";
    private static final String OPERATION_TEMPLATE = "%s -> %s (%.2f)\n%s";
    private static final String WRONG_USER_ADD = "Не твое дело, проходи мимо.";
    private static final String HELP_TEXT = "Это бот векселятор - сохраняет долги в этом чате.\n" +
            "/start - Зарегистрироваться в этом чате. Можно обновить свой ник\n" +
            "/add - Добавить вексель. Жми это, если тебе задолжали.\n" +
            "/list - Меню по просмотру векселей.\n" +
            "/my - Список векселей, где ты участвуешь.\n" +
            "/all - Список вообще всех векселей в этом чате.\n" +
            "/saldo - Расчитать общее сальдо для всех участников чата.\n";

    public String getRegisterText(String username) {
        return String.format(REGISTER_TEMPLATE, username);
    }

    public String getAddOperationText() {
        return ADD_OPERATION_TEMPLATE;
    }

    public String getHelpText() {
        return HELP_TEXT;
    }

    public String getWrongUserAddText() {
        return WRONG_USER_ADD;
    }

    public String getTextForOperation(Operation operation) {
        return String.format(OPERATION_TEMPLATE,
                operation.getuFrom().getUser().getBrief(),
                operation.getuTo().getUser().getBrief(),
                operation.getQty(),
                operation.getComment());
    }
}
