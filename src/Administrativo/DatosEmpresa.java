package Administrativo;

import static Clases.Colores.*;
import Clases.ConexionBD;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DatosEmpresa extends JFrame {

    private static DatosEmpresa instancia;
    private JTextField txtNombre, txtDireccion, txtRuc, txtTelefono, txtEmail;
    private JLabel lblLogo;
    private JButton btnGuardar, btnSubirLogo;
    private byte[] logoBytes;

    public static void abrir() {
        if (instancia == null || !instancia.isVisible()) {
            instancia = new DatosEmpresa();
            instancia.setVisible(true);
        }
        instancia.toFront();
    }

    private DatosEmpresa() {
        setTitle("Datos de la Empresa");
        setSize(600, 500);
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

        lblLogo = new JLabel();
        lblLogo.setPreferredSize(new Dimension(120, 120));
        lblLogo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Imagenes/Compra.png"));
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblLogo.setText("Sin logo");
        }
        c.gridx = 0; c.gridy = 0; c.gridheight = 3; c.weightx = 0;
        c.anchor = GridBagConstraints.NORTH;
        panel.add(lblLogo, c);

        btnSubirLogo = new JButton("Cambiar logo");
        btnSubirLogo.setBackground(CAFE);
        btnSubirLogo.setForeground(Color.WHITE);
        btnSubirLogo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSubirLogo.setFocusPainted(false);
        btnSubirLogo.setBorderPainted(false);
        btnSubirLogo.addActionListener(e -> subirLogo());
        c.gridx = 0; c.gridy = 3; c.gridheight = 1; c.anchor = GridBagConstraints.CENTER;
        panel.add(btnSubirLogo, c);

        c.gridheight = 1; c.anchor = GridBagConstraints.WEST;
        c.gridx = 1; c.gridy = 0; c.weightx = 0;
        panel.add(new JLabel("Nombre:"), c);
        c.gridx = 2; c.weightx = 1;
        txtNombre = new JTextField();
        panel.add(txtNombre, c);

        c.gridx = 1; c.gridy = 1; c.weightx = 0;
        panel.add(new JLabel("Dirección:"), c);
        c.gridx = 2; c.weightx = 1;
        txtDireccion = new JTextField();
        panel.add(txtDireccion, c);

        c.gridx = 1; c.gridy = 2; c.weightx = 0;
        panel.add(new JLabel("RUC:"), c);
        c.gridx = 2; c.weightx = 1;
        txtRuc = new JTextField();
        panel.add(txtRuc, c);

        c.gridx = 1; c.gridy = 3; c.weightx = 0;
        panel.add(new JLabel("Teléfono:"), c);
        c.gridx = 2; c.weightx = 1;
        txtTelefono = new JTextField();
        panel.add(txtTelefono, c);

        c.gridx = 1; c.gridy = 4; c.weightx = 0;
        panel.add(new JLabel("Email:"), c);
        c.gridx = 2; c.weightx = 1;
        txtEmail = new JTextField();
        panel.add(txtEmail, c);

        c.gridx = 0; c.gridy = 5; c.gridwidth = 3; c.weightx = 0;
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

    private void subirLogo() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Imágenes (png, jpg, jpeg)", "png", "jpg", "jpeg"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File archivo = fc.getSelectedFile();
                BufferedImage img = ImageIO.read(archivo);
                if (img == null) {
                    JOptionPane.showMessageDialog(this, "Formato no válido.");
                    return;
                }
                Image scaled = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(scaled));
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    ImageIO.write(img, "png", baos);
                    logoBytes = baos.toByteArray();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cargar imagen: " + e.getMessage());
            }
        }
    }

    private void cargarDatos() {
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT nombre, direccion, ruc, telefono, email, logo FROM empresa WHERE id=1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                txtNombre.setText(rs.getString("nombre") != null ? rs.getString("nombre") : "");
                txtDireccion.setText(rs.getString("direccion") != null ? rs.getString("direccion") : "");
                txtRuc.setText(rs.getString("ruc") != null ? rs.getString("ruc") : "");
                txtTelefono.setText(rs.getString("telefono") != null ? rs.getString("telefono") : "");
                txtEmail.setText(rs.getString("email") != null ? rs.getString("email") : "");
                byte[] imgBytes = rs.getBytes("logo");
                if (imgBytes != null) {
                    try {
                        BufferedImage bi = ImageIO.read(new ByteArrayInputStream(imgBytes));
                        Image scaled = bi.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        lblLogo.setIcon(new ImageIcon(scaled));
                        logoBytes = imgBytes;
                    } catch (Exception e) {
                        // usar logo por defecto
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró registro de empresa.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar: " + e.getMessage());
        }
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String ruc = txtRuc.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();

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
                     "INSERT INTO empresa (id, nombre, direccion, ruc, telefono, email, logo) "
                     + "VALUES (1, ?, ?, ?, ?, ?, ?) "
                     + "ON DUPLICATE KEY UPDATE nombre=?, direccion=?, ruc=?, telefono=?, email=?, logo=?")) {
            ps.setString(1, nombre);
            ps.setString(2, direccion);
            ps.setString(3, ruc);
            ps.setString(4, telefono);
            ps.setString(5, email);
            if (logoBytes != null) {
                ps.setBytes(6, logoBytes);
            } else {
                ps.setNull(6, Types.BLOB);
            }
            ps.setString(7, nombre);
            ps.setString(8, direccion);
            ps.setString(9, ruc);
            ps.setString(10, telefono);
            ps.setString(11, email);
            if (logoBytes != null) {
                ps.setBytes(12, logoBytes);
            } else {
                ps.setNull(12, Types.BLOB);
            }
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Datos guardados correctamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }
}
