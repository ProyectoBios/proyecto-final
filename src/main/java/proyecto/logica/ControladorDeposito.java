package proyecto.logica;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_EXCLUSIONPeer;
import proyecto.datatypes.*;
import proyecto.persistencia.*;

class ControladorDeposito implements  IDeposito {
    private static ControladorDeposito instancia = null;

    public static ControladorDeposito getInstancia(){
        if(instancia == null){
            instancia = new ControladorDeposito();
        }

        return instancia;
    }

    private ControladorDeposito(){}

    private void ValidarEspecificacionProducto(DTEspecificacionProducto ep) throws ExcepcionFrigorifico{
        if(ep == null){
            throw new ExcepcionFrigorifico("¡ERROR! Producto nulo");
        }

        if(ep.getCodigo() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! El codigo no puede ser menor que 0");
        }

        if(ep.getMinStock() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! El stock minimo no puede ser menor que 0");
        }

        if(ep.getMaxStock() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! El stock maximo no puede ser menor que 0");
        }

        if(ep.getStockCritico() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! El stock critico no puede ser menor que 0");
        }

        if(ep.getNombre().length() > 40){
            throw new ExcepcionFrigorifico("¡ERROR! El nombre no puede tener más de 40 caracteres");
        }

        for(DTPrecio p : ep.getHistoricoPrecios()){
            ValidarPrecio(p);
        }
    }

    private void ValidarPrecio(DTPrecio p) throws ExcepcionFrigorifico {
        if(p==null){
            throw new ExcepcionFrigorifico("¡ERROR! Precio nulo");
        }

        if(p.getPrecio() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! El precio no puede ser menor que 0");
        }
    }

    @Override
    public int altaDeProducto(DTEspecificacionProducto ep) throws Exception{
        ValidarEspecificacionProducto(ep);
        return FabricaPersistencia.getControladorDeposito().altaDeProducto(ep);
    }

    @Override
    public void bajaProducto(DTEspecificacionProducto ep) throws Exception{
        FabricaPersistencia.getControladorDeposito().bajaProducto(ep);
    }

    @Override
    public void modificarProducto(DTEspecificacionProducto ep) throws Exception{
        ValidarEspecificacionProducto(ep);
        FabricaPersistencia.getControladorDeposito().modificarProducto(ep);
    }

    @Override
    public DTEspecificacionProducto buscarProducto(int codigo) throws Exception{
        return FabricaPersistencia.getControladorDeposito().buscarProducto(codigo);
    }
}
