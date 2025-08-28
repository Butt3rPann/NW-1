package sit.integrated.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sit.integrated.backend.entities.AuthUserDetail;
import sit.integrated.backend.entities.User;
import sit.integrated.backend.repositories.UserRepository;
import sit.integrated.backend.utils.Role;

import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new AuthUserDetail(user.getId(), user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getUserType().name())));
    }

}
