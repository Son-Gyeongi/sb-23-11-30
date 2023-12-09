package com.ll.sb231130.global.rq;

import com.ll.sb231130.domain.member.member.entity.Member;
import com.ll.sb231130.global.security.SecurityUser;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

// 현재 사용자 및 요청과 관련된 정보를 처리하는 클래스
@Component // 스프링에서 관리하는 빈(Bean)으로 등록되는 클래스임을 나타내는 어노테이션
@RequestScope // 현재 클래스의 인스턴스가 요청 스코프로 생성되어, 각 HTTP 요청마다 새로운 인스턴스가 생성됩니다.
@RequiredArgsConstructor // Lombok 어노테이션으로, 필요한 의존성을 주입받는 생성자를 자동으로 생성
public class Rq {
    private final HttpServletRequest request; // 현재 HTTP 요청과 관련된 정보를 담은 객체
    private final HttpServletResponse response; // 현재 HTTP 응답과 관련된 정보를 담은 객체
    private final EntityManager entityManager; // JPA 엔터티 매니저
    private Member member; // 현재 요청의 회원 정보를 담을 멤버 변수
    private SecurityUser securityUser;

    // 현재 사용자가 관리자인지 여부를 반환하는 메서드
    public boolean isAdmin() {
        if (isLogin()) return false;

        return getSecurityUser()
                .getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));
    }

    // 현재 사용자가 로그인 상태인지 여부를 반환하는 메서드
    public boolean isLogin() {
        return getSecurityUser() != null;
    }

    // 현재 사용자가 로그아웃 상태인지 여부를 반환하는 메서드
    public boolean isLogout() {
        return !isLogin();
    }

    public SecurityUser getSecurityUser() {
        // 이미 SecurityUser 객체가 존재한다면 해당 객체를 반환
        if (securityUser == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) return null;

            Object principal = authentication.getPrincipal();

            if (principal == null) return null;

            securityUser = (SecurityUser) principal;
        }

        return securityUser;
    }

    // 현재 요청의 회원 정보를 반환하는 메서드
    public Member getMember() {
        if (isLogout()) return null;

        // 회원 정보가 아직 초기화되지 않았다면 초기화
        if (member == null) {
            // getReference 메서드는 지연 로딩(Lazy Loading)을 통해 성능을 최적화하는 데 도움
            // member는 프록시 객체, 프록시는 실제 엔터티의 데이터가 필요할 때 데이터베이스에서 로드
            member = entityManager.getReference(Member.class, getSecurityUser().getId());
        }

        return member;
    }

    // 요청 받은 헤더에 name과 같은 이름의 값을 가져온다.
    public String getHeader(String name, String defaultValue) {
        String value = request.getHeader(name);

        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    // 사용자 인증 정보를 설정하는 메서드
    public void setAuthentication(SecurityUser user) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                user.getAuthorities()
        );

        // Spring Security의 SecurityContextHolder에 사용자 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
