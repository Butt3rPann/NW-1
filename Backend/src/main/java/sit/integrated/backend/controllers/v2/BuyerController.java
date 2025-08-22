package sit.integrated.backend.controllers.v2;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.integrated.backend.dtos.BuyerRequestDto;
import sit.integrated.backend.dtos.BuyerResponseDto;
import sit.integrated.backend.services.BuyerService;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/v2")
public class BuyerController {
    @Autowired
    private BuyerService buyerService;

    @PostMapping("/buyer")
    public ResponseEntity<BuyerResponseDto> createBuyer(@ModelAttribute BuyerRequestDto buyerRequestDto) {
        BuyerResponseDto buyer = buyerService.createBuyer(buyerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(buyer);
    }
}
