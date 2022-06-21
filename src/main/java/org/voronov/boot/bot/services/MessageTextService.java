package org.voronov.boot.bot.services;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voronov.boot.bot.caches.operations.AddOperationEntity;
import org.voronov.boot.bot.caches.saldo.SaldoEntity;
import org.voronov.boot.bot.model.dto.Operation;
import org.voronov.boot.bot.model.dto.TgUser;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageTextService {

    private static final String REGISTER_TEMPLATE = "Зарегал %s";
    private static final String ADD_OPERATION_TEMPLATE_SOLO = "Вексель добавлен.\n%s одолжил %s %.2fр.\n%s";
    private static final String ADD_OPERATION_TEMPLATE_SOLO_NOT_SOLO = "Вексель добавлен.\n%s одолжил %s по %.2fр.\n%s";
    private static final String OPERATION_TEMPLATE = "%s -> %s (%.2f) %s\n";
    private static final String SALDO_TEMPLATE = "%s -> %s (%.2f)";
    private static final String SALDO_TEMPLATE_SCHEDULER = "@%s -> @%s (%.2f)";
    private static final String OPERATION_BUTTON_TEMPLATE = "%.2f %s";
    private static final String OPERATION_BUTTON_NEGATIVE_TEMPLATE = "-%.2f %s";
    private static final String WRONG_USER_ADD = "Не твое дело, проходи мимо.";
    private static final String LIST_WELCOME = "Выбери, что показать.";
    private static final String SALDO_MSG_TEMPLATE_WITHOUT_BALANCE = "Взаимный расчет.";
    private static final String SALDO_MSG_TEMPLATE_EMPTY = "Векселей нет в этом чате.";
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

    public String getAddOperationText(AddOperationEntity entity) {
        TgUser from = entity.getFromUser();
        List<TgUser> to = entity.getToUsersWithoutSelf();

        String fromBrief = "@" + from.getBrief();
        List<String> toBriefs = to.stream().map(TgUser::getBrief).collect(Collectors.toList());

        String toString = buildStringTo(toBriefs);

        Double qty = entity.getQtyForOne();

        String format = entity.getType() == AddOperationEntity.Type.FOR_ONE
                || toBriefs.size() == 1
                ? ADD_OPERATION_TEMPLATE_SOLO
                : ADD_OPERATION_TEMPLATE_SOLO_NOT_SOLO;

        return String.format(format, fromBrief, toString, qty, entity.getComment() != null ? entity.getComment() : "");
    }

    public String buildStringForSettingTypeOperation(AddOperationEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("*Параметры векселя*\n");
        sb.append("*На кого:* ");
        List<String> usrs = entity.getToUsers().stream().map(TgUser::getBrief).toList();
        sb.append(buildStringTo(usrs));
        sb.append("\n");
        AddOperationEntity.Type type = entity.getType();
        if (type != AddOperationEntity.Type.FOR_ONE) {
            String msg = type == AddOperationEntity.Type.DIVIDE_TO_ALL
                    ? "делим на всех\n"
                    : "с каждого по\n";
            sb.append("*Тип:* ").append(msg);
        }

        sb.append("\nДля добавления векселя ответьте на это сообщение в формате \"Сумма комментарий\"");
        return sb.toString();
    }

    private String buildStringTo(List<String> briefs) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> iterator = briefs.iterator(); iterator.hasNext(); ) {
            String brief = iterator.next();
            sb.append("@").append(brief);

            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
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

    public String buildTextForSaldoScheduler(Operation operation) {
        return String.format(SALDO_TEMPLATE_SCHEDULER, operation.getuTo().getUser().getBrief(), operation.getuFrom().getUser().getBrief(), operation.getQty());
    }

    public String buildTextForMessageSaldo(SaldoEntity entity) {
        if (CollectionUtils.isNotEmpty(entity.getSaldoAll())) {
            Double balance = entity.getErrorBalance();
            if (balance == null || balance == 0) {
                return SALDO_MSG_TEMPLATE_WITHOUT_BALANCE;
            } else {
                return SALDO_MSG_TEMPLATE_WITH_BALANCE;
            }
        } else {
            return SALDO_MSG_TEMPLATE_EMPTY;
        }
    }
}
