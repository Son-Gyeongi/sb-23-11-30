package com.ll.sb231130.global.rq;

import com.ll.sb231130.domain.member.member.entity.Member;
import com.ll.sb231130.domain.member.member.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

// 사용자에 관한 클래스
@Component // 스프링에서 관리하는 빈(Bean)으로 등록되는 클래스임을 나타내는 어노테이션
@RequestScope // 현재 클래스의 인스턴스가 요청 스코프로 생성되어, 각 HTTP 요청마다 새로운 인스턴스가 생성됩니다.
@RequiredArgsConstructor // Lombok 어노테이션으로, 필요한 의존성을 주입받는 생성자를 자동으로 생성
public class Rq {
    private final HttpServletRequest request; // 현재 HTTP 요청과 관련된 정보를 담은 객체
    private final HttpServletResponse response; // 현재 HTTP 응답과 관련된 정보를 담은 객체
    private final MemberService memberService; // 사용자(Member)와 관련된 비즈니스 로직을 처리하는 서비스
    private Member member; // 현재 요청의 회원 정보를 담을 멤버 변수
    private final EntityManager entityManager; // JPA 엔터티 매니저

    // 현재 요청의 회원 정보를 반환하는 메서드
    public Member getMember() {
        if (member == null) {
            // Spring Security의 인증 객체에서 현재 사용자의 정보를 가져옵니다.
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // 사용자 아이디를 추출하고, 해당 아이디로 회원 정보를 데이터베이스에서 가져옵니다.
            long memberId = Long.parseLong(user.getUsername());

            // getReference 메서드는 지연 로딩(Lazy Loading)을 통해 성능을 최적화하는 데 도움
            // member는 프록시 객체, 프록시는 실제 엔터티의 데이터가 필요할 때 데이터베이스에서 로드
            member = entityManager.getReference(Member.class, memberId);
        }

        return member;
    }

    // 요청 받은 헤더에 name과 같은 이름의 값을 가져온다.
    public String getHeader(String name) {
        return request.getHeader(name);
    }
}
