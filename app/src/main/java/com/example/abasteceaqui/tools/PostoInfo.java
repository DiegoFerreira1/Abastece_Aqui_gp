package com.example.abasteceaqui.tools;

import android.widget.TextView;

public class PostoInfo {

    private String precoGasolinaComum, precoGasolinaAditivada, precoEtanol, precoDieselComum, precoDieselS10, precoGasNatural, nomeFantasia;


    // Adicione outras informações do posto aqui

    public PostoInfo(String nomeFantasia, String precoGasolinaComum,String precoGasolinaAditivada, String precoEtanol,
                     String precoDieselComum, String precoDieselS10, String precoGasNatural) {
        this.nomeFantasia = nomeFantasia;
        this.precoGasolinaComum = precoGasolinaComum;
        this.precoGasolinaAditivada = precoGasolinaAditivada;
        this.precoDieselComum = precoDieselComum;
        this.precoDieselS10 = precoDieselS10;
        this.precoGasNatural = precoGasNatural;
        this.precoEtanol = precoEtanol;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public String getPrecoGasolinaComum() {
        return precoGasolinaComum;
    }

    public String getPrecoGasolinaAditivada() {
        return precoGasolinaAditivada;
    }
    public String getPrecoEtanol() {
        return precoEtanol;
    }
    public String getPrecoDieselComum() {
        return precoDieselComum;
    }
    public String getPrecoDieselS10() {
        return precoDieselS10;
    }
    public String getPrecoGasNatural() {return precoGasNatural;}

    public void setPrecoGasolinaComum(String precoGasolinaComum) {
        this.precoGasolinaComum = precoGasolinaComum;
    }

    public void setPrecoGasolinaAditivada(String precoGasolinaAditivada) {
        this.precoGasolinaAditivada = precoGasolinaAditivada;
    }

    public void setPrecoEtanol(String precoEtanol) {
        this.precoEtanol = precoEtanol;
    }

    public void setPrecoDieselComum(String precoDieselComum) {
        this.precoDieselComum = precoDieselComum;
    }

    public void setPrecoDieselS10(String precoDieselS10) {
        this.precoDieselS10 = precoDieselS10;
    }

    public void setPrecoGasNatural(String precoGasNatural) {
        this.precoGasNatural = precoGasNatural;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }
}
