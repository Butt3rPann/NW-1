package sit.integrated.backend.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("file")
@Getter
@Setter
public class FileStorageProperties {
    private String uploadDir;
    private String[] supportFileTypes;
}
