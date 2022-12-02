package br.fai.ep.epWeb.controller;

import br.fai.ep.epEntities.DTO.NewsDto;
import br.fai.ep.epEntities.Noticia;
import br.fai.ep.epEntities.Noticia.CATOGORY;
import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epWeb.helper.FoldersName;
import br.fai.ep.epWeb.security.provider.EpAuthenticationProvider;
import br.fai.ep.epWeb.service.BaseWebService;
import br.fai.ep.epWeb.service.FileService;
import br.fai.ep.epWeb.service.impl.NewsWebServiceImpl;
import br.fai.ep.epWeb.service.impl.UserWebServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Controller
public class NewsController {

    @Autowired
    private FileService awsFieService;

    private final NewsWebServiceImpl service = new NewsWebServiceImpl();
    private final UserWebServiceImpl userWebService = new UserWebServiceImpl();
    private final EpAuthenticationProvider epAuthenticationProvider = new EpAuthenticationProvider();

    private final String NEWS_LIST = "newsList";
    private final String EXISTS_NEWS = "existsNews";
    private final String AUTHOR_NAME = "authorName";
    private final String LOAD_IMAGE_ERROR = "loadImageError";
    private final String MY_NEWS_REFERENCE = "myNews";
    private final String DELETE_NEWS_ERROR = "deleteNewsError";
    private final String UPDATE_NEWS_ERROR = "updateNewsError";
    private final String SEARCH_BY_CATEGORY = "isSearchByCategory";
    private final String DEFAULT_NEWS_IMAGE_PATH = "/resources/img/noticias/capa/fundo_anonimo.png";
    private final String CATEFORY_LIST_REFERENCE = "categoryList";
    private final String REGISTRATION_REQUEST_PROBLEMS = "registrationRequestProblems";

    private boolean deleteNewsError = false;
    private boolean updateNewsError = false;
    private boolean registrationRequestProblems = false;

    final List<String> categoryList = Arrays.asList(new String[]{CATOGORY.DATA, CATOGORY.LEAKAGE, CATOGORY.LEGISLATION, CATOGORY.PASSWORDS, CATOGORY.TUTORIALS});

    private Noticia temporaryNews = null;

    @GetMapping("/news/news-list")
    public String getNewsListPage(final Model model) {
        final Map<String, String> criteria = new HashMap<>();
        criteria.put(Noticia.NEWS_TABLE.SITUATION_COLUMN, Noticia.SITUATIONS.PUBLISHED);
        final List<NewsDto> newsDtoList = service.readByDtoCriteria(criteria);

        boolean existsNews = false;
        if (newsDtoList != null && !newsDtoList.isEmpty()) {
            existsNews = true;
        }

        model.addAttribute(NEWS_LIST, newsDtoList);
        model.addAttribute(EXISTS_NEWS, existsNews);
        model.addAttribute(SEARCH_BY_CATEGORY, false);
        return FoldersName.NEWS_FOLDER + "/noticia_list";
    }

    @GetMapping("/news/news-by-category/{categIdentification}")
    public String getNewsListPage(@PathVariable final int categIdentification, final Model model) {
        String category = null;
        List<NewsDto> newsDtoList = new ArrayList<>();
        if (categIdentification == 0 || categIdentification <= categoryList.size()) {
            category = categoryList.get(categIdentification);
        }
        if (category != null) {
            final Map<String, String> criteria = new HashMap<>();
            criteria.put(Noticia.NEWS_TABLE.SITUATION_COLUMN, Noticia.SITUATIONS.PUBLISHED);
            criteria.put(Noticia.NEWS_TABLE.CATEGORY_COLUMN, category);
            newsDtoList = service.readByDtoCriteria(criteria);
        }

        boolean existsNews = false;
        if (newsDtoList != null && !newsDtoList.isEmpty()) {
            existsNews = true;
        }

        model.addAttribute(NEWS_LIST, newsDtoList);
        model.addAttribute(EXISTS_NEWS, existsNews);
        model.addAttribute(SEARCH_BY_CATEGORY, true);
        return FoldersName.NEWS_FOLDER + "/noticia_list";
    }

    @GetMapping("/news/categories")
    public String getCategoriesPage() {
        return FoldersName.NEWS_FOLDER + "/noticias_categ";
    }

