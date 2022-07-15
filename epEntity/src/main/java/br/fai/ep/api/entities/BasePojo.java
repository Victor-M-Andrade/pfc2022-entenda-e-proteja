package br.fai.ep.api.entities;

public abstract class BasePojo {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public abstract static class TABLE {
        public static final String ID_COLUMN = "id";
    }
}