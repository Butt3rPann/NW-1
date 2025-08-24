package sit.integrated.backend.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {
    @Value("#{${app.security.jwt.token-max-interval-in-minute}*1000*60}")
    private long MAX_TOKEN_INTERVAL;

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

    public String generateToken(String email, Role role, TokenType tokenType) {
        try {
            JWSSigner signer = new RSASSASigner(rsaPrivateJWK);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(email)
                    .issuer("http://intproj24.sit.kmutt.ac.th")
                    .expirationTime(new Date(new Date().getTime() + MAX_TOKEN_INTERVAL))
                    .issueTime(new Date(new Date().getTime()))
                    .claim("typ", tokenType.toString())
                    .claim("role", role)
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
            System.out.println("Token verification: " + passed);
            if(!passed) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Verified Error, Invalid JWT");
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
}
