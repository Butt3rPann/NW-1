package sit.integrated.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.UserSignInDto;
import sit.integrated.backend.entities.AuthUserDetail;
import sit.integrated.backend.utils.JwtUtils;
import sit.integrated.backend.utils.Role;
import sit.integrated.backend.utils.TokenType;
import sit.integrated.backend.utils.UserStatus;

import java.util.Map;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    public void validateEmailAndPassword(String email, String password) {
        if (email == null || email.isEmpty() || email.length() > 50
                || !email.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
                || password == null || password.isEmpty() || password.length() > 14) {
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
//            return Map.of("access_token", jwtUtils.generateToken(userDetails, ((AuthUserDetail) userDetails).getRole(), ((AuthUserDetail) userDetails).getNickname(), (long) 60*1000, TokenType.ACCESS_TOKEN),
//                    "refresh_token", jwtUtils.generateToken(userDetails, ((AuthUserDetail) userDetails).getRole(), ((AuthUserDetail) userDetails).getNickname(), (long) 60*1000*2, TokenType.REFRESH_TOKEN));
            return Map.of("access_token", jwtUtils.generateToken(userDetails, ((AuthUserDetail) userDetails).getRole(), ((AuthUserDetail) userDetails).getNickname(), (long) 60*1000*30, TokenType.ACCESS_TOKEN),
                    "refresh_token", jwtUtils.generateToken(userDetails, ((AuthUserDetail) userDetails).getRole(), ((AuthUserDetail) userDetails).getNickname(), (long) 60*1000*60*24, TokenType.REFRESH_TOKEN));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
    }

    public Map<String, Object> refreshToken(String refreshToken) {
        jwtUtils.verifyToken(refreshToken);
        Map<String, Object> claims = jwtUtils.getJWTClaimsSet(refreshToken);
        if (jwtUtils.isExpired(claims)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }
        if (! jwtUtils.isValidClaims(claims) || ! "REFRESH_TOKEN".equals(claims.get("typ"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername((String) claims.get("email"));
        String roleStr = (String) claims.get("role");
        Role role = Role.valueOf(roleStr);
        String nickname = (String) claims.get("nickname");

        return Map.of("access_token", jwtUtils.generateToken(userDetails, role, nickname, (long) 60*1000*30, TokenType.ACCESS_TOKEN));
    }
}
