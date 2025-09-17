package sit.integrated.backend.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sit.integrated.backend.utils.JwtUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

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
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userId, null, List.of());
                    auth.setDetails(claims);
                    SecurityContextHolder.getContext().setAuthentication(auth);

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
