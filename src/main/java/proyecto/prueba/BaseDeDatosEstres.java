package proyecto.prueba;

import proyecto.entidades.*;
import proyecto.logica.FabricaLogica;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BaseDeDatosEstres {
    public static void main(String[] args){
        try {

            //codigo para cargar la base de datos de forma masiva.

            ArrayList<EspecificacionProducto> prods = FabricaLogica.getControladorDeposito().listarProductos(false);

            //Cargar Pedidos

            OrdenPedido p;
            p = new OrdenPedido(0, new Date(), "pendiente", "", new Date(), "Av Inexistente 1234", "Peteco", 3000, FabricaLogica.getControladorPedidos().buscarCliente("Carniceria Pepe"), FabricaLogica.getControladorEmpleados().buscarEmpleado("32165498"), null, null, new ArrayList<>());
            for (int i = 0; i < 15000; i++) {
                ArrayList<LineaPedido> lineas = new ArrayList<>();
                for(int j = 0; j<5; j++){
                    LineaPedido l = new LineaPedido(j, j*10, j*10*prods.get(j).getPrecioActual(), prods.get(j));
                    lineas.add(l);
                }
                p.setLineas(lineas);
                FabricaLogica.getControladorPedidos().altaOrdenDePedido(p);
                System.out.println(i);
            }


            //Cargar Empleados

            Empleado e;
            long ci = 10000000;
            for(int i = 0; i < 200; i++){
                e = new Empleado(String.valueOf(ci), "Empleado prueba", "dmsss4398", new Date(), new Date(), "23596285", "funcionario");
                ci++;
                FabricaLogica.getControladorEmpleados().altaEmpleado(e);
                System.out.println(i);
            }

            //Cargar Lotes

            ArrayList<Rack> racks = FabricaLogica.getControladorDeposito().listarRacks();
            Lote l;
            for(Rack r : racks){
                for(int i = 1; i<=r.getDimAncho(); i++){
                    for(int j = 1; j<=r.getDimAlto(); j++){
                        l = new Lote(0, new Date(), new Date(118, 11, 10), 100, prods.get(ThreadLocalRandom.current().nextInt(0, 3)), new Ubicacion(j, i, r));
                        FabricaLogica.getControladorDeposito().altaLote(l);
                        System.out.println(r.getLetra() + " " + i + " " + j);
                    }
                }
            }

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }


    }

}
