package com.ll.sb231130.global.initData;

import com.ll.sb231130.domain.article.article.entity.Article;
import com.ll.sb231130.domain.article.article.service.ArticleService;
import com.ll.sb231130.domain.member.member.entity.Member;
import com.ll.sb231130.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class NotProd {
    @Autowired
    @Lazy
    private NotProd self;

    private final MemberService memberService;
    private final ArticleService articleService;

    @Bean
    public ApplicationRunner initNotProd() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (memberService.count() > 0) return;

        Member member1 = memberService.join("admin", "1234", "admin@test.com", "관리자").getData();
        Member member2 = memberService.join("user1", "1234", "user1@test.com", "user1").getData();
        Member member3 = memberService.join("user2", "1234", "user2@test.com", "user2").getData();

        Article article1 = articleService.write(member1, "제목1", "내용1").getData();
        Article article2 = articleService.write(member1, "제목2", "내용2").getData();
        Article article3 = articleService.write(member1, "제목3", "내용3").getData();
        Article article4 = articleService.write(member1, "제목4", "내용4").getData();
        Article article5 = articleService.write(member2, "제목5", "내용5").getData();
        Article article6 = articleService.write(member2, "제목6", "내용6").getData();
        Article article7 = articleService.write(member2, "제목7", "내용7").getData();
        Article article8 = articleService.write(member2, "제목8", "내용8").getData();
        Article article9 = articleService.write(member3, "제목9", "내용9").getData();
        Article article10 = articleService.write(member3, "제목10", "내용10").getData();
    }
}
