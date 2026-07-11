package Ventas;

import static Clases.Colores.*;

import Clases.Botones;
import Clases.ConexionBD;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import Clases.Auditoria;

public class Ventas extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<String> cmbFiltro;
    private JComboBox<String> cmbMes;
    private JSpinner spAnio;
    private JLabel lblValorDia, lblValorPeriodo, lblTituloPeriodo;

    public Ventas() {

        setTitle("CAFÉ COMETA - VENTAS");
        setSize(1000, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(0, 55));

        JLabel titulo = new JLabel("GESTIÓN DE VENTAS");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        header.add(titulo, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // CENTRO
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(FONDO);

        // TARJETAS KPI
        JPanel tarjetas = new JPanel(new GridLayout(1, 2, 15, 15));
        tarjetas.setBorder(new EmptyBorder(15, 15, 5, 15));
        tarjetas.setBackground(FONDO);

        lblValorDia = new JLabel("S/ 0.00");
        tarjetas.add(crearTarjeta("VENTAS DEL DÍA", lblValorDia, DORADO));

        lblTituloPeriodo = new JLabel("TOTAL GENERAL");
        lblValorPeriodo = new JLabel("S/ 0.00");
        tarjetas.add(crearTarjeta(lblTituloPeriodo, lblValorPeriodo, DORADO_CLARO));

        centro.add(tarjetas, BorderLayout.NORTH);

        // FILTROS + TABLA
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(FONDO);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        filtros.setBackground(FONDO);

        filtros.add(new JLabel("Período:"));
        cmbFiltro = new JComboBox<>(new String[]{"Todas", "Hoy", "Este Mes", "Mes..."});
        cmbFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filtros.add(cmbFiltro);

        filtros.add(new JLabel("  Mes:"));
        cmbMes = new JComboBox<>(new String[]{
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"
        });
        cmbMes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cmbMes.setSelectedIndex(cal.get(java.util.Calendar.MONTH));
        filtros.add(cmbMes);

        filtros.add(new JLabel("Año:"));
        spAnio = new JSpinner(new SpinnerNumberModel(cal.get(java.util.Calendar.YEAR), 2020, 2100, 1));
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) spAnio.getEditor();
        editor.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 13));
        editor.getTextField().setColumns(5);
        filtros.add(spAnio);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnFiltrar.setBackground(DORADO);
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setBorderPainted(false);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.setPreferredSize(new Dimension(90, 28));
        filtros.add(btnFiltrar);

        panelCentral.add(filtros, BorderLayout.NORTH);

        // TABLA
        String columnas[] = {
            "ID",
            "Cliente",
            "Tipo",
            "Fecha",
            "Total",
            "Estado"
        };

        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(CABECERA_TABLA);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(SELECCION_TABLA);
        tabla.setGridColor(GRILLA_TABLA);

        panelCentral.add(new JScrollPane(tabla), BorderLayout.CENTER);

        centro.add(panelCentral, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        // BOTONES
        JPanel botones = new JPanel();
        botones.setBackground(FONDO);

        JButton btnAnular = Botones.crear("Anular", ROJO, ROJO_HOVER);
        JButton btnActualizar = Botones.crear("Actualizar", DORADO, DORADO_CLARO);

        botones.add(btnAnular);
        botones.add(btnActualizar);

        add(botones, BorderLayout.SOUTH);

        btnAnular.addActionListener(e -> anularVenta());
        btnActualizar.addActionListener(e -> cargarVentas());
        btnFiltrar.addActionListener(e -> cargarVentas());
        cmbFiltro.addActionListener(e -> {
            String sel = cmbFiltro.getSelectedItem().toString();
            if (!sel.equals("Todas")) {
                cargarVentas();
            }
        });

        cargarVentas();
    }

    private JPanel crearTarjeta(String titulo, JLabel valor, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        valor.setForeground(Color.WHITE);
        valor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valor.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(valor);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel crearTarjeta(JLabel titulo, JLabel valor, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        valor.setForeground(Color.WHITE);
        valor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valor.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(valor);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void anularVenta() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta de la tabla");
            return;
        }
        int id = (int) modelo.getValueAt(fila, 0);
        double totalVenta = (double) modelo.getValueAt(fila, 4);
                    String estadoActual = modelo.getValueAt(fila, 5).toString();

        if (estadoActual.equals("Anulado")) {
            JOptionPane.showMessageDialog(this, "Esta venta ya está anulada.");
            return;
        }

        String msg = "¿Anular venta ID " + id + "?\nSe revertirá el stock de productos e insumos.";

        int conf = JOptionPane.showConfirmDialog(this,
                msg, "Confirmar Anulación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (conf == JOptionPane.YES_OPTION) {
            try (Connection con = ConexionBD.conectar()) {
                con.setAutoCommit(false);

                // Revertir stock de productos
                    try (PreparedStatement psRevertir = con.prepareStatement(
                            "UPDATE productos SET stock = stock + ? WHERE nombre = ?")) {
                        try (PreparedStatement psDetalle = con.prepareStatement(
                                "SELECT nombre_producto, cantidad FROM detalle_pedido WHERE id_pedido = ?")) {
                            psDetalle.setInt(1, id);
                            try (ResultSet rs = psDetalle.executeQuery()) {
                                while (rs.next()) {
                                    psRevertir.setInt(1, rs.getInt("cantidad"));
                                    psRevertir.setString(2, rs.getString("nombre_producto"));
                                    psRevertir.addBatch();
                                }
                            }
                        }
                        psRevertir.executeBatch();
                    }

                    // Revertir insumos vía recetas
                    try {
                        String sqlInsumo = "SELECT r.id_insumo, r.cantidad FROM recetas r JOIN productos p ON r.id_producto = p.id WHERE p.nombre = ?";
                        String sqlRevertirInsumo = "UPDATE insumos SET stock = stock + ? WHERE id = ?";
                        try (PreparedStatement psReceta = con.prepareStatement(sqlInsumo);
                             PreparedStatement psRevertirInsumo = con.prepareStatement(sqlRevertirInsumo)) {
                            try (PreparedStatement psDetalle = con.prepareStatement(
                                    "SELECT nombre_producto, cantidad FROM detalle_pedido WHERE id_pedido = ?")) {
                                psDetalle.setInt(1, id);
                                try (ResultSet rsDet = psDetalle.executeQuery()) {
                                    while (rsDet.next()) {
                                        psReceta.setString(1, rsDet.getString("nombre_producto"));
                                        try (ResultSet rsReceta = psReceta.executeQuery()) {
                                            while (rsReceta.next()) {
                                                psRevertirInsumo.setDouble(1, rsReceta.getDouble("cantidad") * rsDet.getInt("cantidad"));
                                                psRevertirInsumo.setInt(2, rsReceta.getInt("id_insumo"));
                                                psRevertirInsumo.addBatch();
                                            }
                                        }
                                    }
                                }
                            }
                            psRevertirInsumo.executeBatch();
                        }
                    } catch (Exception e) {
                        // No hay recetas definidas
                    }

                try (PreparedStatement ps = con.prepareStatement("UPDATE pedidos SET estado_pago='Anulado', estado_cocina=NULL WHERE id=?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }

                con.commit();
                Auditoria.anular("pedidos", id, "Venta #" + id + " anulada, total: S/ " + String.format("%.2f", totalVenta));
                cargarVentas();
                JOptionPane.showMessageDialog(this, "Venta ID " + id + " anulada correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al anular venta: " + ex.getMessage());
            }
        }
    }

    private void cargarVentas() {

        modelo.setRowCount(0);
        String filtro = cmbFiltro.getSelectedItem().toString();

        String sql;

        switch (filtro) {
            case "Hoy":
                sql = "SELECT id, nombre_cliente, tipo_pedido, fecha, total, estado_pago FROM pedidos WHERE estado_pago IN ('Pagado','Anulado') AND DATE(fecha) = CURDATE() ORDER BY id DESC";
                break;
            case "Este Mes":
                sql = "SELECT id, nombre_cliente, tipo_pedido, fecha, total, estado_pago FROM pedidos WHERE estado_pago IN ('Pagado','Anulado') AND MONTH(fecha) = MONTH(CURDATE()) AND YEAR(fecha) = YEAR(CURDATE()) ORDER BY id DESC";
                break;
            case "Mes...": {
                int mes = cmbMes.getSelectedIndex() + 1;
                int anio = (int) spAnio.getValue();
                sql = "SELECT id, nombre_cliente, tipo_pedido, fecha, total, estado_pago FROM pedidos WHERE estado_pago IN ('Pagado','Anulado') AND MONTH(fecha) = ? AND YEAR(fecha) = ? ORDER BY id DESC";
                break;
            }
            default:
                sql = "SELECT id, nombre_cliente, tipo_pedido, fecha, total, estado_pago FROM pedidos WHERE estado_pago IN ('Pagado','Anulado') ORDER BY id DESC";
                break;
        }

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if ("Mes...".equals(filtro)) {
                ps.setInt(1, cmbMes.getSelectedIndex() + 1);
                ps.setInt(2, (int) spAnio.getValue());
            }

            double totalFiltrado = 0;
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                double total = rs.getDouble("total");
                String estadoPago = rs.getString("estado_pago");
                if ("Pagado".equals(estadoPago)) {
                    totalFiltrado += total;
                }
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre_cliente"),
                    rs.getString("tipo_pedido"),
                    rs.getTimestamp("fecha"),
                    total,
                    rs.getString("estado_pago")
                });
            }

            // Tarjeta 1: siempre ventas del día (solo pagados)
            try (Statement st = con.createStatement()) {
                ResultSet rsDia = st.executeQuery(
                    "SELECT COALESCE(SUM(total),0) FROM pedidos WHERE DATE(fecha) = CURDATE() AND estado_pago = 'Pagado'");
                lblValorDia.setText("S/ " + String.format("%,.2f", rsDia.next() ? rsDia.getDouble(1) : 0));
            }

            // Tarjeta 2: según el filtro
            switch (filtro) {
                case "Hoy":
                    lblTituloPeriodo.setText("VENTAS DEL DÍA");
                    break;
                case "Este Mes":
                    lblTituloPeriodo.setText("VENTAS DEL MES");
                    break;
                case "Mes...":
                    lblTituloPeriodo.setText("VENTAS DE " + cmbMes.getSelectedItem().toString().toUpperCase() + " " + spAnio.getValue());
                    break;
                default:
                    lblTituloPeriodo.setText("TOTAL GENERAL");
                    break;
            }
            lblValorPeriodo.setText("S/ " + String.format("%,.2f", totalFiltrado));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private static Ventas instancia;

    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new Ventas();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
