package com.ll.sb231130.global.rq;

import com.ll.sb231130.domain.member.member.entity.Member;
import com.ll.sb231130.domain.member.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

// 사용자에 관한 클래스
@Component
@RequestScope
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final MemberService memberService;
    private Member member;

    public Member getMember() {
        if (member == null) {
            // 스프링 시큐리티 세션에서 회원정보 가져오기
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            long memberId = Long.parseLong(user.getUsername());

            member = memberService.findById(memberId).get();
        }

        return member;
    }

    // 요청 받은 헤더에 name과 같은 이름의 값을 가져온다.
    public String getHeader(String name) {
        return request.getHeader(name);
    }
}
