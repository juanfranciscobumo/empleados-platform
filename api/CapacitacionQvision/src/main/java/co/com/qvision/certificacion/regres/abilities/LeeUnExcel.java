package co.com.qvision.certificacion.regres.abilities;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.Actor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static co.com.qvision.certificacion.regres.utils.Constantes.QUERY;
import static co.com.qvision.certificacion.regres.utils.Constantes.RUTA_DATA;

public class LeeUnExcel implements Ability {
    private Fillo fillo = new Fillo();
    private String clave;
    private static final String VCLAVE = "CLAVE";
    private static Logger logger = LoggerFactory.getLogger(LeeUnExcel.class);

    public static LeeUnExcel paraVerLosDatos() {
        return new LeeUnExcel();
    }

    public static LeeUnExcel as(Actor actor) {
        return actor.abilityTo(LeeUnExcel.class);
    }

    public String getClave(String email) {
        try {
            Connection connection = fillo.getConnection(RUTA_DATA);
            Recordset recordset = connection.executeQuery(String.format(QUERY, email));
            while (recordset.next()) {
                clave = recordset.getField(VCLAVE);
            }
            recordset.close();
            connection.close();
        } catch (FilloException e) {
            logger.error(e.getMessage());
        }
        return clave;
    }

    @Override
    public String toString() {
        return "Lee une excel de: " + RUTA_DATA;
    }
}
