package sit.integrated.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import sit.integrated.backend.entities.Seller;
import sit.integrated.backend.repositories.SellerRepository;

@Service
public class SellerService {
    @Autowired
    private SellerRepository sellerRepository;

    public Seller getSellerById(Integer id) {
        return sellerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("seller not found"));
    }
}

