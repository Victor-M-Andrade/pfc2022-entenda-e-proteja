package br.fai.ep.epEntities.DTO;

import br.fai.ep.epEntities.Teste;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestDto extends Teste {
    private int levelTest;
    private long questionId;
}