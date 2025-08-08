package sit.integrated.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sit.integrated.backend.entities.ViewStoragegb;
import sit.integrated.backend.repositories.StorageSizeRepository;

import java.util.List;

@Service
public class StorageSizeService {

    @Autowired
    private StorageSizeRepository storageSizeRepository;

    public List<ViewStoragegb> getAllStorageSizes() {
        return storageSizeRepository.findAll(Sort.by("storageGb").ascending());
    }
}
