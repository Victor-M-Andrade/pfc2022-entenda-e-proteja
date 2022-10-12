package br.fai.ep.api.service.impl;

import br.fai.ep.api.email.EmailService;
import br.fai.ep.api.service.BaseService;
import br.fai.ep.api.service.JwtService;
import br.fai.ep.db.dao.impl.UsuarioDaoImpl;
import br.fai.ep.db.helper.DataBaseHelper.SQL_COMMAND;
import br.fai.ep.epEntities.BasePojo;
import br.fai.ep.epEntities.DTO.MailDto;
import br.fai.ep.epEntities.Usuario;
import br.fai.ep.epEntities.Usuario.USER_TABLE;
import br.fai.ep.epEntities.security.AesEncryptor;
import br.fai.ep.epEntities.security.HeaderPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioServiceImpl implements BaseService {
    @Autowired
    private UsuarioDaoImpl dao;

    @Autowired
    private JwtService jwtService;

    private final String CONTACT_EP = "contato_entenda_e_proteja@outlook.com";

    @Override
    public List<? extends BasePojo> readAll() {
        return dao.readAll();
    }

    @Override
    public Object readById(final long id) {
        return dao.readById(id);
    }

    @Override
    public long create(final Object entity) {
        final Usuario user = (Usuario) entity;
        final String criteria = "WHERE email = \'" + user.getEmail() + "\';";
        final List<? extends BasePojo> userList = dao.readByCriteria(criteria);
        if (userList != null && !userList.isEmpty()) {
            return -2; // representa que existe um usuário com o email informado
        }

        return dao.create(entity);
    }

    @Override
    public boolean update(final Object entity) {
        return dao.update(entity);
    }

    @Override
    public boolean delete(final long id) {
        return dao.delete(id);
    }

    @Override
    public List<? extends BasePojo> readByCriteria(final Map criteria) {
        String queryCriteria = SQL_COMMAND.WHERE + " 1=1 ";
        queryCriteria += buildCriteriaParameters(criteria) + ";";
        return dao.readByCriteria(queryCriteria);
    }

    @Override
    public String buildCriteriaParameters(final Map criteria) {
        String param = "";
        for (final Object key : criteria.keySet()) {
            final String column = (String) key;
            if (USER_TABLE.ACCEPT_COLUMN.equalsIgnoreCase(column) ||
                    USER_TABLE.IS_AUTHOR_COLUMN.equalsIgnoreCase(column) ||
                    USER_TABLE.IS_PARTNER_COLUMN.equalsIgnoreCase(column) ||
                    USER_TABLE.IS_ANONYMOUS_COLUMN.equalsIgnoreCase(column) ||
                    USER_TABLE.IS_ADMINISTRATOR_COLUMN.equalsIgnoreCase(column) ||
                    USER_TABLE.ID_COLUMN.equalsIgnoreCase(column)) {
                param += SQL_COMMAND.AND + column + SQL_COMMAND.EQUAL_COMPATION + criteria.get(key);
                continue;
            } else if (USER_TABLE.DATE_TIME_COLUMN.equalsIgnoreCase(column)) {
                param += SQL_COMMAND.AND + column + "::text" + SQL_COMMAND.ILIKE + "\'%" + criteria.get(key) + "%\'";
                continue;
            }
            param += SQL_COMMAND.AND + column + SQL_COMMAND.ILIKE + "\'%" + criteria.get(key) + "%\'";
        }

        return param;
    }

    public boolean forgotPassword(final String userEmail) {
        final String queryCriteria = SQL_COMMAND.WHERE + USER_TABLE.EMAIL_COLUMN + SQL_COMMAND.EQUAL_COMPATION + "\'" + userEmail + "\';";
        final List<Usuario> userList = (List<Usuario>) dao.readByCriteria(queryCriteria);
        if (userList == null || userList.isEmpty()) {
            return false;
        }
        final Usuario user = userList.get(0);

        final EmailService emailService = new EmailService();
        final String subject = "Recuperação de senha - Projeto Entenda e Proteja";
        final String message = emailService.buildMessage(user.getNome(), user.getId());
        return emailService.send(userEmail, subject, message);
    }

    public Usuario authenticate(final String encodedData) {
        final Map<CREDENCIAIS, String> credentialsMap = decodeAndGetUsernameAndPassword(encodedData);
        if (credentialsMap == null || credentialsMap.size() != 2) {
            return null;

        }

        final String username = credentialsMap.get(CREDENCIAIS.USER_EMAIL);
        final String password = credentialsMap.get(CREDENCIAIS.PASSWORD);

        String queryCriteria = SQL_COMMAND.WHERE + USER_TABLE.EMAIL_COLUMN + SQL_COMMAND.EQUAL_COMPATION + "\'" + username + "\'";
        queryCriteria += SQL_COMMAND.AND + USER_TABLE.PASSWORD_COLUMN + SQL_COMMAND.EQUAL_COMPATION + "\'" + password + "\'";
        queryCriteria += SQL_COMMAND.AND + USER_TABLE.IS_ANONYMOUS_COLUMN + SQL_COMMAND.EQUAL_COMPATION + false + ";";
        final List<Usuario> userList = (List<Usuario>) dao.readByCriteria(queryCriteria);
        if (userList == null || userList.isEmpty()) {
            return null;
        }

        final Usuario user = userList.get(0);
        final String token = jwtService.getJWTToken(user);
        user.setSenha(null);
        user.setToken(token);
        return user;
    }

    private enum CREDENCIAIS {
        USER_EMAIL,
        PASSWORD,
    }

    private Map<CREDENCIAIS, String> decodeAndGetUsernameAndPassword(final String encodedData) {
        // AES + dados
        final String[] splittedData = encodedData.split(HeaderPattern.HEADER_AES_PREFIX);
        if (splittedData.length != 2) {
            return null;
        }

        try {
            // dados
            final byte[] base64Decode = Base64.getDecoder().decode(splittedData[1]);
            final String decodedString = AesEncryptor.decrypt(base64Decode);

            final String[] firstPart = decodedString.split("Username=");
            if (firstPart.length != 2) {
                return null;
            }

            // nome_usuario;Password=senha
            final String[] credentials = firstPart[1].split(";Password=");
            if (credentials.length != 2) {
                return null;
            }

            final Map<CREDENCIAIS, String> credentialsMap = new HashMap<CREDENCIAIS, String>();
            credentialsMap.put(CREDENCIAIS.USER_EMAIL, credentials[0]);
            credentialsMap.put(CREDENCIAIS.PASSWORD, credentials[1]);

            return credentialsMap;

        } catch (final UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (final InvalidAlgorithmParameterException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (final NoSuchPaddingException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (final IllegalBlockSizeException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (final NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (final BadPaddingException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (final NoSuchProviderException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (final InvalidKeyException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean anonymizeUser(final long id) {
        final Usuario user = (Usuario) dao.readById(id);
        if (user == null) {
            return false;
        }

        user.setAnonimo(true);
        return update(user);
    }

    public boolean removeUserAnonymization(final long id) {
        final Usuario user = (Usuario) dao.readById(id);
        if (user == null) {
            return false;
        }

        user.setAnonimo(false);
        return update(user);
    }

    public boolean sendemail(final MailDto mailDto) {
        final EmailService emailService = new EmailService();
        final String bodyEmail = emailService.buildMessageContactUs(mailDto);
        return emailService.send(CONTACT_EP, mailDto.getReason(), bodyEmail);
    }
}