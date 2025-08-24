package sit.integrated.backend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sit.integrated.backend.dtos.BuyerRequestDto;
import sit.integrated.backend.dtos.BuyerResponseDto;
import sit.integrated.backend.exceptions.EmailAlreadyExistsException;
import sit.integrated.backend.repositories.BuyerRepository;

@Service
public class BuyerService {
    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    ModelMapper modelMapper;

    public void isBuyerExists(String email) {
        if(buyerRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Buyer email : " + email + " already exists." );
        }
    }

    @Transactional
    public BuyerResponseDto createBuyer(BuyerRequestDto buyerRequestDto) {
        isBuyerExists(buyerRequestDto.getEmail());
        buyerRequestDto.setId(null);
        Buyer buyer = modelMapper.map(buyerRequestDto, Buyer.class);
        return modelMapper.map(buyerRepository.save(buyer), BuyerResponseDto.class);
    }
}
