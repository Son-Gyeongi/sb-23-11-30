package com.ll.sb231130.domain.article.article.controller;

import com.ll.sb231130.domain.article.article.dto.ArticleDto;
import com.ll.sb231130.domain.article.article.entity.Article;
import com.ll.sb231130.domain.article.article.service.ArticleService;
import com.ll.sb231130.global.rsData.RsData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
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
    public RsData<GetArticlesResponseBody> getArticles() {
        // 모든 엔드포인트의 응답본문에 일정한 양식을 넣어두자.
        return RsData.of("200",
                "성공",
                new GetArticlesResponseBody(articleService.findAllByOrderByIdDesc()));
    }

    @Getter
    public static class GetArticleResponseBody {
        private final ArticleDto item;

        public GetArticleResponseBody(Article article) {
            item = new ArticleDto(article);
        }
    }

    @GetMapping("/{id}")
    public RsData<GetArticleResponseBody> getArticle(@PathVariable long id) {
        return RsData.of(
                "200",
                "성공",
                new GetArticleResponseBody(articleService.findById(id).get())
        );
    }

    @Getter
    public static class RemoveArticleResponseBody {
        private final ArticleDto item;

        public RemoveArticleResponseBody(Article article) {
            item = new ArticleDto(article);
        }
    }

    @DeleteMapping("/{id}")
    public RsData<RemoveArticleResponseBody> removeArticle(@PathVariable long id) {
        // 응답에 보낼 article 객체
        Article article = articleService.findById(id).get();

        articleService.deleteById(id);

        return RsData.of(
                "200",
                "성공",
                new RemoveArticleResponseBody(article)
        );
    }
}
