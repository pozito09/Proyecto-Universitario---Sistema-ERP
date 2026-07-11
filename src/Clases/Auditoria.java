package Clases;

import static Clases.GuardarSesion.nombreCompleto;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Auditoria {

    public static void registrar(String accion, String tabla, int idRegistro, String detalle) {
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO auditoria(usuario, accion, tabla, id_registro, detalle) VALUES(?,?,?,?,?)")) {
            ps.setString(1, nombreCompleto());
            ps.setString(2, accion);
            ps.setString(3, tabla);
            if (idRegistro > 0) ps.setInt(4, idRegistro);
            else ps.setNull(4, java.sql.Types.INTEGER);
            ps.setString(5, detalle != null ? detalle : "");
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al registrar auditoría: " + e.getMessage());
        }
    }

    public static void crear(String tabla, int idRegistro, String detalle) {
        registrar("CREAR", tabla, idRegistro, detalle);
    }

    public static void editar(String tabla, int idRegistro, String detalle) {
        registrar("EDITAR", tabla, idRegistro, detalle);
    }

    public static void eliminar(String tabla, int idRegistro, String detalle) {
        registrar("ELIMINAR", tabla, idRegistro, detalle);
    }

    public static void anular(String tabla, int idRegistro, String detalle) {
        registrar("ANULAR", tabla, idRegistro, detalle);
    }
}
