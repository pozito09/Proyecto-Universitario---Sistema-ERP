package Empleado;

import javax.swing.table.DefaultTableModel;
import Clases.Producto;

public class Pago extends javax.swing.JFrame {

    private Menu menuPadre;
    private javax.swing.table.DefaultTableModel modeloTabla;

    public Pago(Menu padre) {
        initComponents();
        this.menuPadre = padre;
        modeloTabla = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        jTable1.getTableHeader().setBackground(java.awt.Color.WHITE);
        jTable1.getTableHeader().setForeground(java.awt.Color.BLACK);
        jTable1.getTableHeader().setFont(new java.awt.Font("Segoe UI", 1, 16));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void agregarProducto(Producto p) {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        // Buscar si ya existe
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 0).equals(p.getNombre())) {
                int cantidadActual = (int) modelo.getValueAt(i, 1);
                int nuevaCantidad = cantidadActual + 1;
                modelo.setValueAt(nuevaCantidad, i, 1);
                modelo.setValueAt(p.getPrecio() * nuevaCantidad, i, 3);
                actualizarTotal(modelo);
                return;
            }
        }
        // Nueva fila
        modelo.addRow(new Object[]{
            p.getNombre(), 1, p.getPrecio(), p.getPrecio()
        });
        actualizarTotal(modelo);
    }

    private void actualizarTotal(javax.swing.table.DefaultTableModel modelo) {
        double total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total += (double) modelo.getValueAt(i, 3);
        }
        jLabel6.setText(String.format("  S/ %.2f", total));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        Pagar = new javax.swing.JButton();
        Eliminar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 51, 0));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Perfil-Photoroom.png"))); // NOI18N
        jLabel2.setText("jLabel2");

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 30)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("SABOR QUE VIAJA AL INFINITO");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Total:");

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setBackground(new java.awt.Color(255, 255, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Cantidad", "Precio", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jLabel6.setOpaque(true);

        Pagar.setBackground(new java.awt.Color(193, 164, 140));
        Pagar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        Pagar.setForeground(new java.awt.Color(255, 255, 255));
        Pagar.setText("Generar ID");
        Pagar.setBorderPainted(false);
        Pagar.setDefaultCapable(false);
        Pagar.setFocusPainted(false);
        Pagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PagarActionPerformed(evt);
            }
        });

        Eliminar.setBackground(new java.awt.Color(204, 0, 0));
        Eliminar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Eliminar.setForeground(new java.awt.Color(255, 255, 255));
        Eliminar.setText("Eliminar");
        Eliminar.setBorderPainted(false);
        Eliminar.setFocusPainted(false);
        Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nombre del Cliente:");

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(382, 382, 382)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                .addGap(392, 392, 392))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(435, 435, 435))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(Pagar, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(500, 500, 500))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Eliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 802, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Eliminar)))
                .addGap(42, 42, 42)
                .addComponent(Pagar)
                .addGap(37, 37, 37))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarActionPerformed

        int fila = jTable1.getSelectedRow();
        if (fila != -1) {
            // Obtener cantidad antes de eliminar
            int cantidad = (int) modeloTabla.getValueAt(fila, 1);
            modeloTabla.removeRow(fila);

            // Recalcular total
            double total = 0;
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                total += (double) modeloTabla.getValueAt(i, 3);
            }
            jLabel6.setText(String.format("S/ %.2f", total));

            // Restar cantidad correcta al badge
            if (menuPadre != null) {
                menuPadre.resetearCarrito();
            }
        }
    }//GEN-LAST:event_EliminarActionPerformed

    private void PagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PagarActionPerformed
        javax.swing.table.DefaultTableModel modelo
                = (javax.swing.table.DefaultTableModel) jTable1.getModel();

        if (modelo.getRowCount() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "No hay productos agregados.",
                    "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreCliente = jTextField1.getText().trim();
        if (nombreCliente.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Por favor ingresa el nombre del cliente.",
                    "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total += (double) modelo.getValueAt(i, 3);
        }

        // Guardar en BD
        int idPedido;
        try (java.sql.Connection con = Clases.ConexionBD.conectar()) {
            String sqlPedido = "INSERT INTO pedidos (nombre_cliente, total, estado, id_apertura, id_usuario_crea, nombre_usuario_crea) VALUES (?, ?, 'Pendiente', ?, ?, ?)";
            java.sql.PreparedStatement ps = con.prepareStatement(sqlPedido, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nombreCliente);
            ps.setDouble(2, total);
            ps.setInt(3, Caja.CajaVista.getCajaActivaId());
            ps.setInt(4, Clases.Sesion.id);
            ps.setString(5, Clases.Sesion.nombre);
            ps.executeUpdate();
            java.sql.ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            idPedido = rs.getInt(1);

            String sqlDetalle = "INSERT INTO detalle_pedido (id_pedido, nombre_producto, cantidad, precio, subtotal) VALUES (?, ?, ?, ?, ?)";
            java.sql.PreparedStatement psd = con.prepareStatement(sqlDetalle);
            for (int i = 0; i < modelo.getRowCount(); i++) {
                psd.setInt(1, idPedido);
                psd.setString(2, (String) modelo.getValueAt(i, 0));
                psd.setInt(3, (int) modelo.getValueAt(i, 1));
                psd.setDouble(4, (double) modelo.getValueAt(i, 2));
                psd.setDouble(5, (double) modelo.getValueAt(i, 3));
                psd.addBatch();
            }
            psd.executeBatch();
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error al guardar pedido: " + e.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = String.format("%08d", idPedido);

        // Generar PDF
        try {
            String ruta = System.getProperty("user.home") + java.io.File.separator + "Desktop" + java.io.File.separator + "boleta_" + id + ".pdf";
            com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new java.io.FileOutputStream(ruta));
            doc.open();

            com.itextpdf.text.Font tituloFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.NORMAL);
            com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);

            doc.add(new com.itextpdf.text.Paragraph("CAFECOMETA", tituloFont));
            doc.add(new com.itextpdf.text.Paragraph("Boleta de Venta\n", normalFont));
            doc.add(new com.itextpdf.text.Paragraph("ID: " + id, boldFont));
            doc.add(new com.itextpdf.text.Paragraph("Cliente: " + nombreCliente, normalFont));
            doc.add(new com.itextpdf.text.Paragraph("----------------------------------------", normalFont));

            com.itextpdf.text.pdf.PdfPTable tabla = new com.itextpdf.text.pdf.PdfPTable(4);
            tabla.setWidthPercentage(100);
            float[] ancho = {40f, 15f, 20f, 25f};
            tabla.setWidths(ancho);
            tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Producto", boldFont)));
            tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Cant", boldFont)));
            tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Precio", boldFont)));
            tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Subtotal", boldFont)));

            for (int i = 0; i < modelo.getRowCount(); i++) {
                tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(
                        (String) modelo.getValueAt(i, 0), normalFont)));
                tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(
                        String.valueOf(modelo.getValueAt(i, 1)), normalFont)));
                tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(
                        "S/ " + String.format("%.2f", (double) modelo.getValueAt(i, 2)), normalFont)));
                tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(
                        "S/ " + String.format("%.2f", (double) modelo.getValueAt(i, 3)), normalFont)));
            }
            doc.add(tabla);
            doc.add(new com.itextpdf.text.Paragraph("----------------------------------------", normalFont));
            doc.add(new com.itextpdf.text.Paragraph(
                    "TOTAL: S/ " + String.format("%.2f", total), boldFont));
            doc.add(new com.itextpdf.text.Paragraph("\nGracias por su compra!", normalFont));

            doc.close();

            java.awt.Desktop.getDesktop().open(new java.io.File(ruta));
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error al generar PDF: " + e.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        javax.swing.JOptionPane.showMessageDialog(this,
                "ID generado correctamente: " + id,
                "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        // Limpiar todo
        modelo.setRowCount(0);
        jLabel6.setText("S/ 0.00");
        jTextField1.setText("");
        if (menuPadre != null) {
            menuPadre.resetearCarrito();
        }
    }//GEN-LAST:event_PagarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Eliminar;
    private javax.swing.JButton Pagar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
