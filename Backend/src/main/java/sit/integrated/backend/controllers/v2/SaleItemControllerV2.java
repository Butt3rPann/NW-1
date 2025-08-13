package sit.integrated.backend.controllers.v2;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.integrated.backend.dtos.PageDto;
import sit.integrated.backend.dtos.SaleItemDetailDto;
import sit.integrated.backend.dtos.SaleItemImageDto;
import sit.integrated.backend.entities.SaleItem;
import sit.integrated.backend.services.FileService;
import sit.integrated.backend.services.SaleItemService;
import sit.integrated.backend.services.StorageSizeService;
import sit.integrated.backend.utils.ListMapper;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/v2")
public class SaleItemControllerV2 {
    @Autowired
    private SaleItemService saleItemService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @GetMapping("/sale-items")
    public ResponseEntity<PageDto<SaleItemDetailDto>> getSaleItems(@RequestParam(required = false) String sortField,
                                                                   @RequestParam(required = false, defaultValue = "asc") String sortDirection,
                                                                   @RequestParam(required = false) List<String> filterBrands,
                                                                   @RequestParam(required = false) List<Integer> filterStorages,
                                                                   @RequestParam(required = false) Integer filterPriceLower,
                                                                   @RequestParam(required = false) Integer filterPriceUpper,
                                                                   @RequestParam Integer page,
                                                                   @RequestParam(required = false, defaultValue = "10") Integer size) {

        if (filterStorages != null && filterStorages.contains(-1)) {
            filterStorages = filterStorages.stream().map(value -> value == -1 ? null : value).toList();
        }

        if (filterPriceLower != null && filterPriceUpper != null && filterPriceLower > filterPriceUpper) {
            int temp = filterPriceLower;
            filterPriceLower = filterPriceUpper;
            filterPriceUpper = temp;
        } else if (filterPriceLower != null && filterPriceUpper == null) {
            filterPriceUpper = filterPriceLower;
        }

        Page<SaleItem> saleItems = saleItemService.getSaleItems(filterBrands, filterStorages, filterPriceLower, filterPriceUpper, sortField, sortDirection, page, size);
        return ResponseEntity.ok(listMapper.toPageDto(saleItems, SaleItemDetailDto.class, modelMapper));
    }

    @GetMapping("/sale-items/{id}")
    public ResponseEntity<SaleItemDetailDto> getSaleItemDetail(@PathVariable Integer id) {
        SaleItem saleItem = saleItemService.getSaleItemDetail(id);
        List<String> matchedFiles = fileService.getMatchedFiles(id + ".*");
        SaleItemDetailDto saleItemDetailDto = modelMapper.map(saleItem, SaleItemDetailDto.class);
        List<SaleItemImageDto> saleItemImages = new ArrayList<>();
        int imageViewOrder = 1;
        for (String fileName :  matchedFiles) {
            SaleItemImageDto saleItemImage =  new SaleItemImageDto();
            saleItemImage.setFileName(fileName);
            saleItemImage.setImageViewOrder(imageViewOrder++);
            saleItemImages.add(saleItemImage);         }
        saleItemDetailDto.setSaleItemImages(saleItemImages);
        return ResponseEntity.ok(saleItemDetailDto);
    }

    @DeleteMapping("/sale-items/{id}")
    public ResponseEntity<Void> deleteSaleItem(@PathVariable Integer id) {
        String pattern = id + ".";
        List<String> matchedFiles = fileService.getMatchedFiles(pattern);
        for (String fileName : matchedFiles) {
            fileService.removeFile(fileName);
        }
        saleItemService.deleteSaleItem(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}