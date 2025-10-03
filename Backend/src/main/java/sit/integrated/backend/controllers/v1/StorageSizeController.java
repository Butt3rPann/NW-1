package sit.integrated.backend.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.integrated.backend.entities.ViewStoragegb;
import sit.integrated.backend.services.StorageSizeService;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class StorageSizeController {
    @Autowired
    private StorageSizeService storageSizeService;

    @GetMapping("/storage-size")
    public List<ViewStoragegb> getAllStorages() {
        return storageSizeService.getAllStorageSizes();
    }

}
