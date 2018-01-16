package proyecto.logica;

import org.springframework.stereotype.Controller;

class ControladorPedidos implements IPedidos {
    private static ControladorPedidos instancia = null;

    private ControladorPedidos(){}

    public static ControladorPedidos getInstancia(){
        if(instancia == null){
            instancia = new ControladorPedidos();
        }

        return instancia;
    }
}
