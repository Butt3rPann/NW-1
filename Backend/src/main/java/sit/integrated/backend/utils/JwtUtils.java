package sit.integrated.backend.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.entities.AuthUserDetail;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {
    @Value("#{${app.security.jwt.token-max-interval-in-minute}*1000*60*24}")
    private long MAX_EMAIL_TOKEN_INTERVAL;

    @Value("${app.security.jwt.key-id}")
    private String KEY_ID;

    private RSAKey rsaPrivateJWK;
    private RSAKey rsaPublicJWK;

    public JwtUtils() {
        try {
            rsaPrivateJWK = new RSAKeyGenerator(2048)
                    .keyID(KEY_ID).generate();
            rsaPublicJWK = rsaPrivateJWK.toPublicJWK();
            System.out.println(rsaPublicJWK.toJSONString());
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateEmailToken(Integer id, String email, Role role) {
        try {
            JWSSigner signer = new RSASSASigner(rsaPrivateJWK);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(email)
                    .issuer("https://intproj24.sit.kmutt.ac.th/nw1")
                    .expirationTime(new Date(new Date().getTime() + MAX_EMAIL_TOKEN_INTERVAL))
                    .issueTime(new Date(new Date().getTime()))
                    .claim("typ", TokenType.EMAIL_TOKEN.toString())
                    .claim("role", role.toString())
                    .claim("userId", id)
                    .claim("email", email)
                    .build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .keyID(rsaPrivateJWK.getKeyID()).build(), claimsSet);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(UserDetails user, Role role, String nickname, Long tokenAgeInMiliseconds, TokenType typ) {
        try {
            JWSSigner signer = new RSASSASigner(rsaPrivateJWK);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .issuer("https://intproj24.sit.kmutt.ac.th/nw1")
                    .issueTime(new Date(new Date().getTime()))
                    .expirationTime(new Date(new Date().getTime() + tokenAgeInMiliseconds))
                    .claim("nickname", nickname)
                    .claim("id", ((AuthUserDetail) user).getId())
                    .claim("email", user.getUsername())
                    .claim("role", role)
                    .claim("typ", typ.toString())
                    .build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .keyID(rsaPrivateJWK.getKeyID()).build(), claimsSet);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public void verifyToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier(rsaPublicJWK);
            boolean passed = signedJWT.verify(verifier);
            if(!passed) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Verified Error, Invalid JWT");
            }
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            Integer userId = claims.getIntegerClaim("userId");
            String email = claims.getStringClaim("email");
            if (userId == null || email == null || email.isBlank()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token claims: userId or email missing");
            }
        } catch (JOSEException | ParseException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Verified Error, Invalid JWT", ex);
        }
    }

    public Map<String, Object> getJWTClaimsSet(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return  signedJWT.getJWTClaimsSet().getClaims();
        } catch (ParseException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT (Can't parsed)", ex);
        }
    }

    public boolean isExpired(Map<String, Object> token) {
        Date expDate = (Date) token.get("exp");
        return expDate.before(new Date());
    }

    public boolean isValidClaims(Map<String, Object> jwtClaims) {
        return jwtClaims.containsKey("iat")
                && "https://intproj24.sit.kmutt.ac.th/nw1"
                .equals(jwtClaims.get("iss"))
                && jwtClaims.containsKey("uid")
                && (Long) jwtClaims.get("uid") > 0;
    }
}
