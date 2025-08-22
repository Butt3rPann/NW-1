package sit.integrated.backend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sit.integrated.backend.dtos.SellerRequestDto;
import sit.integrated.backend.dtos.SellerResponseDto;
import sit.integrated.backend.entities.Seller;
import sit.integrated.backend.exceptions.EmailAlreadyExistsException;
import sit.integrated.backend.repositories.SellerRepository;


@Service
public class SellerService {
    @Autowired
    private SellerRepository sellerRepository ;

    @Autowired
    ModelMapper modelMapper;

    public void isSellerExists(String email) {
        if(sellerRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Seller email : " + email + " already exists." );
        }
    }

    @Transactional
    public SellerResponseDto createSeller(SellerRequestDto sellerRequestDto) {
        isSellerExists(sellerRequestDto.getEmail());
        sellerRequestDto.setId(null);
        Seller seller = modelMapper.map(sellerRequestDto, Seller.class);
        return modelMapper.map(sellerRepository.save(seller), SellerResponseDto.class);
    }
}
