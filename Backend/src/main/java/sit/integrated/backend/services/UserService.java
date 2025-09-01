package sit.integrated.backend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.UserRequestDto;
import sit.integrated.backend.dtos.UserResponseDto;

import sit.integrated.backend.dtos.UserSignInDto;
import sit.integrated.backend.entities.AuthUserDetail;
import sit.integrated.backend.entities.Buyer;
import sit.integrated.backend.entities.Seller;
import sit.integrated.backend.entities.User;
import sit.integrated.backend.exceptions.EmailAlreadyExistsException;
import sit.integrated.backend.repositories.BuyerRepository;
import sit.integrated.backend.repositories.SellerRepository;
import sit.integrated.backend.repositories.UserRepository;
import sit.integrated.backend.utils.JwtUtils;
import sit.integrated.backend.utils.Role;
import sit.integrated.backend.utils.TokenType;
import sit.integrated.backend.utils.UserStatus;

import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

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

    public void validateEmailAndPassword(String email, String password) {
        if (email == null || email.length() == 0 || email.length() > 50
                || !email.trim().matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
                || password == null || password.length() == 0 || password.length() > 14) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or Password is incorrect");
        }
    }

    public Map<String, Object> authenticateUser(UserSignInDto user) {
        validateEmailAndPassword(user.getEmail(), user.getPassword());
        UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        try {
            authenticationManager.authenticate(upat);
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(user.getEmail());
            if (((AuthUserDetail) userDetails).getStatus().equals(UserStatus.INACTIVE)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User account is inactive.");
            }
            return Map.of("access_token", jwtUtils.generateToken(userDetails, ((AuthUserDetail) userDetails).getRole(), ((AuthUserDetail) userDetails).getNickname(), (long) 60*1000*60*24, TokenType.ACCESS_TOKEN),
                          "refresh_token", jwtUtils.generateToken(userDetails, ((AuthUserDetail) userDetails).getRole(), ((AuthUserDetail) userDetails).getNickname(), (long) 60*1000*30, TokenType.REFRESH_TOKEN));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
    }

    public Map<String, Object> refreshToken(String refreshToken) {
        jwtUtils.verifyToken(refreshToken);
        Map<String, Object> claims = jwtUtils.getJWTClaimsSet(refreshToken);
        jwtUtils.isExpired(claims);
        if (! jwtUtils.isValidClaims(claims) || ! "REFRESH_TOKEN".equals(claims.get("typ"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername((String) claims.get("email"));
        Role role = (Role) claims.get("role");
        String nickname = (String) claims.get("nickname");

        return Map.of("access_token", jwtUtils.generateToken(userDetails, role, nickname, (long) 60*1000*30, TokenType.ACCESS_TOKEN));
    }
}
