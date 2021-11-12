package org.voronov.boot.config;

import org.springframework.beans.factory.InitializingBean;
import org.voronov.boot.bot.commands.core.AbstractCommand;
import org.voronov.boot.bot.core.AbstractInlineCommandBot;

import java.util.List;
import java.util.Objects;

public class CommandInitializer implements InitializingBean {
    private final AbstractInlineCommandBot bot;
    private final List<AbstractCommand> commands;

    public CommandInitializer(AbstractInlineCommandBot bot, List<AbstractCommand> commands) {
        Objects.requireNonNull(bot);
        Objects.requireNonNull(commands);
        this.bot = bot;
        this.commands = commands;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (AbstractCommand command : commands) {
            bot.register(command);
        }
    }
}
