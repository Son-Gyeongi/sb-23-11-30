package com.ll.sb231130.domain.article.article.controller;

import com.ll.sb231130.domain.article.article.entity.Article;
import com.ll.sb231130.domain.article.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ApiV1ArticlesController {
    private final ArticleService articleService;

    @GetMapping("")
    public List<Article> getArticles() {
        return articleService.findAll();
    }
}
