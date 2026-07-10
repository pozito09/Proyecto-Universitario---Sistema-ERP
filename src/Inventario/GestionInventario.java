package Inventario;

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

public class GestionInventario extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;

    private JLabel lblInsumos;
    private JLabel lblStockBajo;
    private JLabel lblDisponibles;

    public GestionInventario() {

        setTitle("CAFÉ COMETA - INVENTARIO");
        setSize(1200, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(100, 70));

        JLabel titulo = new JLabel("GESTIÓN DE INVENTARIO");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setBorder(new EmptyBorder(10, 20, 10, 20));

        header.add(titulo, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // CENTRO
        JPanel centro = new JPanel(new BorderLayout());

        // KPIs
        JPanel tarjetas = new JPanel(
                new GridLayout(1, 3, 15, 15));

        tarjetas.setBorder(
                new EmptyBorder(15, 15, 15, 15));

        lblInsumos = new JLabel("0");
        lblStockBajo = new JLabel("0");
        lblDisponibles = new JLabel("0");

        tarjetas.add(crearTarjeta(
                "INSUMOS",
                lblInsumos,
                DORADO));

        tarjetas.add(crearTarjeta(
                "STOCK BAJO",
                lblStockBajo,
                ROJO));

        tarjetas.add(crearTarjeta(
                "DISPONIBLES",
                lblDisponibles,
                VERDE));

        centro.add(tarjetas,
                BorderLayout.NORTH);

        // TABLA

        String columnas[] = {
            "ID",
            "Insumo",
            "Stock",
            "Unidad",
            "Estado"
        };

        modelo = new DefaultTableModel(
                columnas,
                0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modelo);

        tabla.setRowHeight(28);
        tabla.getTableHeader().setBackground(CABECERA_TABLA);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(SELECCION_TABLA);
        tabla.setGridColor(GRILLA_TABLA);

        JScrollPane scroll =
                new JScrollPane(tabla);

        centro.add(scroll,
                BorderLayout.CENTER);

        add(centro,
                BorderLayout.CENTER);

        // BOTONES

        JPanel botones = new JPanel();

        JButton btnNuevo =
                Botones.crear("Nuevo Insumo", VERDE);

        JButton btnEditar =
                Botones.crear("Editar", DORADO, DORADO_CLARO);

        JButton btnEliminar =
                Botones.crear("Eliminar", ROJO, ROJO_HOVER);

        JButton btnActualizar =
                Botones.crear("Actualizar", DORADO, DORADO_CLARO);

        botones.add(btnNuevo);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);

        add(botones,
                BorderLayout.SOUTH);

        btnNuevo.addActionListener(e -> dialogoNuevoInsumo());
        btnEditar.addActionListener(e -> dialogoEditarInsumo());
        btnEliminar.addActionListener(e -> eliminarInsumo());
        btnActualizar.addActionListener(e -> cargarInventario());

        cargarInventario();
    }

    private void dialogoNuevoInsumo() {
        JTextField txtNombre = new JTextField(15);
        JTextField txtUnidad = new JTextField(8);
        JTextField txtStock = new JTextField("0.00", 8);
        JTextField txtMinimo = new JTextField("0.00", 8);

        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.add(new JLabel("Nombre:")); p.add(txtNombre);
        p.add(new JLabel("Unidad (kg/L/und):")); p.add(txtUnidad);
        p.add(new JLabel("Stock inicial:")); p.add(txtStock);
        p.add(new JLabel("Stock mínimo:")); p.add(txtMinimo);

        int res = JOptionPane.showConfirmDialog(this, p, "Nuevo Insumo",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String nombre = txtNombre.getText().trim();
        String unidad = txtUnidad.getText().trim();
        if (nombre.isEmpty() || unidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y unidad son obligatorios.");
            return;
        }
        try {
            double stock = Double.parseDouble(txtStock.getText().trim().replace(",", "."));
            double minimo = Double.parseDouble(txtMinimo.getText().trim().replace(",", "."));
            String sql = "INSERT INTO insumos (nombre, unidad, stock, stock_minimo) VALUES (?,?,?,?)";
            try (Connection con = ConexionBD.conectar();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nombre);
                ps.setString(2, unidad);
                ps.setDouble(3, stock);
                ps.setDouble(4, minimo);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Insumo agregado correctamente.");
                cargarInventario();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stock y mínimo deben ser números.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void dialogoEditarInsumo() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un insumo de la tabla.");
            return;
        }
        int id = (int) modelo.getValueAt(fila, 0);
        String nombreActual = modelo.getValueAt(fila, 1).toString();
        String unidadActual = modelo.getValueAt(fila, 3).toString();

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT stock_minimo FROM insumos WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            double minimoActual = rs.next() ? rs.getDouble("stock_minimo") : 0;
            rs.close();

            JTextField txtNombre = new JTextField(nombreActual, 15);
            JTextField txtUnidad = new JTextField(unidadActual, 8);
            JTextField txtMinimo = new JTextField(String.valueOf(minimoActual), 8);

            JPanel p = new JPanel(new GridLayout(3, 2, 8, 8));
            p.add(new JLabel("Nombre:")); p.add(txtNombre);
            p.add(new JLabel("Unidad:")); p.add(txtUnidad);
            p.add(new JLabel("Stock mínimo:")); p.add(txtMinimo);

            int res = JOptionPane.showConfirmDialog(this, p, "Editar Insumo",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) return;

            double minimo = Double.parseDouble(txtMinimo.getText().trim().replace(",", "."));
            String sql = "UPDATE insumos SET nombre=?, unidad=?, stock_minimo=? WHERE id=?";
            try (PreparedStatement ps2 = con.prepareStatement(sql)) {
                ps2.setString(1, txtNombre.getText().trim());
                ps2.setString(2, txtUnidad.getText().trim());
                ps2.setDouble(3, minimo);
                ps2.setInt(4, id);
                ps2.executeUpdate();
                JOptionPane.showMessageDialog(this, "Insumo actualizado.");
                cargarInventario();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stock mínimo debe ser un número.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminarInsumo() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un insumo de la tabla.");
            return;
        }
        int id = (int) modelo.getValueAt(fila, 0);
        String nombre = modelo.getValueAt(fila, 1).toString();
        int conf = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar \"" + nombre + "\"?\nEsto también eliminará sus movimientos y recetas asociadas.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (conf != JOptionPane.YES_OPTION) return;

        try (Connection con = ConexionBD.conectar()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps1 = con.prepareStatement("DELETE FROM movimientos_insumos WHERE id_insumo = ?")) {
                ps1.setInt(1, id);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = con.prepareStatement("DELETE FROM recetas WHERE id_insumo = ?")) {
                ps2.setInt(1, id);
                ps2.executeUpdate();
            }
            try (PreparedStatement ps3 = con.prepareStatement("DELETE FROM detalle_compras WHERE id_insumo = ?")) {
                ps3.setInt(1, id);
                ps3.executeUpdate();
            }
            try (PreparedStatement ps4 = con.prepareStatement("DELETE FROM insumos WHERE id = ?")) {
                ps4.setInt(1, id);
                ps4.executeUpdate();
            }
            con.commit();
            JOptionPane.showMessageDialog(this, "Insumo eliminado.");
            cargarInventario();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private JPanel crearTarjeta(
            String titulo,
            JLabel lblValor,
            Color color) {

        JPanel panel = new JPanel();

        panel.setBackground(color);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS));

        JLabel lblTitulo =
                new JLabel(titulo);

        lblTitulo.setForeground(Color.WHITE);

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18));

        lblTitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        lblValor.setForeground(
                Color.WHITE);

        lblValor.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24));

        lblValor.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblValor);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void cargarInventario() {

        modelo.setRowCount(0);

        int total = 0;
        int bajo = 0;
        int disponible = 0;

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM insumos ORDER BY id");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                double stock =
                        rs.getDouble("stock");

                double minimo =
                        rs.getDouble("stock_minimo");

                String estado =
                        stock <= minimo
                        ? "Stock Bajo"
                        : "Disponible";

                if (estado.equals("Stock Bajo")) {
                    bajo++;
                } else {
                    disponible++;
                }

                total++;

                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    stock,
                    rs.getString("unidad"),
                    estado
                });
            }

            lblInsumos.setText(
                    String.valueOf(total));

            lblStockBajo.setText(
                    String.valueOf(bajo));

            lblDisponibles.setText(
                    String.valueOf(disponible));

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar inventario:\n"
                    + e.getMessage());
        }
    }

    private static GestionInventario instancia;

    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new GestionInventario();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
