package sit.integrated.backend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
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

    public List<SaleItem> getSaleItems() {
        return saleItemRepository.findAll(Sort.by("createdOn").ascending().and(Sort.by("id")));
    }

    public SaleItem getSaleItemDetail(Integer id) {
        return saleItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SaleItem not found for this id : " + id));
    }

    public SaleItemDetailDto createSaleItem(SaleItemFormDto formDto) {
        SaleItem saleItem = modelMapper.map(formDto, SaleItem.class);
        return modelMapper.map(saleItemRepository.saveAndFlush(saleItem), SaleItemDetailDto.class);
    }

    public SaleItemDetailDto updateSaleItem(Integer id, SaleItemFormDto formDto) {
        if(!saleItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("SaleItem not found for this id : " + id);
        }
        formDto.setId(id);
        SaleItem saleItem = modelMapper.map(formDto, SaleItem.class);
        return modelMapper.map(saleItemRepository.saveAndFlush(saleItem), SaleItemDetailDto.class);
    }

    public void deleteSaleItem (Integer id) {
        SaleItem saleItem = getSaleItemDetail(id);
        saleItemRepository.delete(saleItem);
    }
}
