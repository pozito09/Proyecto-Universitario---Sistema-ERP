package Clases;
import java.sql.*;
public class MigrarTrazabilidad {
    public static void main(String[] args) {
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement()) {
            String[] alters = {
                "ALTER TABLE pedidos ADD COLUMN id_usuario_crea INT AFTER id_apertura",
                "ALTER TABLE pedidos ADD COLUMN id_usuario_prepara INT AFTER id_usuario_crea",
                "ALTER TABLE pedidos ADD COLUMN id_usuario_cobra INT AFTER id_usuario_prepara",
                "ALTER TABLE pedidos ADD COLUMN nombre_usuario_crea VARCHAR(100) AFTER id_usuario_cobra",
                "ALTER TABLE pedidos ADD COLUMN nombre_usuario_prepara VARCHAR(100) AFTER nombre_usuario_crea",
                "ALTER TABLE pedidos ADD COLUMN nombre_usuario_cobra VARCHAR(100) AFTER nombre_usuario_prepara"
            };
            for (String sql : alters) {
                try { st.executeUpdate(sql); System.out.println("OK: " + sql); }
                catch (SQLException e) { System.out.println("Ya existe: " + e.getMessage().substring(0, Math.min(60, e.getMessage().length()))); }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
