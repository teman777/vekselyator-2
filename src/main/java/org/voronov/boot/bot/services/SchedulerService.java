package org.voronov.boot.bot.services;


import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.voronov.boot.bot.Bot;
import org.voronov.boot.bot.model.dto.Operation;
import org.voronov.boot.bot.model.dto.TgChat;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
public class SchedulerService {

    @Autowired
    private Bot bot;

    @Autowired
    private ChatCache chatCache;

    @Autowired
    private SaldoService saldoService;

    @Autowired
    private MessageTextService textService;

    @Scheduled(cron = "0 0 10 * * *", zone = "Europe/Samara")
    public void sendSaldoOnMorning() {
        List<TgChat> chats = chatCache.getGroupChats();
        for (TgChat chat : chats) {
            Set<Operation> optimized = saldoService.optimizeEntity(chat.getAllChatsOperations());
            if (CollectionUtils.isNotEmpty(optimized)) {
                StringBuilder sb = new StringBuilder("Баланс на сегодня\n");
                for (Operation operation : optimized) {
                    sb.append(textService.buildTextForSaldoScheduler(operation)).append("\n");
                }
                bot.sendMessage(sb.toString(), chat.getId());
            }
        }
    }


}
