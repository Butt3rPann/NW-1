package sit.integrated.backend.entities;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class AuthUserDetail extends org.springframework.security.core.userdetails.User {
    private Integer id;
    public AuthUserDetail(Integer id, String email, String password) {
        this(id, email, password,new ArrayList<GrantedAuthority>());
    }
    public AuthUserDetail(Integer id, String email, String password
            , Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.id = id;
    }

}
