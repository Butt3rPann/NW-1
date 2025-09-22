package sit.integrated.backend.controllers.v2;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.PageDto;
import sit.integrated.backend.dtos.SaleItemDetailDto;
import sit.integrated.backend.dtos.SaleItemDto;
import sit.integrated.backend.entities.AuthUserDetail;
import sit.integrated.backend.entities.SaleItem;
import sit.integrated.backend.services.FileService;
import sit.integrated.backend.services.SaleItemService;
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
    private FileService fileService;

    @GetMapping("/seller/{id}/sale-items")
    public ResponseEntity<PageDto<SaleItemDetailDto>> getSaleItems(@PathVariable Integer id,
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
        PageDto<SaleItemDetailDto> dtos = listMapper.toPageDto(saleItems, SaleItemDetailDto.class, modelMapper);
        dtos.getContent().forEach(item -> item.setSaleItemImages(fileService.getSaleItemImages(item.getId())));
        return ResponseEntity.ok(dtos);
    }
}
