package com.sobreplanosstaging.herokuapp.utils.enums;

public enum TagsEnum {
    NAME("name"),
    JOB("job"),
    MEXICO("México"),
    CODIGO_RESPUESTA("Código de respuesta");
    private String atributo;

    TagsEnum(String atributo) {
        this.atributo = atributo;
    }

    public String getAtributo() {
        return atributo;
    }
}
