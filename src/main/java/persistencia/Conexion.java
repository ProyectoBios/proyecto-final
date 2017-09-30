package persistencia;

import java.sql.DriverManager;
import java.sql.Connection;

public class Conexion {

    public static Connection con;

    public static Connection AbrirConexion() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://LocalHost:3306/ProyectoFinal", "root", "1234");

        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return con;
    }


}