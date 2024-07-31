package org.example.mappers;

import org.example.entities.Parent;
import org.springframework.stereotype.Component;

@Component
public class ParentMapper {
    public Parent parent(long chatId) {
        Parent parent = new Parent();
        parent.setChatId(chatId);
        return parent;
    }
}
