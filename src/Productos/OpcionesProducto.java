package Productos;

import static Clases.Colores.*;
import Clases.Auditoria;

import java.io.ByteArrayOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class OpcionesProducto extends JPanel {

    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtDescripcion;
    private JComboBox<String> cmbCategoria;
    private JLabel lblImagenPreview;
    private JLabel lblRutaImagen;
    private byte[] bytesImagenSeleccionada = null;

    public OpcionesProducto() {
        setLayout(new BorderLayout());
        setBackground(FONDO);
        initUI();
    }

    private void initUI() {
        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(0, 55));
        JLabel lblTitulo = new JLabel("Agregar nuevo producto", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // FORMULARIO
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(FONDO);
        formulario.setBorder(new EmptyBorder(25, 50, 10, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formulario.add(crearLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        txtNombre = crearCampo();
        formulario.add(txtNombre, gbc);

        // Descripción
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formulario.add(crearLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        txtDescripcion = crearCampo();
        formulario.add(txtDescripcion, gbc);

        // Precio
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formulario.add(crearLabel("Precio (S/):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        txtPrecio = crearCampo();
        txtPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
        formulario.add(txtPrecio, gbc);

        // Categoría
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        formulario.add(crearLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        cmbCategoria = new JComboBox<>(new String[]{"Cafés", "Emparedados", "Postres"});
        cmbCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbCategoria.setBackground(Color.WHITE);
        cmbCategoria.setPreferredSize(new Dimension(200, 32));
        formulario.add(cmbCategoria, gbc);

        // Imagen
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        formulario.add(crearLabel("Imagen:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        JPanel panelImagen = new JPanel(new BorderLayout(5, 0));
        panelImagen.setOpaque(false);

        lblRutaImagen = new JLabel("Sin imagen seleccionada");
        lblRutaImagen.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblRutaImagen.setForeground(TEXTO_CLARO);

        JButton btnSeleccionar = new JButton("Seleccionar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? DORADO_CLARO : DORADO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnSeleccionar.setForeground(Color.WHITE);
        btnSeleccionar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnSeleccionar.setFocusPainted(false);
        btnSeleccionar.setBorderPainted(false);
        btnSeleccionar.setContentAreaFilled(false);
        btnSeleccionar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSeleccionar.setPreferredSize(new Dimension(100, 30));
        btnSeleccionar.addActionListener(e -> seleccionarImagen());

        panelImagen.add(lblRutaImagen, BorderLayout.CENTER);
        panelImagen.add(btnSeleccionar, BorderLayout.EAST);
        formulario.add(panelImagen, gbc);

        // Preview imagen
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        lblImagenPreview = new JLabel("Vista previa", SwingConstants.CENTER);
        lblImagenPreview.setPreferredSize(new Dimension(150, 110));
        lblImagenPreview.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblImagenPreview.setForeground(TEXTO_PREVIEW);
        lblImagenPreview.setBorder(BorderFactory.createLineBorder(DORADO_BORDE, 1));
        lblImagenPreview.setBackground(FONDO_TARJETA);
        lblImagenPreview.setOpaque(true);
        formulario.add(lblImagenPreview, gbc);

        add(formulario, BorderLayout.CENTER);

        // BOTÓN AGREGAR
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(FONDO);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        JButton btnAgregar = new JButton("+ Agregar producto") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed() ? new Color(140, 95, 15)
                        : getModel().isRollover() ? DORADO_CLARO : DORADO;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorderPainted(false);
        btnAgregar.setContentAreaFilled(false);
        btnAgregar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAgregar.setPreferredSize(new Dimension(180, 38));
        btnAgregar.addActionListener(e -> agregarProducto());

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnAgregar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void seleccionarImagen() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccionar imagen del producto");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes (JPG, PNG)", "jpg", "jpeg", "png"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            lblRutaImagen.setText(archivo.getName());
            try {
                BufferedImage img = ImageIO.read(archivo);
                if (img != null) {
                    String formato = archivo.getName().toLowerCase().endsWith(".png") ? "PNG" : "JPG";
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(img, formato, baos);
                    bytesImagenSeleccionada = baos.toByteArray();

                    Image scaled = img.getScaledInstance(150, 110, Image.SCALE_SMOOTH);
                    lblImagenPreview.setIcon(new ImageIcon(scaled));
                    lblImagenPreview.setText("");
                }
            } catch (IOException ex) {
                lblImagenPreview.setText("Error al cargar imagen");
                bytesImagenSeleccionada = null;
            }
        }
    }

    private void agregarProducto() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el nombre del producto.",
                    "Campo vacío", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(txtPrecio.getText().trim());
            if (precio <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un precio válido.",
                    "Precio inválido", JOptionPane.WARNING_MESSAGE);
            txtPrecio.requestFocus();
            return;
        }

        String descripcion = txtDescripcion.getText().trim();
        String categoria = (String) cmbCategoria.getSelectedItem();

        // Guardar en BD
        String sql = "INSERT INTO productos (nombre, descripcion, categoria, precio, emoji, imagen) VALUES (?,?,?,?,?,?)";
        try (java.sql.Connection con = Clases.ConexionBD.conectar(); java.sql.PreparedStatement ps = con.prepareStatement(sql, java.sql.PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, categoria);
            ps.setDouble(4, precio);
            ps.setString(5, "");
            if (bytesImagenSeleccionada != null) {
                ps.setBytes(6, bytesImagenSeleccionada);
            } else {
                ps.setNull(6, java.sql.Types.BLOB);
            }
            ps.executeUpdate();
            try (java.sql.ResultSet rsKeys = ps.getGeneratedKeys()) {
                if (rsKeys.next()) {
                    Auditoria.crear("productos", rsKeys.getInt(1), "Producto: " + nombre);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar en BD: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "✓ \"" + nombre + "\" agregado correctamente.",
                "Producto agregado", JOptionPane.INFORMATION_MESSAGE);
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        cmbCategoria.setSelectedIndex(0);
        lblRutaImagen.setText("Sin imagen seleccionada");
        lblImagenPreview.setIcon(null);
        lblImagenPreview.setText("Vista previa");
        bytesImagenSeleccionada = null;
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(TEXTO);
        return lbl;
    }

    private JTextField crearCampo() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(200, 32));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DORADO_BORDE, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return txt;
    }
}
