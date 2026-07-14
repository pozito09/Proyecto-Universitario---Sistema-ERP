package Compras;

import static Clases.Colores.*;
import Clases.Botones;
import Clases.ConexionBD;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import Clases.Auditoria;

// ── DESCRIPCIÓN: Ventana para registrar una nueva compra a un proveedor con múltiples insumos. ──
public class OpcionesCompra extends JFrame {

    private JComboBox<String> cmbProveedor;
    private JComboBox<String> cmbInsumo;
    private JTextField txtCantidad, txtPrecio;
    private JTable tabla;
    private JButton btnAgregar, btnQuitar, btnGuardar, btnLimpiar;
    private DefaultTableModel modelo;
    private final java.util.List<ItemTemp> items = new ArrayList<>();

    // ── DESCRIPCIÓN: Clase auxiliar para almacenar temporalmente un ítem de compra. ──
    static class ItemTemp {
        String insumo;
        int idInsumo;
        double cantidad;
        double precio;
        double subtotal;
        ItemTemp(String insumo, int idInsumo, double cantidad, double precio, double subtotal) {
            this.insumo = insumo;
            this.idInsumo = idInsumo;
            this.cantidad = cantidad;
            this.precio = precio;
            this.subtotal = subtotal;
        }
    }

    // ── DESCRIPCIÓN: Inicializa la ventana, carga proveedores e insumos desde BD. ──
    public OpcionesCompra() {
        setTitle("Gestión de Compras - Cafe Cometa");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        iniciarComponentes();
        cargarProveedores();
        cargarInsumos();
    }

