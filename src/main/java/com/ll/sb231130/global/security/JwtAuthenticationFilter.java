package com.ll.sb231130.global.security;

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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// 모든 api 요청(/api/**)이 처리되기전에 작동하는 필터 JwtAuthenticationFilter 도입
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //  Spring Security를 사용하여 JWT(JSON Web Token) 인증을 처리하는 필터
    private final MemberService memberService;

    @Override
    @SneakyThrows
    // doFilterInternal 메서드에서 HTTP 요청을 필터링하고 JWT 인증을 처리
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        // 요청에서 "username" 파라미터를 추출합니다.
        String apiKey = request.getHeader("X-ApiKey");

        // 어쩔 수 없이 요청에서 username 파라미터를 통해서 보낸 사람이 누군지 판단
        // 만약 apiKey가 존재한다면,
        if (apiKey != null) {
            // MemberService를 사용하여 해당 apiKey을 가진 User 객체를 찾습니다.
            User user = memberService.getUserFromApiKey(apiKey);

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
