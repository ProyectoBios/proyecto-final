package proyecto.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
//@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    /*@Autowired
    private ProductoFormatter productoFormatter;*/

    @Override
    public void addFormatters(FormatterRegistry registry){
        ProductoFormatter productoFormatter = new ProductoFormatter();
        registry.addFormatter(productoFormatter);

        RackFormatter rackFormatter = new RackFormatter();
        registry.addFormatter(rackFormatter);
    }
}
