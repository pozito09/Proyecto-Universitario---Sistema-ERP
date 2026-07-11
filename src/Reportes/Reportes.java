package Reportes;

import static Clases.Colores.*;
import Clases.ConexionBD;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Reportes extends JFrame {

    private JSpinner spComprasDesde, spComprasHasta;
    private JTable tablaCompras;
    private DefaultTableModel modeloCompras;

    private JSpinner spVentasDesde, spVentasHasta;
    private JTable tablaVentas;
    private DefaultTableModel modeloVentas;

    private JTabbedPane tabs;
    private JLabel lblFechaActualizacion;

    public Reportes() {
        setTitle("CAFÉ COMETA - REPORTES");
        setSize(1200, 750);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        cargarCompras();
        cargarVentas();
    }

    private void initComponents() {
        add(crearHeader(), BorderLayout.NORTH);
        add(crearCentro(), BorderLayout.CENTER);
        add(crearFooter(), BorderLayout.SOUTH);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(100, 70));

        JLabel titulo = new JLabel("REPORTES");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setBorder(new EmptyBorder(10, 20, 10, 20));
        header.add(titulo, BorderLayout.WEST);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 16));
        botones.setOpaque(false);

        JButton btnExportarPDF = crearBotonExport("Exportar PDF", DORADO);
        btnExportarPDF.addActionListener(e -> exportarPDF());

        JButton btnExportarExcel = crearBotonExport("Exportar Excel", new Color(39, 174, 96));
        btnExportarExcel.addActionListener(e -> exportarExcel());

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

    private JPanel crearCentro() {
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.addTab("Compras", crearTabCompras());
        tabs.addTab("Ventas", crearTabVentas());

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(FONDO);
        centro.add(tabs, BorderLayout.CENTER);
        return centro;
    }

    private JPanel crearFiltros(JSpinner spDesde, JSpinner spHasta, JButton btnFiltrar, String label) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setBackground(FONDO);
        panel.add(new JLabel(label + " desde:"));
        panel.add(spDesde);
        panel.add(new JLabel("hasta:"));
        panel.add(spHasta);
        panel.add(btnFiltrar);
        return panel;
    }

    private JSpinner crearSpinnerFecha() {
        JSpinner sp = new JSpinner(new SpinnerDateModel());
        sp.setEditor(new JSpinner.DateEditor(sp, "dd/MM/yyyy"));
        sp.setPreferredSize(new Dimension(130, 28));
        return sp;
    }

    private JButton crearBotonFiltrar(Runnable action) {
        JButton btn = new JButton("Filtrar");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(DORADO);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private DefaultTableModel crearModelo(Object[] columnas) {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        modelo.setColumnIdentifiers(columnas);
        return modelo;
    }

    private JTable crearTabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(CABECERA_TABLA);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setRowHeight(24);
        tabla.setSelectionBackground(SELECCION_TABLA);
        return tabla;
    }

    private JPanel crearTabCompras() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);

        JSpinner spDesde = crearSpinnerFecha();
        JSpinner spHasta = crearSpinnerFecha();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        spDesde.setValue(cal.getTime());
        spHasta.setValue(new java.util.Date());
        spComprasDesde = spDesde;
        spComprasHasta = spHasta;

        JPanel filtros = crearFiltros(spDesde, spHasta, crearBotonFiltrar(this::cargarCompras), "Compras");
        panel.add(filtros, BorderLayout.NORTH);

        modeloCompras = crearModelo(new Object[]{"ID", "Proveedor", "Fecha", "Total"});
        tablaCompras = crearTabla(modeloCompras);
        panel.add(new JScrollPane(tablaCompras), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearTabVentas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);

        JSpinner spDesde = crearSpinnerFecha();
        JSpinner spHasta = crearSpinnerFecha();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        spDesde.setValue(cal.getTime());
        spHasta.setValue(new java.util.Date());
        spVentasDesde = spDesde;
        spVentasHasta = spHasta;

        JPanel filtros = crearFiltros(spDesde, spHasta, crearBotonFiltrar(this::cargarVentas), "Ventas");
        panel.add(filtros, BorderLayout.NORTH);

        modeloVentas = crearModelo(new Object[]{"ID", "Cliente", "Fecha", "Total", "Estado"});
        tablaVentas = crearTabla(modeloVentas);
        panel.add(new JScrollPane(tablaVentas), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(FONDO);
        footer.setBorder(new EmptyBorder(5, 15, 5, 15));

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnActualizar.setBackground(DORADO);
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> {
            cargarCompras();
            cargarVentas();
        });
        footer.add(btnActualizar, BorderLayout.WEST);

        lblFechaActualizacion = new JLabel("Última actualización: --");
        lblFechaActualizacion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFechaActualizacion.setForeground(TEXTO);
        footer.add(lblFechaActualizacion, BorderLayout.EAST);

        return footer;
    }

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
            lblFechaActualizacion.setText("Última actualización: " + new java.util.Date());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar compras: " + e.getMessage());
        }
    }

    private void cargarVentas() {
        String sql = "SELECT id, nombre_cliente, fecha, total, estado_pago "
                + "FROM pedidos WHERE estado_pago IN ('Pagado', 'Anulado') ORDER BY id DESC";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            modeloVentas.setRowCount(0);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modeloVentas.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre_cliente"),
                    rs.getTimestamp("fecha"),
                    "S/ " + String.format("%.2f", rs.getDouble("total")),
                    rs.getString("estado_pago")
                });
            }
            lblFechaActualizacion.setText("Última actualización: " + new java.util.Date());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar ventas: " + e.getMessage());
        }
    }

    private boolean esTabVentas() {
        return tabs.getSelectedIndex() == 1;
    }

    private DefaultTableModel obtenerModeloActual() {
        return esTabVentas() ? modeloVentas : modeloCompras;
    }

    private String obtenerPrefijoArchivo() {
        return esTabVentas() ? "ventas" : "compras";
    }

    private String obtenerTituloReporte() {
        return esTabVentas() ? "Reporte de Ventas" : "Reporte de Compras";
    }

    private String obtenerPeriodo() {
        if (esTabVentas()) {
            return formatFecha(spVentasDesde) + " - " + formatFecha(spVentasHasta);
        }
        return formatFecha(spComprasDesde) + " - " + formatFecha(spComprasHasta);
    }

    private void exportarPDF() {
        String ruta = System.getProperty("user.home") + java.io.File.separator
                + "Desktop" + java.io.File.separator
                + obtenerPrefijoArchivo() + "_cafecometa_"
                + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".pdf";

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

            doc.add(new com.itextpdf.text.Paragraph("CAFECOMETA", t));
            doc.add(new com.itextpdf.text.Paragraph(obtenerTituloReporte() + "\n", st));
            doc.add(new com.itextpdf.text.Paragraph(
                    "Generado: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()), n));
            doc.add(new com.itextpdf.text.Paragraph("Período: " + obtenerPeriodo(), n));
            doc.add(new com.itextpdf.text.Paragraph(" "));

            DefaultTableModel modelo = obtenerModeloActual();
            String[] columnas = new String[modelo.getColumnCount()];
            for (int i = 0; i < columnas.length; i++) columnas[i] = modelo.getColumnName(i);
            float[] anchos = new float[columnas.length];
            for (int i = 0; i < anchos.length; i++) anchos[i] = 100f / anchos.length;

            doc.add(tablaPDF(columnas, anchos, modelo, n, b));
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
        try { tbl.setWidths(anchos); } catch (Exception e) { }
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

    private void exportarExcel() {
        String ruta = System.getProperty("user.home") + java.io.File.separator
                + "Desktop" + java.io.File.separator
                + obtenerPrefijoArchivo() + "_cafecometa_"
                + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".xls";

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
            pw.println("</style></head><body>");

            pw.println("<h1>CAFECOMETA</h1>");
            pw.println("<h3>" + obtenerTituloReporte() + " — Generado: " + fechaGen + "</h3>");
            pw.println("<h3>Período: " + obtenerPeriodo() + "</h3>");
            pw.println("<hr>");
            pw.println(tablaHTML(obtenerModeloActual()));
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

    private static Reportes instancia;

    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new Reportes();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
