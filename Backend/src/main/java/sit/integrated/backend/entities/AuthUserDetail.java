package sit.integrated.backend.entities;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import sit.integrated.backend.utils.Role;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class AuthUserDetail extends org.springframework.security.core.userdetails.User {
    private Integer id;
    private Role role;
    private String nickname;

    public AuthUserDetail(Integer id, String email, String password, Role role, String nickname) {
        this(id, email, password, role, nickname, new ArrayList<GrantedAuthority>());
    }
    public AuthUserDetail(Integer id, String email, String password, Role role, String nickname, Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.id = id;
        this.role = role;
        this.nickname = nickname;
    }
}