    // ── DESCRIPCIÓN: Construye la interfaz: selectors de proveedor/insumo, campos cantidad/precio, tabla de ítems, botones. ──
    private void iniciarComponentes() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(FONDO);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(100, 70));
        header.setBorder(new EmptyBorder(10, 20, 10, 20));
        JLabel titulo = new JLabel("GESTIÓN DE COMPRAS");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(titulo, BorderLayout.WEST);
        content.add(header, BorderLayout.NORTH);

        JPanel centro = new JPanel(null);
        centro.setBackground(FONDO);

        JLabel lblProveedor = new JLabel("Proveedor:");
        lblProveedor.setBounds(30, 20, 100, 25);
        centro.add(lblProveedor);
        cmbProveedor = new JComboBox<>();
        cmbProveedor.setBounds(130, 20, 220, 25);
        centro.add(cmbProveedor);

        JLabel lblInsumo = new JLabel("Insumo:");
        lblInsumo.setBounds(400, 20, 100, 25);
        centro.add(lblInsumo);
        cmbInsumo = new JComboBox<>();
        cmbInsumo.setBounds(500, 20, 220, 25);
        centro.add(cmbInsumo);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(30, 60, 100, 25);
        centro.add(lblCantidad);
        txtCantidad = new JTextField();
        txtCantidad.setBounds(130, 60, 100, 25);
        centro.add(txtCantidad);

        JLabel lblPrecio = new JLabel("Precio Unit.:");
        lblPrecio.setBounds(260, 60, 100, 25);
        centro.add(lblPrecio);
        txtPrecio = new JTextField();
        txtPrecio.setBounds(350, 60, 100, 25);
        centro.add(txtPrecio);

        btnAgregar = Botones.crear("Agregar", DORADO, DORADO_CLARO);
        btnAgregar.setBounds(500, 55, 120, 35);
        centro.add(btnAgregar);

        modelo = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        modelo.setColumnIdentifiers(new Object[]{"Insumo", "Cantidad", "Precio Unit.", "Subtotal"});
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(30, 110, 820, 320);
        centro.add(scroll);

        btnQuitar = Botones.crear("Quitar seleccionado", CAFE);
        btnQuitar.setBounds(30, 440, 160, 35);
        centro.add(btnQuitar);

        btnGuardar = Botones.crear("Guardar Compra", DORADO, DORADO_CLARO);
        btnGuardar.setBounds(350, 440, 180, 35);
        centro.add(btnGuardar);

        btnLimpiar = Botones.crear("Limpiar todo", CAFE);
        btnLimpiar.setBounds(550, 440, 160, 35);
        centro.add(btnLimpiar);

        btnAgregar.addActionListener(e -> agregarItem());
        btnQuitar.addActionListener(e -> quitarItem());
        btnGuardar.addActionListener(e -> guardarCompra());
        btnLimpiar.addActionListener(e -> limpiarTodo());

        content.add(centro, BorderLayout.CENTER);
        add(content);
    }

    // ── DESCRIPCIÓN: Carga los nombres de proveedores en el ComboBox. ──
    private void cargarProveedores() {
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT nombre FROM proveedores");
             ResultSet rs = ps.executeQuery()) {
            cmbProveedor.removeAllItems();
            while (rs.next()) {
                cmbProveedor.addItem(rs.getString("nombre"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + e.getMessage());
        }
    }

    // ── DESCRIPCIÓN: Carga los nombres de insumos en el ComboBox. ──
    private void cargarInsumos() {
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT id, nombre FROM insumos");
             ResultSet rs = ps.executeQuery()) {
            cmbInsumo.removeAllItems();
            while (rs.next()) {
                cmbInsumo.addItem(rs.getString("nombre"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar insumos: " + e.getMessage());
        }
    }

    // ── DESCRIPCIÓN: Valida datos, obtiene ID del insumo, agrega ítem a la tabla y lista temporal. ──
    private void agregarItem() {
        if (cmbInsumo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un insumo.");
            return;
        }
        String insumo = cmbInsumo.getSelectedItem().toString();
        double cantidad, precio;
        try {
            cantidad = Double.parseDouble(txtCantidad.getText().trim());
            precio = Double.parseDouble(txtPrecio.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese cantidad y precio válidos.");
            return;
        }
        if (cantidad <= 0 || precio <= 0) {
            JOptionPane.showMessageDialog(this, "Cantidad y precio deben ser mayores a 0.");
            return;
        }

        int idInsumo = 0;
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT id FROM insumos WHERE nombre=?")) {
            ps.setString(1, insumo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) idInsumo = rs.getInt("id");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return;
        }

        double subtotal = cantidad * precio;
        items.add(new ItemTemp(insumo, idInsumo, cantidad, precio, subtotal));
        modelo.addRow(new Object[]{insumo, cantidad, precio, subtotal});
        txtCantidad.setText("");
        txtPrecio.setText("");
    }

    // ── DESCRIPCIÓN: Elimina el ítem seleccionado de la tabla y lista temporal. ──
    private void quitarItem() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un item de la tabla.");
            return;
        }
        items.remove(fila);
        modelo.removeRow(fila);
    }

    // ── DESCRIPCIÓN: Valida proveedor e ítems, crea compra en BD, inserta detalle, incrementa stock de insumos, con transacción y auditoría. ──
    private void guardarCompra() {
        if (cmbProveedor.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor.");
            return;
        }
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un insumo a la compra.");
            return;
        }

        String proveedor = cmbProveedor.getSelectedItem().toString();
        double total = items.stream().mapToDouble(i -> i.subtotal).sum();

        try (Connection con = ConexionBD.conectar()) {
            con.setAutoCommit(false);

            int idProveedor = 0;
            try (PreparedStatement ps = con.prepareStatement("SELECT id FROM proveedores WHERE nombre=?")) {
                ps.setString(1, proveedor);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) idProveedor = rs.getInt("id");
                }
            }

            int idCompra;
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO compras(id_proveedor, total, estado) VALUES(?,?,'Pendiente')",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idProveedor);
                ps.setDouble(2, total);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    idCompra = rs.getInt(1);
                }
            }

            try (PreparedStatement psDetalle = con.prepareStatement(
                    "INSERT INTO detalle_compras(id_compra, id_insumo, cantidad, precio_unitario, subtotal) VALUES(?,?,?,?,?)");
                 PreparedStatement psStock = con.prepareStatement("UPDATE insumos SET stock = stock + ? WHERE id=?")) {
                for (ItemTemp item : items) {
                    psDetalle.setInt(1, idCompra);
                    psDetalle.setInt(2, item.idInsumo);
                    psDetalle.setDouble(3, item.cantidad);
                    psDetalle.setDouble(4, item.precio);
                    psDetalle.setDouble(5, item.subtotal);
                    psDetalle.addBatch();
                    psStock.setDouble(1, item.cantidad);
                    psStock.setInt(2, item.idInsumo);
                    psStock.addBatch();
                }
                psDetalle.executeBatch();
                psStock.executeBatch();
            }

            con.commit();
            Auditoria.crear("compras", idCompra, "Compra a " + proveedor + " por S/ " + String.format("%.2f", total));
            JOptionPane.showMessageDialog(this, "Compra registrada correctamente (Total: S/ " + String.format("%,.2f", total) + ")");
            limpiarTodo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar compra: " + e.getMessage());
        }
    }

    // ── DESCRIPCIÓN: Reinicia todos los campos, tabla y lista de ítems. ──
    private void limpiarTodo() {
        items.clear();
        modelo.setRowCount(0);
        txtCantidad.setText("");
        txtPrecio.setText("");
        if (cmbProveedor.getItemCount() > 0) cmbProveedor.setSelectedIndex(0);
        if (cmbInsumo.getItemCount() > 0) cmbInsumo.setSelectedIndex(0);
    }

    private static OpcionesCompra instancia;

    // ── DESCRIPCIÓN: Patrón singleton. ──
    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new OpcionesCompra();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
