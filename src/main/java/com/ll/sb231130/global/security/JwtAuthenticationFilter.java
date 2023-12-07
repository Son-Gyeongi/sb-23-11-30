package com.ll.sb231130.global.security;

import com.ll.sb231130.domain.member.member.entity.Member;
import com.ll.sb231130.domain.member.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

// 모든 api 요청(/api/**)이 처리되기전에 작동하는 필터 JwtAuthenticationFilter 도입
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //  Spring Security를 사용하여 JWT(JSON Web Token) 인증을 처리하는 필터
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @SneakyThrows
    // doFilterInternal 메서드에서 HTTP 요청을 필터링하고 JWT 인증을 처리
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        // 요청에서 "username" 파라미터를 추출합니다.
        String username = request.getParameter("username");
        // 패스워드까지 체크하여 클라이언트가 쉽게 신원을 위조할 수 없도록 한다.
        String password = request.getParameter("password");

        // 어쩔 수 없이 요청에서 username 파라미터를 통해서 보낸 사람이 누군지 판단
        // 만약 username, password가 존재한다면,
        if (username != null && password != null) {
            // MemberService를 사용하여 해당 username을 가진 Member 엔터티를 찾습니다.
            Member member = memberService.findByUsername(username).get();

            // 비밀번호 체크
            if (!passwordEncoder.matches(password, member.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            // 찾은 Member 정보를 기반으로 Spring Security의 User 객체를 생성합니다.
            User user = new User(
                    member.getUsername(),
                    member.getPassword(),
                    List.of()
            );

            // 생성된 User 객체를 사용하여 Authentication 객체를 만듭니다.
            // Authentication 객체는 Spring Security에서 현재 사용자의 인증 정보를 나타내는 객체
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user,
                    user.getPassword(),
                    user.getAuthorities()
            );

            // SecurityContextHolder를 사용하여 현재 Security Context에 Authentication 객체를 설정합니다.
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 다음 필터로 요청을 전달합니다.
        filterChain.doFilter(request, response);
    }
}
