package sit.integrated.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sit.integrated.backend.entities.SaleItem;
import sit.integrated.backend.repositories.SaleItemRepository;

import java.util.List;

@Service
public class SaleItemService {
    @Autowired
    private SaleItemRepository saleItemRepository;

    public List<SaleItem> getSaleItems() {
        return saleItemRepository.findAll(Sort.by(Sort.Direction.ASC, "createdOn"));
    }
}
