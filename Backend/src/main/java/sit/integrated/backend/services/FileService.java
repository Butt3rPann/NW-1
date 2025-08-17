package sit.integrated.backend.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sit.integrated.backend.dtos.SaleItemImageDto;
import sit.integrated.backend.dtos.SaleItemImageRequest;
import sit.integrated.backend.utils.FileStorageProperties;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    public String getExtension(String fileName) {
        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            extension = fileName.substring(dotIndex);
        }
        return extension;
    }

    public String store(MultipartFile file, Integer id, int imgNumber) {
        if(!isSupportedContentType(file)) {
            return null;
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Filename contains invalid path sequence " + fileName);
            }
            String extension = getExtension(fileName);
            String newFileName = id + "." + imgNumber + extension;

            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file " + fileName, ex);
        }
    }

    public List<String> store(List<MultipartFile> files, Integer id) {
        List<String> fileNames = new ArrayList<>();
        int imgNumber = 1;
        for (MultipartFile file : files) {
            if (file == null) {
                imgNumber++;
                continue;
            }
            String fileName = store(file, id, imgNumber);
            if (fileName != null) {
                fileNames.add(fileName);
            }
            imgNumber++;
        }
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

    public List<String> getMatchedFiles(String pattern) {
        List<String> fileNames = new ArrayList<>();
        FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                FileSystem fileSystem = FileSystems.getDefault();
                PathMatcher pathMatcher = fileSystem.getPathMatcher("glob:" + pattern);
                Path name = file.getFileName();
                if (pathMatcher.matches(name)) {
                    fileNames.add(name.toString());
                }
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            Files.walkFileTree(this.fileStorageLocation, matcherVisitor);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return fileNames;
    }

    public List<SaleItemImageDto> getSaleItemImages(Integer id) {
        String pattern = id + ".*";
        List<String> fileNames = getMatchedFiles(pattern);
        Collections.sort(fileNames);
        List<SaleItemImageDto> saleItemImageDtos = new ArrayList<>();
        int imageViewOrder = 1;
        for (String fileName : fileNames) {
            SaleItemImageDto saleItemImage = new SaleItemImageDto();
            saleItemImage.setFileName(fileName);
            saleItemImage.setImageViewOrder(imageViewOrder++);
            saleItemImageDtos.add(saleItemImage);
        }
        return saleItemImageDtos;
    }

    public void removeFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            if(Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                throw new ResourceNotFoundException("File not found " + fileName);
            }
        } catch (IOException ex) {
            throw new RuntimeException("File operation (DELETE) error: " + fileName, ex);
        }
    }

    public void moveFile(Path src, Path dest) {
        try {
            Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to move file: " + src + " to " + dest, e);
        }
    }

    public void createOrUpdateSaleItemImages(Integer id, List<SaleItemImageRequest> imageInfos) {
        List<MultipartFile> newFiles = new ArrayList<>();
        List<Path[]> renameList = new ArrayList<>();
        int imgNumber = 1;

        for (SaleItemImageRequest image : imageInfos) {
            switch (image.getStatus()) {
                case "ONLINE":
                    newFiles.add(null);
                    imgNumber++;
                    break;

                case "DELETE":
                    removeFile(image.getFileName());
                    break;

                case "MOVE":
                    String extension = getExtension(image.getFileName());
                    Path oldPath = fileStorageLocation.resolve(image.getFileName());
                    Path tempPath = fileStorageLocation.resolve("tmp_" + image.getFileName());
                    moveFile(oldPath, tempPath);
                    Path finalPath = fileStorageLocation.resolve(id.toString() + '.' + imgNumber + extension);
                    renameList.add(new Path[]{tempPath, finalPath});
                    newFiles.add(null);
                    imgNumber++;
                    break;

                case "NEW":
                    newFiles.add(image.getImageFile());
                    imgNumber++;
                    break;
            }
        }

        for (Path[] path : renameList) {
            moveFile(path[0], path[1]);
        }
        store(newFiles, id);
    }
}
