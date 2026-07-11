package Empleado;

import javax.swing.table.DefaultTableModel;
import Clases.Producto;

public class listaMenu extends javax.swing.JFrame {

    private Menu menuPadre;
    private javax.swing.table.DefaultTableModel modeloTabla;
    private javax.swing.JTextField jTextField2;
    /** Snapshot del pedido al cargarlo, para detectar cambios */
    private String snapshotOriginal = "";

    public listaMenu(Menu padre) {
        initComponents();
        this.menuPadre = padre;
        modeloTabla = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        jTable1.getTableHeader().setBackground(java.awt.Color.WHITE);
        jTable1.getTableHeader().setForeground(java.awt.Color.BLACK);
        jTable1.getTableHeader().setFont(new java.awt.Font("Segoe UI", 1, 16));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        menuPadre.resetearCarrito();

        // Campo de ID para modificar pedidos existentes
        jTextField2 = new javax.swing.JTextField(8);
        javax.swing.JPanel panelId = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 3));
        panelId.setBackground(new java.awt.Color(102, 51, 0));
        javax.swing.JLabel lblId = new javax.swing.JLabel("ID Pedido (vacío=nuevo):");
        lblId.setForeground(java.awt.Color.WHITE);
        panelId.add(lblId);
        panelId.add(jTextField2);

        javax.swing.JButton btnCargar = new javax.swing.JButton("Cargar");
        btnCargar.setBackground(new java.awt.Color(193, 164, 140));
        btnCargar.setForeground(java.awt.Color.WHITE);
        btnCargar.setBorderPainted(false);
        btnCargar.setFocusPainted(false);
        btnCargar.addActionListener(e -> cargarPedidoExistente());
        panelId.add(btnCargar);

        // Al borrar el ID se reinicia el formulario
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (jTextField2.getText().trim().isEmpty()) {
                    limpiarFormulario();
                }
            }
        });

        getContentPane().removeAll();
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        getContentPane().add(panelId, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void cargarPedidoExistente() {
        String texto = jTextField2.getText().trim();
        if (texto.isEmpty()) return;

        int id;
        try {
            id = Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "ID inválido.");
            return;
        }

        try (java.sql.Connection con = Clases.ConexionBD.conectar()) {
            String sql = "SELECT nombre_cliente, estado_cocina, estado_pago FROM pedidos WHERE id = ?";
            try (java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        javax.swing.JOptionPane.showMessageDialog(this, "Pedido no encontrado.");
                        return;
                    }
                    if ("Pagado".equals(rs.getString("estado_pago")) || "Anulado".equals(rs.getString("estado_pago"))) {
                        javax.swing.JOptionPane.showMessageDialog(this,
                                "No se puede modificar un pedido pagado o anulado.");
                        jTextField2.setText("");
                        limpiarFormulario();
                        return;
                    }
                    jTextField1.setText(rs.getString("nombre_cliente"));
                }
            }

            modeloTabla.setRowCount(0);
            StringBuilder sb = new StringBuilder();
            String sqlDet = "SELECT nombre_producto, cantidad, precio, subtotal FROM detalle_pedido WHERE id_pedido = ?";
            try (java.sql.PreparedStatement ps = con.prepareStatement(sqlDet)) {
                ps.setInt(1, id);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String nombre = rs.getString("nombre_producto");
                        int cantidad = rs.getInt("cantidad");
                        double precio = rs.getDouble("precio");
                        double subtotal = rs.getDouble("subtotal");
                        modeloTabla.addRow(new Object[]{nombre, cantidad, precio, subtotal});
                        sb.append(nombre).append(":").append(cantidad).append("|");
                    }
                }
            }
            snapshotOriginal = sb.toString();

            actualizarTotal(modeloTabla);

            if (menuPadre != null) {
                menuPadre.resetearCarrito();
                // El badge solo cuenta items nuevos, no los originales
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error al cargar pedido: " + e.getMessage());
        }
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

    private void limpiarFormulario() {
        modeloTabla.setRowCount(0);
        jLabel6.setText("S/ 0.00");
        jTextField1.setText("");
        snapshotOriginal = "";
        if (menuPadre != null) {
            menuPadre.resetearCarrito();
        }
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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
        if (fila == -1) return;

        int cantidadActual = (int) modeloTabla.getValueAt(fila, 1);
        if (cantidadActual > 1) {
            double precio = (double) modeloTabla.getValueAt(fila, 2);
            modeloTabla.setValueAt(cantidadActual - 1, fila, 1);
            modeloTabla.setValueAt(precio * (cantidadActual - 1), fila, 3);
        } else {
            modeloTabla.removeRow(fila);
        }
        if (menuPadre != null) {
            menuPadre.actualizarBadgeCarrito(-1);
        }

        // Recalcular total
        double total = 0;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            total += (double) modeloTabla.getValueAt(i, 3);
        }
        jLabel6.setText(String.format("S/ %.2f", total));
    }//GEN-LAST:event_EliminarActionPerformed

    private boolean huboCambios() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            sb.append(modeloTabla.getValueAt(i, 0))
              .append(":")
              .append(modeloTabla.getValueAt(i, 1))
              .append("|");
        }
        return !sb.toString().equals(snapshotOriginal);
    }

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

        if (!Caja.ControlCaja.hayCajaAbierta()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "No hay una caja aperturada. Debe abrir la caja antes de generar un pedido.",
                    "Caja no aperturada", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idModificarStr = jTextField2.getText().trim();
        boolean esModificacion = !idModificarStr.isEmpty();

        double total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total += (double) modelo.getValueAt(i, 3);
        }

        int idPedido;
        if (esModificacion) {
            // Verificar que el pedido no esté Pagado o Anulado
            int idModificar = Integer.parseInt(idModificarStr);
            try (java.sql.Connection con = Clases.ConexionBD.conectar();
                 java.sql.PreparedStatement ps = con.prepareStatement("SELECT estado_pago FROM pedidos WHERE id = ?")) {
                ps.setInt(1, idModificar);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String estadoPago = rs.getString("estado_pago");
                        if ("Pagado".equals(estadoPago) || "Anulado".equals(estadoPago)) {
                            javax.swing.JOptionPane.showMessageDialog(this,
                                    "No se puede modificar un pedido pagado o anulado.");
                            jTextField2.setText("");
                            limpiarFormulario();
                            return;
                        }
                    }
                }
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Error al verificar pedido: " + ex.getMessage());
                return;
            }
            if (!huboCambios()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "No se realizaron cambios en el pedido.",
                        "Ticket no modificado", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            idPedido = modificarPedido(idModificar, nombreCliente, modelo, total);
        } else {
            idPedido = crearPedido(nombreCliente, modelo, total);
        }
        if (idPedido <= 0) return;

        String id = String.format("%09d", idPedido);
        String accion = esModificacion ? "actualizado" : "generado";

        // Generar PDF
        try {
            String ruta = System.getProperty("user.home") + java.io.File.separator + "Desktop"
                    + java.io.File.separator + (esModificacion ? "actualizacion_" : "boleta_") + id + ".pdf";
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
            doc.add(new com.itextpdf.text.Paragraph((esModificacion ? "Ticket Actualizado" : "Ticket") + "\n", normalFont));
            doc.add(new com.itextpdf.text.Paragraph("ID: " + id, boldFont));
            doc.add(new com.itextpdf.text.Paragraph("Cliente: " + nombreCliente, normalFont));
            doc.add(new com.itextpdf.text.Paragraph("Mesero: " + Clases.GuardarSesion.nombreCompleto(), normalFont));
            doc.add(new com.itextpdf.text.Paragraph("----------------------------------------\n", normalFont));

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
        }

        javax.swing.JOptionPane.showMessageDialog(this,
                "Pedido " + accion + ": " + id,
                "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        modelo.setRowCount(0);
        jLabel6.setText("S/ 0.00");
        jTextField1.setText("");
        jTextField2.setText("");
        if (menuPadre != null) {
            menuPadre.resetearCarrito();
        }
    }//GEN-LAST:event_PagarActionPerformed

    private int crearPedido(String nombreCliente,
            javax.swing.table.DefaultTableModel modelo, double total) {
        int idPedido;
        try (java.sql.Connection con = Clases.ConexionBD.conectar()) {
            con.setAutoCommit(false);
            try {
                String sqlPedido = "INSERT INTO pedidos (nombre_cliente, total, estado_cocina, estado_pago, id_apertura, id_usuario_crea, nombre_usuario_crea) VALUES (?, ?, 'Pendiente', 'En cola', ?, ?, ?)";
                java.sql.PreparedStatement ps = con.prepareStatement(sqlPedido, java.sql.Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, nombreCliente);
                ps.setDouble(2, total);
                ps.setInt(3, Caja.ControlCaja.getCajaActivaId());
                ps.setInt(4, Clases.GuardarSesion.id);
                ps.setString(5, Clases.GuardarSesion.nombreCompleto());
                ps.executeUpdate();
                java.sql.ResultSet rs = ps.getGeneratedKeys();
                if (!rs.next()) {
                    con.rollback();
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Error al guardar pedido: no se generó ID.",
                            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return 0;
                }
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

                if (!deducirStock(con, modelo)) {
                    con.rollback();
                    return 0;
                }

                deducirInsumos(con, modelo);

                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error al guardar pedido: " + e.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        return idPedido;
    }

    private int modificarPedido(int idPedido, String nombreCliente,
            javax.swing.table.DefaultTableModel modelo, double total) {
        try (java.sql.Connection con = Clases.ConexionBD.conectar()) {
            con.setAutoCommit(false);

            // Cargar items viejos
            java.util.List<Object[]> itemsViejos = new java.util.ArrayList<>();
            String sqlViejo = "SELECT nombre_producto, cantidad, precio, subtotal FROM detalle_pedido WHERE id_pedido = ?";
            try (java.sql.PreparedStatement ps = con.prepareStatement(sqlViejo)) {
                ps.setInt(1, idPedido);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        itemsViejos.add(new Object[]{
                            rs.getString("nombre_producto"),
                            rs.getInt("cantidad"),
                            rs.getDouble("precio"),
                            rs.getDouble("subtotal")
                        });
                    }
                }
            }

            // Ajustar stock por diferencia (no revertir todo y re-deducir)
            java.util.Map<String, Integer> oldQtyMap = new java.util.HashMap<>();
            for (Object[] item : itemsViejos) {
                String nom = (String) item[0];
                oldQtyMap.put(nom, oldQtyMap.getOrDefault(nom, 0) + (int) item[1]);
            }

            // Revertir items eliminados o reducidos
            for (java.util.Map.Entry<String, Integer> e : oldQtyMap.entrySet()) {
                String nom = e.getKey();
                int oldQty = e.getValue();
                int newQty = 0;
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    if (nom.equals(modelo.getValueAt(i, 0))) {
                        newQty = (int) modelo.getValueAt(i, 1);
                        break;
                    }
                }
                if (oldQty > newQty) {
                    int diff = oldQty - newQty;
                    javax.swing.table.DefaultTableModel tmp = new javax.swing.table.DefaultTableModel(
                        new String[]{"Producto", "Cantidad", "Precio", "Subtotal"}, 0);
                    tmp.addRow(new Object[]{nom, diff, 0.0, 0.0});
                    revertirStock(con, tmp);
                    revertirInsumos(con, tmp);
                }
            }

            // Eliminar detalle viejo
            try (java.sql.PreparedStatement psDel = con.prepareStatement("DELETE FROM detalle_pedido WHERE id_pedido = ?")) {
                psDel.setInt(1, idPedido);
                psDel.executeUpdate();
            }

            // Insertar detalle nuevo
            String sqlDetalle = "INSERT INTO detalle_pedido (id_pedido, nombre_producto, cantidad, precio, subtotal) VALUES (?, ?, ?, ?, ?)";
            try (java.sql.PreparedStatement psd = con.prepareStatement(sqlDetalle)) {
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    psd.setInt(1, idPedido);
                    psd.setString(2, (String) modelo.getValueAt(i, 0));
                    psd.setInt(3, (int) modelo.getValueAt(i, 1));
                    psd.setDouble(4, (double) modelo.getValueAt(i, 2));
                    psd.setDouble(5, (double) modelo.getValueAt(i, 3));
                    psd.addBatch();
                }
                psd.executeBatch();
            }

            // Deducir items nuevos o aumentados (verificando stock)
            for (int i = 0; i < modelo.getRowCount(); i++) {
                String nom = (String) modelo.getValueAt(i, 0);
                int newQty = (int) modelo.getValueAt(i, 1);
                int oldQty = oldQtyMap.getOrDefault(nom, 0);
                int diff = newQty - oldQty;
                if (diff > 0) {
                    String sqlCheck = "UPDATE productos SET stock = stock - ? WHERE nombre = ? AND stock >= ?";
                    try (java.sql.PreparedStatement ps = con.prepareStatement(sqlCheck)) {
                        ps.setInt(1, diff);
                        ps.setString(2, nom);
                        ps.setInt(3, diff);
                        if (ps.executeUpdate() == 0) {
                            javax.swing.JOptionPane.showMessageDialog(this,
                                "Stock insuficiente para " + nom + ". Revirtiendo...",
                                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                            con.rollback();
                            return 0;
                        }
                    }
                    // Deducir insumos para diff
                    javax.swing.table.DefaultTableModel tmp = new javax.swing.table.DefaultTableModel(
                        new String[]{"Producto", "Cantidad", "Precio", "Subtotal"}, 0);
                    tmp.addRow(new Object[]{nom, diff, 0.0, 0.0});
                    deducirInsumos(con, tmp);
                }
            }

            // Actualizar cabecera
            String sqlUpdate = "UPDATE pedidos SET nombre_cliente=?, total=?, modificado=TRUE, estado_cocina= CASE WHEN estado_cocina IN ('Preparando','Listo') THEN 'Pendiente' ELSE estado_cocina END WHERE id=?";
            try (java.sql.PreparedStatement psUpd = con.prepareStatement(sqlUpdate)) {
                psUpd.setString(1, nombreCliente);
                psUpd.setDouble(2, total);
                psUpd.setInt(3, idPedido);
                psUpd.executeUpdate();
            }

            con.commit();
            return idPedido;

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error al modificar pedido: " + e.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }

    private boolean deducirStock(java.sql.Connection con, javax.swing.table.DefaultTableModel modelo)
            throws java.sql.SQLException {
        // Primero verificar stock suficiente de cada producto
        StringBuilder sinStock = new StringBuilder();
        StringBuilder stockBajo = new StringBuilder();
        String sqlCheck = "SELECT stock FROM productos WHERE nombre = ?";
        try (java.sql.PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
            for (int i = 0; i < modelo.getRowCount(); i++) {
                String nombre = (String) modelo.getValueAt(i, 0);
                int cantidad = (int) modelo.getValueAt(i, 1);
                psCheck.setString(1, nombre);
                try (java.sql.ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        int stock = rs.getInt("stock");
                        if (stock < cantidad) {
                            if (sinStock.length() > 0) sinStock.append(", ");
                            sinStock.append(nombre).append(" (disponible: ").append(stock).append(", necesario: ").append(cantidad).append(")");
                        } else if (stock - cantidad <= 2) {
                            if (stockBajo.length() > 0) stockBajo.append(", ");
                            stockBajo.append(nombre).append(" (quedarán: ").append(stock - cantidad).append(")");
                        }
                    }
                }
            }
        }

        if (sinStock.length() > 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "No hay suficiente stock para:\n" + sinStock.toString(),
                    "Stock insuficiente", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (stockBajo.length() > 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Productos con stock bajo:\n" + stockBajo.toString(),
                    "Stock bajo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }

        // Proceder con la deducción
        String sql = "UPDATE productos SET stock = stock - ? WHERE nombre = ? AND stock >= ?";
        try (java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < modelo.getRowCount(); i++) {
                ps.setInt(1, (int) modelo.getValueAt(i, 1));
                ps.setString(2, (String) modelo.getValueAt(i, 0));
                ps.setInt(3, (int) modelo.getValueAt(i, 1));
                ps.addBatch();
            }
            int[] resultados = ps.executeBatch();
            for (int r : resultados) {
                if (r == 0) return false;
            }
        }
        return true;
    }

    private void revertirStock(java.sql.Connection con, javax.swing.table.DefaultTableModel modelo)
            throws java.sql.SQLException {
        String sql = "UPDATE productos SET stock = stock + ? WHERE nombre = ?";
        try (java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < modelo.getRowCount(); i++) {
                ps.setInt(1, (int) modelo.getValueAt(i, 1));
                ps.setString(2, (String) modelo.getValueAt(i, 0));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deducirInsumos(java.sql.Connection con, javax.swing.table.DefaultTableModel modelo) {
        try {
            String sql = "SELECT r.id_insumo, r.cantidad FROM recetas r JOIN productos p ON r.id_producto = p.id WHERE p.nombre = ?";
            String sqlDeducir = "UPDATE insumos SET stock = stock - ? WHERE id = ?";
            try (java.sql.PreparedStatement psReceta = con.prepareStatement(sql);
                 java.sql.PreparedStatement psDeducir = con.prepareStatement(sqlDeducir)) {
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    psReceta.setString(1, (String) modelo.getValueAt(i, 0));
                    try (java.sql.ResultSet rs = psReceta.executeQuery()) {
                        while (rs.next()) {
                            psDeducir.setDouble(1, rs.getDouble("cantidad") * ((int) modelo.getValueAt(i, 1)));
                            psDeducir.setInt(2, rs.getInt("id_insumo"));
                            psDeducir.addBatch();
                        }
                    }
                }
                psDeducir.executeBatch();
            }
        } catch (Exception e) {
            // No hay recetas definidas
        }
    }

    private void revertirInsumos(java.sql.Connection con, javax.swing.table.DefaultTableModel modelo) {
        try {
            String sql = "SELECT r.id_insumo, r.cantidad FROM recetas r JOIN productos p ON r.id_producto = p.id WHERE p.nombre = ?";
            String sqlRevertir = "UPDATE insumos SET stock = stock + ? WHERE id = ?";
            try (java.sql.PreparedStatement psReceta = con.prepareStatement(sql);
                 java.sql.PreparedStatement psRevertir = con.prepareStatement(sqlRevertir)) {
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    psReceta.setString(1, (String) modelo.getValueAt(i, 0));
                    try (java.sql.ResultSet rs = psReceta.executeQuery()) {
                        while (rs.next()) {
                            psRevertir.setDouble(1, rs.getDouble("cantidad") * ((int) modelo.getValueAt(i, 1)));
                            psRevertir.setInt(2, rs.getInt("id_insumo"));
                            psRevertir.addBatch();
                        }
                    }
                }
                psRevertir.executeBatch();
            }
        } catch (Exception e) {
            // No hay recetas definidas
        }
    }

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
