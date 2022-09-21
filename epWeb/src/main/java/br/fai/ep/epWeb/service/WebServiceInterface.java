package br.fai.ep.epWeb.service;

import br.fai.ep.epEntities.BasePojo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface WebServiceInterface<T> {

    List<? extends BasePojo> readAll();

    T readById(long id);

    long create(T entity);

    boolean update(T entity);

    boolean delete(long id);

    List<? extends BasePojo> readByCriteria(Map<String, String> criteria);

    String buildNameNewFile(T entity);

    default String getCreationDate(final Timestamp timestamp) {
        return BaseWebService.dateFormate(timestamp);
    }

    default String getCreationDateAndTime(final Timestamp timestamp) {
        return BaseWebService.dateFormateComplete(timestamp);
    }

    default String prepareNameWithExtension(final String nameFile, final String newName) {
        final String[] nameSplit = nameFile.split("\\.");
        if (nameSplit == null || nameSplit.length == 0) {
            return null;
        }

        final String extension = nameSplit[nameSplit.length - 1];
        return newName + "." + extension;
    }

    default String saveFileInProfile(final MultipartFile myfile, final String folderPath, final String nameFileWithExtension, final String nameFile) {
        try {
            final Path imagesPath = Paths.get(folderPath);
            if (!Files.exists(imagesPath)) {
                Files.createDirectories(imagesPath);
            }

            this.deleteFileIfExists(folderPath, nameFile);
            final Path dest = Paths.get(folderPath + File.separator + nameFileWithExtension);
            Files.copy(myfile.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
            return dest.toString();
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    default void deleteFileIfExists(final String folderPath, final String nameFile) {
        final File[] fileList = Paths.get(folderPath).toFile().listFiles();
        for (final File file : fileList) {
            final String name = file.getName();
            if (name.toLowerCase().contains(nameFile.toLowerCase())) {
                file.delete();
            }
        }
    }
}