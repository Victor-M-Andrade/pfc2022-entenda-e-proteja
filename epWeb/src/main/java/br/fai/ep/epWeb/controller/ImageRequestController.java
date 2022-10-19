package br.fai.ep.epWeb.controller;

import br.fai.ep.epWeb.helper.ReadDefaultImages;
import br.fai.ep.epWeb.service.BaseWebService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
public class ImageRequestController {

    private final String targetDirectory = System.getProperty("user.dir");

    @GetMapping("/images/users/{urlImagemLocal}")
    @ResponseBody
    public byte[] requesUserImage(@PathVariable("urlImagemLocal") final String nomeImagem) {
        final File imagemArquivo = new File(targetDirectory + BaseWebService.PATH_IMAGENS_USERS + "/" + nomeImagem);
        try {
            final byte[] byteImage = Files.readAllBytes(imagemArquivo.toPath());
            System.out.println(byteImage);
            return (byteImage == null || byteImage.length == 0) ?
                    ReadDefaultImages.getDefaultProfileImage() : byteImage;
        } catch (final IOException e) {
            System.out.println("primeira exception");
            System.out.println(e.getMessage());
            return ReadDefaultImages.getDefaultProfileImage();
        }
    }

    @GetMapping("/images/news/{urlImagemLocal}")
    @ResponseBody
    public byte[] requesNewsImage(@PathVariable("urlImagemLocal") final String nomeImagem) {
        final File imagemArquivo = new File(targetDirectory + BaseWebService.PATH_IMAGENS_NEWS + "/" + nomeImagem);
        try {
            final byte[] byteImage = Files.readAllBytes(imagemArquivo.toPath());
            System.out.println(byteImage);
            return (byteImage == null || byteImage.length == 0) ?
                    ReadDefaultImages.getDefaultProfileImage() : byteImage;
        } catch (final IOException e) {
            System.out.println(e.getMessage());
            return ReadDefaultImages.getDefaultNewsImage();
        }
    }

    @GetMapping("/images/partners/{urlImagemLocal}")
    @ResponseBody
    public byte[] requesPartnerImage(@PathVariable("urlImagemLocal") final String nomeImagem) {
        final File imagemArquivo = new File(targetDirectory + BaseWebService.PATH_IMAGENS_PARTNER + "/" + nomeImagem);
        try {
            return Files.readAllBytes(imagemArquivo.toPath());
        } catch (final IOException e) {
            System.out.println(e.getMessage());
            return ReadDefaultImages.getDefaultProfileImage();
        }
    }
}