package proyecto.controladores;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import proyecto.entidades.DTOrdenPedido;
import proyecto.entidades.DTPicking;
import proyecto.logica.FabricaLogica;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;

@Component
public class SessionListener implements HttpSessionListener, ApplicationContextAware {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setMaxInactiveInterval(1800);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        if(se.getSession().getAttribute("pedidosPicking") != null){
            //Codigo defensivo de rollback que se ejecutará cuando un funcionario no finalice su picking y se cierre su sesión.
            //Devolver los pedidos a su estado 'pendiente'.
            try {
                ArrayList<DTOrdenPedido> ordenes = (ArrayList<DTOrdenPedido>) se.getSession().getAttribute("pedidosPicking");
                for (DTOrdenPedido ordenPedido : ordenes) {
                    FabricaLogica.getControladorPedidos().modificarEstadoDePedido(ordenPedido, "pendiente");
                }
                se.getSession().removeAttribute("pedidosPicking");
            }catch (Exception ex){

            }
        }

        if(se.getSession().getAttribute("listaPicking") != null){
            try {
                ArrayList<DTPicking> picking = (ArrayList<DTPicking>) se.getSession().getAttribute("listaPicking");
                for(DTPicking p : picking){
                    for(int i=0; i<p.getLotes().size(); i++){
                        if(i!=p.getLotes().size()-1) {
                            FabricaLogica.getControladorDeposito().deshacerBajaLogicaLote(p.getLotes().get(i)); //deshacer la baja logica temporal sobre el lote
                        }else{
                            if(p.getCantidad()==p.getCantidadUnidadesTotal()){
                                FabricaLogica.getControladorDeposito().deshacerBajaLogicaLote(p.getLotes().get(i));
                            }else {
                                FabricaLogica.getControladorDeposito().actualizarStock(p.getLotes().get(i), p.getLotes().get(i).getCantUnidades() - (p.getCantidadUnidadesTotal() - p.getCantidad())); //devolver el stock al lugar de origen
                            }
                        }
                    }
                }
                se.getSession().removeAttribute("listaPicking");
            }catch (Exception ex){

            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof WebApplicationContext) {
            ((WebApplicationContext) applicationContext).getServletContext().addListener(this);
        } else {
            //throw new RuntimeException();
        }
    }
}
