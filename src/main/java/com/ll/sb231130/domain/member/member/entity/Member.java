package com.ll.sb231130.domain.member.member.entity;

import com.ll.sb231130.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Entity
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class Member extends BaseEntity {
    private String username;
    private String password;
    private String email;
    private String nickname;

    // name을 어떤 걸로 할지 여기서 정한다. nickname 또는 username
    public String getName() {
        return nickname;
    }

    // 사용자의 권한 목록을 Spring Security의 GrantedAuthority 타입으로 변환하여 반환하는 메서드입니다.
    @SuppressWarnings("JpaAttributeTypeInspection") // 무시해도 되는 오류를 끌 수 있다.
    public List<? extends GrantedAuthority> getAuthorities() {
        // 아래의 getAuthoritiesAsStrList() 메서드를 호출하여 문자열 형태의 권한 목록을 가져온 후,
        // 각 권한을 SimpleGrantedAuthority 객체로 매핑하고 리스트로 변환하여 반환합니다.
        return getAuthoritiesAsStrList()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
    /*
     Spring Security에서는 권한 정보를 GrantedAuthority 타입으로 표현하며,
     SimpleGrantedAuthority는 이를 구현한 간단한 클래스 중 하나

     코드에서 getAuthoritiesAsStrList() 메서드에서 가져온 문자열 형태의 권한 목록을
     SimpleGrantedAuthority로 변환하는 것은 Spring Security의 규칙에 맞게 권한을 표현하기 위함입니다.
     Spring Security는 이러한 GrantedAuthority 객체들을 사용하여 인증 및 권한 부여를 처리
     */

    // 문자열 형태의 권한 목록을 가져오는 메서드입니다.
    @SuppressWarnings("JpaAttributeTypeInspection") // 무시해도 되는 오류를 끌 수 있다.
    public List<String> getAuthoritiesAsStrList() {
        // 단순히 "ROLE_MEMBER"라는 권한을 포함한 리스트를 생성하여 반환합니다.
        return List.of("ROLE_MEMBER");
    }
}
