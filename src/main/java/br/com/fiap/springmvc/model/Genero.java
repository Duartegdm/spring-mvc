package br.com.fiap.springmvc.model;

public enum Genero {
    TERROR("Terror"),
    ROMANCE("Romance"),
    ACAO("Ação"),
    SUSPENSE("Suspense"),
    FICCAO_CIENTIFICA("Ficção Científica"),
    BIOGRAFIA("Biografia"),
    FILOSOFIA("Filosofia"),
    RELIGIOSO("Religioso"),
    HISTORIA("História"),
    CIENCIA("Ciência"),
    DIDATICO("Didático");

    private final String descricao;

    Genero(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
