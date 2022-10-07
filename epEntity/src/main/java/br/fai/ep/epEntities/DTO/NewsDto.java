package br.fai.ep.epEntities.DTO;

import br.fai.ep.epEntities.Noticia;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsDto extends Noticia {
    private String authorName;
    private String authorEmail;
    private boolean anonimo;
}
