package sit.integrated.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.integrated.backend.entities.Saleitem;
import sit.integrated.backend.repositories.SaleItemRepository;

import java.util.List;

@Service
public class SaleItemService {
    @Autowired
    private SaleItemRepository saleItemRepository;

    public List<Saleitem> getSaleItems() {
        return saleItemRepository.findAll();
    }
}
