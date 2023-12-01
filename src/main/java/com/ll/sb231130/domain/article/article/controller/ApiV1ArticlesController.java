package com.ll.sb231130.domain.article.article.controller;

import com.ll.sb231130.domain.article.article.dto.ArticleDto;
import com.ll.sb231130.domain.article.article.entity.Article;
import com.ll.sb231130.domain.article.article.service.ArticleService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/articles")
// API 버저닝(V1)을 하는 이유는, API 버전업을 해도 기존 클라이언트가 잘 작동하게 하기 위해서
@RequiredArgsConstructor
public class ApiV1ArticlesController {
    private final ArticleService articleService;

    // 각 엔드포인트 당 ResponseBody 클래스를 두는 것이 좋다.
    @Getter
    public static class GetArticlesResponseBody {
        private final List<ArticleDto> items;
        private final Map pagination;

        public GetArticlesResponseBody(List<Article> articles) {
            items = articles
                    .stream()
                    .map(ArticleDto::new)
                    .toList();

            pagination = Map.of("page", 1);
        }
    }

    @GetMapping("")
    public GetArticlesResponseBody getArticles() {
        return new GetArticlesResponseBody(articleService.findAllByOrderByIdDesc());
    }
}
