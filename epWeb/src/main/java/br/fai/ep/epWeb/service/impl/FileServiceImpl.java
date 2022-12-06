package br.fai.ep.epWeb.service.impl;

import br.fai.ep.epWeb.service.FileService;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    private final String BUKET_NAME = "ep-images-project";

    @Autowired
    private AmazonS3Client awsCS3Client;

    @Override
    public String uploadFile(final MultipartFile file, final String newKeyFile, final String oldKeyFile) {

        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            final byte[] oldImage = downloadFile(oldKeyFile);
            if (oldImage != null && oldImage.length > 0) {
                deleteFile(oldKeyFile);
            }

            awsCS3Client.putObject(BUKET_NAME, newKeyFile, file.getInputStream(), metadata);
            awsCS3Client.setObjectAcl(BUKET_NAME, newKeyFile, CannedAccessControlList.PublicRead);
            return "/" + newKeyFile;
        } catch (final AmazonServiceException serviceException) {
            System.out.println("Erro ao solicitar a imagem pra AWS");
            System.out.println(serviceException.getMessage());
        } catch (final SdkClientException clientException) {
            System.out.println("Erro de conexão com a AWS");
            System.out.println(clientException.getMessage());
        } catch (final IOException ioException) {
            System.out.println("Erro ao ler os bytes da imagem");
            System.out.println(ioException.getMessage());
        }
        return null;
    }

    @Override
    public byte[] downloadFile(final String keyFile) {
        byte[] content = null;

        try {
            final S3Object object = awsCS3Client.getObject(BUKET_NAME, keyFile);
            final S3ObjectInputStream inputStream = object.getObjectContent();
            content = IOUtils.toByteArray(inputStream);
        } catch (final AmazonServiceException serviceException) {
            System.out.println("Erro ao solicitar a imagem pra AWS");
            System.out.println(serviceException.getMessage());
        } catch (final SdkClientException clientException) {
            System.out.println("Erro de conexão com a AWS");
            System.out.println(clientException.getMessage());
        } catch (final IOException ioException) {
            System.out.println("Erro ao ler os bytes da imagem");
            System.out.println(ioException.getMessage());
        }
        return content;
    }

    @Override
    public boolean deleteFile(final String keyFile) {
        try {
            awsCS3Client.deleteObject(BUKET_NAME, keyFile);
            System.out.println("processo de exclusao de arquivo concluido");
            return true;
        } catch (final AmazonServiceException serviceException) {
            System.out.println("Erro ao solicitar a imagem pra AWS");
            System.out.println(serviceException.getMessage());
        } catch (final SdkClientException clientException) {
            System.out.println("Erro de conexão com a AWS");
            System.out.println(clientException.getMessage());
        }
        return false;
    }


}