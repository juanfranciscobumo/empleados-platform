package com.qvision.certificacion.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.qvision.certificacion.enums.Meses.valueOf;
import static com.qvision.certificacion.utils.Constantes.ANNO_MM_DD;
import static com.qvision.certificacion.utils.Constantes.FECHA;

public class DiferenciaDeDias {
    private static Logger logger = LoggerFactory.getLogger(DiferenciaDeDias.class);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(ANNO_MM_DD);
    private static int dias = 0;

    public static int diferenciaDeDias(String dia, String mes, String ano, String diaFin, String mesFin, String anoFin) {
        try {
            dias = (int) ((dateFormat.parse(String.format(FECHA, anoFin, valueOf(mesFin)
                    .getMesNumerico(), diaFin)).getTime() - dateFormat
                    .parse(String.format(FECHA, ano, valueOf(mes).getMesNumerico(), dia)).getTime()) / 86400000);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return dias;
    }
}
