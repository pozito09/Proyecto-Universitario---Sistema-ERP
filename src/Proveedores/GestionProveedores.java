package Proveedores;

import static Clases.Colores.*;
import Clases.Botones;
import Clases.ConexionBD;
import Clases.FixedDocument;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Clases.Auditoria;

public class GestionProveedores extends javax.swing.JFrame {

    private JTextField txtId, txtNombre, txtRuc, txtTelefono, txtDireccion, txtCorreo, txtBuscar;
    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar, btnBuscar;
    private JTable tabla;
    private DefaultTableModel modelo;

    public GestionProveedores() {
        initComponents();
        setLocationRelativeTo(null);
        cargarTabla("");
    }

    private void initComponents() {
        setTitle("CAFÉ COMETA - GESTIÓN DE PROVEEDORES");
        setSize(1100, 750);
        javax.swing.SwingUtilities.invokeLater(() -> setExtendedState(JFrame.MAXIMIZED_BOTH));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(FONDO);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel titulo = new JLabel("GESTIÓN DE PROVEEDORES");
        titulo.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(titulo, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        Font fontCampo = new Font("Segoe UI", Font.PLAIN, 15);
        Color bordeColor = new Color(200, 160, 80);
        txtId = new JTextField(15);
        txtId.setVisible(false);

        // ── FORMULARIO (una columna, centrado) ──
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(FONDO);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 60, 10, 60));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0;
        formPanel.add(new JLabel("Nombre:"), g);
        g.gridx = 1; g.weightx = 1.0;
        txtNombre = new JTextField(20);
        txtNombre.setFont(fontCampo);
        txtNombre.setPreferredSize(new Dimension(280, 34));
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordeColor, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        formPanel.add(txtNombre, g);
        g.weightx = 0;

        g.gridx = 0; g.gridy = 1;
        formPanel.add(new JLabel("RUC:"), g);
        g.gridx = 1; g.weightx = 1.0;
        txtRuc = new JTextField(15);
        txtRuc.setFont(fontCampo);
        txtRuc.setPreferredSize(new Dimension(280, 34));
        txtRuc.setDocument(new FixedDocument(11, true));
        txtRuc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordeColor, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        formPanel.add(txtRuc, g);
        g.weightx = 0;

        g.gridx = 0; g.gridy = 2;
        formPanel.add(new JLabel("Teléfono:"), g);
        g.gridx = 1; g.weightx = 1.0;
        txtTelefono = new JTextField(15);
        txtTelefono.setFont(fontCampo);
        txtTelefono.setPreferredSize(new Dimension(280, 34));
        txtTelefono.setDocument(new FixedDocument(9, true));
        txtTelefono.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordeColor, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        formPanel.add(txtTelefono, g);
        g.weightx = 0;

        g.gridx = 0; g.gridy = 3;
        formPanel.add(new JLabel("Dirección:"), g);
        g.gridx = 1; g.weightx = 1.0;
        txtDireccion = new JTextField(20);
        txtDireccion.setFont(fontCampo);
        txtDireccion.setPreferredSize(new Dimension(280, 34));
        txtDireccion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordeColor, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        formPanel.add(txtDireccion, g);
        g.weightx = 0;

        g.gridx = 0; g.gridy = 4;
        formPanel.add(new JLabel("Correo:"), g);
        g.gridx = 1; g.weightx = 1.0;
        txtCorreo = new JTextField(20);
        txtCorreo.setFont(fontCampo);
        txtCorreo.setPreferredSize(new Dimension(280, 34));
        txtCorreo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordeColor, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        formPanel.add(txtCorreo, g);
        g.weightx = 0;

        // ── BOTONES ──
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        actionPanel.setBackground(FONDO);

        btnGuardar = Botones.crear("Guardar", DORADO, DORADO_CLARO);
        btnActualizar = Botones.crear("Actualizar", DORADO, DORADO_CLARO);
        btnEliminar = Botones.crear("Eliminar", ROJO, ROJO_HOVER);
        btnLimpiar = Botones.crear("Limpiar", CAFE);

        actionPanel.add(btnGuardar);
        actionPanel.add(btnActualizar);
        actionPanel.add(btnEliminar);
        actionPanel.add(btnLimpiar);

        // ── BÚSQUEDA ──
        JPanel busquedaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        busquedaPanel.setBackground(FONDO);
        busquedaPanel.add(new JLabel("Buscar:"));
        txtBuscar = new JTextField(15);
        txtBuscar.setFont(fontCampo);
        busquedaPanel.add(txtBuscar);
        btnBuscar = Botones.crear("Buscar", DORADO, DORADO_CLARO);
        busquedaPanel.add(btnBuscar);

        // ── TABLA ──
        modelo = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        modelo.setColumnIdentifiers(new Object[]{"ID", "Nombre", "RUC", "Teléfono", "Dirección", "Correo"});

        tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(CABECERA_TABLA);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(SELECCION_TABLA);
        tabla.setGridColor(GRILLA_TABLA);
        tabla.setRowHeight(26);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        JScrollPane scroll = new JScrollPane(tabla);

