package sit.integrated.backend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.*;
import sit.integrated.backend.entities.*;
import sit.integrated.backend.exceptions.EmailAlreadyExistsException;
import sit.integrated.backend.repositories.SellerRepository;
import sit.integrated.backend.repositories.UserRepository;
import sit.integrated.backend.utils.Role;
import sit.integrated.backend.utils.UserStatus;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private ModelMapper modelMapper;

    public UserResponseDto getUserResponseDtoById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Does not Exist"));
        UserResponseDto dto = modelMapper.map(user, UserResponseDto.class);
        if (user.getUserType().equals(Role.SELLER)) {
            Seller seller = sellerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
            dto.setPhoneNumber(seller.getPhoneNumber());
        }
        return dto;
    }

    public void isUserExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("User email : " + email + " already exists.");
        }
    }

    public void validateUser(UserRequestDto user) {
        if (user.getNickName() == null ||
                user.getEmail() == null ||
                user.getPassword() == null ||
                user.getFullName() == null) {
            throw new IllegalArgumentException("Missing required fields for User");
        }
        if (user.getUserType().equals(Role.SELLER)) {
            if (user.getPhoneNumber() == null ||
                    user.getBankAccount() == null ||
                    user.getBankName() == null ||
                    user.getIdCardNumber() == null ||
                    user.getIdCardImageFront() == null ||
                    user.getIdCardImageBack() == null) {
                throw new IllegalArgumentException("Missing required fields for Seller");
            }
        }
        validateEmailAndPassword(user.getEmail(), user.getPassword());
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        validateUser(userRequestDto);
        isUserExists(userRequestDto.getEmail());

        userRequestDto.setId(null);
        userRequestDto.setStatus(UserStatus.INACTIVE);
        userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        User user = userRepository.save(modelMapper.map(userRequestDto, User.class));

        UserResponseDto response = modelMapper.map(user, UserResponseDto.class);
        if (user.getUserType().equals(Role.SELLER)) {
            Seller seller = modelMapper.map(userRequestDto, Seller.class);
            seller.setUser(user);
            Seller savedSeller = sellerRepository.save(seller);
            response.setPhoneNumber(savedSeller.getPhoneNumber());
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

    public void validateEmailAndPassword(String email, String password) {
        if (email == null || email.isEmpty() || email.length() > 50
                || !email.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
                || password == null || password.isEmpty() || password.length() > 14) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or Password is incorrect");
        }
    }

    public void validateUserProfile(UserProfileDto userProfileDto) {
        if (userProfileDto.getIdCardNumber() == null) {
            userProfileDto.setIdCardNumber("");
        }
        if (userProfileDto.getNickName() == null ||
                userProfileDto.getEmail() == null ||
                userProfileDto.getFullName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data");
        }
        if (!userProfileDto.getEmail().trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data");
        }
        if (userProfileDto.getUserType() != null && userProfileDto.getUserType().equals(Role.SELLER)) {
            if (userProfileDto.getPhoneNumber() == null ||
                    userProfileDto.getBankAccount() == null ||
                    userProfileDto.getBankName() == null ) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data");
            }
        }
    }

    public BuyerResponseDto getUserProfileById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        if (user.getStatus().equals(UserStatus.INACTIVE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not active");
        }

        return getBuyerOrSellerResponseDto(id, user);
    }

    @Transactional
    public BuyerResponseDto updateUserProfileById(Integer id, UserProfileDto userProfileDto) {
        userProfileDto.setId(id);
        validateUserProfile(userProfileDto);
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        if (user.getStatus().equals(UserStatus.INACTIVE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not active");
        }
        modelMapper.map(userProfileDto, user);
        userRepository.save(user);
        return getBuyerOrSellerResponseDto(id, user);
    }

    private BuyerResponseDto getBuyerOrSellerResponseDto(Integer id, User user) {
        if (user.getUserType().equals(Role.BUYER)) {
            return modelMapper.map(user, BuyerResponseDto.class);
        } else {
            Seller seller = sellerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
            SellerResponseDto dto = modelMapper.map(user, SellerResponseDto.class);
            modelMapper.map(seller, dto);
            return dto;
        }
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("seller not found"));
    }
}
