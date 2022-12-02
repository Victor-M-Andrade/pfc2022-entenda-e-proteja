package br.fai.ep.epWeb.controller;

import br.fai.ep.epWeb.helper.ReadDefaultImages;
import br.fai.ep.epWeb.service.BaseWebService;
import br.fai.ep.epWeb.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImageRequestController {

    private static final String TARGET_DIRECTORY = System.getProperty("user.dir");

    @Autowired
    private FileService awsFileService;

    @GetMapping("/images/users/{urlImagemLocal}")
    @ResponseBody
    public byte[] requesUserImage(@PathVariable("urlImagemLocal") final String nomeImagem, final Model model) {

        final byte[] byteImage = awsFileService.downloadFile(BaseWebService.PATH_IMAGENS_USERS + nomeImagem);
        System.out.println(byteImage);
        return (byteImage == null || byteImage.length == 0) ? ReadDefaultImages.getDefaultProfileImage() : byteImage;
    }

    @GetMapping("/images/news/{urlImagemLocal}")
    @ResponseBody
    public byte[] requesNewsImage(@PathVariable("urlImagemLocal") final String nomeImagem, final Model model) {
        final byte[] byteImage = awsFileService.downloadFile(BaseWebService.PATH_IMAGENS_NEWS + nomeImagem);
        System.out.println(byteImage);
        return (byteImage == null || byteImage.length == 0) ? ReadDefaultImages.getDefaultNewsImage() : byteImage;
    }

    @GetMapping("/images/partners/{urlImagemLocal}")
    @ResponseBody
    public byte[] requesPartnerImage(@PathVariable("urlImagemLocal") final String nomeImagem, final Model model) {
        final byte[] byteImage = awsFileService.downloadFile(BaseWebService.PATH_IMAGENS_USERS + nomeImagem);
        System.out.println(byteImage);
        return (byteImage == null || byteImage.length == 0) ? ReadDefaultImages.getDefaultProfileImage() : byteImage;
    }
}