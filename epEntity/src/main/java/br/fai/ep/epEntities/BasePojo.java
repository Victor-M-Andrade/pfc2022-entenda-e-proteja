package br.fai.ep.epEntities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BasePojo {
    private long id;

    public abstract static class TABLE {
        public static final String ID_COLUMN = "id";
    }
}