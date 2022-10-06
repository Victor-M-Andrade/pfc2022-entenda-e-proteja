package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.Noticia;
import br.fai.ep.epEntities.Noticia.CATOGORY;
import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epWeb.helper.FoldersName;
import br.fai.ep.epWeb.service.BaseWebService;
import br.fai.ep.epWeb.service.impl.NewsWebServiceImpl;
import br.fai.ep.epWeb.service.impl.UserWebServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class NewsController {
    final NewsWebServiceImpl service = new NewsWebServiceImpl();

    private final String USER_ID = "userId";
    private final String NEWS_LIST = "newsList";
    private final String EXISTS_NEWS = "existsNews";
    private final String AUTHOR_NAME = "authorName";
    private final String MY_NEWS_REFERENCE = "myNews";
    private final String CATEFORY_LIST_REFERENCE = "categoryList";
    private final String REGISTRATION_REQUEST_PROBLEMS = "registrationRequestProblems";

    private boolean registrationRequestProblems = false;

    final List<String> categoryList = Arrays.asList(new String[]{CATOGORY.DATA, CATOGORY.LEAKAGE, CATOGORY.LEGISLATION, CATOGORY.PASSWORDS, CATOGORY.TUTORIALS});

    private Noticia temporaryNews = null;

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

    @GetMapping("/news/create-new-news/{id}")
    public String getCreateNewNewsPage(@PathVariable final long id, final Model model, Noticia news) {
        model.addAttribute(USER_ID, id);

        model.addAttribute(REGISTRATION_REQUEST_PROBLEMS, registrationRequestProblems);
        if (registrationRequestProblems) {
            registrationRequestProblems = false;
        }

        if (temporaryNews != null) {
            news = temporaryNews;
            temporaryNews = null;
        }
        news.setIdAutor(id);
        model.addAttribute(MY_NEWS_REFERENCE, news);
        model.addAttribute(CATEFORY_LIST_REFERENCE, categoryList);
        return FoldersName.NEWS_FOLDER + "/news_register";
    }

    @PostMapping("/news/request-news-publication")
    public String requestnewsRegistration(@RequestParam("newsCover") final MultipartFile file, final Noticia news) {
        news.setPathImageNews("/resources/img/noticias/capa/banner_exemplo.png");
        news.setDataCriacao(Timestamp.from(Instant.now()));

        temporaryNews = news;
        long newNewsId = -1;
        if (file.isEmpty()) {
            newNewsId = service.create(news);
            if (newNewsId < 0) {
                registrationRequestProblems = true;
                temporaryNews = news;
                return "redirect:/news/create-new-news/" + news.getIdAutor();
            }
            temporaryNews = null;
            return "redirect:/news/user-news-detail/" + newNewsId;
        }
        final String newNameFile = service.buildNameNewFile(news);
        final String nameFileWithExtension = service.prepareNameWithExtension(file.getOriginalFilename(), newNameFile);
        if (nameFileWithExtension == null) {
            newNewsId = service.create(news);
            if (newNewsId < 0) {
                registrationRequestProblems = true;
                temporaryNews = news;
                return "redirect:/news/create-new-news/" + news.getIdAutor();
            }
            temporaryNews = null;
            return "redirect:/news/user-news-detail/" + newNewsId;
        }

        final String pathImage = service.saveFileInProfile(file, BaseWebService.PATH_IMAGENS_NEWS, nameFileWithExtension, newNameFile);
        if (pathImage != null) {
            news.setPathImageNews(pathImage);
        }

        newNewsId = service.create(news);
        if (newNewsId < 0) {
            registrationRequestProblems = true;
            temporaryNews = news;
            return "redirect:/news/create-new-news/" + news.getIdAutor();
        }
        temporaryNews = null;
        return "redirect:/news/user-news-detail/" + newNewsId;
    }

    @GetMapping("/news/user-news-list/{id}")
    public String getUserNewsListPage(@PathVariable final long id, final Model model) {
        final Map<String, Long> criteria = new HashMap<>();
        criteria.put(Noticia.NEWS_TABLE.ID_AUTHOR_COLUMN, id);
        final List<Noticia> newsList = (List<Noticia>) service.readByCriteria(criteria);

        boolean existsNews = true;
        if (newsList == null || newsList.isEmpty()) {
            existsNews = false;
        }

        model.addAttribute(USER_ID, id);
        model.addAttribute(EXISTS_NEWS, existsNews);
        model.addAttribute(NEWS_LIST, newsList);
        return FoldersName.NEWS_FOLDER + "/user_news_list";
    }

    @GetMapping("/news/user-news-detail/{id}")
    public String getUserNewsDetailPage(@PathVariable final long id, final Model model) {
        final Noticia news = (Noticia) service.readById(id);
        final Usuario user = (Usuario) new UserWebServiceImpl().readById(news.getIdAutor());


        model.addAttribute(USER_ID, news.getIdAutor());
        model.addAttribute(MY_NEWS_REFERENCE, news);
        model.addAttribute(AUTHOR_NAME, user.getNome());

        return FoldersName.NEWS_FOLDER + "/user_news";
    }
}