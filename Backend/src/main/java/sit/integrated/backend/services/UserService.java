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
import sit.integrated.backend.utils.Role;
import sit.integrated.backend.utils.UserStatus;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    ModelMapper modelMapper;

    public void isUserExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("User email : " + email + " already exists.");
        }
    }

    public void validateUser(UserRequestDto user) {
        if (user.getNickname() == null ||
                user.getEmail() == null ||
                user.getPassword() == null ||
                user.getFullname() == null) {
            throw new IllegalArgumentException("Missing required fields for User");
        }
        if (user.getUserType().equals(Role.SELLER)) {
            if (user.getMobileNumber() == null ||
                    user.getBankAccountNumber() == null ||
                    user.getBankName() == null ||
                    user.getNationalId() == null) {
                throw new IllegalArgumentException("Missing required fields for Seller");
            }
        }
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        validateUser(userRequestDto);
        isUserExists(userRequestDto.getEmail());

        userRequestDto.setId(null);
        userRequestDto.setStatus(UserStatus.INACTIVE);

        User user = userRepository.save(modelMapper.map(userRequestDto, User.class));
        UserResponseDto response = modelMapper.map(user, UserResponseDto.class);
        if (user.getUserType().equals(Role.SELLER)) {
            Seller seller = modelMapper.map(userRequestDto, Seller.class);
            seller.setUser(user);
            Seller savedSeller = sellerRepository.save(seller);
            response.setNickname(savedSeller.getNickname());
            response.setFullname(savedSeller.getFullname());
            response.setBankAccountNumber(savedSeller.getBankAccountNumber());
            response.setBankName(savedSeller.getBankName());
            response.setMobileNumber(savedSeller.getMobileNumber());
            response.setNationalId(savedSeller.getNationalId());
        } else if (user.getUserType().equals(Role.BUYER)) {
            Buyer buyer = modelMapper.map(userRequestDto, Buyer.class);
            buyer.setUser(user);
            Buyer savedBuyer = buyerRepository.save(buyer);
            response.setNickname(savedBuyer.getNickname());
            response.setFullname(savedBuyer.getFullname());
        }
        return response;
    }

    @Transactional
    public void updateStatus(String email) {
        int updated = userRepository.updateStatusByEmail(email, UserStatus.ACTIVE);
        if (updated == 0) {
            throw new RuntimeException("No user found with email: " + email);
        }
    }
}