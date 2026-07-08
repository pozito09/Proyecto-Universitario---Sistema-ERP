package Clientes;

import static Clases.Colores.*;
import Clases.Botones;
import Clases.ConexionBD;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClientesVista extends JFrame {

    private JTextField txtId, txtNombre, txtTelefono, txtCorreo, txtDni, txtDireccion, txtBuscar;
    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar, btnBuscar;
    private JTable tabla;
    private DefaultTableModel modelo;

    public ClientesVista() {
        initComponents();
        setLocationRelativeTo(null);
        cargarTabla("");
    }

    private void initComponents() {
        setTitle("Gestión de Clientes - CafeCometa");
        setSize(1100, 750);
        javax.swing.SwingUtilities.invokeLater(() -> setExtendedState(JFrame.MAXIMIZED_BOTH));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(FONDO);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel titulo = new JLabel("GESTIÓN DE CLIENTES");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(titulo, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        Font fontCampo = new Font("Segoe UI", Font.PLAIN, 15);
        Color bordeColor = new Color(200, 160, 80);
        txtId = new JTextField(15);
        txtId.setVisible(false);

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
        formPanel.add(new JLabel("Teléfono:"), g);
        g.gridx = 1; g.weightx = 1.0;
        txtTelefono = new JTextField(15);
        txtTelefono.setFont(fontCampo);
        txtTelefono.setPreferredSize(new Dimension(280, 34));
        txtTelefono.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordeColor, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        formPanel.add(txtTelefono, g);
        g.weightx = 0;

        g.gridx = 0; g.gridy = 2;
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

        g.gridx = 0; g.gridy = 3;
        formPanel.add(new JLabel("DNI:"), g);
        g.gridx = 1; g.weightx = 1.0;
        txtDni = new JTextField(15);
        txtDni.setFont(fontCampo);
        txtDni.setPreferredSize(new Dimension(280, 34));
        txtDni.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordeColor, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        formPanel.add(txtDni, g);
        g.weightx = 0;

        g.gridx = 0; g.gridy = 4;
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

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        actionPanel.setBackground(FONDO);

        btnGuardar = Botones.crear("Guardar", DORADO, DORADO_HOVER);
        btnActualizar = Botones.crear("Actualizar", DORADO, DORADO_HOVER);
        btnEliminar = Botones.crear("Eliminar", ROJO, ROJO_HOVER);
        btnLimpiar = Botones.crear("Limpiar", CAFE);

        actionPanel.add(btnGuardar);
        actionPanel.add(btnActualizar);
        actionPanel.add(btnEliminar);
        actionPanel.add(btnLimpiar);

        JPanel busquedaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        busquedaPanel.setBackground(FONDO);
        busquedaPanel.add(new JLabel("Buscar:"));
        txtBuscar = new JTextField(15);
        txtBuscar.setFont(fontCampo);
        busquedaPanel.add(txtBuscar);
        btnBuscar = Botones.crear("Buscar", DORADO, DORADO_HOVER);
        busquedaPanel.add(btnBuscar);

        modelo = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        modelo.setColumnIdentifiers(new Object[]{"ID", "Nombre", "Teléfono", "Correo", "DNI", "Dirección"});

        tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(CABECERA_TABLA);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(SELECCION_TABLA);
        tabla.setGridColor(GRILLA_TABLA);
        tabla.setRowHeight(26);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        JScrollPane scroll = new JScrollPane(tabla);

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

        btnGuardar.addActionListener(e -> guardarCliente());
        btnActualizar.addActionListener(e -> actualizarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnBuscar.addActionListener(e -> cargarTabla(txtBuscar.getText()));

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tabla.getSelectedRow();
                if (fila == -1) return;
                txtId.setText(tabla.getValueAt(fila, 0).toString());
                txtNombre.setText(tabla.getValueAt(fila, 1).toString());
                txtTelefono.setText(tabla.getValueAt(fila, 2).toString());
                txtCorreo.setText(tabla.getValueAt(fila, 3).toString());
                txtDni.setText(tabla.getValueAt(fila, 4).toString());
                txtDireccion.setText(tabla.getValueAt(fila, 5).toString());
            }
        });
    }

    private void cargarTabla(String buscar) {
        modelo.setRowCount(0);

        String sql = "SELECT * FROM clientes WHERE nombre LIKE ? OR dni LIKE ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + buscar + "%");
            ps.setString(2, "%" + buscar + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("telefono") != null ? rs.getString("telefono") : "",
                    rs.getString("correo") != null ? rs.getString("correo") : "",
                    rs.getString("dni") != null ? rs.getString("dni") : "",
                    rs.getString("direccion") != null ? rs.getString("direccion") : ""
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String dni = txtDni.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del cliente es obligatorio.");
            txtNombre.requestFocus();
            return false;
        }

        if (!dni.isEmpty() && !dni.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(this, "El DNI debe tener exactamente 8 dígitos numéricos.");
            txtDni.requestFocus();
            return false;
        }

        if (!telefono.isEmpty() && !telefono.matches("\\d{9,15}")) {
            JOptionPane.showMessageDialog(this, "El teléfono debe tener entre 9 y 15 dígitos numéricos.");
            txtTelefono.requestFocus();
            return false;
        }

        if (!correo.isEmpty() && !correo.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            JOptionPane.showMessageDialog(this, "El correo ingresado no es válido.");
            txtCorreo.requestFocus();
            return false;
        }

        return true;
    }

    private void guardarCliente() {
        if (!validarCampos()) return;

        String sql = "INSERT INTO clientes(nombre, telefono, correo, dni, direccion) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtNombre.getText().trim());
            ps.setString(2, txtTelefono.getText().trim());
            ps.setString(3, txtCorreo.getText().trim());
            ps.setString(4, txtDni.getText().trim());
            ps.setString(5, txtDireccion.getText().trim());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
            limpiarCampos();
            cargarTabla("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar cliente: " + e.getMessage());
        }
    }

    private void actualizarCliente() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente de la tabla.");
            return;
        }
        if (!validarCampos()) return;

        String sql = "UPDATE clientes SET nombre=?, telefono=?, correo=?, dni=?, direccion=? WHERE id=?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtNombre.getText().trim());
            ps.setString(2, txtTelefono.getText().trim());
            ps.setString(3, txtCorreo.getText().trim());
            ps.setString(4, txtDni.getText().trim());
            ps.setString(5, txtDireccion.getText().trim());
            ps.setInt(6, Integer.parseInt(txtId.getText()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.");
            limpiarCampos();
            cargarTabla("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar cliente: " + e.getMessage());
        }
    }

    private void eliminarCliente() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente de la tabla.");
            return;
        }
        int id = (int) tabla.getValueAt(fila, 0);

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Deseas eliminar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM clientes WHERE id=?";

            try (Connection con = ConexionBD.conectar();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.");
                limpiarCampos();
                cargarTabla("");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar cliente: " + e.getMessage());
            }
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDni.setText("");
        txtDireccion.setText("");
        txtBuscar.setText("");
    }

    private static ClientesVista instancia;

    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new ClientesVista();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
