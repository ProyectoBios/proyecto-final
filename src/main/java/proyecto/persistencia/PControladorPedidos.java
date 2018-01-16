package proyecto.persistencia;

class PControladorPedidos implements IPPedidos{
    private static PControladorPedidos instancia = null;

    private PControladorPedidos() {}

    public static IPPedidos getInstancia(){
        if(instancia==null){
            instancia = new PControladorPedidos();
        }

        return instancia;
    }
}
