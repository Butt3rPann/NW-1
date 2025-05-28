package sit.integrated.backend.utils;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import sit.integrated.backend.dtos.PageDto;

public class ListMapper {
    private static final ListMapper listMapper = new ListMapper();
    private ListMapper() { }
    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass, ModelMapper modelMapper) {
        return source.stream().map(entity -> modelMapper.map(entity, targetClass)).toList();
    }
    public static ListMapper getInstance() {
        return listMapper;
    }

    public <S, T> PageDto<T> toPageDto(Page<S> source, Class<T> targetClass, ModelMapper modelMapper) {
        PageDto<T> pageDto = modelMapper.map(source, PageDto.class);
        pageDto.setContent(mapList(source.getContent(), targetClass, modelMapper));
        return pageDto;
    }
}