package ttv.poltoraha.pivka.dao.request;

import lombok.Builder;
import lombok.Data;

// ДАО, ДТО - это классы, с которыми как правило удобно работать.
// Обычно в контроллеры приходит инфа, которая недостаточна для того, чтобы заполнить полноценную Entity
// Бэк из этих данных достаёт Entity, работает с ней, потом возвращает фронту только нужные данные.
@Data
@Builder
public class ReviewRequestDto {
    private String readerUsername;
    private Integer bookId;
    private String text;
    private Integer rating;
}
