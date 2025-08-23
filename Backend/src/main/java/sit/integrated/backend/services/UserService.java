package sit.integrated.backend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sit.integrated.backend.dtos.UserRequestDto;
import sit.integrated.backend.dtos.UserResponseDto;
import sit.integrated.backend.entities.Buyer;
import sit.integrated.backend.entities.Seller;
import sit.integrated.backend.entities.User;
import sit.integrated.backend.exceptions.EmailAlreadyExistsException;
import sit.integrated.backend.repositories.BuyerRepository;
import sit.integrated.backend.repositories.SellerRepository;
import sit.integrated.backend.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    ModelMapper modelMapper;

    public void isUserExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("User email : " + email + " already exists." );
        }
    }

    public void validateUser(UserRequestDto user) {
        if (user.getNickname() == null || user.getEmail() == null || user.getPassword() == null || user.getFullname() == null) {
            throw new IllegalArgumentException("Missing required fields for user");
        }
        if ("SELLER".equalsIgnoreCase(user.getUserType())) {
            if (user.getMobileNumber() == null || user.getBankAccountNumber() == null || user.getBankName() == null || user.getNationalId() == null) {
                throw new IllegalArgumentException("Missing required fields for Seller");
            }
        }
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        validateUser(userRequestDto);
        isUserExists(userRequestDto.getEmail());
        userRequestDto.setId(null);
        userRequestDto.setStatus("INACTIVE");
        User user = userRepository.save(modelMapper.map(userRequestDto, User.class));
        if ("SELLER".equalsIgnoreCase(user.getUserType())) {
            Seller seller = modelMapper.map(userRequestDto, Seller.class);
            seller.setUser(user);
            sellerRepository.save(seller);
        } else if ("BUYER".equalsIgnoreCase(user.getUserType())) {
            Buyer buyer = modelMapper.map(userRequestDto, Buyer.class);
            buyer.setUser(user);
            buyerRepository.save(buyer);
        }
        return modelMapper.map(userRepository.save(user), UserResponseDto.class);
    }
}
