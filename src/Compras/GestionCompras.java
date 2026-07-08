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

public class GestionCompras extends JFrame {

    private JComboBox<String> cmbProveedor;
    private JComboBox<String> cmbInsumo;
    private JTextField txtCantidad, txtPrecio;
    private JTable tabla;
    private JButton btnGuardar, btnLimpiar;
    private DefaultTableModel modelo;

    public GestionCompras() {
        setTitle("Gestión de Compras - CafeCometa");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarProveedores();
        cargarInsumos();
    }

    private void iniciarComponentes() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(FONDO);

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(100, 70));
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("GESTIÓN DE COMPRAS");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(titulo, BorderLayout.WEST);

        content.add(header, BorderLayout.NORTH);

        // CENTRO
        JPanel centro = new JPanel(null);
        centro.setBackground(FONDO);

        JLabel lblProveedor = new JLabel("Proveedor:");
        lblProveedor.setBounds(50, 30, 100, 25);
        centro.add(lblProveedor);

        cmbProveedor = new JComboBox<>();
        cmbProveedor.setBounds(160, 30, 220, 25);
        centro.add(cmbProveedor);

        JLabel lblInsumo = new JLabel("Insumo:");
        lblInsumo.setBounds(450, 30, 100, 25);
        centro.add(lblInsumo);

        cmbInsumo = new JComboBox<>();
        cmbInsumo.setBounds(550, 30, 220, 25);
        centro.add(cmbInsumo);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(50, 80, 100, 25);
        centro.add(lblCantidad);

        txtCantidad = new JTextField();
        txtCantidad.setBounds(160, 80, 220, 25);
        centro.add(txtCantidad);

        JLabel lblPrecio = new JLabel("Precio Unitario:");
        lblPrecio.setBounds(450, 80, 100, 25);
        centro.add(lblPrecio);

        txtPrecio = new JTextField();
        txtPrecio.setBounds(550, 80, 220, 25);
        centro.add(txtPrecio);

        btnGuardar = Botones.crear("Guardar Compra", DORADO, DORADO_HOVER);
        btnGuardar.setBounds(250, 150, 180, 35);
        centro.add(btnGuardar);

        btnLimpiar = Botones.crear("Limpiar", CAFE);
        btnLimpiar.setBounds(470, 150, 180, 35);
        centro.add(btnLimpiar);

        modelo = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        modelo.setColumnIdentifiers(new Object[]{
            "Proveedor", "Insumo", "Cantidad", "Precio Unitario", "Subtotal"
        });

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(50, 230, 780, 250);
        centro.add(scroll);

        btnGuardar.addActionListener(e -> agregarCompraATabla());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        content.add(centro, BorderLayout.CENTER);
        add(content);
    }



    private void cargarProveedores() {
        String sql = "SELECT nombre FROM proveedores";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            cmbProveedor.removeAllItems();

            while (rs.next()) {
                cmbProveedor.addItem(rs.getString("nombre"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + e.getMessage());
        }
    }

    private void cargarInsumos() {
        String sql = "SELECT nombre FROM insumos";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            cmbInsumo.removeAllItems();

            while (rs.next()) {
                cmbInsumo.addItem(rs.getString("nombre"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar insumos: " + e.getMessage());
        }
    }

    private void agregarCompraATabla() {

    try {

        String proveedor = cmbProveedor.getSelectedItem().toString();
        String insumo = cmbInsumo.getSelectedItem().toString();

        double cantidad = Double.parseDouble(txtCantidad.getText());
        double precio = Double.parseDouble(txtPrecio.getText());

        double subtotal = cantidad * precio;

        Connection con = ConexionBD.conectar();

        // Obtener ID proveedor
        int idProveedor = 0;

        PreparedStatement psProveedor = con.prepareStatement(
                "SELECT id FROM proveedores WHERE nombre=?");

        psProveedor.setString(1, proveedor);

        ResultSet rsProveedor = psProveedor.executeQuery();

        if (rsProveedor.next()) {
            idProveedor = rsProveedor.getInt("id");
        }

        // Registrar compra
        PreparedStatement psCompra = con.prepareStatement(
                "INSERT INTO compras(id_proveedor,total,estado) VALUES(?,?,'Pendiente')",
                PreparedStatement.RETURN_GENERATED_KEYS);

        psCompra.setInt(1, idProveedor);
        psCompra.setDouble(2, subtotal);

        psCompra.executeUpdate();

        ResultSet rsCompra = psCompra.getGeneratedKeys();

        int idCompra = 0;

        if (rsCompra.next()) {
            idCompra = rsCompra.getInt(1);
        }

        // Obtener ID insumo
        int idInsumo = 0;

        PreparedStatement psInsumo = con.prepareStatement(
                "SELECT id FROM insumos WHERE nombre=?");

        psInsumo.setString(1, insumo);

        ResultSet rsInsumo = psInsumo.executeQuery();

        if (rsInsumo.next()) {
            idInsumo = rsInsumo.getInt("id");
        }

        // Registrar detalle compra
        PreparedStatement psDetalle = con.prepareStatement(
                "INSERT INTO detalle_compras(id_compra,id_insumo,cantidad,precio_unitario,subtotal) VALUES(?,?,?,?,?)");

        psDetalle.setInt(1, idCompra);
        psDetalle.setInt(2, idInsumo);
        psDetalle.setDouble(3, cantidad);
        psDetalle.setDouble(4, precio);
        psDetalle.setDouble(5, subtotal);

        psDetalle.executeUpdate();

        // Actualizar stock
        PreparedStatement psStock = con.prepareStatement(
                "UPDATE insumos SET stock = stock + ? WHERE id=?");

        psStock.setDouble(1, cantidad);
        psStock.setInt(2, idInsumo);

        psStock.executeUpdate();

        // Mostrar en tabla
        modelo.addRow(new Object[]{
            proveedor,
            insumo,
            cantidad,
            precio,
            subtotal
        });

        JOptionPane.showMessageDialog(this,
                "Compra registrada correctamente");

        txtCantidad.setText("");
        txtPrecio.setText("");

    } catch (Exception e) {

        JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage());
    }
}

    private void limpiarCampos() {
        txtCantidad.setText("");
        txtPrecio.setText("");
        modelo.setRowCount(0);
    }

    private static GestionCompras instancia;

    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new GestionCompras();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
