package com.ll.sb231130.domain.member.member.service;

import com.ll.sb231130.domain.member.member.entity.Member;
import com.ll.sb231130.domain.member.member.repository.MemberRepository;
import com.ll.sb231130.global.rsData.RsData;
import com.ll.sb231130.global.security.SecurityUser;
import com.ll.sb231130.global.util.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RsData<Member> join(String username, String password, String email, String nickname) {
        Member member = Member.builder()
                .modifyDate(LocalDateTime.now())
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .build();

        memberRepository.save(member);

        return RsData.of("200", "%s님 가입을 환영합니다.".formatted(username), member);
    }

    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }

    public long count() {
        return memberRepository.count();
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    // 주어진 API 키를 사용하여 사용자 정보를 가져오는 메서드
    public SecurityUser getUserFromApiKey(String apiKey) { // 여기서 apiKey는 JwtToken이다.
        // 주어진 API 키를 복호화하여 클레임(클레임 선언) 객체를 얻습니다.
        Claims claims = JwtUtil.decode(apiKey); // accessToken에 받아온 사용자 정보

        // 클레임에서 'data' 키에 해당하는 맵을 추출합니다.
        Map<String, Object> data = (Map<String, Object>) claims.get("data");
        // 맵에서 'id' 키에 해당하는 값을 가져옵니다.
        long id = Long.parseLong((String) data.get("id")); // data에서 id 가져오기
        String username = (String) data.get("username"); // data에서 username 가져오기
        // 맵에서 'authorities' 키에 해당하는 값, 즉 권한 목록을 가져와서
        // SimpleGrantedAuthority 객체로 매핑한 후 리스트로 변환합니다.
        List<? extends GrantedAuthority> authorities =
                ((List<String>) data.get("authorities"))
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        // 위에서 추출한 사용자 정보를 사용하여 Spring Security의 User 객체를 생성하여 반환합니다.
        return new SecurityUser(
                id,
                username,
                "",
                authorities
        );
    }

    public RsData<Member> checkUsernameAndPassword(String username, String password) {
        // 사용자 불러오기
        Optional<Member> memberOp = findByUsername(username);

        if (memberOp.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        if (!passwordEncoder.matches(password, memberOp.get().getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return RsData.of("200", "로그인 성공", memberOp.get());
    }
}
