package sit.integrated.backend.utils;

import java.util.List;
import org.modelmapper.ModelMapper;

public class ListMapper {
    private static final ListMapper listMapper = new ListMapper();
    private ListMapper() { }
    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass, ModelMapper modelMapper) {
        return source.stream().map(entity -> modelMapper.map(entity, targetClass)).toList();
    }
    public static ListMapper getInstance() {
        return listMapper;
    }
}