package main.xiaoqihui.common.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class LoginUser implements UserDetails {

    private final Long userId;
    private final String mobile;
    private final String password;
    private final String userType;
    private final String realName;
    private final String status;

    public LoginUser(Long userId, String mobile, String password, String userType, String realName, String status) {
        this.userId = userId;
        this.mobile = mobile;
        this.password = password;
        this.userType = userType;
        this.realName = realName;
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }

    public String getRealName() {
        return realName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userType));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return mobile;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !"DISABLED".equalsIgnoreCase(status);
    }
}
