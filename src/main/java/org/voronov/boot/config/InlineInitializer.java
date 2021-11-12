package org.voronov.boot.config;

import org.springframework.beans.factory.InitializingBean;
import org.voronov.boot.bot.core.AbstractInlineCommandBot;
import org.voronov.boot.bot.inlines.core.AbstractInlineHandler;

import java.util.List;
import java.util.Objects;

public class InlineInitializer implements InitializingBean {

    private AbstractInlineCommandBot bot;
    private List<AbstractInlineHandler> inlineHandlers;

    public InlineInitializer(AbstractInlineCommandBot bot, List<AbstractInlineHandler> inlineHandlers) {
        Objects.requireNonNull(bot);
        Objects.requireNonNull(inlineHandlers);

        this.bot = bot;
        this.inlineHandlers = inlineHandlers;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (AbstractInlineHandler handler : inlineHandlers) {
            bot.registerInline(handler);
        }
    }
}
