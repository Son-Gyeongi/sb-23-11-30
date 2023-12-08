package com.ll.sb231130.global.security;

import com.ll.sb231130.domain.member.member.entity.Member;
import com.ll.sb231130.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberService memberService;

    // 인증에 필요한 사용자 정보를 스프링 시큐리티에 제공하고 있어서 로그인 에러가 해결
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberService.findByUsername(username).get();

        return new User(
                member.getId() + "",
                member.getPassword(),
                member.getAuthorities()
        );
    }
}
