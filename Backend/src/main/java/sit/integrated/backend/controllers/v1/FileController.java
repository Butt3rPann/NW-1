package sit.integrated.backend.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.integrated.backend.services.FileService;
import sit.integrated.backend.utils.FileStorageProperties;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    FileStorageProperties fileStorageProperties;
    @Autowired
    FileService fileService;

    @PostMapping
    public ResponseEntity<Object> uploadFile(@RequestParam List<MultipartFile> files) {
        return ResponseEntity.ok(fileService.store(files));
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = fileService.loadFileAsResource(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(fileService.getFileType(file))).body(file);
    }
}
