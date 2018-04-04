package proyecto.controladores;

import org.springframework.format.Formatter;
import proyecto.entidades.DTEspecificacionProducto;
import proyecto.logica.FabricaLogica;

import java.text.ParseException;
import java.util.Locale;


public class ProductoFormatter implements Formatter<DTEspecificacionProducto>{
    @Override
    public DTEspecificacionProducto parse(String codigo, Locale locale) throws ParseException {
        try {
            return FabricaLogica.getControladorDeposito().buscarProducto(Integer.valueOf(codigo));
        }catch (Exception ex){
            throw new ParseException("Error al convertir codigo en producto", 0);
        }
    }

    @Override
    public String print(DTEspecificacionProducto producto, Locale locale) {
        return String.valueOf(producto.getCodigo());
    }
}
