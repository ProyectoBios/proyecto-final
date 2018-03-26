package proyecto.controladores;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import proyecto.datatypes.DTEspecificacionProducto;
import proyecto.datatypes.DTLote;
import proyecto.datatypes.DTRack;
import proyecto.datatypes.ExcepcionFrigorifico;
import proyecto.logica.FabricaLogica;

import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Controller
public class ControladorDeposito {
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    //region ABMProducto
    @RequestMapping(value="/ABMProducto", method = RequestMethod.GET)
    public String GetAbmProducto(ModelMap modelMap){
        modelMap.addAttribute("producto", new DTEspecificacionProducto());
        botonesPorDefectoProducto(modelMap);

        return "ABMProducto";
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Buscar")
    public String buscarProducto(@ModelAttribute DTEspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
            producto = FabricaLogica.getControladorDeposito().buscarProducto(producto.getCodigo());

            if(producto==null){
                producto = new DTEspecificacionProducto();
                modelMap.addAttribute("producto", producto);
                botonesAltaProducto(modelMap);

            }else{
                modelMap.addAttribute("producto", producto);
                botonesBMProducto(modelMap);
            }

            return "ABMProducto";
        }catch(ExcepcionFrigorifico ex){
            producto = new DTEspecificacionProducto();
            modelMap.addAttribute("producto", producto);
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "ABMProducto";
        }
        catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR!");
            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Agregar")
    public String agregarABMProducto(@ModelAttribute DTEspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
            int codigo = FabricaLogica.getControladorDeposito().altaDeProducto(producto);

            modelMap.addAttribute("producto", new DTEspecificacionProducto());
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensaje", "Alta exitosa. ID:  " + codigo + ".");
            return "ABMProducto";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("producto", producto);
            botonesAltaProducto(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "ABMProducto";
        }catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesAltaProducto(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR!");
            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Eliminar")
    public String eliminarABMProducto(@ModelAttribute DTEspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
            FabricaLogica.getControladorDeposito().bajaProducto(producto);

            modelMap.addAttribute("producto", new DTEspecificacionProducto());
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensaje", "Baja exitosa!");
            return "ABMProducto";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("producto", producto);
            botonesBMProducto(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "ABMProducto";
        }catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesBMProducto(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR!");
            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Modificar")
    public String modificarABMProducto(@ModelAttribute DTEspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
            FabricaLogica.getControladorDeposito().modificarProducto(producto);

            modelMap.addAttribute("producto", new DTEspecificacionProducto());
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensaje", "Modificacion exitosa!");
            return "ABMProducto";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("producto", producto);
            botonesBMProducto(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "ABMProducto";
        }catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesBMProducto(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR!");
            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Limpiar")
    public String limpiarABMProducto(ModelMap modelMap){
        modelMap.addAttribute("producto", new DTEspecificacionProducto());
        botonesPorDefectoProducto(modelMap);

        return "ABMProducto";
    }

    public void botonesPorDefectoProducto(ModelMap modelMap){
        modelMap.addAttribute("agregarHabilitado", "false");
        modelMap.addAttribute("eliminarHabilitado", "false");
        modelMap.addAttribute("modificarHabilitado", "false");
        modelMap.addAttribute("buscarHabilitado", "true");
        modelMap.addAttribute("codigoBloqueado", "false");
    }

    public void botonesAltaProducto(ModelMap modelMap){
        modelMap.addAttribute("agregarHabilitado", "true");
        modelMap.addAttribute("eliminarHabilitado", "false");
        modelMap.addAttribute("modificarHabilitado", "false");
        modelMap.addAttribute("buscarHabilitado", "false");
        modelMap.addAttribute("codigoBloqueado", "true");
    }

    public void botonesBMProducto(ModelMap modelMap){
        modelMap.addAttribute("agregarHabilitado", "false");
        modelMap.addAttribute("eliminarHabilitado", "true");
        modelMap.addAttribute("modificarHabilitado", "true");
        modelMap.addAttribute("buscarHabilitado", "false");
        modelMap.addAttribute("codigoBloqueado", "true");
    }
    //endregion

    //region AltaRack
    @RequestMapping(value="/AltaRack", method = RequestMethod.GET)
    public String GetAltaRack(ModelMap modelMap){
        modelMap.addAttribute("rack", new DTRack());
        botonesPorDefectoRack(modelMap);

        return "AltaRack";
    }

    @RequestMapping(value="/AltaRack", method = RequestMethod.POST, params="action=Agregar")
    public String agregarAltaRack(@ModelAttribute DTRack rack, BindingResult bindingResult, ModelMap modelMap){
        try {
            FabricaLogica.getControladorDeposito().altaRack(rack);

            modelMap.addAttribute("rack", new DTRack());
            botonesPorDefectoRack(modelMap);
            modelMap.addAttribute("mensaje", "¡Alta exitosa!");
            return "AltaRack";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("rack", rack);
            botonesAltaRack(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "AltaRack";
        }catch(Exception ex){
            modelMap.addAttribute("rack", rack);
            botonesAltaRack(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR! No se pudo dar de alta el rack.");
            return "AltaRack";
        }
    }

    @RequestMapping(value="/AltaRack", method = RequestMethod.POST, params="action=Buscar")
    public String buscarAltaRack(@ModelAttribute DTRack rack, BindingResult bindingResult, ModelMap modelMap){
        String letra = rack.getLetra();
        try{
            rack = FabricaLogica.getControladorDeposito().buscarRack(letra);

            if(rack == null){
                rack = new DTRack();
                rack.setLetra(letra);
                modelMap.addAttribute("rack", rack);
                botonesAltaRack(modelMap);
            }else{
                modelMap.addAttribute("rack", rack);
                botonesBajaRack(modelMap);
            }

            return "AltaRack";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("rack", new DTRack());
            botonesPorDefectoRack(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "AltaRack";
        }catch(Exception ex){
            modelMap.addAttribute("rack", new DTRack());
            botonesPorDefectoRack(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR! No se pudo buscar el rack");
            return "AltaRack";
        }
    }

    @RequestMapping(value="/AltaRack", method = RequestMethod.POST, params="action=Eliminar")
    public String bajaRack(@ModelAttribute DTRack rack, BindingResult bindingResult, ModelMap modelMap){
        try{
            FabricaLogica.getControladorDeposito().bajaRack(rack);

            modelMap.addAttribute("rack", new DTRack());
            modelMap.addAttribute("mensaje", "Baja exitosa.");
            botonesPorDefectoRack(modelMap);
            return "AltaRack";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("rack", rack);
            modelMap.addAttribute("mensaje", ex.getMessage());
            botonesBajaRack(modelMap);
            return "AltaRack";
        }catch(Exception ex){
            modelMap.addAttribute("rack", rack);
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al dar de baja el rack");
            botonesBajaRack(modelMap);
            return "AltaRack";
        }
    }

    @RequestMapping(value="/EstadoDeRack", method = RequestMethod.GET)
    public String getListarRacks(ModelMap modelMap, HttpSession session) throws Exception {
        try{
            ArrayList<DTRack> listaRacks = FabricaLogica.getControladorDeposito().listarRacks();
            if(listaRacks.size() == 0) {
                modelMap.addAttribute("mensaje", "No se encontraron Racks");
            }
            modelMap.addAttribute("racks",listaRacks);
            session.setAttribute("racks", listaRacks);
            return "EstadoDeRack";

        }catch(Exception ex) {
            modelMap.addAttribute("mensaje", "Error al listar los Racks");
            return "EstadoDeRack";
        }
    }

    public void botonesPorDefectoRack(ModelMap modelMap){
        modelMap.addAttribute("agregarHabilitado", "false");
        modelMap.addAttribute("buscarHabilitado", "true");
        modelMap.addAttribute("eliminarHabilitado", "false");
        modelMap.addAttribute("letraBloqueado", "false");
    }

    public void botonesAltaRack(ModelMap modelMap){
        modelMap.addAttribute("agregarHabilitado", "true");
        modelMap.addAttribute("buscarHabilitado", "false");
        modelMap.addAttribute("eliminarHabilitado", "false");
        modelMap.addAttribute("letraBloqueado", "true");
    }

    public void botonesBajaRack(ModelMap modelMap){
        modelMap.addAttribute("agregarHabilitado", "false");
        modelMap.addAttribute("buscarHabilitado", "false");
        modelMap.addAttribute("eliminarHabilitado", "true");
        modelMap.addAttribute("letraBloqueado", "true");
    }

    @RequestMapping(value="/AltaRack", method = RequestMethod.POST, params="action=Limpiar")
    public String limpiarAltaRack(ModelMap modelMap){
        modelMap.addAttribute("rack", new DTRack());
        botonesPorDefectoRack(modelMap);

        return "AltaRack";
    }
    //endregion

    //region Lotes

    @RequestMapping(value="/BajaLoteXVencimiento", method = RequestMethod.GET)
    public String getBajaLoteXVencimiento(ModelMap modelMap){
        try{
            ArrayList<DTLote> lotes = FabricaLogica.getControladorDeposito().obtenerLotesVencidos();
            modelMap.addAttribute("lotes", lotes);

            return "BajaLoteXVencimiento";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "BajaLoteXVencimiento";
        }catch(Exception ex){
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al obtener los lotes vencidos");
            return "BajaLoteXVencimiento";
        }
    }

    @RequestMapping(value="/BajaLoteXVencimiento", method = RequestMethod.POST)
    public String eliminarLote(@RequestParam(value="IdLote", required = false) String idLote, ModelMap modelMap){
        try{
            DTLote lote = FabricaLogica.getControladorDeposito().buscarLote(Integer.valueOf(idLote));
            FabricaLogica.getControladorDeposito().bajaLote(lote);
            modelMap.addAttribute("lotes", FabricaLogica.getControladorDeposito().obtenerLotesVencidos());
            modelMap.addAttribute("mensaje", "Baja de lote exitosa.");
            return "BajaLoteXVencimiento";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "BajaLoteXVencimiento";
        }catch(Exception ex){
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al eliminar el lote.");
            return "BajaLoteXVencimiento";
        }

    }

    @RequestMapping(value="/AltaLote", method = RequestMethod.GET)
    public String getAltaLote(ModelMap modelMap){
        ArrayList<DTEspecificacionProducto> prods = new ArrayList<DTEspecificacionProducto>();
        ArrayList<DTRack> racks = new ArrayList<DTRack>();
        try {
            prods=FabricaLogica.getControladorDeposito().listarProductos();
            racks=FabricaLogica.getControladorDeposito().listarRacks();
            modelMap.addAttribute("lote", new DTLote());
            modelMap.addAttribute("productos",prods);
            modelMap.addAttribute("racks", racks);
            return "AltaLote";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("lote", new DTLote());
            modelMap.addAttribute("productos", prods);
            modelMap.addAttribute("racks", racks);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "AltaLote";
        }catch (Exception ex){
            modelMap.addAttribute("lote", new DTLote());
            modelMap.addAttribute("productos", prods);
            modelMap.addAttribute("racks", racks);
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al obtener el formulario.");
            return "AltaLote";
        }
    }

    @RequestMapping(value="/AltaLote", method = RequestMethod.POST,  params="action=Agregar")
    public <T> T altaLote(@ModelAttribute @Valid DTLote lote, BindingResult bindingResult, ModelMap modelMap){
        ArrayList<DTEspecificacionProducto> prods = new ArrayList<>();
        ArrayList<DTRack> racks = new ArrayList<>();
        try{
            prods=FabricaLogica.getControladorDeposito().listarProductos();
            racks=FabricaLogica.getControladorDeposito().listarRacks();
            int codigo = FabricaLogica.getControladorDeposito().altaLote(lote);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Document qrCode = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(qrCode, byteArrayOutputStream);
            qrCode.open();
            Paragraph parrafo = new Paragraph(new Phrase(10f, "ID: " + codigo, FontFactory.getFont(FontFactory.COURIER, 34)));
            parrafo.setAlignment(Element.ALIGN_CENTER);
            qrCode.add(parrafo);
            qrCode.add(Chunk.NEWLINE);
            qrCode.add(Chunk.NEWLINE);
            qrCode.add(Chunk.NEWLINE);
            qrCode.add(Chunk.NEWLINE);
            qrCode.add(Chunk.NEWLINE);
            qrCode.add(Chunk.NEWLINE);

            BarcodeQRCode codigoQR = new BarcodeQRCode(String.valueOf(codigo), 300, 300, null);
            Image qrImagen = codigoQR.getImage();
            qrImagen.setAlignment(Element.ALIGN_CENTER);
            qrCode.add(qrImagen);
            qrCode.close();

            byte[] pdfBytes = byteArrayOutputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            String nombreArchivo = "Lote" + codigo + ".pdf";
            //headers.setContentDispositionFormData("inline", nombreArchivo);
            headers.add("Content-Disposition", "inline;filename=" + nombreArchivo);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfBytes, headers, HttpStatus.OK);
            return (T)response;



            /*modelMap.addAttribute("lote", new DTLote());
            modelMap.addAttribute("productos", prods);
            modelMap.addAttribute("racks", racks);
            modelMap.addAttribute("mensaje", "Alta exitosa. ID: " + codigo);
            return "AltaLote";*/
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("lote", lote);
            modelMap.addAttribute("productos", prods);
            modelMap.addAttribute("racks", racks);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return (T)"AltaLote";
        }catch(Exception ex){
            modelMap.addAttribute("lote", lote);
            modelMap.addAttribute("productos", prods);
            modelMap.addAttribute("racks", racks);
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrió un error al dar de alta el lote.");
            return (T)"AltaLote";
        }
    }

    @RequestMapping(value = "/EstadoDeRack", method = RequestMethod.POST, params = "action=Seleccionar")
    public String listarLotesXRack(@RequestParam(value="letraRack", required = false) String letraRack, ModelMap modelMap, HttpSession session) throws Exception {
        ArrayList<DTLote> lotes = FabricaLogica.getControladorDeposito().listarLotesXRack(letraRack);
        session.setAttribute("lotes", lotes);
        modelMap.addAttribute("tablaRack", true);
        return "EstadoDeRack";
    }

    @RequestMapping(value="/AltaLote", method = RequestMethod.POST, params="action=Limpiar")
    public String limpiarAltaLote(ModelMap modelMap){
        try {
            modelMap.addAttribute("lote", new DTLote());
            modelMap.addAttribute("productos", FabricaLogica.getControladorDeposito().listarProductos());
            modelMap.addAttribute("racks", FabricaLogica.getControladorDeposito().listarRacks());
            return "AltaLote";
        }catch (ExcepcionFrigorifico ex){
            return "error";
        }catch (Exception ex){
            return "error";
        }
    }



    @InitBinder
    public void initDateBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
        binder.registerCustomEditor(Date.class, "fechaVencimiento", new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    //endregion
}
