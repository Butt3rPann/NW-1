package sit.integrated.backend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sit.integrated.backend.dtos.*;
import sit.integrated.backend.entities.SaleItem;
import sit.integrated.backend.entities.Seller;
import sit.integrated.backend.repositories.SaleItemRepository;
import sit.integrated.backend.repositories.SellerRepository;
import sit.integrated.backend.utils.SaleItemSpecifications;

import java.util.List;

@Service
public class SaleItemService {
    @Autowired
    private SaleItemRepository saleItemRepository;
    @Autowired
    private SellerRepository sellerRepository;
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

    public Specification<SaleItem> findFilteredItems(List<String> brands, List<Integer> filterStorages, boolean hasNull, Integer filterPriceLower, Integer filterPriceUpper, String keyword) {
        return Specification.where(SaleItemSpecifications.hasBrand(brands)
                .and(SaleItemSpecifications.hasPriceLessThanOrEqual(filterPriceUpper))
                .and(SaleItemSpecifications.hasPriceGreaterThanOrEqual(filterPriceLower))
                .and(SaleItemSpecifications.hasStorages(filterStorages, hasNull))
                .and(SaleItemSpecifications.hasKeyWord(keyword)));
    }

    public Page<SaleItem> getSaleItems(List<String> brands, List<Integer> filterStorages, Integer filterPriceLower, Integer filterPriceUpper, String keyword, String sortField, String sortDirection,  Integer page, Integer size) {
        Sort sort = (sortField == null ? Sort.by("createdOn", "id") : Sort.by(Sort.Direction.fromString(sortDirection), sortField).and(Sort.by("id")));
        if (brands == null && filterStorages == null && filterPriceLower == null && filterPriceUpper == null && (keyword == null || keyword.isBlank())) {
            return saleItemRepository.findAll(PageRequest.of(page, size, sort));
        } else {
            return saleItemRepository.findAll(findFilteredItems(brands, filterStorages, filterStorages != null && filterStorages.contains(null), filterPriceLower, filterPriceUpper, keyword), PageRequest.of(page, size, sort));
        }
    }

    public Page<SaleItem> getSaleItemsBySeller(Integer sellerId, String sortField, String sortDirection, Integer page, Integer size) {
        Sort sort = (sortField == null ? Sort.by("createdOn", "id") : Sort.by(Sort.Direction.fromString(sortDirection), sortField).and(Sort.by("id")));
        return saleItemRepository.getSaleItemsBySeller(sellerId, PageRequest.of(page, size, sort));
    }

    public SaleItem getSaleItemDetail(Integer id) {
        return saleItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SaleItem not found for this id :: " + id));
    }

    @Transactional
    public SaleItemDetailDto createSaleItem(SaleItemFormDto formDto) {
	    formDto.setId(null);
        SaleItem saleItem = modelMapper.map(formDto, SaleItem.class);
        return modelMapper.map(saleItemRepository.save(saleItem), SaleItemDetailDto.class);
    }

    @Transactional
    public SaleItemDetailDto updateSaleItem(Integer id, SaleItemFormDto formDto) {
        isSaleItemExists(id);
        formDto.setId(id);
        SaleItem saleItem = modelMapper.map(formDto, SaleItem.class);
        return modelMapper.map(saleItemRepository.save(saleItem), SaleItemDetailDto.class);
    }

    @Transactional
    public void deleteSaleItem (Integer id) {
        isSaleItemExists(id);
        saleItemRepository.deleteById(id);
    }

    public SaleItemDetailDto createSaleItemBySeller(SaleItemFormDto formDto, Integer sellerId) {
        formDto.setId(null);
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
        SaleItem saleItem = modelMapper.map(formDto, SaleItem.class);
        saleItem.setSeller(seller);
        return modelMapper.map(saleItemRepository.save(saleItem), SaleItemDetailDto.class);
    }
}
