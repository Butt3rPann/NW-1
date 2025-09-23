package sit.integrated.backend.controllers.v2;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.*;
import sit.integrated.backend.entities.AuthUserDetail;
import sit.integrated.backend.entities.SaleItem;
import sit.integrated.backend.services.SaleItemService;
import sit.integrated.backend.services.SellerService;
import sit.integrated.backend.utils.ListMapper;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v2")
public class SellerController {
    @Autowired
    private SaleItemService saleItemService;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SellerService sellerService;

    @GetMapping("/sellers/{id}/sale-items")
    public ResponseEntity<PageDto<SaleItemListDto>> getSaleItems(@PathVariable Integer id,
                                                                 @RequestParam(required = false) String sortField,
                                                                 @RequestParam(required = false, defaultValue = "asc") String sortDirection,
                                                                 @RequestParam Integer page,
                                                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        AuthUserDetail userDetail = (AuthUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tokenUserId = userDetail.getId();
        if (!Objects.equals(id, tokenUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Request user id not matched");
        }
        Page<SaleItem> saleItems = saleItemService.getSaleItemsBySeller(id, sortField, sortDirection, page, size);
        PageDto<SaleItemListDto> dtos = listMapper.toPageDto(saleItems, SaleItemListDto.class, modelMapper);
        dtos.getContent().forEach(item -> item.setSeller(modelMapper.map(sellerService.getSellerById(id), SellerDto.class)));
        return ResponseEntity.ok(dtos);
    }
}
