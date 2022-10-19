package br.fai.ep.epWeb.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ReadDefaultImages {

    private static final String IMAGE_DIRECTORY_PATH = File.separator + "static" + File.separator + "resources" + File.separator + "img";
    private static final String DEFAULT_PROFILE_IMAGE = File.separator + "logo_invertido.png";
    private static final String DEFAULT_NEWS_IMAGE = File.separator + "noticias" + File.separator + "capa" + File.separator + "fundo_anonimo.png";

    public static byte[] getDefaultProfileImage() {
        final File currentDirectory = new File(ReadDefaultImages.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        final File defaultImage = new File(currentDirectory.getPath() + IMAGE_DIRECTORY_PATH + DEFAULT_PROFILE_IMAGE);
        try {
            return Files.readAllBytes(defaultImage.toPath());
        } catch (final IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static byte[] getDefaultNewsImage() {
        final File currentDirectory = new File(ReadDefaultImages.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        System.out.println(currentDirectory.getPath() + IMAGE_DIRECTORY_PATH + DEFAULT_NEWS_IMAGE);
        final File defaultImage = new File(currentDirectory.getPath() + IMAGE_DIRECTORY_PATH + DEFAULT_NEWS_IMAGE);
        try {
            return Files.readAllBytes(defaultImage.toPath());
        } catch (final IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}