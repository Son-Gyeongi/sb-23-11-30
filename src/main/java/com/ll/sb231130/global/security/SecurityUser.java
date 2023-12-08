package com.ll.sb231130.global.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

// 시큐리티 User 클래스 확장하여, username 들어갈 자리에 억지로 id 를 넣었던 억지코드를 원래대로 고친다.
// 스프링 시큐리티에서 제공하는 User에 다른 필드도 추가하기 위한 클래스
public class SecurityUser extends User {
    @Getter
    private long id;

    public SecurityUser(long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public SecurityUser(long id, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }
}
