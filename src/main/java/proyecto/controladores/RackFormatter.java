package proyecto.controladores;

import org.springframework.format.Formatter;
import proyecto.datatypes.DTRack;
import proyecto.logica.FabricaLogica;

import java.text.ParseException;
import java.util.Locale;

public class RackFormatter implements Formatter<DTRack> {
    @Override
    public DTRack parse(String text, Locale locale) throws ParseException {
        try {
            return FabricaLogica.getControladorDeposito().buscarRack(text);
        }catch (Exception ex){
            throw new ParseException("Error al convertir letra en rack", 0);
        }
    }

    @Override
    public String print(DTRack object, Locale locale) {
        return object.getLetra();
    }
}
