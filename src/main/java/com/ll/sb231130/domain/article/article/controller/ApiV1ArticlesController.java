package com.ll.sb231130.domain.article.article.controller;

import com.ll.sb231130.domain.article.article.dto.ArticleDto;
import com.ll.sb231130.domain.article.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/articles")
// API 버저닝(V1)을 하는 이유는, API 버전업을 해도 기존 클라이언트가 잘 작동하게 하기 위해서
@RequiredArgsConstructor
public class ApiV1ArticlesController {
    private final ArticleService articleService;

    @GetMapping("")
    public List<ArticleDto> getArticles() {
        // 엔티티를 API 로 그대로 노출하는 것은 좋지 않다. API 규격에 맞춰진 DTO를 사용하자
        return articleService
                .findAll()
                .stream()
                .map(article -> new ArticleDto(article))
                .toList();
    }
}