        // ── PANEL SUPERIOR (buscador + form + botones) ──
        JPanel superior = new JPanel();
        superior.setBackground(FONDO);
        superior.setLayout(new BoxLayout(superior, BoxLayout.Y_AXIS));
        superior.add(Box.createVerticalStrut(8));
        superior.add(busquedaPanel);
        superior.add(formPanel);
        superior.add(Box.createVerticalStrut(5));
        superior.add(actionPanel);
        superior.add(Box.createVerticalStrut(8));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, superior, scroll);
        split.setResizeWeight(0.5);
        split.setDividerSize(4);
        split.setContinuousLayout(true);
        split.setBorder(null);

        add(split, BorderLayout.CENTER);

        // ── LISTENERS ──
        btnGuardar.addActionListener(e -> guardarProveedor());
        btnActualizar.addActionListener(e -> actualizarProveedor());
        btnEliminar.addActionListener(e -> eliminarProveedor());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnBuscar.addActionListener(e -> cargarTabla(txtBuscar.getText()));

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tabla.getSelectedRow();
                if (fila == -1) return;
                txtId.setText(tabla.getValueAt(fila, 0).toString());
                txtNombre.setText(tabla.getValueAt(fila, 1).toString());
                txtRuc.setText(tabla.getValueAt(fila, 2).toString());
                txtTelefono.setText(tabla.getValueAt(fila, 3).toString());
                txtDireccion.setText(tabla.getValueAt(fila, 4).toString());
                txtCorreo.setText(tabla.getValueAt(fila, 5).toString());
            }
        });
    }

    private void cargarTabla(String buscar) {
        modelo.setRowCount(0);

        String sql = "SELECT * FROM proveedores WHERE nombre LIKE ? OR ruc LIKE ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + buscar + "%");
            ps.setString(2, "%" + buscar + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("ruc"),
                    rs.getString("telefono"),
                    rs.getString("direccion"),
                    rs.getString("correo")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        String ruc = txtRuc.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del proveedor es obligatorio.");
            txtNombre.requestFocus();
            return false;
        }

        if (!ruc.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "El RUC debe tener exactamente 11 dígitos numéricos.");
            txtRuc.requestFocus();
            return false;
        }

        if (!telefono.matches("\\d{9}")) {
            JOptionPane.showMessageDialog(this, "El teléfono debe tener exactamente 9 dígitos numéricos.");
            txtTelefono.requestFocus();
            return false;
        }

        if (!correo.contains("@") || !correo.contains(".")) {
            JOptionPane.showMessageDialog(this, "Ingrese un correo válido (ej: usuario@dominio.com).");
            txtCorreo.requestFocus();
            return false;
        }

        return true;
    }

    private void guardarProveedor() {
        if (!validarCampos()) return;

        String sql = "INSERT INTO proveedores(nombre, ruc, telefono, direccion, correo) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtRuc.getText());
            ps.setString(3, txtTelefono.getText());
            ps.setString(4, txtDireccion.getText());
            ps.setString(5, txtCorreo.getText());

            ps.executeUpdate();

            try (ResultSet rsKeys = ps.getGeneratedKeys()) {
                if (rsKeys.next()) {
                    Auditoria.crear("proveedores", rsKeys.getInt(1), "Proveedor: " + txtNombre.getText().trim());
                }
            }

            JOptionPane.showMessageDialog(this, "Proveedor registrado correctamente.");
            limpiarCampos();
            cargarTabla("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar proveedor: " + e.getMessage());
        }
    }

    private void actualizarProveedor() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor y haz clic en Editar.");
            return;
        }
        if (!validarCampos()) return;

        String sql = "UPDATE proveedores SET nombre=?, ruc=?, telefono=?, direccion=?, correo=? WHERE id=?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtNombre.getText().trim());
            ps.setString(2, txtRuc.getText().trim());
            ps.setString(3, txtTelefono.getText().trim());
            ps.setString(4, txtDireccion.getText().trim());
            ps.setString(5, txtCorreo.getText().trim());
            ps.setInt(6, Integer.parseInt(txtId.getText()));

            ps.executeUpdate();

            Auditoria.editar("proveedores", Integer.parseInt(txtId.getText()), "Proveedor: " + txtNombre.getText().trim());
            JOptionPane.showMessageDialog(this, "Proveedor actualizado correctamente.");
            limpiarCampos();
            cargarTabla("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar proveedor: " + e.getMessage());
        }
    }

    private void eliminarProveedor() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor de la tabla.");
            return;
        }
        int id = (int) tabla.getValueAt(fila, 0);
        String nombreProveedor = tabla.getValueAt(fila, 1).toString();

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Deseas eliminar este proveedor?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try (Connection con = ConexionBD.conectar()) {
                try (PreparedStatement psCheck = con.prepareStatement(
                        "SELECT COUNT(*) FROM compras WHERE id_proveedor = ?")) {
                    psCheck.setInt(1, id);
                    ResultSet rsCheck = psCheck.executeQuery();
                    if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(this, "No se puede eliminar: el proveedor tiene compras asociadas.");
                        return;
                    }
                }
                try (PreparedStatement ps = con.prepareStatement("DELETE FROM proveedores WHERE id=?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                Auditoria.eliminar("proveedores", id, nombreProveedor);
                JOptionPane.showMessageDialog(this, "Proveedor eliminado correctamente.");
                limpiarCampos();
                cargarTabla("");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar proveedor: " + e.getMessage());
            }
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtRuc.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtCorreo.setText("");
        txtBuscar.setText("");
    }

    private static GestionProveedores instancia;

    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new GestionProveedores();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
