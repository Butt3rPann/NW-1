package sit.integrated.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sit.integrated.backend.utils.FileStorageProperties;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileService {
    private final Path fileStorageLocation;
    private final FileStorageProperties fileStorageProperties;

    public FileService(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                                        .toAbsolutePath()
                                        .normalize();
        try {
            if (!Files.exists(this.fileStorageLocation)) {
                Files.createDirectory(this.fileStorageLocation);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Can’t create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Boolean isSupportedContentType(MultipartFile file) {
        List<String> supportFileType = Arrays.stream(fileStorageProperties.getSupportFileTypes()).toList();
        return supportFileType.contains(file.getContentType());
    }

    public String store(MultipartFile file) {
        if(!isSupportedContentType(file)) {
            return null;
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file " + fileName, ex);
        }
    }

    public List<String> store(List<MultipartFile> files) {
        List<String> fileNames = new ArrayList<>();
        files.forEach(file -> {
            String fileName = store(file);
            if (fileName != null) {
                fileNames.add(fileName);
            }
        });
        return fileNames;
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new ResourceNotFoundException("File not found " + fileName);
            }
            return resource;
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File operation error: " + fileName, ex);
        }
    }

    public String getFileType(Resource resource) {
        try {
            String type = Files.probeContentType(resource.getFile().toPath());
            return type == null ? "image/jpeg" : type;
        } catch (IOException ex) {
            throw new RuntimeException("ProbeContentType error: " + resource, ex);
        }
    }
}
