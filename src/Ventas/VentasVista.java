package Ventas;

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

public class VentasVista extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;

    public VentasVista() {

        setTitle("CAFECOMETA - VENTAS");
        setSize(1000, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());
        getContentPane().setBackground(FONDO);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(0, 55));

        JLabel titulo = new JLabel("GESTIÓN DE VENTAS");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        header.add(titulo, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        String columnas[] = {
            "ID",
            "Cliente",
            "Fecha",
            "Total",
            "Estado"
        };

        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(CABECERA_TABLA);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(SELECCION_TABLA);
        tabla.setGridColor(GRILLA_TABLA);

        add(new JScrollPane(tabla),
                BorderLayout.CENTER);

        JPanel botones = new JPanel();
        botones.setBackground(FONDO);

        JButton btnNuevaVenta =
                Botones.crear("Nueva Venta", VERDE);

        JButton btnEditar =
                Botones.crear("Editar", DORADO, DORADO_HOVER);

        JButton btnEliminar =
                Botones.crear("Eliminar", ROJO, ROJO_HOVER);

        JButton btnActualizar =
                Botones.crear("Actualizar", DORADO, DORADO_HOVER);

        botones.add(btnNuevaVenta);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);

        add(botones,
                BorderLayout.SOUTH);

        btnNuevaVenta.addActionListener(e -> mostrarDialogoNuevaVenta());
        btnEditar.addActionListener(e -> editarVenta());
        btnEliminar.addActionListener(e -> eliminarVenta());
        btnActualizar.addActionListener(e -> cargarVentas());

        cargarVentas();
    }

    private void mostrarDialogoNuevaVenta() {
        JDialog dlg = new JDialog(this, "Nueva Venta", true);
        dlg.getContentPane().setBackground(FONDO);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(FONDO);
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0;
        form.add(new JLabel("Cliente:"), g);
        g.gridx = 1;
        JComboBox<ClienteItem> cmbCliente = new JComboBox<>();
        cmbCliente.setPreferredSize(new Dimension(200, 28));
        cargarClientes(cmbCliente);
        form.add(cmbCliente, g);

        JButton btnNuevoCliente = new JButton("+");
        btnNuevoCliente.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNuevoCliente.setBackground(VERDE);
        btnNuevoCliente.setForeground(Color.WHITE);
        btnNuevoCliente.setBorderPainted(false);
        btnNuevoCliente.setPreferredSize(new Dimension(32, 28));
        btnNuevoCliente.addActionListener(ev -> {
            Clientes.ClientesVista.abrir();
            cargarClientes(cmbCliente);
        });
        g.gridx = 2;
        form.add(btnNuevoCliente, g);

        g.gridx = 0; g.gridy = 1;
        form.add(new JLabel("Total:"), g);
        g.gridx = 1; g.gridwidth = 2;
        JTextField txtTotal = new JTextField(20);
        form.add(txtTotal, g);

        g.gridx = 0; g.gridy = 2;
        form.add(new JLabel("Estado:"), g);
        g.gridx = 1;
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Pendiente", "Completado", "Anulado"});
        form.add(cmbEstado, g);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(DORADO);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setBorderPainted(false);
        btnGuardar.addActionListener(ev -> {
            ClienteItem seleccion = (ClienteItem) cmbCliente.getSelectedItem();
            if (seleccion == null) {
                JOptionPane.showMessageDialog(dlg, "Selecciona un cliente.");
                return;
            }
            String sql = "INSERT INTO pedidos(id_cliente, nombre_cliente, total, estado, id_apertura) VALUES (?, ?, ?, ?, ?)";
            try (Connection con = ConexionBD.conectar();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, seleccion.id);
                ps.setString(2, seleccion.nombre);
                ps.setDouble(3, Double.parseDouble(txtTotal.getText()));
                ps.setString(4, cmbEstado.getSelectedItem().toString());
                ps.setInt(5, Caja.CajaVista.getCajaActivaId());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(dlg, "Venta registrada");
                dlg.dispose();
                cargarVentas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Error: " + ex.getMessage());
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(FONDO);
        btnPanel.add(btnGuardar);

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void cargarClientes(JComboBox<ClienteItem> cmb) {
        cmb.removeAllItems();
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT id, nombre FROM clientes ORDER BY nombre")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cmb.addItem(new ClienteItem(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + e.getMessage());
        }
    }

    private static class ClienteItem {
        final int id;
        final String nombre;
        ClienteItem(int id, String nombre) { this.id = id; this.nombre = nombre; }
        @Override public String toString() { return nombre; }
    }

    private void editarVenta() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta de la tabla");
            return;
        }
        int id = (int) modelo.getValueAt(fila, 0);
        String estadoActual = modelo.getValueAt(fila, 4).toString();

        String nuevoEstado = JOptionPane.showInputDialog(this, "Nuevo estado:", estadoActual);
        if (nuevoEstado != null && !nuevoEstado.trim().isEmpty()) {
            try (Connection con = ConexionBD.conectar();
                 PreparedStatement ps = con.prepareStatement("UPDATE pedidos SET estado=? WHERE id=?")) {
                ps.setString(1, nuevoEstado);
                ps.setInt(2, id);
                ps.executeUpdate();
                cargarVentas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void eliminarVenta() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta de la tabla");
            return;
        }
        int id = (int) modelo.getValueAt(fila, 0);
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar venta ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try (Connection con = ConexionBD.conectar();
                 PreparedStatement ps = con.prepareStatement("DELETE FROM pedidos WHERE id=?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
                cargarVentas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void cargarVentas() {

        modelo.setRowCount(0);

        try {

            Connection con =
                    ConexionBD.conectar();

            String sql =
                    "SELECT * FROM pedidos ORDER BY id DESC";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre_cliente"),
                    rs.getTimestamp("fecha"),
                    rs.getDouble("total"),
                    rs.getString("estado")
                });

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage());

        }
    }

    private static VentasVista instancia;

    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new VentasVista();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
