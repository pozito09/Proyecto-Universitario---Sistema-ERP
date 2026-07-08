package Productos;

import static Clases.Colores.*;
import Clases.Botones;

import Productos.AgregarProducto;
import Clases.ConexionBD;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ProductosVista extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;

    private JLabel lblProductos;
    private JLabel lblCategorias;
    private JLabel lblStock;

    private JButton btnEditar;

    public ProductosVista() {

        setTitle("CAFECOMETA ERP - PRODUCTOS");
        setSize(1200, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // HEADER

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(100, 70));

        JLabel titulo = new JLabel("GESTIÓN DE PRODUCTOS");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setBorder(new EmptyBorder(10,20,10,20));

        header.add(titulo, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // CENTRO

        JPanel centro = new JPanel(new BorderLayout());

        // TARJETAS KPI

        JPanel tarjetas = new JPanel(
                new GridLayout(1,3,15,15));

        tarjetas.setBorder(
                new EmptyBorder(15,15,15,15));

        lblProductos = new JLabel("0");
        lblCategorias = new JLabel("0");
        lblStock = new JLabel("0");

        tarjetas.add(
                crearTarjeta(
                        "PRODUCTOS",
                        lblProductos,
                        DORADO));

        tarjetas.add(
                crearTarjeta(
                        "CATEGORÍAS",
                        lblCategorias,
                        DORADO_HOVER));

        tarjetas.add(
                crearTarjeta(
                        "STOCK TOTAL",
                        lblStock,
                        PRECIO));

        centro.add(tarjetas,
                BorderLayout.NORTH);

        // TABLA

        String columnas[] = {
            "ID",
            "Producto",
            "Categoría",
            "Precio",
            "Stock"
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
                Botones.crear("Nuevo Producto", VERDE);

        btnEditar =
                Botones.crear("Editar", DORADO, DORADO_HOVER);

        JButton btnEliminar =
                Botones.crear("Eliminar", ROJO, ROJO_HOVER);

        JButton btnActualizar =
                Botones.crear("Actualizar", DORADO, DORADO_HOVER);

        btnNuevo.addActionListener(e -> {
            JDialog dlg = new JDialog(this, "Nuevo Producto", true);
            dlg.getContentPane().add(new AgregarProducto());
            dlg.pack();
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);
            cargarProductos();
        });
        btnEditar.addActionListener(e -> mostrarDialogoEditar());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnActualizar.addActionListener(e -> cargarProductos());

        botones.add(btnNuevo);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);

        add(botones,
                BorderLayout.SOUTH);

        cargarProductos();
    }

    private void mostrarDialogoEditar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla");
            return;
        }
        int id = (int) modelo.getValueAt(fila, 0);

        JDialog dlg = new JDialog(this, "Editar Producto", true);
        dlg.getContentPane().setBackground(FONDO);
        dlg.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(0, 55));
        JLabel lblTitulo = new JLabel("EDITAR PRODUCTO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.CENTER);
        dlg.add(header, BorderLayout.NORTH);

        // Formulario
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(FONDO);
        formulario.setBorder(new EmptyBorder(20, 50, 10, 50));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7, 5, 7, 5);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNombre = new JTextField(20);
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNombre.setPreferredSize(new Dimension(200, 32));
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 160, 80), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JTextField txtDescripcion = new JTextField(20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescripcion.setPreferredSize(new Dimension(200, 32));
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 160, 80), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JTextField txtPrecio = new JTextField(20);
        txtPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPrecio.setPreferredSize(new Dimension(200, 32));
        txtPrecio.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 160, 80), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        txtPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != java.awt.event.KeyEvent.VK_BACK_SPACE)
                    e.consume();
            }
        });

        JComboBox<String> cmbCategoria = new JComboBox<>(new String[]{"Cafés", "Emparedados", "Postres"});
        cmbCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbCategoria.setBackground(Color.WHITE);

        JLabel lblRutaImagen = new JLabel("Sin imagen seleccionada");
        lblRutaImagen.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblRutaImagen.setForeground(new Color(150, 120, 70));

        JLabel lblImagenPreview = new JLabel("Vista previa", SwingConstants.CENTER);
        lblImagenPreview.setPreferredSize(new Dimension(130, 95));
        lblImagenPreview.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblImagenPreview.setForeground(new Color(180, 150, 100));
        lblImagenPreview.setBorder(BorderFactory.createLineBorder(new Color(200, 160, 80), 1));
        lblImagenPreview.setBackground(new Color(230, 220, 195));
        lblImagenPreview.setOpaque(true);

        final byte[][] bytesImagen = new byte[1][];
        bytesImagen[0] = null;

        // Cargar datos del producto
        String sqlSelect = "SELECT nombre, descripcion, precio, categoria, imagen FROM productos WHERE id=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sqlSelect)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtNombre.setText(rs.getString("nombre"));
                txtDescripcion.setText(rs.getString("descripcion"));
                txtPrecio.setText(String.valueOf(rs.getDouble("precio")));
                cmbCategoria.setSelectedItem(rs.getString("categoria"));

                byte[] imgBytes = rs.getBytes("imagen");
                if (imgBytes != null && imgBytes.length > 0) {
                    lblRutaImagen.setText("Imagen guardada en BD");
                    BufferedImage bi = ImageIO.read(new java.io.ByteArrayInputStream(imgBytes));
                    if (bi != null) {
                        Image scaled = bi.getScaledInstance(130, 95, Image.SCALE_SMOOTH);
                        lblImagenPreview.setIcon(new ImageIcon(scaled));
                        lblImagenPreview.setText("");
                    }
                }
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
            dlg.dispose();
            return;
        }

        // Nombre
        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        formulario.add(crearLabelForm("Nombre:"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(txtNombre, g);

        // Descripción
        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        formulario.add(crearLabelForm("Descripción:"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(txtDescripcion, g);

        // Precio
        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        formulario.add(crearLabelForm("Precio (S/):"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(txtPrecio, g);

        // Categoría
        g.gridx = 0; g.gridy = 3; g.weightx = 0;
        formulario.add(crearLabelForm("Categoría:"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(cmbCategoria, g);

        // Imagen
        g.gridx = 0; g.gridy = 4; g.weightx = 0;
        formulario.add(crearLabelForm("Imagen:"), g);
        g.gridx = 1; g.weightx = 1;
        JPanel panelImg = new JPanel(new BorderLayout(5, 0));
        panelImg.setOpaque(false);
        JButton btnSeleccionar = Botones.crear("Seleccionar", DORADO, DORADO_HOVER);
        btnSeleccionar.setPreferredSize(new Dimension(110, 28));
        btnSeleccionar.addActionListener(ev -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Seleccionar imagen");
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png"));
            if (fc.showOpenDialog(dlg) == JFileChooser.APPROVE_OPTION) {
                File archivo = fc.getSelectedFile();
                lblRutaImagen.setText(archivo.getName());
                try {
                    BufferedImage img = ImageIO.read(archivo);
                    if (img != null) {
                        String formato = archivo.getName().toLowerCase().endsWith(".png") ? "PNG" : "JPG";
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(img, formato, baos);
                        bytesImagen[0] = baos.toByteArray();
                        Image scaled = img.getScaledInstance(130, 95, Image.SCALE_SMOOTH);
                        lblImagenPreview.setIcon(new ImageIcon(scaled));
                        lblImagenPreview.setText("");
                    }
                } catch (IOException ex) {
                    lblImagenPreview.setText("Error al cargar imagen");
                    bytesImagen[0] = null;
                }
            }
        });
        panelImg.add(lblRutaImagen, BorderLayout.CENTER);
        panelImg.add(btnSeleccionar, BorderLayout.EAST);
        formulario.add(panelImg, g);

        // Preview
        g.gridx = 0; g.gridy = 5; g.gridwidth = 2;
        g.fill = GridBagConstraints.NONE;
        g.anchor = GridBagConstraints.CENTER;
        formulario.add(lblImagenPreview, g);

        dlg.add(formulario, BorderLayout.CENTER);

        // Botón Guardar Cambios
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        botones.setBackground(FONDO);

        JButton btnGuardar = Botones.crear("Guardar Cambios", DORADO, DORADO_HOVER);
        btnGuardar.setPreferredSize(new Dimension(170, 36));
        btnGuardar.addActionListener(ev -> {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "El nombre no puede estar vacío.");
                return;
            }
            double precio;
            try {
                precio = Double.parseDouble(txtPrecio.getText().trim());
                if (precio <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Ingresa un precio válido.");
                return;
            }
            String descripcion = txtDescripcion.getText().trim();
            String categoria = (String) cmbCategoria.getSelectedItem();

            if (bytesImagen[0] != null) {
                String sql = "UPDATE productos SET nombre=?, descripcion=?, categoria=?, precio=?, imagen=? WHERE id=?";
                try (Connection con = ConexionBD.conectar();
                     PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, nombre);
                    ps.setString(2, descripcion);
                    ps.setString(3, categoria);
                    ps.setDouble(4, precio);
                    ps.setBytes(5, bytesImagen[0]);
                    ps.setInt(6, id);
                    ps.executeUpdate();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dlg, "Error: " + ex.getMessage());
                    return;
                }
            } else {
                String sql = "UPDATE productos SET nombre=?, descripcion=?, categoria=?, precio=? WHERE id=?";
                try (Connection con = ConexionBD.conectar();
                     PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, nombre);
                    ps.setString(2, descripcion);
                    ps.setString(3, categoria);
                    ps.setDouble(4, precio);
                    ps.setInt(5, id);
                    ps.executeUpdate();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dlg, "Error: " + ex.getMessage());
                    return;
                }
            }
            JOptionPane.showMessageDialog(dlg, "Producto actualizado correctamente.");
            dlg.dispose();
            cargarProductos();
        });

        botones.add(btnGuardar);
        dlg.add(botones, BorderLayout.SOUTH);

        dlg.setSize(550, 480);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private JLabel crearLabelForm(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(TEXTO);
        return lbl;
    }



    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla");
            return;
        }
        int id = (int) modelo.getValueAt(fila, 0);
        String nombre = modelo.getValueAt(fila, 1).toString();
        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el producto \"" + nombre + "\"?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (conf == JOptionPane.YES_OPTION) {
            try (Connection con = ConexionBD.conectar();
                 PreparedStatement ps = con.prepareStatement("DELETE FROM productos WHERE id=?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
                cargarProductos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
            }
        }
    }

    private JPanel crearTarjeta(
            String titulo,
            JLabel valor,
            Color color){

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

        valor.setForeground(Color.WHITE);

        valor.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24));

        valor.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(valor);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void cargarProductos() {

        modelo.setRowCount(0);

        try {

            Connection con =
                    ConexionBD.conectar();

            String sql =
                    "SELECT id,nombre,categoria,precio,stock FROM productos";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            int totalProductos = 0;
            int stockTotal = 0;

            while (rs.next()) {

                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("categoria"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                });

                totalProductos++;

                stockTotal += rs.getInt("stock");
            }

            String sqlCategorias =
                    "SELECT COUNT(DISTINCT categoria) total FROM productos";

            PreparedStatement ps2 =
                    con.prepareStatement(sqlCategorias);

            ResultSet rs2 =
                    ps2.executeQuery();

            int categorias = 0;

            if(rs2.next()) {
                categorias =
                        rs2.getInt("total");
            }

            lblProductos.setText(
                    String.valueOf(totalProductos));

            lblCategorias.setText(
                    String.valueOf(categorias));

            lblStock.setText(
                    String.valueOf(stockTotal));

            rs.close();
            ps.close();

            rs2.close();
            ps2.close();

            con.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar productos:\n" +
                    e.getMessage());

        }
    }

    private static ProductosVista instancia;

    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new ProductosVista();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
