package org.voronov.boot.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.voronov.boot.core.AbstractCommand;
import org.voronov.boot.core.AbstractInlineCommandBot;
import org.voronov.boot.core.AbstractInlineHandler;
import org.voronov.boot.core.AbstractReplyHandler;

import java.util.Collections;
import java.util.List;

@Configuration
public class BeanConfig {

    @Bean
    public CommandInitializer commandInitializer(AbstractInlineCommandBot bot, ObjectProvider<List<AbstractCommand>> commands) {
        return new CommandInitializer(bot, commands.getIfAvailable(Collections::emptyList));
    }

    @Bean
    public InlineInitializer inlineInitializer(AbstractInlineCommandBot bot, ObjectProvider<List<AbstractInlineHandler>> inlines) {
        return new InlineInitializer(bot, inlines.getIfAvailable(Collections::emptyList));
    }

    @Bean
    public ReplyInitializer replyInitializer(AbstractInlineCommandBot bot, ObjectProvider<List<AbstractReplyHandler>> replyList) {
        return new ReplyInitializer(bot, replyList.getIfAvailable(Collections::emptyList));
    }
}
