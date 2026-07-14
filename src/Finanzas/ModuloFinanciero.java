package Finanzas;

import static Clases.Colores.*;
import Clases.ConexionBD;
import static Clases.GuardarSesion.nombreCompleto;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

// ── DESCRIPCIÓN: Módulo financiero. Muestra ingresos, gastos y margen bruto con filtros de período. Permite registrar gastos operativos. ──
public class ModuloFinanciero extends JFrame {

    private JLabel lblIngresos, lblGastos, lblMargenBruto;
    private DefaultTableModel modelo;
    private JSpinner spDesde, spHasta;

    // ── DESCRIPCIÓN: Configura header, filtros de fecha, tarjetas KPI (ingresos, gastos, margen), tabla de movimientos, y botones. ──
    public ModuloFinanciero() {
        setTitle("CAFÉ COMETA - FINANZAS");
        setSize(1200, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(100, 70));
        JLabel titulo = new JLabel("MÓDULO FINANCIERO");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setBorder(new EmptyBorder(10, 20, 10, 20));
        header.add(titulo, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout());

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        filtros.setBackground(FONDO);
        spDesde = new JSpinner(new SpinnerDateModel());
        spDesde.setEditor(new JSpinner.DateEditor(spDesde, "dd/MM/yyyy"));
        spDesde.setPreferredSize(new Dimension(130, 28));
        spHasta = new JSpinner(new SpinnerDateModel());
        spHasta.setEditor(new JSpinner.DateEditor(spHasta, "dd/MM/yyyy"));
        spHasta.setPreferredSize(new Dimension(130, 28));
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        spDesde.setValue(cal.getTime());
        spHasta.setValue(new java.util.Date());

        filtros.add(new JLabel("Desde:"));
        filtros.add(spDesde);
        filtros.add(new JLabel("Hasta:"));
        filtros.add(spHasta);
        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBackground(DORADO);
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.addActionListener(e -> cargarDatos());
        filtros.add(btnFiltrar);
        centro.add(filtros, BorderLayout.NORTH);

        // TARJETAS KPI
        JPanel tarjetas = new JPanel(new GridLayout(1, 3, 15, 15));
        tarjetas.setBorder(new EmptyBorder(15, 15, 15, 15));
        lblIngresos = new JLabel("S/ 0.00");
        lblGastos = new JLabel("S/ 0.00");
        lblMargenBruto = new JLabel("S/ 0.00");
        tarjetas.add(crearTarjeta("INGRESOS", lblIngresos, DORADO_CLARO));
        tarjetas.add(crearTarjeta("GASTOS", lblGastos, ROJO));
        tarjetas.add(crearTarjeta("MARGEN BRUTO", lblMargenBruto, DORADO));
        centro.add(tarjetas, BorderLayout.CENTER);

        // TABLA
        modelo = new DefaultTableModel(new Object[]{"Fecha", "Descripción", "Ingreso", "Egreso", "Tipo"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setBackground(CABECERA_TABLA);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(SELECCION_TABLA);
        tabla.setGridColor(GRILLA_TABLA);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel inferior = new JPanel(new BorderLayout());
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tarjetas, scroll);
        split.setResizeWeight(0.3);
        inferior.add(split, BorderLayout.CENTER);
        centro.add(inferior, BorderLayout.CENTER);

        add(centro, BorderLayout.CENTER);

        // BOTONES
        JPanel botones = new JPanel();

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarDatos());
        botones.add(btnActualizar);

        JButton btnNuevoGasto = new JButton("Nuevo Gasto Operativo");
        btnNuevoGasto.setBackground(DORADO);
        btnNuevoGasto.setForeground(Color.WHITE);
        btnNuevoGasto.setFocusPainted(false);
        btnNuevoGasto.addActionListener(e -> nuevoGasto());
        botones.add(btnNuevoGasto);

        JButton btnEliminarGasto = new JButton("Eliminar Gasto");
        btnEliminarGasto.setBackground(ROJO);
        btnEliminarGasto.setForeground(Color.WHITE);
        btnEliminarGasto.setFocusPainted(false);
        btnEliminarGasto.addActionListener(e -> eliminarGasto(tabla));
        botones.add(btnEliminarGasto);

        add(botones, BorderLayout.SOUTH);

        cargarDatos();
    }

