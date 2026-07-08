package Clases;
import java.sql.*;
public class MigrarEmpresa {
    public static void main(String[] args) {
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement()) {
            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS empresa ("
                + "id INT PRIMARY KEY DEFAULT 1, "
                + "nombre VARCHAR(200) NOT NULL DEFAULT '', "
                + "direccion VARCHAR(300) NOT NULL DEFAULT '', "
                + "ruc VARCHAR(11) NOT NULL DEFAULT '', "
                + "CHECK (id = 1)"
                + ")"
            );
            st.executeUpdate(
                "INSERT INTO empresa (id, nombre, direccion, ruc) VALUES (1, '', '', '') "
                + "ON DUPLICATE KEY UPDATE id=1"
            );
            System.out.println("OK");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
