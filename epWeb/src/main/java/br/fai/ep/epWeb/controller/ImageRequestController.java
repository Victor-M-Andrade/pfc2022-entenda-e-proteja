package br.fai.ep.epWeb.controller;

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

    @GetMapping("/images/users/{urlImagemLocal}")
    @ResponseBody
    public byte[] requesUserImage(@PathVariable("urlImagemLocal") final String nomeImagem) {
        final File imagemArquivo = new File(BaseWebService.PATH_IMAGENS_USERS + "/" + nomeImagem);
        try {
            return Files.readAllBytes(imagemArquivo.toPath());
        } catch (final IOException e) {
            return null;
        }
    }

    @GetMapping("/images/news/{urlImagemLocal}")
    @ResponseBody
    public byte[] requesNewsImage(@PathVariable("urlImagemLocal") final String nomeImagem) {
        final File imagemArquivo = new File(BaseWebService.PATH_IMAGENS_USERS + "/" + nomeImagem);
        try {
            return Files.readAllBytes(imagemArquivo.toPath());
        } catch (final IOException e) {
            return null;
        }
    }

    @GetMapping("/images/partners/{urlImagemLocal}")
    @ResponseBody
    public byte[] requesPartnerImage(@PathVariable("urlImagemLocal") final String nomeImagem) {
        final File imagemArquivo = new File(BaseWebService.PATH_IMAGENS_USERS + "/" + nomeImagem);
        try {
            return Files.readAllBytes(imagemArquivo.toPath());
        } catch (final IOException e) {
            return null;
        }
    }
}