    @GetMapping("/news/new/{id}")
    public String getNewPAge(@PathVariable final long id, final Model model) {
        final NewsDto newsDto = service.readByNewsDtoId(id);
        if (newsDto == null) {
            return "redirect:/not-found";
        }

        if (!newsDto.getPathImageNews().equalsIgnoreCase(DEFAULT_NEWS_IMAGE_PATH)) {
            final byte[] byteImage = awsFieService.downloadFile(newsDto.getPathImageNews().substring(1));
            model.addAttribute(LOAD_IMAGE_ERROR, (byteImage == null || byteImage.length == 0));
        }

        model.addAttribute(MY_NEWS_REFERENCE, newsDto);
        return FoldersName.NEWS_FOLDER + "/noticias_modelo";
    }

    @GetMapping("/news/create-new-news")
    public String getCreateNewNewsPage(final Model model, Noticia news) {
        final Usuario authenticatedUser = epAuthenticationProvider.getAuthenticatedUser();
        if (authenticatedUser == null) {
            return "redirect:/not-found";
        }

        model.addAttribute(REGISTRATION_REQUEST_PROBLEMS, registrationRequestProblems);
        if (registrationRequestProblems) {
            registrationRequestProblems = false;
        }

        if (temporaryNews != null) {
            news = temporaryNews;
            temporaryNews = null;
        }
        news.setIdAutor(authenticatedUser.getId());
        model.addAttribute(MY_NEWS_REFERENCE, news);
        model.addAttribute(CATEFORY_LIST_REFERENCE, categoryList);
        return FoldersName.NEWS_FOLDER + "/news_register";
    }

    @PostMapping("/news/request-news-publication")
    public String requestnewsRegistration(@RequestParam("newsCover") final MultipartFile file, final Noticia news) {
        news.setPathImageNews(DEFAULT_NEWS_IMAGE_PATH);
        news.setDataCriacao(Timestamp.from(Instant.now()));

        temporaryNews = news;
        long newNewsId = -1;
        if (file.isEmpty()) {
            newNewsId = service.create(news);
            if (newNewsId < 0) {
                registrationRequestProblems = true;
                temporaryNews = news;
                return "redirect:/news/create-new-news";
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
                return "redirect:/news/create-new-news";
            }
            temporaryNews = null;
            return "redirect:/news/user-news-detail/" + newNewsId;
        }

        final String newKeyFile = BaseWebService.PATH_IMAGENS_NEWS + nameFileWithExtension;
        final String pathImage = awsFieService.uploadFile(file, newKeyFile, news.getPathImageNews().substring(1));
        if (pathImage != null) {
            news.setPathImageNews(pathImage);
        }

        newNewsId = service.create(news);
        if (newNewsId < 0) {
            registrationRequestProblems = true;
            temporaryNews = news;
            return "redirect:/news/create-new-news";
        }
        temporaryNews = null;
        return "redirect:/news/user-news-detail/" + newNewsId;
    }

    @GetMapping("/news/user-news-list")
    public String getUserNewsListPage(final Model model) {
        final Usuario authenticatedUser = epAuthenticationProvider.getAuthenticatedUser();
        if (authenticatedUser == null) {
            return "redirect:/not-found";
        }

        final Map<String, Long> criteria = new HashMap<>();
        criteria.put(Noticia.NEWS_TABLE.ID_AUTHOR_COLUMN, authenticatedUser.getId());
        final List<Noticia> newsList = (List<Noticia>) service.readByCriteria(criteria);

        boolean existsNews = true;
        if (newsList == null || newsList.isEmpty()) {
            existsNews = false;
        }

        model.addAttribute(EXISTS_NEWS, existsNews);
        model.addAttribute(NEWS_LIST, newsList);
        return FoldersName.NEWS_FOLDER + "/user_news_list";
    }

    @GetMapping("/news/user-news-detail/{id}")
    public String getUserNewsDetailPage(@PathVariable final long id, final Model model) {
        final Noticia news = (Noticia) service.readById(id);
        if (news == null) {
            return "redirect:/not-found";
        }

        final Usuario user = (Usuario) userWebService.readById(news.getIdAutor());
        if (user == null) {
            return "redirect:/not-found";
        }

        if (!news.getPathImageNews().equalsIgnoreCase(DEFAULT_NEWS_IMAGE_PATH)) {
            final byte[] byteImage = awsFieService.downloadFile(news.getPathImageNews().substring(1));
            model.addAttribute(LOAD_IMAGE_ERROR, (byteImage == null || byteImage.length == 0));
        }

        model.addAttribute(MY_NEWS_REFERENCE, news);
        model.addAttribute(AUTHOR_NAME, user.getNome());

        model.addAttribute(DELETE_NEWS_ERROR, deleteNewsError);
        if (deleteNewsError) {
            deleteNewsError = false;
        }

        model.addAttribute(UPDATE_NEWS_ERROR, updateNewsError);
        if (updateNewsError) {
            updateNewsError = false;
        }

        return FoldersName.NEWS_FOLDER + "/user_news";
    }

