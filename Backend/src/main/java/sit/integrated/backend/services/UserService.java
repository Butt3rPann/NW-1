package sit.integrated.backend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.UserRequestDto;
import sit.integrated.backend.dtos.UserResponseDto;
import sit.integrated.backend.dtos.UserSignInDto;
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
    private Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(
            16, 16,
            8, 1024 * 128, 2);

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    ModelMapper modelMapper;

    public UserResponseDto getUserById(Integer id) {
        UserResponseDto user = modelMapper.map(userRepository.findById(id), UserResponseDto.class);
        if (user.getUserType().equals(Role.SELLER)) {
            Seller seller = sellerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
            user.setNickName(seller.getNickName());
            user.setFullName(seller.getFullName());
            user.setPhoneNumber(seller.getPhoneNumber());
        } else if (user.getUserType().equals(Role.BUYER)) {
            Buyer buyer = buyerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));
            user.setNickName(buyer.getNickName());
            user.setFullName(buyer.getFullName());
        }
        return user;
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
            response.setNickName(savedSeller.getNickName());
            response.setFullName(savedSeller.getFullName());
            response.setPhoneNumber(savedSeller.getPhoneNumber());
        } else if (user.getUserType().equals(Role.BUYER)) {
            Buyer buyer = modelMapper.map(userRequestDto, Buyer.class);
            buyer.setUser(user);
            Buyer savedBuyer = buyerRepository.save(buyer);
            response.setNickName(savedBuyer.getNickName());
            response.setFullName(savedBuyer.getFullName());
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

    public UserResponseDto loginUser(UserSignInDto userSigninDto) {
        String email = userSigninDto.getEmail();
        String password = userSigninDto.getPassword();
        if (email.isEmpty() || email.length() > 50 ||
                !email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or Password is incorrect");
        }
        if (password.isEmpty() || password.length() > 14) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or Password is incorrect");
        }
        User user = userRepository.findByEmail(userSigninDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password incorrect"));
        if (!passwordEncoder.matches(userSigninDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password is incorrect");
        }
        return modelMapper.map(user, UserResponseDto.class);
    }
}