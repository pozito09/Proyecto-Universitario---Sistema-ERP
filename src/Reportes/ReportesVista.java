package Reportes;

import static Clases.Colores.*;
import Clases.ConexionBD;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ReportesVista extends JFrame {

    private JLabel lblVentas, lblIngresos, lblProductos, lblProveedores;
    private JLabel lblFechaActualizacion;

    // Ventas
    private JSpinner spVentasDesde, spVentasHasta;
    private JTable tablaVentas;
    private DefaultTableModel modeloVentas;

    // Compras
    private JSpinner spComprasDesde, spComprasHasta;
    private JTable tablaCompras;
    private DefaultTableModel modeloCompras;

    // Productos
    private JTextField txtBuscarProducto;
    private JTable tablaProductos;
    private DefaultTableModel modeloProductos;

    // Stock crítico
    private JTable tablaStock;
    private DefaultTableModel modeloStock;

    public ReportesVista() {
        setTitle("CAFECOMETA ERP - REPORTES");
        setSize(1200, 750);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        add(crearHeader(), BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(FONDO);

        centro.add(crearKPIs(), BorderLayout.NORTH);
        centro.add(crearTabs(), BorderLayout.CENTER);

        add(centro, BorderLayout.CENTER);
        add(crearFooter(), BorderLayout.SOUTH);
    }

    // ========================================
    // HEADER
    // ========================================
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(100, 70));

        JLabel titulo = new JLabel("REPORTES Y ANALÍTICAS");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setBorder(new EmptyBorder(10, 20, 10, 20));
        header.add(titulo, BorderLayout.WEST);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 16));
        botones.setOpaque(false);

        JButton btnExportarPDF = crearBotonExport("Exportar PDF", DORADO);
        btnExportarPDF.addActionListener(e -> exportarPDF());

        JButton btnExportarExcel = crearBotonExport("Exportar Excel", new Color(39, 174, 96));
        btnExportarExcel.addActionListener(e -> exportarCSV());

        botones.add(btnExportarPDF);
        botones.add(btnExportarExcel);
        header.add(botones, BorderLayout.EAST);

        return header;
    }

    private JButton crearBotonExport(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.brighter(), 2),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ========================================
    // KPIs
    // ========================================
    private JPanel crearKPIs() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 5, 15));
        panel.setBackground(FONDO);

        lblVentas = crearTarjeta(panel, "VENTAS", DORADO);
        lblIngresos = crearTarjeta(panel, "INGRESOS", DORADO_HOVER);
        lblProductos = crearTarjeta(panel, "PRODUCTOS", PRECIO);
        lblProveedores = crearTarjeta(panel, "PROVEEDORES", TEXTO);

        return panel;
    }

    private JLabel crearTarjeta(JPanel contenedor, String titulo, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValor = new JLabel("0");
        lblValor.setForeground(Color.WHITE);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblValor);
        panel.add(Box.createVerticalGlue());

        contenedor.add(panel);
        return lblValor;
    }

    // ========================================
    // TABS
    // ========================================
    private JTabbedPane crearTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(FONDO);

        tabs.addTab("Ventas por Período", crearTabVentas());
        tabs.addTab("Compras por Período", crearTabCompras());
        tabs.addTab("Reporte de Productos", crearTabProductos());
        tabs.addTab("Stock Crítico", crearTabStock());

        return tabs;
    }

    // ========================================
    // TAB 1: VENTAS
    // ========================================
    private JPanel crearTabVentas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);

        panel.add(crearPanelFechas("Ventas",
                f -> { spVentasDesde = f[0]; spVentasHasta = f[1]; },
                e -> cargarVentas()), BorderLayout.NORTH);

        modeloVentas = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        modeloVentas.setColumnIdentifiers(new Object[]{"ID", "Cliente", "Fecha", "Total", "Método Pago"});
        tablaVentas = new JTable(modeloVentas);
        tablaVentas.getTableHeader().setBackground(CABECERA_TABLA);
        tablaVentas.getTableHeader().setForeground(Color.WHITE);
        tablaVentas.setRowHeight(24);
        tablaVentas.setSelectionBackground(SELECCION_TABLA);

        panel.add(new JScrollPane(tablaVentas), BorderLayout.CENTER);
        return panel;
    }

    // ========================================
    // TAB 2: COMPRAS
    // ========================================
    private JPanel crearTabCompras() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);

        panel.add(crearPanelFechas("Compras",
                f -> { spComprasDesde = f[0]; spComprasHasta = f[1]; },
                e -> cargarCompras()), BorderLayout.NORTH);

        modeloCompras = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        modeloCompras.setColumnIdentifiers(new Object[]{"ID", "Proveedor", "Fecha", "Total"});
        tablaCompras = new JTable(modeloCompras);
        tablaCompras.getTableHeader().setBackground(CABECERA_TABLA);
        tablaCompras.getTableHeader().setForeground(Color.WHITE);
        tablaCompras.setRowHeight(24);
        tablaCompras.setSelectionBackground(SELECCION_TABLA);

        panel.add(new JScrollPane(tablaCompras), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelFechas(String titulo,
            java.util.function.Consumer<JSpinner[]> asignarSpinners,
            java.awt.event.ActionListener listener) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setBackground(FONDO);

        JSpinner spDesde = new JSpinner(new SpinnerDateModel());
        spDesde.setEditor(new JSpinner.DateEditor(spDesde, "dd/MM/yyyy"));
        spDesde.setPreferredSize(new Dimension(130, 28));

        JSpinner spHasta = new JSpinner(new SpinnerDateModel());
        spHasta.setEditor(new JSpinner.DateEditor(spHasta, "dd/MM/yyyy"));
        spHasta.setPreferredSize(new Dimension(130, 28));

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        spDesde.setValue(cal.getTime());
        spHasta.setValue(new java.util.Date());

        asignarSpinners.accept(new JSpinner[]{spDesde, spHasta});

        panel.add(new JLabel(titulo + " desde:"));
        panel.add(spDesde);
        panel.add(new JLabel("hasta:"));
        panel.add(spHasta);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFiltrar.setBackground(DORADO);
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.addActionListener(listener);
        panel.add(btnFiltrar);

        return panel;
    }

    // ========================================
    // TAB 3: PRODUCTOS
    // ========================================
    private JPanel crearTabProductos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.setBackground(FONDO);
        top.add(new JLabel("Buscar producto:"));
        txtBuscarProducto = new JTextField(20);
        txtBuscarProducto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        top.add(txtBuscarProducto);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.setBackground(DORADO);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.addActionListener(e -> cargarProductos());
        top.add(btnBuscar);

        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        btnMostrarTodos.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnMostrarTodos.setBackground(TEXTO);
        btnMostrarTodos.setForeground(Color.WHITE);
        btnMostrarTodos.setFocusPainted(false);
        btnMostrarTodos.addActionListener(e -> { txtBuscarProducto.setText(""); cargarProductos(); });
        top.add(btnMostrarTodos);

        panel.add(top, BorderLayout.NORTH);

        modeloProductos = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        modeloProductos.setColumnIdentifiers(new Object[]{"ID", "Nombre", "Categoría", "Precio", "Stock"});
        tablaProductos = new JTable(modeloProductos);
        tablaProductos.getTableHeader().setBackground(CABECERA_TABLA);
        tablaProductos.getTableHeader().setForeground(Color.WHITE);
        tablaProductos.setRowHeight(24);
        tablaProductos.setSelectionBackground(SELECCION_TABLA);

        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        return panel;
    }

    // ========================================
    // TAB 4: STOCK CRÍTICO
    // ========================================
    private JPanel crearTabStock() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);

        modeloStock = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        modeloStock.setColumnIdentifiers(new Object[]{"Insumo", "Stock Actual", "Stock Mínimo", "Faltante"});
        tablaStock = new JTable(modeloStock);
        tablaStock.getTableHeader().setBackground(CABECERA_TABLA);
        tablaStock.getTableHeader().setForeground(Color.WHITE);
        tablaStock.setRowHeight(24);
        tablaStock.setSelectionBackground(SELECCION_TABLA);

        panel.add(new JScrollPane(tablaStock), BorderLayout.CENTER);
        return panel;
    }

    // ========================================
    // FOOTER
    // ========================================
    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(FONDO);
        footer.setBorder(new EmptyBorder(5, 15, 5, 15));

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnActualizar.setBackground(DORADO);
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> cargarDatos());
        footer.add(btnActualizar, BorderLayout.WEST);

        lblFechaActualizacion = new JLabel("Última actualización: --");
        lblFechaActualizacion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFechaActualizacion.setForeground(TEXTO);
        footer.add(lblFechaActualizacion, BorderLayout.EAST);

        return footer;
    }

    // ========================================
    // CARGAR TODO
    // ========================================
    private void cargarDatos() {
        cargarKPIs();
        cargarVentas();
        cargarCompras();
        cargarProductos();
        cargarStockCritico();
        lblFechaActualizacion.setText("Última actualización: " + new java.util.Date());
    }

    // ========================================
    // KPIs
    // ========================================
    private void cargarKPIs() {
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery("SELECT COUNT(*) total FROM pedidos");
            lblVentas.setText(rs.next() ? rs.getString("total") : "0");

            rs = st.executeQuery("SELECT COALESCE(SUM(total),0) total FROM pedidos");
            lblIngresos.setText(rs.next() ? "S/ " + String.format("%,.2f", rs.getDouble("total")) : "S/ 0.00");

            rs = st.executeQuery("SELECT COUNT(*) total FROM productos");
            lblProductos.setText(rs.next() ? rs.getString("total") : "0");

            rs = st.executeQuery("SELECT COUNT(*) total FROM proveedores");
            lblProveedores.setText(rs.next() ? rs.getString("total") : "0");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar KPIs: " + e.getMessage());
        }
    }

    // ========================================
    // VENTAS
    // ========================================
    private void cargarVentas() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String desde = sdf.format((java.util.Date) spVentasDesde.getValue());
        String hasta = sdf.format((java.util.Date) spVentasHasta.getValue());

        String sql = "SELECT id, nombre_cliente, fecha, total, metodo_pago FROM pedidos "
                + "WHERE fecha >= ? AND fecha <= ? ORDER BY fecha DESC";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, desde + " 00:00:00");
            ps.setString(2, hasta + " 23:59:59");

            modeloVentas.setRowCount(0);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modeloVentas.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre_cliente"),
                    rs.getTimestamp("fecha"),
                    "S/ " + String.format("%.2f", rs.getDouble("total")),
                    rs.getString("metodo_pago") != null ? rs.getString("metodo_pago") : "—"
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar ventas: " + e.getMessage());
        }
    }

    // ========================================
    // COMPRAS
    // ========================================
    private void cargarCompras() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String desde = sdf.format((java.util.Date) spComprasDesde.getValue());
        String hasta = sdf.format((java.util.Date) spComprasHasta.getValue());

        String sql = "SELECT c.id, p.nombre AS proveedor, c.fecha, c.total "
                + "FROM compras c JOIN proveedores p ON c.id_proveedor = p.id "
                + "WHERE c.fecha >= ? AND c.fecha <= ? ORDER BY c.fecha DESC";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, desde + " 00:00:00");
            ps.setString(2, hasta + " 23:59:59");

            modeloCompras.setRowCount(0);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modeloCompras.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("proveedor"),
                    rs.getTimestamp("fecha"),
                    "S/ " + String.format("%.2f", rs.getDouble("total"))
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar compras: " + e.getMessage());
        }
    }

    // ========================================
    // PRODUCTOS
    // ========================================
    private void cargarProductos() {
        String buscar = txtBuscarProducto.getText().trim();

        String sql = "SELECT id, nombre, categoria, precio, stock FROM productos";
        if (!buscar.isEmpty()) {
            sql += " WHERE nombre LIKE ?";
        }
        sql += " ORDER BY nombre";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (!buscar.isEmpty()) {
                ps.setString(1, "%" + buscar + "%");
            }

            modeloProductos.setRowCount(0);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modeloProductos.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("categoria"),
                    "S/ " + String.format("%.2f", rs.getDouble("precio")),
                    rs.getInt("stock")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
        }
    }

    // ========================================
    // STOCK CRÍTICO
    // ========================================
    private void cargarStockCritico() {
        String sql = "SELECT nombre, stock, stock_minimo FROM insumos "
                + "WHERE stock < stock_minimo ORDER BY stock ASC";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            modeloStock.setRowCount(0);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int stock = rs.getInt("stock");
                int min = rs.getInt("stock_minimo");
                modeloStock.addRow(new Object[]{
                    rs.getString("nombre"),
                    stock,
                    min,
                    min - stock
                });
            }

            if (modeloStock.getRowCount() == 0) {
                modeloStock.addRow(new Object[]{"Todos los insumos tienen stock suficiente", "-", "-", "-"});
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar stock crítico: " + e.getMessage());
        }
    }

    // ========================================
    // EXPORTAR PDF
    // ========================================
    private void exportarPDF() {
        String ruta = System.getProperty("user.home") + java.io.File.separator
                + "Desktop" + java.io.File.separator
                + "reporte_cafecometa_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".pdf";

        try {
            com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new java.io.FileOutputStream(ruta));
            doc.open();

            com.itextpdf.text.Font t = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 20, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font st = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font n = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.NORMAL);
            com.itextpdf.text.Font b = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD);

            doc.add(new com.itextpdf.text.Paragraph("CAFECOMETA ERP", t));
            doc.add(new com.itextpdf.text.Paragraph("Reporte General\n", st));
            doc.add(new com.itextpdf.text.Paragraph(
                    "Generado: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()), n));
            doc.add(new com.itextpdf.text.Paragraph(" "));

            // KPIs
            doc.add(new com.itextpdf.text.Paragraph("RESUMEN", st));
            com.itextpdf.text.pdf.PdfPTable tk = new com.itextpdf.text.pdf.PdfPTable(2);
            tk.setWidthPercentage(60);
            tk.setWidths(new float[]{50f, 50f});
            String[][] kpis = {
                {"Ventas", lblVentas.getText()},
                {"Ingresos", lblIngresos.getText()},
                {"Productos", lblProductos.getText()},
                {"Proveedores", lblProveedores.getText()}
            };
            for (String[] kv : kpis) {
                tk.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(kv[0], b)));
                tk.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(kv[1], n)));
            }
            doc.add(tk);
            doc.add(new com.itextpdf.text.Paragraph(" "));

            // Ventas
            doc.add(new com.itextpdf.text.Paragraph(
                    "VENTAS: " + formatFecha(spVentasDesde) + " - " + formatFecha(spVentasHasta), st));
            doc.add(tablaPDF(new String[]{"ID", "Cliente", "Fecha", "Total", "Método Pago"},
                    new float[]{10f, 30f, 20f, 20f, 20f}, modeloVentas, n, b));
            doc.add(new com.itextpdf.text.Paragraph(" "));

            // Compras
            doc.add(new com.itextpdf.text.Paragraph(
                    "COMPRAS: " + formatFecha(spComprasDesde) + " - " + formatFecha(spComprasHasta), st));
            doc.add(tablaPDF(new String[]{"ID", "Proveedor", "Fecha", "Total"},
                    new float[]{10f, 40f, 25f, 25f}, modeloCompras, n, b));
            doc.add(new com.itextpdf.text.Paragraph(" "));

            // Productos
            int totalProd = modeloProductos.getRowCount();
            boolean hayFiltro = !txtBuscarProducto.getText().trim().isEmpty();
            doc.add(new com.itextpdf.text.Paragraph(
                    "PRODUCTOS" + (hayFiltro ? " (filtrado: " + txtBuscarProducto.getText().trim() + ")" : "")
                    + " — " + totalProd + " registros", st));
            doc.add(tablaPDF(new String[]{"ID", "Nombre", "Categoría", "Precio", "Stock"},
                    new float[]{10f, 35f, 20f, 15f, 20f}, modeloProductos, n, b));
            doc.add(new com.itextpdf.text.Paragraph(" "));

            // Stock crítico
            doc.add(new com.itextpdf.text.Paragraph("STOCK CRÍTICO", st));
            doc.add(tablaPDF(new String[]{"Insumo", "Stock Actual", "Stock Mínimo", "Faltante"},
                    new float[]{40f, 20f, 20f, 20f}, modeloStock, n, b));

            doc.close();
            java.awt.Desktop.getDesktop().open(new java.io.File(ruta));

            JOptionPane.showMessageDialog(this,
                    "PDF exportado:\n" + ruta, "Exportar PDF", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al exportar PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private com.itextpdf.text.pdf.PdfPTable tablaPDF(String[] columnas, float[] anchos,
            DefaultTableModel modelo, com.itextpdf.text.Font normal, com.itextpdf.text.Font bold) {
        com.itextpdf.text.pdf.PdfPTable tbl = new com.itextpdf.text.pdf.PdfPTable(columnas.length);
        tbl.setWidthPercentage(100);
        try { tbl.setWidths(anchos); } catch (Exception e) { /* fallback */ }
        for (String col : columnas) {
            tbl.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(col, bold)));
        }
        for (int i = 0; i < modelo.getRowCount(); i++) {
            for (int j = 0; j < modelo.getColumnCount(); j++) {
                Object val = modelo.getValueAt(i, j);
                tbl.addCell(new com.itextpdf.text.pdf.PdfPCell(
                        new com.itextpdf.text.Phrase(val != null ? val.toString() : "", normal)));
            }
        }
        return tbl;
    }

    private String formatFecha(JSpinner sp) {
        return new SimpleDateFormat("dd/MM/yyyy").format((java.util.Date) sp.getValue());
    }

    // ========================================
    // EXPORTAR CSV (Excel)
    // ========================================
    private void exportarCSV() {
        String ruta = System.getProperty("user.home") + java.io.File.separator
                + "Desktop" + java.io.File.separator
                + "reporte_cafecometa_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".xls";

        try (java.io.PrintWriter pw = new java.io.PrintWriter(ruta, "UTF-8")) {

            String fechaGen = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());

            pw.println("<!DOCTYPE html>");
            pw.println("<html><head><meta charset=\"UTF-8\">");
            pw.println("<style>");
            pw.println("body { font-family: 'Segoe UI', Arial, sans-serif; padding: 20px; background: #f5f5dc; }");
            pw.println("h1 { color: #663300; font-size: 24px; margin-bottom: 2px; }");
            pw.println("h2 { color: #663300; font-size: 16px; margin: 20px 0 6px 0; }");
            pw.println("h3 { font-size: 13px; color: #555; font-weight: normal; margin-top: 0; }");
            pw.println("table { border-collapse: collapse; width: 100%; margin-bottom: 18px; font-size: 12px; }");
            pw.println("th { background-color: #c8a050; color: white; padding: 8px 10px; text-align: left; font-weight: bold; }");
            pw.println("td { padding: 6px 10px; border: 1px solid #dcd4b0; }");
            pw.println("tr:nth-child(even) { background-color: #f0ecd8; }");
            pw.println("tr:nth-child(odd) { background-color: white; }");
            pw.println(".kpi-table td:first-child { font-weight: bold; width: 200px; }");
            pw.println("</style></head><body>");

            pw.println("<h1>CAFECOMETA ERP</h1>");
            pw.println("<h3>Reporte General — Generado: " + fechaGen + "</h3>");
            pw.println("<hr>");

            // KPIs
            pw.println("<h2>RESUMEN</h2>");
            pw.println("<table class=\"kpi-table\">");
            String[][] kpis = {
                {"Ventas", lblVentas.getText()},
                {"Ingresos", lblIngresos.getText()},
                {"Productos", lblProductos.getText()},
                {"Proveedores", lblProveedores.getText()}
            };
            for (String[] kv : kpis) {
                pw.println("<tr><td>" + esc(kv[0]) + "</td><td>" + esc(kv[1]) + "</td></tr>");
            }
            pw.println("</table>");

            // Ventas
            pw.println("<h2>VENTAS: " + formatFecha(spVentasDesde) + " — " + formatFecha(spVentasHasta) + "</h2>");
            pw.println(tablaHTML(modeloVentas));

            // Compras
            pw.println("<h2>COMPRAS: " + formatFecha(spComprasDesde) + " — " + formatFecha(spComprasHasta) + "</h2>");
            pw.println(tablaHTML(modeloCompras));

            // Productos
            String filtro = txtBuscarProducto.getText().trim();
            pw.println("<h2>PRODUCTOS" + (!filtro.isEmpty() ? " (filtro: " + esc(filtro) + ")" : "")
                    + " — " + modeloProductos.getRowCount() + " registros</h2>");
            pw.println(tablaHTML(modeloProductos));

            // Stock
            pw.println("<h2>STOCK CRÍTICO</h2>");
            pw.println(tablaHTML(modeloStock));

            pw.println("</body></html>");

            java.awt.Desktop.getDesktop().open(new java.io.File(ruta));

            JOptionPane.showMessageDialog(this,
                    "Excel exportado:\n" + ruta, "Exportar Excel", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al exportar Excel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String tablaHTML(DefaultTableModel modelo) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");

        sb.append("<tr>");
        for (int c = 0; c < modelo.getColumnCount(); c++) {
            sb.append("<th>").append(esc(modelo.getColumnName(c))).append("</th>");
        }
        sb.append("</tr>");

        for (int r = 0; r < modelo.getRowCount(); r++) {
            sb.append("<tr>");
            for (int c = 0; c < modelo.getColumnCount(); c++) {
                Object val = modelo.getValueAt(r, c);
                sb.append("<td>").append(esc(val != null ? val.toString() : "")).append("</td>");
            }
            sb.append("</tr>");
        }

        sb.append("</table>");
        return sb.toString();
    }

    private String esc(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    // ========================================
    // SINGLETON
    // ========================================
    private static ReportesVista instancia;

    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new ReportesVista();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
