package org.voronov.boot.config;

import org.springframework.beans.factory.InitializingBean;
import org.voronov.boot.core.AbstractInlineCommandBot;
import org.voronov.boot.core.AbstractReplyHandler;

import java.util.List;
import java.util.Objects;

public class ReplyInitializer implements InitializingBean {

    private AbstractInlineCommandBot bot;
    private List<AbstractReplyHandler> replyList;

    public ReplyInitializer(AbstractInlineCommandBot bot, List<AbstractReplyHandler> handlerList) {
        Objects.requireNonNull(bot);
        Objects.requireNonNull(handlerList);

        this.bot = bot;
        this.replyList = handlerList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (AbstractReplyHandler reply : replyList) {
            bot.registerReply(reply);
        }
    }
}
