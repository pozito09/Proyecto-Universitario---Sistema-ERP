package Empleado;

import Clases.ConexionBD;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Cajero extends JFrame {

    private JTextField txtId;
    private JTable tablaDetalle;
    private DefaultTableModel modeloTabla;
    private JLabel lblCliente;
    private JLabel lblId;
    private JButton btnPagar;
    private JLabel lblTotal;
    private int idPedidoActual;
    private String clienteActual;

    public Cajero() {
        initUI();
        javax.swing.SwingUtilities.invokeLater(() ->
            setExtendedState(JFrame.MAXIMIZED_BOTH)
        );
    }

    private void initUI() {
        setTitle("CAJERO - PROCESAR PAGO");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(new Color(245, 245, 220));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBusqueda.setBackground(new Color(245, 245, 220));
        JLabel lblBuscar = new JLabel("ID del Pedido:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtId = new JTextField(15);
        txtId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtId.setHorizontalAlignment(JTextField.CENTER);
        JButton btnBuscar = new JButton("BUSCAR");
        btnBuscar.setBackground(new Color(200, 140, 40));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtId);
        panelBusqueda.add(btnBuscar);

        JPanel panelInfo = new JPanel(new GridLayout(1, 2, 10, 0));
        panelInfo.setBackground(new Color(245, 245, 220));
        lblId = new JLabel("ID: —", SwingConstants.CENTER);
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblId.setBackground(new Color(102, 51, 0));
        lblId.setForeground(Color.WHITE);
        lblId.setOpaque(true);
        lblId.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        lblCliente = new JLabel("Cliente: —", SwingConstants.CENTER);
        lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCliente.setBackground(new Color(102, 51, 0));
        lblCliente.setForeground(Color.WHITE);
        lblCliente.setOpaque(true);
        lblCliente.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelInfo.add(lblId);
        panelInfo.add(lblCliente);

        modeloTabla = new DefaultTableModel(
                new String[]{"Producto", "Cantidad", "Precio", "Subtotal"}, 0
        ) {
            final Class[] tipos = {String.class, Integer.class, Double.class, Double.class};
            final boolean[] edit = {false, false, false, false};
            @Override public Class getColumnClass(int i) { return tipos[i]; }
            @Override public boolean isCellEditable(int r, int c) { return edit[c]; }
        };
        tablaDetalle = new JTable(modeloTabla);
        tablaDetalle.getTableHeader().setBackground(new Color(102, 51, 0));
        tablaDetalle.getTableHeader().setForeground(Color.WHITE);
        tablaDetalle.setSelectionBackground(new Color(200, 140, 40));
        tablaDetalle.setGridColor(new Color(200, 180, 150));
        JScrollPane scroll = new JScrollPane(tablaDetalle);
        scroll.setBorder(BorderFactory.createTitledBorder("Detalle del Pedido"));

        lblTotal = new JLabel("Total: S/ 0.00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotal.setBackground(new Color(102, 51, 0));
        lblTotal.setForeground(Color.WHITE);
        lblTotal.setOpaque(true);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(new Color(245, 245, 220));
        panelCentro.add(panelInfo, BorderLayout.NORTH);
        panelCentro.add(scroll, BorderLayout.CENTER);
        panelCentro.add(lblTotal, BorderLayout.SOUTH);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelSur.setBackground(new Color(245, 245, 220));
        btnPagar = new JButton("PAGAR");
        btnPagar.setBackground(new Color(102, 51, 0));
        btnPagar.setForeground(Color.WHITE);
        btnPagar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnPagar.setFocusPainted(false);
        btnPagar.setBorderPainted(false);
        btnPagar.setPreferredSize(new Dimension(200, 45));
        btnPagar.setEnabled(false);
        panelSur.add(btnPagar);

        JButton btnCerrarSesion = new JButton("CERRAR SESIÓN");
        btnCerrarSesion.setBackground(new Color(102, 51, 0));
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
        JLabel lblCajero = new JLabel("  Cajero: " + Clases.Sesion.nombre);
        lblCajero.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        JPanel panelSur2 = new JPanel(new BorderLayout());
        panelSur2.setBackground(new Color(245, 245, 220));
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

        try (Connection con = ConexionBD.conectar()) {
            String sql = "SELECT id, nombre_cliente, total, estado FROM pedidos WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Pedido no encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                limpiar();
                return;
            }

            idPedidoActual = rs.getInt("id");
            clienteActual = rs.getString("nombre_cliente");
            double total = rs.getDouble("total");
            String estado = rs.getString("estado");

            if ("Pagado".equals(estado)) {
                JOptionPane.showMessageDialog(this, "Este pedido ya fue pagado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                limpiar();
                return;
            }

            lblId.setText("ID: " + String.format("%08d", idPedidoActual));
            lblCliente.setText("Cliente: " + clienteActual);

            modeloTabla.setRowCount(0);
            String sqld = "SELECT nombre_producto, cantidad, precio, subtotal FROM detalle_pedido WHERE id_pedido = ?";
            PreparedStatement psd = con.prepareStatement(sqld);
            psd.setInt(1, idPedidoActual);
            ResultSet rsd = psd.executeQuery();
            while (rsd.next()) {
                modeloTabla.addRow(new Object[]{
                    rsd.getString("nombre_producto"),
                    rsd.getInt("cantidad"),
                    rsd.getDouble("precio"),
                    rsd.getDouble("subtotal")
                });
            }

            lblTotal.setText("Total: S/ " + String.format("%.2f", total));
            btnPagar.setEnabled(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarPago() {
        double total = 0;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            total += (double) modeloTabla.getValueAt(i, 3);
        }

        String[] metodos = {"Efectivo", "Tarjeta", "Yape / Plin"};
        String metodoPago = (String) JOptionPane.showInputDialog(this,
                "Total a cobrar:  S/ " + String.format("%.2f", total)
                + "\n\nSelecciona el método de pago:",
                "Método de Pago",
                JOptionPane.PLAIN_MESSAGE, null, metodos, metodos[0]);

        if (metodoPago == null) return;

        double montoRecibido = total;
        double vuelto = 0;

        if (metodoPago.equals("Efectivo")) {
            while (true) {
                String input = JOptionPane.showInputDialog(this,
                        "Total: S/ " + String.format("%.2f", total)
                        + "\n\nIngresa el monto recibido (S/):");
                if (input == null) return;
                try {
                    montoRecibido = Double.parseDouble(input.trim().replace(",", "."));
                    if (montoRecibido < total) {
                        JOptionPane.showMessageDialog(this,
                                "Monto insuficiente. Total: S/ " + String.format("%.2f", total),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        vuelto = montoRecibido - total;
                        break;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        try (Connection con = ConexionBD.conectar()) {
            String sql = "UPDATE pedidos SET estado = 'Pagado', metodo_pago = ?, monto_recibido = ?, vuelto = ?, id_usuario_cobra = ?, nombre_usuario_cobra = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, metodoPago);
            ps.setDouble(2, montoRecibido);
            ps.setDouble(3, vuelto);
            ps.setInt(4, Clases.Sesion.id);
            ps.setString(5, Clases.Sesion.nombre);
            ps.setInt(6, idPedidoActual);
            ps.executeUpdate();

            descontarStock(con);

            generarComprobantePDF(metodoPago, montoRecibido, vuelto);

            JOptionPane.showMessageDialog(this,
                    "¡Pago exitoso!\n\nCliente: " + clienteActual
                    + "\nMétodo: " + metodoPago
                    + "\nTotal: S/ " + String.format("%.2f", total)
                    + (metodoPago.equals("Efectivo")
                    ? "\nRecibido: S/ " + String.format("%.2f", montoRecibido)
                    + "\nVuelto: S/ " + String.format("%.2f", vuelto) : ""),
                    "Confirmación", JOptionPane.INFORMATION_MESSAGE);

            limpiar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al procesar pago: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
    }

    private void descontarStock(Connection con) throws SQLException {
        String sql = "UPDATE productos SET stock = GREATEST(0, stock - ?) WHERE nombre = ? AND stock >= ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                String nombre = modeloTabla.getValueAt(i, 0).toString();
                int cantidad = ((Number) modeloTabla.getValueAt(i, 1)).intValue();
                ps.setInt(1, cantidad);
                ps.setString(2, nombre);
                ps.setInt(3, cantidad);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void generarComprobantePDF(String metodoPago, double montoRecibido, double vuelto) {
        String idStr = String.format("%08d", idPedidoActual);
        String nombreArchivo = "comprobante_" + idStr + ".pdf";
        String ruta = System.getProperty("user.home") + java.io.File.separator
                + "Desktop" + java.io.File.separator + nombreArchivo;

        // Cargar datos de empresa
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
        } catch (Exception ignored) {}

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

            double total = 0;
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                total += (double) modeloTabla.getValueAt(i, 3);
            }

            double subtotal = total / 1.18;
            double igv = total - subtotal;

            // Encabezado
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
            doc.add(p);

            // Línea
            com.itextpdf.text.pdf.PdfPTable linea = new com.itextpdf.text.pdf.PdfPTable(1);
            linea.setWidthPercentage(100);
            com.itextpdf.text.pdf.PdfPCell celdaLinea = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(""));
            celdaLinea.setBorder(com.itextpdf.text.pdf.PdfPCell.BOTTOM);
            celdaLinea.setPaddingBottom(3);
            linea.addCell(celdaLinea);
            doc.add(linea);

            // Datos
            com.itextpdf.text.pdf.PdfPTable datos = new com.itextpdf.text.pdf.PdfPTable(2);
            datos.setWidthPercentage(100);
            float[] anchoDatos = {22f, 78f};
            datos.setWidths(anchoDatos);

            String[] labels = {"Fecha:", "N° Pedido:", "Cliente:", "Cajero:"};
            String[] values = {
                new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()),
                idStr,
                clienteActual != null ? clienteActual : "—",
                Clases.Sesion.nombre
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

            // Tabla productos
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

            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(
                        modeloTabla.getValueAt(i, 0).toString(), normalFont)));
                tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(
                        modeloTabla.getValueAt(i, 1).toString(), normalFont)));
                tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(
                        "S/ " + String.format("%.2f", (double) modeloTabla.getValueAt(i, 2)), normalFont)));
                tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(
                        "S/ " + String.format("%.2f", (double) modeloTabla.getValueAt(i, 3)), normalFont)));
            }
            doc.add(tabla);

            // IGV y Total a la derecha
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

            // Monto en letras
            String letras = Clases.NumeroALetras.convertir(total);
            p = new com.itextpdf.text.Paragraph("Son: " + letras.toUpperCase() + " SOLES", normalFont);
            doc.add(p);

            // Forma de pago
            doc.add(new com.itextpdf.text.Paragraph(" "));
            p = new com.itextpdf.text.Paragraph("Forma de pago: " + metodoPago, boldFont);
            doc.add(p);
            if (metodoPago.equals("Efectivo")) {
                doc.add(new com.itextpdf.text.Paragraph("Recibido: S/ " + String.format("%.2f", montoRecibido), normalFont));
                doc.add(new com.itextpdf.text.Paragraph("Vuelto: S/ " + String.format("%.2f", vuelto), normalFont));
            }

            // QR
            doc.add(new com.itextpdf.text.Paragraph(" "));
            try {
                java.net.URL qrUrl = getClass().getResource("/Imagenes/QR.jpeg");
                if (qrUrl != null) {
                    com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(qrUrl);
                    img.scaleToFit(80, 80);
                    img.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    img.setBorder(com.itextpdf.text.Rectangle.BOX);
                    doc.add(img);
                }
            } catch (Exception ignored) {}

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
