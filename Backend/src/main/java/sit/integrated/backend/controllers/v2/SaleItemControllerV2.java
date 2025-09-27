package sit.integrated.backend.controllers.v2;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.integrated.backend.dtos.*;
import sit.integrated.backend.entities.AuthUserDetail;
import sit.integrated.backend.entities.SaleItem;
import sit.integrated.backend.services.FileService;
import sit.integrated.backend.services.SaleItemService;
import sit.integrated.backend.services.UserService;
import sit.integrated.backend.utils.ListMapper;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
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
    @Autowired
    private UserService userService;

    @GetMapping("/sale-items")
    public ResponseEntity<PageDto<SaleItemDetailDto>> getSaleItems(@RequestParam(required = false) String sortField,
                                                                   @RequestParam(required = false, defaultValue = "asc") String sortDirection,
                                                                   @RequestParam(required = false) List<String> filterBrands,
                                                                   @RequestParam(required = false) List<Integer> filterStorages,
                                                                   @RequestParam(required = false) Integer filterPriceLower,
                                                                   @RequestParam(required = false) Integer filterPriceUpper,
                                                                   @RequestParam(required = false) String searchKeyWord,
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

        Page<SaleItem> saleItems = saleItemService.getSaleItems(filterBrands, filterStorages, filterPriceLower, filterPriceUpper, searchKeyWord, sortField, sortDirection, page, size);
        PageDto<SaleItemDetailDto> dtos = listMapper.toPageDto(saleItems, SaleItemDetailDto.class, modelMapper);
        dtos.getContent().forEach(item -> item.setSaleItemImages(fileService.getSaleItemImages(item.getId())));
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/sale-items/{id}")
    public ResponseEntity<SaleItemDetailDto> getSaleItemDetail(@PathVariable Integer id) {
        SaleItem saleItem = saleItemService.getSaleItemDetail(id);
        SaleItemDetailDto saleItemDetailDto = modelMapper.map(saleItem, SaleItemDetailDto.class);
        saleItemDetailDto.setSaleItemImages(fileService.getSaleItemImages(id));
        return ResponseEntity.ok(saleItemDetailDto);
    }

    @PostMapping( "/sale-items")
    public ResponseEntity<SaleItemDetailDto> createSaleItem(@ModelAttribute SaleItemFormDto formDto,
                                                            @RequestParam(required = false) List<MultipartFile> images) {
        AuthUserDetail userDetail = (AuthUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tokenUserId = userDetail.getId();
        SaleItemDetailDto saleItem = saleItemService.createSaleItemBySeller(formDto, tokenUserId);
        if (images != null && !images.isEmpty()) {
            fileService.storeSaleItem(images, saleItem.getId());
        }
        saleItem.setSaleItemImages(fileService.getSaleItemImages(saleItem.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(saleItem);
    }

    @PutMapping("/sale-items/{id}")
    public ResponseEntity<SaleItemDetailDto> updateSaleItem(@PathVariable Integer id,
                                                            @ModelAttribute SaleItemWithImageInfo request) {
        SaleItemDetailDto saleItemDetailDto =
                (request.getSaleItem() != null) ? saleItemService.updateSaleItem(id, request.getSaleItem())
                                                : modelMapper.map(saleItemService.getSaleItemDetail(id), SaleItemDetailDto.class);

        if (request.getImageInfos() != null && !request.getImageInfos().isEmpty()) {
            fileService.createOrUpdateSaleItemImages(id, request.getImageInfos());
        }

        saleItemDetailDto.setSaleItemImages(fileService.getSaleItemImages(id));
        return ResponseEntity.ok(saleItemDetailDto);
    }

    @DeleteMapping("/sale-items/{id}")
    public ResponseEntity<Void> deleteSaleItem(@PathVariable Integer id) {
        List<SaleItemImageDto> matchedFiles = fileService.getSaleItemImages(id);
        for (SaleItemImageDto saleItemImage : matchedFiles) {
            fileService.removeFile(saleItemImage.getFileName());
        }
        saleItemService.deleteSaleItem(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
