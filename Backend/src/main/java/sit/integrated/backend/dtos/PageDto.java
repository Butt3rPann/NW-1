package sit.integrated.backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PageDto<T> {
    private List<T> content;
    private String sort;
}