    @GetMapping("/news/delete-user-news/{id}")
    public String deleteUserNews(@PathVariable final long id) {
        final Noticia news = (Noticia) service.readById(id);
        if (news == null) {
            deleteNewsError = true;
            return "redirect:/news/user-news-detail/" + id;
        }
        deleteNewsError = !service.delete(id);
        if (deleteNewsError) {
            return "redirect:/news/user-news-detail/" + id;
        }

        final Map<String, Long> criteria = new HashMap<>();
        criteria.put(Noticia.NEWS_TABLE.ID_AUTHOR_COLUMN, news.getIdAutor());
        final List<Noticia> newsList = (List<Noticia>) service.readByCriteria(criteria);
        if (newsList == null || newsList.isEmpty()) {
            final Usuario user = (Usuario) userWebService.readById(news.getIdAutor());
            user.setAutor(false);

            deleteNewsError = !userWebService.update(user);
            if (deleteNewsError) {
                return "redirect:/news/user-news-detail/" + news.getIdAutor();
            }
            return "redirect:/user/profile";
        }

        return "redirect:/news/user-news-list";
    }

    @GetMapping("/news/new-publication-request/{id}")
    public String newPublicationRequest(@PathVariable final long id) {
        final Noticia news = (Noticia) service.readById(id);
        if (news == null) {
            updateNewsError = true;
            return "redirect:/news/user-news-detail/" + id;
        }
        news.setSituacao(Noticia.SITUATIONS.CREATED);
        news.setDataCriacao(Timestamp.from(Instant.now()));

        updateNewsError = !service.update(news);
        return "redirect:/news/user-news-detail/" + id;
    }

    @GetMapping("/news/user-news-edit/{id}")
    public String getUserNewsEditPage(@PathVariable final long id, final Model model) {
        final Noticia news = (Noticia) service.readById(id);
        if (news == null) {
            return "redirect:/not-found";
        }

        model.addAttribute(MY_NEWS_REFERENCE, news);
        model.addAttribute(CATEFORY_LIST_REFERENCE, categoryList);

        model.addAttribute(UPDATE_NEWS_ERROR, updateNewsError);
        if (updateNewsError) {
            updateNewsError = false;
        }
        return FoldersName.NEWS_FOLDER + "/news_edit";
    }

    @PostMapping("/news/update-user-news")
    public String updateteNews(@RequestParam("newsCover") final MultipartFile file, final Noticia news) {
        temporaryNews = (Noticia) service.readById(news.getId());
        if (temporaryNews == null) {
            updateNewsError = true;
            return "redirect:/news/user-news-edit/" + news.getId();
        }
        news.setDataCriacao(temporaryNews.getDataCriacao());
        news.setDataPublicacao(temporaryNews.getDataPublicacao());

        if (file.isEmpty()) {
            updateNewsError = !service.update(news);
            if (updateNewsError) {
                temporaryNews = news;
                return "redirect:/news/user-news-edit/" + news.getId();
            }
            temporaryNews = null;
            return "redirect:/news/user-news-detail/" + news.getId();
        }
        final String newNameFile = service.buildNameNewFile(news);
        final String nameFileWithExtension = service.prepareNameWithExtension(file.getOriginalFilename(), newNameFile);
        if (nameFileWithExtension == null) {
            updateNewsError = !service.update(news);
            if (updateNewsError) {
                temporaryNews = news;
                return "redirect:/news/user-news-edit/" + news.getId();
            }
            temporaryNews = null;
            return "redirect:/news/user-news-detail/" + news.getId();
        }
        final String newKeyFile = BaseWebService.PATH_IMAGENS_NEWS + nameFileWithExtension;
        final String pathImage = awsFieService.uploadFile(file, newKeyFile, news.getPathImageNews().substring(1));
        if (pathImage != null) {
            news.setPathImageNews(pathImage);
        }

        updateNewsError = !service.update(news);
        if (updateNewsError) {
            temporaryNews = news;
            return "redirect:/news/user-news-edit/" + news.getId();
        }
        temporaryNews = null;
        return "redirect:/news/user-news-detail/" + news.getId();
    }
}