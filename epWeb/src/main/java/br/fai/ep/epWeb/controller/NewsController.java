package br.fai.ep.epWeb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewsController {

    @GetMapping("/news/news-list")
    public String getNewsListPage() {
        return "noticia/noticia_list";
    }

    @GetMapping("/news/categories")
    public String getCategoriesPage() {
        return "noticia/noticias_categ";
    }

    @GetMapping("/news/new")
    public String getNewPAge() {
        return "noticia/noticias_modelo";
    }
}