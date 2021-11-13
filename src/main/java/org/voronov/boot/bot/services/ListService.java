package org.voronov.boot.bot.services;

import org.springframework.stereotype.Component;
import org.voronov.boot.bot.model.dto.Operation;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListService {

    public List<Operation> narrowByUsers(List<Operation> operations, Long mainUser, Long anotherUser) {
        return operations.stream().filter(a -> (a.getuTo().getUser().getId().equals(mainUser) && a.getuFrom().getUser().getId().equals(anotherUser))
                || ((a.getuFrom().getUser().getId().equals(mainUser) && a.getuTo().getUser().getId().equals(anotherUser)))).collect(Collectors.toList());
    }
}
