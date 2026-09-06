package com.sobreplanosstaging.herokuapp.models.back.response;

public class ConsultaUsuariosResponse {
    private DataResponse data;
    private SupportResponse support;

    public DataResponse getData() {
        return data;
    }

    public void setData(DataResponse data) {
        this.data = data;
    }

    public SupportResponse getSupport() {
        return support;
    }

    public void setSupport(SupportResponse support) {
        this.support = support;
    }
}