    // ── DESCRIPCIÓN: Crea tarjeta KPI con título y valor. ──
    private JPanel crearTarjeta(String titulo, JLabel lblValor, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblValor.setForeground(Color.WHITE);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalGlue());
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblValor);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    // ── DESCRIPCIÓN: Calcula ingresos (ventas pagadas), gastos (compras + gastos operativos), margen bruto, y carga movimientos en tabla. ──
    private void cargarDatos() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String desde = sdf.format((java.util.Date) spDesde.getValue());
        String hasta = sdf.format((java.util.Date) spHasta.getValue());

        try (Connection cn = ConexionBD.conectar()) {
            modelo.setRowCount(0);

            double ingresos = 0, gastosCompras = 0, gastosOp = 0;

            String sqlIngresos = "SELECT COALESCE(SUM(total),0) total FROM pedidos WHERE estado_pago = 'Pagado' AND fecha >= ? AND fecha <= ?";
            try (PreparedStatement ps = cn.prepareStatement(sqlIngresos)) {
                ps.setString(1, desde + " 00:00:00");
                ps.setString(2, hasta + " 23:59:59");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) ingresos = rs.getDouble("total");
                }
            }

            String sqlGastos = "SELECT COALESCE(SUM(total),0) total FROM compras WHERE estado != 'Anulado' AND fecha >= ? AND fecha <= ?";
            try (PreparedStatement ps = cn.prepareStatement(sqlGastos)) {
                ps.setString(1, desde + " 00:00:00");
                ps.setString(2, hasta + " 23:59:59");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) gastosCompras = rs.getDouble("total");
                }
            }

            String sqlGastosOp = "SELECT COALESCE(SUM(monto),0) total FROM gastos_operativos WHERE fecha >= ? AND fecha <= ?";
            try (PreparedStatement ps = cn.prepareStatement(sqlGastosOp)) {
                ps.setString(1, desde);
                ps.setString(2, hasta);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) gastosOp = rs.getDouble("total");
                }
            }

            double gastos = gastosCompras + gastosOp;
            double margen = ingresos - gastos;

            lblIngresos.setText("S/ " + String.format("%.2f", ingresos));
            lblGastos.setText("S/ " + String.format("%.2f", gastos));
            lblMargenBruto.setText("S/ " + String.format("%.2f", margen));

            String sqlVentas = "SELECT fecha, total FROM pedidos WHERE estado_pago = 'Pagado' AND fecha >= ? AND fecha <= ? ORDER BY fecha DESC";
            try (PreparedStatement ps = cn.prepareStatement(sqlVentas)) {
                ps.setString(1, desde + " 00:00:00");
                ps.setString(2, hasta + " 23:59:59");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        modelo.addRow(new Object[]{rs.getString("fecha"), "Venta", "S/ " + String.format("%.2f", rs.getDouble("total")), "-", "Venta"});
                    }
                }
            }

            String sqlCompras = "SELECT fecha, total FROM compras WHERE estado != 'Anulado' AND fecha >= ? AND fecha <= ? ORDER BY fecha DESC";
            try (PreparedStatement ps = cn.prepareStatement(sqlCompras)) {
                ps.setString(1, desde + " 00:00:00");
                ps.setString(2, hasta + " 23:59:59");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        modelo.addRow(new Object[]{rs.getString("fecha"), "Compra", "-", "S/ " + String.format("%.2f", rs.getDouble("total")), "Compra"});
                    }
                }
            }

            String sqlGastosOpT = "SELECT id, descripcion, monto, categoria, fecha FROM gastos_operativos WHERE fecha >= ? AND fecha <= ? ORDER BY fecha DESC";
            try (PreparedStatement ps = cn.prepareStatement(sqlGastosOpT)) {
                ps.setString(1, desde);
                ps.setString(2, hasta);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        modelo.addRow(new Object[]{
                            rs.getString("fecha"),
                            rs.getString("descripcion") + (rs.getString("categoria") != null ? " (" + rs.getString("categoria") + ")" : ""),
                            "-",
                            "S/ " + String.format("%.2f", rs.getDouble("monto")),
                            "GastoOp:" + rs.getInt("id")
                        });
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ── DESCRIPCIÓN: Abre diálogo para registrar un gasto operativo con descripción, monto, categoría y fecha. ──
    private void nuevoGasto() {
        JTextField txtDescripcion = new JTextField();
        JTextField txtMonto = new JTextField();
        JTextField txtCategoria = new JTextField();
        JSpinner spFecha = new JSpinner(new SpinnerDateModel());
        spFecha.setEditor(new JSpinner.DateEditor(spFecha, "dd/MM/yyyy"));
        spFecha.setValue(new java.util.Date());

        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.add(new JLabel("Descripción:"));
        panel.add(txtDescripcion);
        panel.add(new JLabel("Monto:"));
        panel.add(txtMonto);
        panel.add(new JLabel("Categoría:"));
        panel.add(txtCategoria);
        panel.add(new JLabel("Fecha:"));
        panel.add(spFecha);

        if (JOptionPane.showConfirmDialog(this, panel, "Nuevo Gasto Operativo",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;

        String descripcion = txtDescripcion.getText().trim();
        String categoria = txtCategoria.getText().trim();
        double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Monto inválido.");
            return;
        }
        if (descripcion.isEmpty() || monto <= 0) {
            JOptionPane.showMessageDialog(this, "Descripción y monto son obligatorios.");
            return;
        }
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format((java.util.Date) spFecha.getValue());

        try (Connection cn = ConexionBD.conectar();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO gastos_operativos(descripcion, monto, categoria, fecha, usuario_crea) VALUES(?,?,?,?,?)")) {
            ps.setString(1, descripcion);
            ps.setDouble(2, monto);
            ps.setString(3, categoria.isEmpty() ? null : categoria);
            ps.setString(4, fecha);
            ps.setString(5, nombreCompleto());
            ps.executeUpdate();
            cargarDatos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }

    // ── DESCRIPCIÓN: Elimina un gasto operativo seleccionado (solo permite eliminar gastos, no ventas ni compras). ──
    private void eliminarGasto(JTable tabla) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un gasto de la tabla.");
            return;
        }
        String tipo = modelo.getValueAt(fila, 4).toString();
        if (!tipo.startsWith("GastoOp:")) {
            JOptionPane.showMessageDialog(this, "Solo puede eliminar gastos operativos.");
            return;
        }
        int id = Integer.parseInt(tipo.split(":")[1]);
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar este gasto?") != JOptionPane.YES_OPTION) return;

        try (Connection cn = ConexionBD.conectar();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM gastos_operativos WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            cargarDatos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }

    private static ModuloFinanciero instancia;

    // ── DESCRIPCIÓN: Patrón singleton. ──
    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new ModuloFinanciero();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
