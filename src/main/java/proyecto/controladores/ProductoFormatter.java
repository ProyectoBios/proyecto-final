package proyecto.controladores;

import org.springframework.format.Formatter;
import proyecto.entidades.EspecificacionProducto;
import proyecto.logica.FabricaLogica;

import java.text.ParseException;
import java.util.Locale;


public class ProductoFormatter implements Formatter<EspecificacionProducto>{
    @Override
    public EspecificacionProducto parse(String codigo, Locale locale) throws ParseException {
        try {
            return FabricaLogica.getControladorDeposito().buscarProducto(Integer.valueOf(codigo), false);
        }catch (Exception ex){
            throw new ParseException("Error al convertir codigo en producto", 0);
        }
    }

    @Override
    public String print(EspecificacionProducto producto, Locale locale) {
        return String.valueOf(producto.getCodigo());
    }
}
