package club.kwcoder.book.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private String email;
    private String name;
    private Integer remain;
    private Long borrowTotal;

}
