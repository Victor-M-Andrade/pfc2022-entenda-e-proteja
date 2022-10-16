package br.fai.ep.epEntities.DTO;

import br.fai.ep.epEntities.Questao;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDto extends Questao {
    private long testId;
    private long userId;

    private String userAswer;
}