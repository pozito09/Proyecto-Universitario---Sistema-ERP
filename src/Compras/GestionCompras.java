package Compras;

import static Clases.Colores.*;
import Clases.Botones;
import Clases.ConexionBD;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import Clases.Auditoria;

// ── DESCRIPCIÓN: Módulo de gestión de compras. Lista compras registradas, permite cambiar estado y eliminar (revertiendo stock). ──
public class GestionCompras extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;

    // ── DESCRIPCIÓN: Configura header, tabla de compras, y botones (nueva, editar, eliminar, actualizar). ──
    public GestionCompras() {

        setTitle("CAFÉ COMETA - COMPRAS");
        setSize(1000, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(100, 70));
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("GESTIÓN DE COMPRAS");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        header.add(titulo, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        String columnas[] = {
            "ID",
            "Proveedor",
            "Fecha",
            "Monto",
            "Estado"
        };

        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setBackground(CABECERA_TABLA);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(SELECCION_TABLA);
        tabla.setGridColor(GRILLA_TABLA);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel botones = new JPanel();

        JButton btnNueva = Botones.crear("Nueva Compra", VERDE);
        JButton btnEditar = Botones.crear("Editar", DORADO, DORADO_CLARO);
        JButton btnEliminar = Botones.crear("Eliminar", ROJO, ROJO_HOVER);
        JButton btnActualizar = Botones.crear("Actualizar", DORADO, DORADO_CLARO);

        botones.add(btnNueva);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);

        add(botones, BorderLayout.SOUTH);

        btnNueva.addActionListener(e -> OpcionesCompra.abrir());
        btnEditar.addActionListener(e -> editarCompra());
        btnEliminar.addActionListener(e -> eliminarCompra());
        btnActualizar.addActionListener(e -> cargarCompras());

        cargarCompras();
    }

    // ── DESCRIPCIÓN: Carga todas las compras de la BD con nombre del proveedor. ──
    private void cargarCompras() {

        modelo.setRowCount(0);

        try (Connection con = ConexionBD.conectar()) {

            String sql = """
                SELECT
                    c.id,
                    p.nombre,
                    c.fecha,
                    c.total,
                    COALESCE(c.estado, 'Pendiente') AS estado
                FROM compras c
                LEFT JOIN proveedores p
                    ON c.id_proveedor = p.id
                ORDER BY c.id DESC
                """;

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getTimestamp("fecha"),
                        rs.getDouble("total"),
                        rs.getString("estado")
                    });
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar compras:\n" + e.getMessage()
            );
        }
    }

    // ── DESCRIPCIÓN: Permite cambiar el estado de una compra (Pendiente/Completado/Anulado). ──
    private void editarCompra() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una compra de la tabla");
            return;
        }
        int id = (int) modelo.getValueAt(fila, 0);
        String estadoActual = modelo.getValueAt(fila, 4).toString();

        String[] opciones = {"Pendiente", "Completado", "Anulado"};
        String nuevoEstado = (String) JOptionPane.showInputDialog(this,
                "Cambiar estado de la compra:", "Editar Estado",
                JOptionPane.QUESTION_MESSAGE, null, opciones, estadoActual);

        if (nuevoEstado != null && !nuevoEstado.equals(estadoActual)) {
            try (Connection con = ConexionBD.conectar();
                 PreparedStatement ps = con.prepareStatement(
                         "UPDATE compras SET estado=? WHERE id=?")) {
                ps.setString(1, nuevoEstado);
                ps.setInt(2, id);
                ps.executeUpdate();
                Auditoria.editar("compras", id, "Estado cambiado a " + nuevoEstado);
                cargarCompras();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    // ── DESCRIPCIÓN: Elimina una compra: revierte stock de insumos, elimina detalle y registro, con auditoría. ──
    private void eliminarCompra() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una compra de la tabla");
            return;
        }
        int id = (int) modelo.getValueAt(fila, 0);
        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar compra ID " + id + "?\nSe reversará el stock de los insumos asociados.",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try (Connection con = ConexionBD.conectar()) {
                con.setAutoCommit(false);

                try (PreparedStatement psRevertir = con.prepareStatement(
                        "UPDATE insumos SET stock = stock - ? WHERE id = ? AND stock >= ?")) {
                    try (PreparedStatement psDetalle = con.prepareStatement(
                            "SELECT id_insumo, cantidad FROM detalle_compras WHERE id_compra = ?")) {
                        psDetalle.setInt(1, id);
                        try (ResultSet rs = psDetalle.executeQuery()) {
                            while (rs.next()) {
                                psRevertir.setDouble(1, rs.getDouble("cantidad"));
                                psRevertir.setInt(2, rs.getInt("id_insumo"));
                                psRevertir.setDouble(3, rs.getDouble("cantidad"));
                                psRevertir.addBatch();
                            }
                        }
                    }
                    psRevertir.executeBatch();
                }

                try (PreparedStatement psDelDetalle = con.prepareStatement(
                        "DELETE FROM detalle_compras WHERE id_compra = ?")) {
                    psDelDetalle.setInt(1, id);
                    psDelDetalle.executeUpdate();
                }

                try (PreparedStatement psDelCompra = con.prepareStatement(
                        "DELETE FROM compras WHERE id = ?")) {
                    psDelCompra.setInt(1, id);
                    psDelCompra.executeUpdate();
                }

                con.commit();
                Auditoria.eliminar("compras", id, "Compra #" + id + " eliminada");
                cargarCompras();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private static GestionCompras instancia;

    // ── DESCRIPCIÓN: Patrón singleton. ──
    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new GestionCompras();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
