package sit.integrated.backend.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.services.JwtUserDetailsService;
import sit.integrated.backend.utils.JwtUtils;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request
            , HttpServletResponse response
            , FilterChain chain) throws ServletException, IOException {
        response.setHeader("request-uri", request.getRequestURI());
        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader != null) {
            if (requestTokenHeader.startsWith("Bearer ")) {
                String jwtToken = requestTokenHeader.substring(7);
                try {
                    jwtUtils.verifyToken(jwtToken);
                    Map<String, Object> claims = jwtUtils.getJWTClaimsSet(jwtToken);
                    if (jwtUtils.isExpired(claims)) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired");
                        return;
                    }
                    if (!"ACCESS_TOKEN".equals(claims.get("typ"))) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT access token");
                        return;
                    }

                    Number userIdNumber = (Number) claims.get("id");
                    Integer userId = userIdNumber.intValue();
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    if (authentication == null && userId != null) {
                        UserDetails userDetails = jwtUserDetailsService.loadUserById(userId);
                        if (userDetails == null || !userDetails.getUsername().equals(claims.get("email"))) {
                            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT Token");
                        }
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "JWT Token does not begin with Bearer String");
                return;
            }
        }
        chain.doFilter(request, response);
    }

}
