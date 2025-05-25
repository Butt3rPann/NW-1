package sit.integrated.backend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sit.integrated.backend.dtos.SaleItemDetailDto;
import sit.integrated.backend.dtos.SaleItemFormDto;
import sit.integrated.backend.entities.SaleItem;
import sit.integrated.backend.repositories.SaleItemRepository;


import java.util.List;

@Service
public class SaleItemService {
    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    ModelMapper modelMapper;

    public void isSaleItemExists(Integer id) {
        if(!saleItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("SaleItem not found for this id :: " + id);
        }
    }

    public List<SaleItem> getSaleItems() {
        return saleItemRepository.findAll(Sort.by("createdOn").ascending().and(Sort.by("id")));
    }

    public Page<SaleItem> getSaleItems(List<String> brands, String sortField, String sortDirection, Integer page, Integer size) {
        Sort sort = (sortField == null ? Sort.by("createdOn", "id") : Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        if (brands.isEmpty()) {
            return saleItemRepository.findAll(PageRequest.of(page, size, sort));
        } else {
            return saleItemRepository.findByBrands(brands, PageRequest.of(page, size, sort));
        }
    }

    public SaleItem getSaleItemDetail(Integer id) {
        return saleItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SaleItem not found for this id :: " + id));
    }

    @Transactional
    public SaleItemDetailDto createSaleItem(SaleItemFormDto formDto) {
	    formDto.setId(null);
        SaleItem saleItem = modelMapper.map(formDto, SaleItem.class);
        return modelMapper.map(saleItemRepository.saveAndFlush(saleItem), SaleItemDetailDto.class);
    }

    @Transactional
    public SaleItemDetailDto updateSaleItem(Integer id, SaleItemFormDto formDto) {
        isSaleItemExists(id);
        formDto.setId(id);
        SaleItem saleItem = modelMapper.map(formDto, SaleItem.class);
        return modelMapper.map(saleItemRepository.saveAndFlush(saleItem), SaleItemDetailDto.class);
    }

    @Transactional
    public void deleteSaleItem (Integer id) {
        isSaleItemExists(id);
        saleItemRepository.deleteById(id);
    }

}
