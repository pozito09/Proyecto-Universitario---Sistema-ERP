package Administrativo;

import static Clases.Colores.*;
import Clases.ConexionBD;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class DatosEmpresa extends JFrame {

    private static DatosEmpresa instancia;
    private JTextField txtNombre, txtDireccion, txtRuc;
    private JButton btnGuardar;

    public static void abrir() {
        if (instancia == null || !instancia.isVisible()) {
            instancia = new DatosEmpresa();
            instancia.setVisible(true);
        }
        instancia.toFront();
    }

    private DatosEmpresa() {
        setTitle("Datos de la Empresa");
        setSize(500, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        cargarDatos();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(FONDO);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 10, 6, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        panel.add(new JLabel("Nombre:"), c);
        c.gridx = 1; c.weightx = 1;
        txtNombre = new JTextField();
        panel.add(txtNombre, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0;
        panel.add(new JLabel("Dirección:"), c);
        c.gridx = 1; c.weightx = 1;
        txtDireccion = new JTextField();
        panel.add(txtDireccion, c);

        c.gridx = 0; c.gridy = 2; c.weightx = 0;
        panel.add(new JLabel("RUC:"), c);
        c.gridx = 1; c.weightx = 1;
        txtRuc = new JTextField();
        panel.add(txtRuc, c);

        c.gridx = 0; c.gridy = 3; c.gridwidth = 2; c.weightx = 0;
        c.anchor = GridBagConstraints.CENTER;
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(CAFE);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        panel.add(btnGuardar, c);

        btnGuardar.addActionListener(e -> guardar());

        add(panel);
    }

    private void cargarDatos() {
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT nombre, direccion, ruc FROM empresa WHERE id=1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String direccion = rs.getString("direccion");
                String ruc = rs.getString("ruc");
                txtNombre.setText(nombre != null ? nombre : "");
                txtDireccion.setText(direccion != null ? direccion : "");
                txtRuc.setText(ruc != null ? ruc : "");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el registro de empresa.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar: " + e.getMessage());
        }
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String ruc = txtRuc.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            txtNombre.requestFocus();
            return;
        }

        if (!ruc.isEmpty() && !ruc.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "El RUC debe tener 11 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
            txtRuc.requestFocus();
            return;
        }

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO empresa (id, nombre, direccion, ruc) VALUES (1, ?, ?, ?) "
                     + "ON DUPLICATE KEY UPDATE nombre=?, direccion=?, ruc=?")) {
            ps.setString(1, nombre);
            ps.setString(2, direccion);
            ps.setString(3, ruc);
            ps.setString(4, nombre);
            ps.setString(5, direccion);
            ps.setString(6, ruc);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Datos guardados correctamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }
}
