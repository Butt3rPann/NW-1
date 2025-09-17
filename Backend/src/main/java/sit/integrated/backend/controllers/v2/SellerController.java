package sit.integrated.backend.controllers.v2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2")
public class SellerController {
    
    @GetMapping("/seller/{id}/sale-items")
    public ResponseEntity<String> getSaleItem() {
        return ResponseEntity.ok("Ok!");
    }
}
