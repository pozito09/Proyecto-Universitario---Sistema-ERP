package Administrativo;

import Clases.ConexionBD;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class EmpresaVista extends JFrame {

    private static EmpresaVista instancia;
    private JTextField txtNombre, txtDireccion, txtRuc;
    private JButton btnGuardar;

    public static void abrir() {
        if (instancia == null || !instancia.isVisible()) {
            instancia = new EmpresaVista();
            instancia.setVisible(true);
        }
        instancia.toFront();
    }

    private EmpresaVista() {
        setTitle("Datos de la Empresa");
        setSize(500, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        cargarDatos();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 220));
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
        btnGuardar.setBackground(new Color(102, 51, 0));
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
             PreparedStatement ps = con.prepareStatement("SELECT * FROM empresa WHERE id=1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                txtNombre.setText(rs.getString("nombre"));
                txtDireccion.setText(rs.getString("direccion"));
                txtRuc.setText(rs.getString("ruc"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar: " + e.getMessage());
        }
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String ruc = txtRuc.getText().trim();
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE empresa SET nombre=?, direccion=?, ruc=? WHERE id=1")) {
            ps.setString(1, nombre);
            ps.setString(2, direccion);
            ps.setString(3, ruc);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Datos guardados correctamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }
}
