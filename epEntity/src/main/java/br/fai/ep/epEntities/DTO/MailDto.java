package br.fai.ep.epEntities.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDto {
    private String mailContact;
    private String username;
    private String reason;
    private String message;
}