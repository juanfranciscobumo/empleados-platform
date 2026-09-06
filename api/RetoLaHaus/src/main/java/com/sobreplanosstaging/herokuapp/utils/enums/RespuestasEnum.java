package com.sobreplanosstaging.herokuapp.utils.enums;

public enum RespuestasEnum {
    OK_CONSULTA(200),
    OK_CREACION(201),
    OK_BORRADO(204);
    private int codigo;

    private RespuestasEnum(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
