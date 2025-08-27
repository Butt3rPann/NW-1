package sit.integrated.backend.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.integrated.backend.services.FileService;
import sit.integrated.backend.utils.FileStorageProperties;

@RestController
@RequestMapping("/v1")
public class FileController {
    @Autowired
    FileStorageProperties fileStorageProperties;
    @Autowired
    FileService fileService;

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = fileService.loadFileAsResource(filename);
        return ResponseEntity.ok().contentType(MediaType.valueOf(fileService.getFileType(file))).body(file);
    }
}
