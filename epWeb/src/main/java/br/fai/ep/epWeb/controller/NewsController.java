package br.fai.ep.epWeb.controller;

import br.fai.ep.epWeb.helper.FoldersName;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewsController {

    @GetMapping("/news/news-list")
    public String getNewsListPage() {
        return FoldersName.NEWS_FOLDER + "/noticia_list";
    }

    @GetMapping("/news/categories")
    public String getCategoriesPage() {
        return FoldersName.NEWS_FOLDER + "/noticias_categ";
    }

    @GetMapping("/news/new")
    public String getNewPAge() {
        return FoldersName.NEWS_FOLDER + "/noticias_modelo";
    }
}