package Empleado;

import Clases.ConexionBD;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class Cajero extends JFrame {

    private static final Color BG_COLOR = new Color(245, 245, 220);
    private static final Color PRIMARY_COLOR = new Color(102, 51, 0);
    private static final Color ACCENT_COLOR = new Color(200, 140, 40);
    private static final Color GRID_COLOR = new Color(200, 180, 150);

    private JTextField txtId;
    private DefaultTableModel modeloTabla;
    private JLabel lblCliente;
    private JLabel lblId;
    private JButton btnPagar;
    private JLabel lblTotal;
    private static final double IGV_RATE = 1.18;
    private int idPedidoActual;
    private String nombreMesero;
    private String nombreCocinero;
    private String clienteActual;
    private double totalActual;

    public Cajero() {
        initUI();
    }

    private void initUI() {
        setTitle("CAFÉ COMETA - PANEL DE CAJERO");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG_COLOR);
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBusqueda.setBackground(BG_COLOR);
        JLabel lblBuscar = new JLabel("ID del Pedido:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtId = new JTextField(15);
        txtId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtId.setHorizontalAlignment(JTextField.CENTER);
        ((AbstractDocument) txtId.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (fb.getDocument().getLength() + string.length() <= 9) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (fb.getDocument().getLength() - length + (text != null ? text.length() : 0) <= 9) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        JButton btnBuscar = new JButton("BUSCAR");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtId);
        panelBusqueda.add(btnBuscar);

        JPanel panelInfo = new JPanel(new GridLayout(1, 2));
        panelInfo.setBackground(BG_COLOR);
        lblId = new JLabel("ID: —", SwingConstants.CENTER);
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblId.setBackground(PRIMARY_COLOR);
        lblId.setForeground(Color.WHITE);
        lblId.setOpaque(true);
        lblId.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        lblCliente = new JLabel("Cliente: —", SwingConstants.CENTER);
        lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCliente.setBackground(PRIMARY_COLOR);
        lblCliente.setForeground(Color.WHITE);
        lblCliente.setOpaque(true);
        lblCliente.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelInfo.add(lblId);
        panelInfo.add(lblCliente);

        modeloTabla = new DefaultTableModel(
                new String[]{"Producto", "Cantidad", "Precio", "Subtotal"}, 0
        ) {
            final Class<?>[] tipos = {String.class, Integer.class, Double.class, Double.class};
            final boolean[] edit = {false, false, false, false};
            @Override public Class<?> getColumnClass(int i) { return tipos[i]; }
            @Override public boolean isCellEditable(int r, int c) { return edit[c]; }
        };
        JTable tablaDetalle = new JTable(modeloTabla);
        tablaDetalle.getTableHeader().setBackground(PRIMARY_COLOR);
        tablaDetalle.getTableHeader().setForeground(Color.WHITE);
        tablaDetalle.setSelectionBackground(ACCENT_COLOR);
        tablaDetalle.setGridColor(GRID_COLOR);
        JScrollPane scroll = new JScrollPane(tablaDetalle);
        scroll.setBorder(BorderFactory.createTitledBorder("Detalle del Pedido"));

        lblTotal = new JLabel("Total: S/ 0.00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotal.setBackground(PRIMARY_COLOR);
        lblTotal.setForeground(Color.WHITE);
        lblTotal.setOpaque(true);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(BG_COLOR);
        panelCentro.add(panelInfo, BorderLayout.NORTH);
        panelCentro.add(scroll, BorderLayout.CENTER);
        panelCentro.add(lblTotal, BorderLayout.SOUTH);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelSur.setBackground(BG_COLOR);
        btnPagar = new JButton("PAGAR");
        btnPagar.setBackground(PRIMARY_COLOR);
        btnPagar.setForeground(Color.WHITE);
        btnPagar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnPagar.setFocusPainted(false);
        btnPagar.setBorderPainted(false);
        btnPagar.setPreferredSize(new Dimension(200, 45));
        btnPagar.setEnabled(false);
        panelSur.add(btnPagar);

        JButton btnCerrarSesion = new JButton("CERRAR SESIÓN");
        btnCerrarSesion.setBackground(PRIMARY_COLOR);
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            Acceso.Login login = new Acceso.Login();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
        });
        JLabel lblCajero = new JLabel("Cajero: " + Clases.GuardarSesion.nombreCompleto());
        lblCajero.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblCajero.setBorder(new EmptyBorder(0, 10, 0, 0));
        JPanel panelSur2 = new JPanel(new BorderLayout());
        panelSur2.setBackground(BG_COLOR);
        panelSur2.add(lblCajero, BorderLayout.WEST);
        panelSur2.add(btnCerrarSesion, BorderLayout.EAST);
        panelSur2.add(panelSur, BorderLayout.CENTER);

        main.add(panelBusqueda, BorderLayout.NORTH);
        main.add(panelCentro, BorderLayout.CENTER);
        main.add(panelSur2, BorderLayout.SOUTH);

        add(main);

        btnBuscar.addActionListener(e -> buscarPedido());
        txtId.addActionListener(e -> buscarPedido());
        btnPagar.addActionListener(e -> procesarPago());
    }

    private void buscarPedido() {
        String texto = txtId.getText().trim();
        if (texto.isEmpty()) return;

        int id;
        try {
            id = Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (id <= 0) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        nombreMesero = null;
        nombreCocinero = null;

        try (Connection con = ConexionBD.conectar()) {
            String sql = "SELECT p.id, p.nombre_cliente, p.total, p.estado_cocina, p.estado_pago,"
                    + " COALESCE(CONCAT_WS(' ', u_crea.nombres, u_crea.apellidos), '') AS nom_mesero,"
                    + " COALESCE(CONCAT_WS(' ', u_prepara.nombres, u_prepara.apellidos), '') AS nom_cocinero"
                    + " FROM pedidos p"
                    + " LEFT JOIN usuarios u_crea ON p.id_usuario_crea = u_crea.id"
                    + " LEFT JOIN usuarios u_prepara ON p.id_usuario_prepara = u_prepara.id"
                    + " WHERE p.id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this, "Pedido no encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        limpiar();
                        return;
                    }

                    idPedidoActual = rs.getInt("id");
                    clienteActual = rs.getString("nombre_cliente");
                    totalActual = rs.getDouble("total");
                    nombreMesero = rs.getString("nom_mesero");
                    nombreCocinero = rs.getString("nom_cocinero");

                    if ("Pagado".equals(rs.getString("estado_pago"))) {
                        JOptionPane.showMessageDialog(this, "Este pedido ya fue pagado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        limpiar();
                        return;
                    }

                    lblId.setText("ID: " + String.format("%09d", idPedidoActual));
                    lblCliente.setText("Cliente: " + clienteActual);

                    modeloTabla.setRowCount(0);
                    String sqld = "SELECT nombre_producto, cantidad, precio, subtotal FROM detalle_pedido WHERE id_pedido = ?";
                    try (PreparedStatement psd = con.prepareStatement(sqld)) {
                        psd.setInt(1, idPedidoActual);
                        try (ResultSet rsd = psd.executeQuery()) {
                            while (rsd.next()) {
                                modeloTabla.addRow(new Object[]{
                                    rsd.getString("nombre_producto"),
                                    rsd.getInt("cantidad"),
                                    rsd.getDouble("precio"),
                                    rsd.getDouble("subtotal")
                                });
                            }
                        }
                    }

                    lblTotal.setText("Total: S/ " + String.format("%.2f", totalActual));
                    btnPagar.setEnabled(true);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarPago() {
        if (!Caja.ControlCaja.hayCajaAbierta()) {
            JOptionPane.showMessageDialog(this,
                    "No hay caja aperturada. Debe abrir la caja antes de realizar un pago.",
                    "Caja no aperturada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] metodos = {"Efectivo", "Tarjeta", "Yape / Plin"};
        String metodoPago = (String) JOptionPane.showInputDialog(this,
                "Total a cobrar:  S/ " + String.format("%.2f", totalActual)
                + "\n\nSelecciona el método de pago:",
                "Método de Pago",
                JOptionPane.PLAIN_MESSAGE, null, metodos, metodos[0]);

        if (metodoPago == null) return;

        double montoRecibido = totalActual;
        double vuelto = 0;

        if (metodoPago.equals("Efectivo")) {
            while (true) {
                String input = JOptionPane.showInputDialog(this,
                        "Total: S/ " + String.format("%.2f", totalActual)
                        + "\n\nIngresa el monto recibido (S/):");
                if (input == null) return;
                try {
                    montoRecibido = Double.parseDouble(input.trim().replace(",", "."));
                    if (montoRecibido < totalActual) {
                        JOptionPane.showMessageDialog(this,
                                "Monto insuficiente. Total: S/ " + String.format("%.2f", totalActual),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        vuelto = montoRecibido - totalActual;
                        break;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Confirmar pago?\n\nCliente: " + clienteActual
                + "\nMétodo: " + metodoPago
                + "\nTotal: S/ " + String.format("%.2f", totalActual)
                + (metodoPago.equals("Efectivo")
                ? "\nRecibido: S/ " + String.format("%.2f", montoRecibido)
                + "\nVuelto: S/ " + String.format("%.2f", vuelto) : ""),
                "Confirmar Pago", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmar != JOptionPane.YES_OPTION) return;

        String clienteGuardado = clienteActual;
        double totalGuardado = totalActual;
        int idGuardado = idPedidoActual;
        java.util.List<Object[]> productosGuardados = new java.util.ArrayList<>();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            Object[] fila = new Object[]{
                modeloTabla.getValueAt(i, 0),
                modeloTabla.getValueAt(i, 1),
                modeloTabla.getValueAt(i, 2),
                modeloTabla.getValueAt(i, 3)
            };
            productosGuardados.add(fila);
        }

        try (Connection con = ConexionBD.conectar()) {
            con.setAutoCommit(false);

            int idAperturaActual = Caja.ControlCaja.getCajaActivaId();
            String sql = "UPDATE pedidos SET estado_pago = 'Pagado', metodo_pago = ?, monto_recibido = ?, vuelto = ?, id_usuario_cobra = ?, nombre_usuario_cobra = ?, id_apertura = ? WHERE id = ? AND estado_pago = 'En cola'";
            int filasActualizadas;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, metodoPago);
                ps.setDouble(2, montoRecibido);
                ps.setDouble(3, vuelto);
                ps.setInt(4, Clases.GuardarSesion.id);
                ps.setString(5, Clases.GuardarSesion.nombre);
                ps.setInt(6, idAperturaActual);
                ps.setInt(7, idPedidoActual);
                filasActualizadas = ps.executeUpdate();
            }

            if (filasActualizadas == 0) {
                con.rollback();
                JOptionPane.showMessageDialog(this, "Este pedido ya fue pagado por otro usuario.", "Aviso", JOptionPane.WARNING_MESSAGE);
                limpiar();
                return;
            }

            con.commit();

            JOptionPane.showMessageDialog(this,
                    "¡Pago exitoso!\n\nCliente: " + clienteActual
                    + "\nMétodo: " + metodoPago
                    + "\nTotal: S/ " + String.format("%.2f", totalActual)
                    + (metodoPago.equals("Efectivo")
                    ? "\nRecibido: S/ " + String.format("%.2f", montoRecibido)
                    + "\nVuelto: S/ " + String.format("%.2f", vuelto) : ""),
                    "Confirmación", JOptionPane.INFORMATION_MESSAGE);

            limpiar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al procesar pago: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        generarComprobantePDF(metodoPago, montoRecibido, vuelto, clienteGuardado, totalGuardado, productosGuardados, idGuardado);
    }

    private void limpiar() {
        modeloTabla.setRowCount(0);
        lblId.setText("ID: —");
        lblCliente.setText("Cliente: —");
        lblTotal.setText("Total: S/ 0.00");
        txtId.setText("");
        btnPagar.setEnabled(false);
        idPedidoActual = 0;
        clienteActual = null;
        totalActual = 0;
    }

    private void agregarSeparadorPDF(com.itextpdf.text.Document doc) throws com.itextpdf.text.DocumentException {
        com.itextpdf.text.pdf.PdfPTable sep = new com.itextpdf.text.pdf.PdfPTable(1);
        sep.setWidthPercentage(100);
        com.itextpdf.text.pdf.PdfPCell sepCelda = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(""));
        sepCelda.setBorder(com.itextpdf.text.Rectangle.BOTTOM);
        sepCelda.setBorderWidth(1);
        sepCelda.setPaddingBottom(5);
        sep.addCell(sepCelda);
        doc.add(sep);
    }

    private void generarComprobantePDF(String metodoPago, double montoRecibido, double vuelto, String cliente, double total, java.util.List<Object[]> productos, int idPedido) {
        String idStr = String.format("%09d", idPedido);
        String nombreArchivo = "comprobante_" + idStr + ".pdf";
        String ruta = System.getProperty("user.home") + java.io.File.separator
                + "Desktop" + java.io.File.separator + nombreArchivo;

        double subtotal = total / IGV_RATE;
        double igv = total - subtotal;

        String empNombre = "CAFECOMETA", empDireccion = "", empRuc = "";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM empresa WHERE id=1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                empNombre = rs.getString("nombre");
                if (empNombre.isEmpty()) empNombre = "CAFECOMETA";
                empDireccion = rs.getString("direccion");
                empRuc = rs.getString("ruc");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos de empresa: " + e.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }

        try {
            com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new java.io.FileOutputStream(ruta));
            doc.open();

            com.itextpdf.text.Font tituloFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 20, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font subtituloFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL);
            com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font smallFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 8, com.itextpdf.text.Font.NORMAL);

            com.itextpdf.text.Paragraph p = new com.itextpdf.text.Paragraph(empNombre.toUpperCase(), tituloFont);
            p.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            doc.add(p);

            if (!empDireccion.isEmpty()) {
                p = new com.itextpdf.text.Paragraph(empDireccion, smallFont);
                p.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                doc.add(p);
            }

            if (!empRuc.isEmpty()) {
                p = new com.itextpdf.text.Paragraph("RUC: " + empRuc, subtituloFont);
                p.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                doc.add(p);
            }

            p = new com.itextpdf.text.Paragraph("COMPROBANTE DE PAGO", subtituloFont);
            p.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            p.setSpacingAfter(10f);
            doc.add(p);

            agregarSeparadorPDF(doc);

            com.itextpdf.text.pdf.PdfPTable datos = new com.itextpdf.text.pdf.PdfPTable(2);
            datos.setWidthPercentage(100);
            float[] anchoDatos = {22f, 78f};
            datos.setWidths(anchoDatos);

            java.util.Date ahora = new java.util.Date();
            java.text.SimpleDateFormat formatoFecha = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.text.SimpleDateFormat formatoHora = new java.text.SimpleDateFormat("HH:mm");

            String[] labels = {"Fecha:", "Hora:", "N° Pedido:", "Cliente:", "Mesero:", "Cocinero:", "Cajero:"};
            String[] values = {
                formatoFecha.format(ahora),
                formatoHora.format(ahora),
                idStr,
                cliente != null ? cliente : "—",
                nombreMesero != null && !nombreMesero.trim().isEmpty() ? nombreMesero : "—",
                nombreCocinero != null && !nombreCocinero.trim().isEmpty() ? nombreCocinero : "—",
                Clases.GuardarSesion.nombreCompleto()
            };

            for (int i = 0; i < labels.length; i++) {
                com.itextpdf.text.pdf.PdfPCell cLabel = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(labels[i], boldFont));
                cLabel.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
                cLabel.setPaddingBottom(2);
                datos.addCell(cLabel);
                com.itextpdf.text.pdf.PdfPCell cValue = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(values[i], normalFont));
                cValue.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
                cValue.setPaddingBottom(2);
                datos.addCell(cValue);
            }
            doc.add(datos);
            doc.add(new com.itextpdf.text.Paragraph(" "));

            agregarSeparadorPDF(doc);

            doc.add(new com.itextpdf.text.Paragraph(" "));

            com.itextpdf.text.pdf.PdfPTable tabla = new com.itextpdf.text.pdf.PdfPTable(4);
            tabla.setWidthPercentage(100);
            float[] anchoTabla = {40f, 14f, 20f, 26f};
            tabla.setWidths(anchoTabla);

            com.itextpdf.text.pdf.PdfPCell ch = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Producto", boldFont));
            ch.setPadding(5);
            tabla.addCell(ch);
            ch = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Cant", boldFont));
            ch.setPadding(5);
            tabla.addCell(ch);
            ch = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Precio", boldFont));
            ch.setPadding(5);
            tabla.addCell(ch);
            ch = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Subtotal", boldFont));
            ch.setPadding(5);
            tabla.addCell(ch);

            for (int i = 0; i < productos.size(); i++) {
                Object[] fila = productos.get(i);
                tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(
                        fila[0].toString(), normalFont)));
                tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(
                        fila[1].toString(), normalFont)));
                tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(
                        "S/ " + String.format("%.2f", (double) fila[2]), normalFont)));
                tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(
                        "S/ " + String.format("%.2f", (double) fila[3]), normalFont)));
            }
            doc.add(tabla);
            doc.add(new com.itextpdf.text.Paragraph(" "));

            agregarSeparadorPDF(doc);

            doc.add(new com.itextpdf.text.Paragraph(" "));

            com.itextpdf.text.pdf.PdfPTable t2 = new com.itextpdf.text.pdf.PdfPTable(2);
            t2.setWidthPercentage(100);
            float[] anchoT2 = {68f, 32f};
            t2.setWidths(anchoT2);

            com.itextpdf.text.pdf.PdfPCell cv = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(""));
            cv.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
            t2.addCell(cv);

            com.itextpdf.text.pdf.PdfPCell cv2 = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(
                    "Subtotal: S/ " + String.format("%.2f", subtotal), normalFont));
            cv2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            cv2.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
            cv2.setPaddingBottom(1);
            t2.addCell(cv2);

            cv = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(""));
            cv.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
            t2.addCell(cv);
            cv2 = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(
                    "IGV (18%): S/ " + String.format("%.2f", igv), normalFont));
            cv2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            cv2.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
            cv2.setPaddingBottom(1);
            t2.addCell(cv2);

            cv = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(""));
            cv.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
            t2.addCell(cv);
            cv2 = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(
                    "TOTAL: S/ " + String.format("%.2f", total), boldFont));
            cv2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            cv2.setBorder(com.itextpdf.text.Rectangle.TOP);
            cv2.setPaddingTop(4);
            t2.addCell(cv2);
            doc.add(t2);
            doc.add(new com.itextpdf.text.Paragraph(" "));

            agregarSeparadorPDF(doc);

            doc.add(new com.itextpdf.text.Paragraph(" "));

            p = new com.itextpdf.text.Paragraph("Forma de pago: " + metodoPago, boldFont);
            doc.add(p);
            if (metodoPago.equals("Efectivo")) {
                doc.add(new com.itextpdf.text.Paragraph("Recibido: S/ " + String.format("%.2f", montoRecibido), normalFont));
                doc.add(new com.itextpdf.text.Paragraph("Vuelto: S/ " + String.format("%.2f", vuelto), normalFont));
            }
            doc.add(new com.itextpdf.text.Paragraph(" "));

            agregarSeparadorPDF(doc);

            doc.add(new com.itextpdf.text.Paragraph(" "));

            try {
                java.net.URL qrUrl = getClass().getResource("/Imagenes/QR.jpeg");
                if (qrUrl != null) {
                    com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(qrUrl);
                    img.scaleToFit(80, 80);
                    img.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    doc.add(img);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo cargar el código QR: " + e.getMessage(),
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }

            p = new com.itextpdf.text.Paragraph("¡Gracias por su preferencia!", smallFont);
            p.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            doc.add(p);

            doc.close();
            java.awt.Desktop.getDesktop().open(new java.io.File(ruta));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al generar comprobante PDF: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
