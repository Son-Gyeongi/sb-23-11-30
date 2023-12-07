package com.ll.sb231130.domain.member.member.controller;

import com.ll.sb231130.domain.member.member.dto.MemberDto;
import com.ll.sb231130.domain.member.member.entity.Member;
import com.ll.sb231130.domain.member.member.service.MemberService;
import com.ll.sb231130.global.rq.Rq;
import com.ll.sb231130.global.rsData.RsData;
import com.ll.sb231130.global.util.jwt.JwtUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MembersController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final Rq rq;

    @Getter
    @Setter
    public static class LoginRequestBody {
        private String username;
        private String password;
    }

    // 응답할 때 객체를 Dto로 변환해서 응답
    @Getter
    public static class LoginResponseBody {
        private final MemberDto item;
        private final String accessToken;

        public LoginResponseBody(Member member, String accessToken) {
            item = new MemberDto(member);
            this.accessToken = accessToken;
        }
    }

    @PostMapping("/login")
    public RsData<LoginResponseBody> login(@RequestBody LoginRequestBody requestBody) {
        // username, password 확인
        RsData<Member> checkRs = memberService.checkUsernameAndPassword(
                requestBody.getUsername(),
                requestBody.getPassword()
        );

        Member member = checkRs.getData();

        Long id = member.getId();
        String accessToken = JwtUtil.encode(Map.of("id", id.toString())); // id 정보로 accessToken 만든다.

        return RsData.of(
                "200",
                "로그인 성공",
                new LoginResponseBody(member, accessToken)
        );
    }
}
