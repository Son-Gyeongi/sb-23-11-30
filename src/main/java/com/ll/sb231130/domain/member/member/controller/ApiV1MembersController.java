package com.ll.sb231130.domain.member.member.controller;

import com.ll.sb231130.domain.member.member.dto.MemberDto;
import com.ll.sb231130.domain.member.member.entity.Member;
import com.ll.sb231130.domain.member.member.service.MemberService;
import com.ll.sb231130.global.rq.Rq;
import com.ll.sb231130.global.rsData.RsData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        public LoginResponseBody(Member member) {
            item = new MemberDto(member);
        }
    }

    // 로그인을 통해서 apiKey를 클라이언트가 발급받는다.
    // 이후 모든 요청에는 헤더에 X-ApiKey 로 발급받은 키를 첨부해야 함
    @PostMapping("/login")
    public RsData<LoginResponseBody> login(@RequestBody LoginRequestBody requestBody) {
        RsData<Member> checkRs = memberService.checkUsernameAndPassword(
                requestBody.getUsername(),
                requestBody.getPassword()
        );

        Member member = checkRs.getData();

        return RsData.of(
                "200",
                "로그인 성공",
                new LoginResponseBody(member)
        );
    }

    // 전 기기 로그아웃 (새로 생성도니 apiKey가 저장된다.)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/apiKey")
    public RsData<?> regenApiKey() {
        Member member = rq.getMember();

        memberService.regenApiKey(member);

        return RsData.of(
                "200",
                "해당 키가 재생성 되었습니다."
        );
    }
}
