package Clases;

import static Clases.GuardarSesion.nombreCompleto;
import java.sql.Connection;
import java.sql.PreparedStatement;

// ============================================================
// CLASE AUDITORIA
// Registra en la tabla "auditoria" de la base de datos
// quién realizó una acción, en qué tabla y con qué detalle.
// ============================================================
public class Auditoria {

    // --------------------------------------------------------
    // Método principal: registrar()
    // Recibe la acción (CREAR, EDITAR, ELIMINAR, ANULAR),
    // el nombre de la tabla afectada, el ID del registro
    // y un detalle descriptivo de lo que se hizo.
    // Inserta un registro nuevo en la tabla "auditoria"
    // con el usuario logueado actualmente, la acción, tabla,
    // id del registro y el detalle.
    // --------------------------------------------------------
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

    // --------------------------------------------------------
    // Método crear()
    // Registra la creación de un registro nuevo en una tabla.
    // Ejemplo: crear usuario, crear producto, crear compra, etc.
    // --------------------------------------------------------
    public static void crear(String tabla, int idRegistro, String detalle) {
        registrar("CREAR", tabla, idRegistro, detalle);
    }

    // --------------------------------------------------------
    // Método editar()
    // Registra la modificación de un registro existente en una tabla.
    // Ejemplo: editar usuario, editar producto, cambiar estado de compra, etc.
    // --------------------------------------------------------
    public static void editar(String tabla, int idRegistro, String detalle) {
        registrar("EDITAR", tabla, idRegistro, detalle);
    }

    // --------------------------------------------------------
    // Método eliminar()
    // Registra la eliminación de un registro de una tabla.
    // Ejemplo: eliminar usuario, eliminar producto, eliminar insumo, etc.
    // --------------------------------------------------------
    public static void eliminar(String tabla, int idRegistro, String detalle) {
        registrar("ELIMINAR", tabla, idRegistro, detalle);
    }

    // --------------------------------------------------------
    // Método anular()
    // Registra la anulación de un registro en una tabla.
    // Ejemplo: anular una venta/pedido, anular una compra, etc.
    // --------------------------------------------------------
    public static void anular(String tabla, int idRegistro, String detalle) {
        registrar("ANULAR", tabla, idRegistro, detalle);
    }
}
