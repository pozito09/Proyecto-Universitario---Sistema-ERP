package Clases;

import java.util.ArrayList;

public class CatalogoProductos {

    public static ArrayList<Producto> obtenerProductos() {
        ArrayList<Producto> lista = new ArrayList<>();

        String sql = "SELECT nombre, descripcion, categoria, precio, emoji, imagen, stock FROM productos";

        try (java.sql.Connection con = ConexionBD.conectar();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Color por defecto según categoría
                java.awt.Color color;
                switch (rs.getString("categoria")) {
                    case "Cafés":        color = new java.awt.Color(100, 60,  20); break;
                    case "Emparedados":  color = new java.awt.Color(180, 140, 60); break;
                    case "Postres":      color = new java.awt.Color(220, 160,150); break;
                    default:             color = new java.awt.Color(100, 60,  20); break;
                }

                lista.add(new Producto(
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getString("categoria"),
                    rs.getString("descripcion"),
                    rs.getString("emoji"),
                    color,
                    rs.getBytes("imagen"),
                    rs.getInt("stock")
                ));
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error cargando productos: " + e.getMessage());
        }

        return lista;
    }
}