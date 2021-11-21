package org.voronov.boot.bot.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voronov.boot.bot.caches.saldo.SaldoEntity;
import org.voronov.boot.bot.model.dto.Operation;

import java.util.Collection;
import java.util.Iterator;

@Service
public class MessageTextService {

    private static final String REGISTER_TEMPLATE = "Зарегал %s";
    private static final String ADD_OPERATION_TEMPLATE = "Вексель добавлен.";
    private static final String OPERATION_TEMPLATE = "%s -> %s (%.2f) %s\n";
    private static final String SALDO_TEMPLATE = "%s -> %s (%.2f)";
    private static final String OPERATION_BUTTON_TEMPLATE = "%.2f %s";
    private static final String OPERATION_BUTTON_NEGATIVE_TEMPLATE = "-%.2f %s";
    private static final String WRONG_USER_ADD = "Не твое дело, проходи мимо.";
    private static final String LIST_WELCOME = "Выбери, что показать.";
    private static final String SALDO_MSG_TEMPLATE_WITHOUT_BALANCE = "Взаимный расчет.";
    private static final String SALDO_MSG_TEMPLATE_WITH_BALANCE = SALDO_MSG_TEMPLATE_WITHOUT_BALANCE
            + "\nПри подсчете сальдо образовалась погрешность.\nShit happens";

    private static final String HELP_TEXT = "Это бот векселятор - сохраняет долги в этом чате.\n" +
            "/start - Зарегистрироваться в этом чате. Можно обновить свой ник\n" +
            "/add - Добавить вексель. Жми это, если тебе задолжали.\n" +
            "/list - Меню по просмотру векселей.\n" +
            "/saldo - Расчитать общее сальдо для всех участников чата.\n";

    private static final String ERROR = "Произошла какая-то ебала, я ничего не сделал :(";

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
                operation.getuTo().getUser().getBrief(),
                operation.getuFrom().getUser().getBrief(),
                operation.getQty(),
                operation.getComment());
    }

    public String getListWelcome() {
        return LIST_WELCOME;
    }

    public String getError() {
        return ERROR;
    }

    public String buildBriefForUser(User user) {
        String brief = user.getUserName();
        if (brief == null || "".equals(brief)) {
            brief = user.getFirstName();
            if (user.getLastName() != null && !user.getLastName().equals("")) {
                brief += " " + user.getLastName();
            }
        }
        return brief;
    }

    public String buildOperationTextForMessage(Collection<Operation> operations) {
        StringBuilder text = new StringBuilder();

        for (Iterator<Operation> iterator = operations.iterator(); iterator.hasNext(); ) {
            Operation operation = iterator.next();
            text.append(getTextForOperation(operation));
            if (iterator.hasNext()) {
                text.append("----------------------\n");
            }
        }
        return text.toString();
    }

    public String buildOperationButtonText(Operation operation) {
        return String.format(OPERATION_BUTTON_TEMPLATE, operation.getQty(), operation.getComment() == null ? "" : operation.getComment());
    }

    public String buildOperationNegativeButtonText(Operation operation) {
        return String.format(OPERATION_BUTTON_NEGATIVE_TEMPLATE, operation.getQty(), operation.getComment() == null ? "" : operation.getComment());
    }

    public String buildTextForSaldo(Operation operation) {
        return String.format(SALDO_TEMPLATE, operation.getuTo().getUser().getBrief(), operation.getuFrom().getUser().getBrief(), operation.getQty());
    }

    public String buildTextForMessageSaldo(SaldoEntity entity) {
        Double balance = entity.getErrorBalance();
        if (balance == null || balance == 0) {
            return SALDO_MSG_TEMPLATE_WITHOUT_BALANCE;
        } else {
            return SALDO_MSG_TEMPLATE_WITH_BALANCE;
        }
    }
}
