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
    private JLabel lblFechaActualizacion;

    public Reportes() {
        setTitle("CAFÉ COMETA - REPORTES DE COMPRAS");
        setSize(1200, 750);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        cargarCompras();
    }

    private void initComponents() {
        add(crearHeader(), BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(FONDO);
        centro.add(crearTabCompras(), BorderLayout.CENTER);

        add(centro, BorderLayout.CENTER);
        add(crearFooter(), BorderLayout.SOUTH);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(100, 70));

        JLabel titulo = new JLabel("REPORTES DE COMPRAS");
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

    private JPanel crearTabCompras() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        filtros.setBackground(FONDO);

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

        spComprasDesde = spDesde;
        spComprasHasta = spHasta;

        filtros.add(new JLabel("Compras desde:"));
        filtros.add(spDesde);
        filtros.add(new JLabel("hasta:"));
        filtros.add(spHasta);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFiltrar.setBackground(DORADO);
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.addActionListener(e -> cargarCompras());
        filtros.add(btnFiltrar);

        panel.add(filtros, BorderLayout.NORTH);

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

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(FONDO);
        footer.setBorder(new EmptyBorder(5, 15, 5, 15));

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnActualizar.setBackground(DORADO);
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> cargarCompras());
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

    private void exportarPDF() {
        String ruta = System.getProperty("user.home") + java.io.File.separator
                + "Desktop" + java.io.File.separator
                + "compras_cafecometa_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".pdf";

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
            doc.add(new com.itextpdf.text.Paragraph("Reporte de Compras\n", st));
            doc.add(new com.itextpdf.text.Paragraph(
                    "Generado: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()), n));
            doc.add(new com.itextpdf.text.Paragraph("Período: " + formatFecha(spComprasDesde) + " - " + formatFecha(spComprasHasta), n));
            doc.add(new com.itextpdf.text.Paragraph(" "));

            doc.add(tablaPDF(new String[]{"ID", "Proveedor", "Fecha", "Total"},
                    new float[]{10f, 40f, 25f, 25f}, modeloCompras, n, b));

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

    private void exportarCSV() {
        String ruta = System.getProperty("user.home") + java.io.File.separator
                + "Desktop" + java.io.File.separator
                + "compras_cafecometa_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".xls";

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
            pw.println("<h3>Reporte de Compras — Generado: " + fechaGen + "</h3>");
            pw.println("<h3>Período: " + formatFecha(spComprasDesde) + " - " + formatFecha(spComprasHasta) + "</h3>");
            pw.println("<hr>");
            pw.println(tablaHTML(modeloCompras));
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
