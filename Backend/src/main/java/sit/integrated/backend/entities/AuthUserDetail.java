package sit.integrated.backend.entities;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import sit.integrated.backend.utils.Role;
import sit.integrated.backend.utils.UserStatus;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class AuthUserDetail extends org.springframework.security.core.userdetails.User {
    private Integer id;
    private Role role;
    private String nickname;
    private UserStatus status;

    public AuthUserDetail(Integer id, String email, String password, Role role, String nickname, UserStatus status) {
        this(id, email, password, role, nickname, status, new ArrayList<GrantedAuthority>());
    }
    public AuthUserDetail(Integer id, String email, String password, Role role, String nickname, UserStatus status, Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.id = id;
        this.role = role;
        this.nickname = nickname;
        this.status = status;
    }
}
