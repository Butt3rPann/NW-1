package sit.integrated.backend.controllers.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.integrated.backend.dtos.SellerRequestDto;
import sit.integrated.backend.dtos.SellerResponseDto;
import sit.integrated.backend.services.FileService;
import sit.integrated.backend.services.SellerService;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/v2")
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @Autowired
    private FileService fileService;

    @PostMapping("/seller")
    public ResponseEntity<SellerResponseDto> createSeller(@ModelAttribute SellerRequestDto sellerRequestDto,
                                                          @RequestParam List<MultipartFile> sellerNationalIdPhotos) {
        SellerResponseDto seller = sellerService.createSeller(sellerRequestDto);
        if (!sellerNationalIdPhotos.isEmpty()) {
            fileService.storeNationalId(sellerNationalIdPhotos, seller.getId());
        }
        seller.setSellerNationalIdPhotoDto(fileService.getSellerPhotos(seller.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(seller);
    }
}
