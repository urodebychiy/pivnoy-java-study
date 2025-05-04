package util;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

// Утилитный класс, сюда можно закидывать всякие полезные методы, которые облегчают написание кода
@Component
public class MyUtility {
    public static <T> T findEntityById(Optional<T> entity, String entityName, String entityId) {
        return entity
                .orElseThrow(() -> new EntityNotFoundException(String.format("Entity %s with id = %s was not found", entityName, entityId)));
    }
}
