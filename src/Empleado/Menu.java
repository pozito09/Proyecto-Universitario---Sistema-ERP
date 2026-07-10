package Empleado;

import Acceso.Login;
import Clases.Botones;
import Clases.CatalogoProductos;
import Clases.Producto;
import static Clases.Colores.*;
import java.util.*;


public class Menu extends javax.swing.JFrame {

    private ArrayList<Producto> productos = new ArrayList<>();
    private listaMenu ventanaPago = null;
    private javax.swing.JDialog ventanaBusqueda = null;
    // contador del carrito
    private int totalCarrito = 0;
    private javax.swing.JLabel badgeCarrito;

    public Menu() {
        initComponents();
        setTitle("CAFÉ COMETA - PANEL DE MOZO");
        javax.swing.JLabel lblUsuario = new javax.swing.JLabel("  Mozo: " + Clases.GuardarSesion.nombreCompleto());
        lblUsuario.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 14));
        getContentPane().add(lblUsuario, java.awt.BorderLayout.SOUTH);
        inicializarProductos();
        cargarCatalogo();
        // Esto elimina el azul de fondo de la pestaña seleccionada
        javax.swing.UIManager.put("TabbedPane.selected", new java.awt.Color(245, 245, 220));
        // Esto elimina el borde azul/punteado que sale al hacer click
        javax.swing.UIManager.put("TabbedPane.focus", new java.awt.Color(0, 0, 0, 0));
        // Esto quita el borde de las pestañas que no están seleccionadas
        javax.swing.UIManager.put("TabbedPane.contentAreaColor", new java.awt.Color(245, 245, 220));
        // Obliga a que los cambios se apliquen ahora mismo
        javax.swing.SwingUtilities.updateComponentTreeUI(this.JTabbedPane);
        //velocidad de los scroll "esa cosa que de desliza p"
        jScrollPane3.getVerticalScrollBar().setUnitIncrement(20);
        jScrollPane4.getVerticalScrollBar().setUnitIncrement(20);
        jScrollPane5.getVerticalScrollBar().setUnitIncrement(20);
        //quitar las rayitas xD
        jScrollPane3.setBorder(null);
        jScrollPane4.setBorder(null);
        jScrollPane5.setBorder(null);
        // Badge del carrito usando GlassPane
        javax.swing.JPanel glass = new javax.swing.JPanel(null);
        glass.setOpaque(false);
        setGlassPane(glass);
        glass.setVisible(true);
        //reset al scrull
        JTabbedPane.addChangeListener(e -> {
            jScrollPane3.getVerticalScrollBar().setValue(0);
            jScrollPane4.getVerticalScrollBar().setValue(0);
            jScrollPane5.getVerticalScrollBar().setValue(0);
        });

        badgeCarrito = new javax.swing.JLabel("0") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new java.awt.Color(210, 40, 40));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badgeCarrito.setForeground(java.awt.Color.WHITE);
        badgeCarrito.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10));
        badgeCarrito.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        badgeCarrito.setBounds(0, 0, 20, 20);
        glass.add(badgeCarrito);

        // Posicionar badge cuando la ventana esté lista
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                reponerBadge();
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                reponerBadge();
            }
        });

        javax.swing.SwingUtilities.invokeLater(() -> {
            setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        });

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Busqueda = new javax.swing.JButton();
        JTabbedPane = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Café = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        Emparedados = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        Postres = new javax.swing.JPanel();
        CerrarSesion = new javax.swing.JButton();
        Compra = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 51, 0));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Perfil-Photoroom.png"))); // NOI18N

        Busqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa40x40 (1).png"))); // NOI18N
        Busqueda.setBorder(null);
        Busqueda.setBorderPainted(false);
        Busqueda.setContentAreaFilled(false);
        Busqueda.setFocusPainted(false);
        Busqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BusquedaActionPerformed(evt);
            }
        });

        JTabbedPane.setBackground(new java.awt.Color(193, 164, 140));
        JTabbedPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel8.setBackground(new java.awt.Color(245, 245, 220));

        jScrollPane3.setBorder(null);
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane3.setFocusable(false);

        Café.setBackground(new java.awt.Color(245, 245, 220));

        javax.swing.GroupLayout CaféLayout = new javax.swing.GroupLayout(Café);
        Café.setLayout(CaféLayout);
        CaféLayout.setHorizontalGroup(
            CaféLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1257, Short.MAX_VALUE)
        );
        CaféLayout.setVerticalGroup(
            CaféLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 744, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(Café);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE)
        );

        JTabbedPane.addTab("Cafés", jPanel8);

        jPanel9.setBackground(new java.awt.Color(245, 245, 220));

        jScrollPane4.setBorder(null);
        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane4.setFocusable(false);

        Emparedados.setBackground(new java.awt.Color(245, 245, 220));

        javax.swing.GroupLayout EmparedadosLayout = new javax.swing.GroupLayout(Emparedados);
        Emparedados.setLayout(EmparedadosLayout);
        EmparedadosLayout.setHorizontalGroup(
            EmparedadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1257, Short.MAX_VALUE)
        );
        EmparedadosLayout.setVerticalGroup(
            EmparedadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 809, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(Emparedados);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE)
        );

        JTabbedPane.addTab("Emparedados", jPanel9);

        jPanel10.setBackground(new java.awt.Color(245, 245, 220));

        jScrollPane5.setBorder(null);
        jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane5.setFocusable(false);

        Postres.setBackground(new java.awt.Color(245, 245, 220));

        javax.swing.GroupLayout PostresLayout = new javax.swing.GroupLayout(Postres);
        Postres.setLayout(PostresLayout);
        PostresLayout.setHorizontalGroup(
            PostresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1257, Short.MAX_VALUE)
        );
        PostresLayout.setVerticalGroup(
            PostresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 744, Short.MAX_VALUE)
        );

        jScrollPane5.setViewportView(Postres);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE)
        );

        JTabbedPane.addTab("Postres", jPanel10);

        CerrarSesion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Exit.png"))); // NOI18N
        CerrarSesion.setBorder(null);
        CerrarSesion.setBorderPainted(false);
        CerrarSesion.setContentAreaFilled(false);
        CerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CerrarSesionActionPerformed(evt);
            }
        });

        Compra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Compra.png"))); // NOI18N
        Compra.setBorderPainted(false);
        Compra.setContentAreaFilled(false);
        Compra.setFocusable(false);
        Compra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CompraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Busqueda)
                .addGap(29, 29, 29)
                .addComponent(Compra, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(CerrarSesion)
                .addGap(24, 24, 24))
            .addComponent(JTabbedPane)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Compra, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(CerrarSesion)
                            .addComponent(Busqueda, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTabbedPane))
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

    private void BusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BusquedaActionPerformed
        // Panel de búsqueda flotante
        if (ventanaBusqueda != null && ventanaBusqueda.isVisible()) {
            ventanaBusqueda.toFront();
            return;
        }
        ventanaBusqueda = new javax.swing.JDialog(this, "Buscar producto", false);
        javax.swing.JDialog dialogo = ventanaBusqueda;
        dialogo.setSize(400, 350);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new java.awt.BorderLayout(10, 10));

        // Campo de búsqueda
        javax.swing.JTextField campoBusqueda = new javax.swing.JTextField();
        campoBusqueda.setFont(new java.awt.Font("Segoe UI", 0, 16));
        campoBusqueda.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 140, 40), 2),
                javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Panel de resultados
        javax.swing.JPanel panelResultados = new javax.swing.JPanel();
        panelResultados.setLayout(new javax.swing.BoxLayout(panelResultados, javax.swing.BoxLayout.Y_AXIS));
        javax.swing.JScrollPane scrollResultados = new javax.swing.JScrollPane(panelResultados);

        // Buscar al escribir
        campoBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String texto = campoBusqueda.getText().toLowerCase();
                panelResultados.removeAll();
                for (Producto p : productos) {
                    if (p.getNombre().toLowerCase().contains(texto) && !texto.isEmpty()) {
                        javax.swing.JPanel fila = new javax.swing.JPanel(new java.awt.BorderLayout());
                        fila.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 45));
                        fila.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(220, 200, 160)));
                        javax.swing.JLabel lblNombre = new javax.swing.JLabel("  " + p.getNombre());
                        lblNombre.setFont(new java.awt.Font("Segoe UI", 0, 14));
                        javax.swing.JLabel lblPrecio = new javax.swing.JLabel(" S/ " + p.getPrecio() + "  ");
                        lblPrecio.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
                        lblPrecio.setForeground(new java.awt.Color(140, 90, 20));
                        javax.swing.JButton btnAgregar = Botones.crear("+ Agregar", DORADO, DORADO_CLARO);
                        btnAgregar.addActionListener(ev -> {
                            if (p.getStock() <= 0) {
                                javax.swing.JOptionPane.showMessageDialog(
                                        dialogo, "Sin stock disponible", "Café Cometa", javax.swing.JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            if (ventanaPago == null) {
                                ventanaPago = new listaMenu(Menu.this);
                            }
                            ventanaPago.agregarProducto(p);
                            javax.swing.JOptionPane.showMessageDialog(
                                    dialogo, "✓ " + p.getNombre() + " agregado al carrito", "Café Cometa", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                            actualizarBadgeCarrito(1);
                        });
                        fila.add(lblNombre, java.awt.BorderLayout.WEST);
                        fila.add(lblPrecio, java.awt.BorderLayout.CENTER);
                        fila.add(btnAgregar, java.awt.BorderLayout.EAST);
                        panelResultados.add(fila);
                    }
                }

                panelResultados.revalidate();
                panelResultados.repaint();
            }
        });

        dialogo.add(campoBusqueda, java.awt.BorderLayout.NORTH);
        dialogo.add(scrollResultados, java.awt.BorderLayout.CENTER);
        dialogo.setVisible(true);
    }//GEN-LAST:event_BusquedaActionPerformed
    private void reponerBadge() {
        java.awt.Point p = javax.swing.SwingUtilities.convertPoint(
                Compra, Compra.getWidth() - 8, -6, getGlassPane());
        badgeCarrito.setBounds(p.x, p.y, 20, 20);
        getGlassPane().repaint();
    }

    public void actualizarBadgeCarrito(int cantidad) {
        totalCarrito += cantidad;
        if (totalCarrito < 0) totalCarrito = 0;
        badgeCarrito.setText(String.valueOf(totalCarrito));
        reponerBadge();
    }

    public void resetearCarrito() {
        totalCarrito = 0;
        badgeCarrito.setText("0");
        reponerBadge();
    }
    private void CerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CerrarSesionActionPerformed
        if (ventanaPago != null) {
            ventanaPago.dispose();
            ventanaPago = null;
        }
        // 2. Cerrar la ventana actual (Menu)
        this.dispose();
        // 3. Abrir el Login nuevamente :v
        Login ventanaLogin = new Login();
        ventanaLogin.setLocationRelativeTo(null);
        ventanaLogin.setVisible(true);

    }//GEN-LAST:event_CerrarSesionActionPerformed

    private void CompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CompraActionPerformed
        if (ventanaPago == null) {
            ventanaPago = new listaMenu(this);
        }
        ventanaPago.setVisible(true);
        ventanaPago.setLocationRelativeTo(null);
        ventanaPago.toFront();
    }//GEN-LAST:event_CompraActionPerformed
    private void inicializarProductos() {
        productos = CatalogoProductos.obtenerProductos();
    }

    public void refrescarCatalogo() {
        inicializarProductos();
        Café.removeAll();
        Emparedados.removeAll();
        Postres.removeAll();
        cargarCatalogo();
    }

    // ── Cargar productos en los paneles del Designer ──────────────────
    private void cargarCatalogo() {
        // Configura el layout de cada panel del Designer
        Café.setLayout(new java.awt.GridLayout(0, 6, 15, 15));
        Emparedados.setLayout(new java.awt.GridLayout(0, 6, 15, 15));
        Postres.setLayout(new java.awt.GridLayout(0, 6, 15, 15));
        Café.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        Emparedados.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        Postres.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        for (Producto p : productos) {
            if (p.getCategoria().equals("Cafés")) {
                Café.add(crearTarjeta(p));
            }
            if (p.getCategoria().equals("Emparedados")) {
                Emparedados.add(crearTarjeta(p));
            }
            if (p.getCategoria().equals("Postres")) {
                Postres.add(crearTarjeta(p));
            }
        }

        // Refrescar
        for (javax.swing.JPanel p : new javax.swing.JPanel[]{Café, Emparedados, Postres}) {
            p.revalidate();
            p.repaint();
        }
    }

    // ── Crea la tarjeta y la agrega al panel de su categoría ──
    private javax.swing.JPanel crearTarjeta(Producto p) {
        boolean disponible = p.getStock() > 0;
        // ── TARJETA con sombra y bordes redondeados ──────────────────
        javax.swing.JPanel tarjeta = new javax.swing.JPanel(new java.awt.BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                // Fondo blanco
                g2.setColor(disponible ? java.awt.Color.WHITE : new java.awt.Color(215, 215, 215));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

            }
        };
        tarjeta.setOpaque(true);
        tarjeta.setBackground(java.awt.Color.WHITE);
        //Tamaño de las tarjetas
        tarjeta.setPreferredSize(new java.awt.Dimension(175, 320));
        tarjeta.setMinimumSize(new java.awt.Dimension(175, 320));
        tarjeta.setMaximumSize(new java.awt.Dimension(175, 320));
        tarjeta.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 4));

        // ── IMAGEN con fondo redondeado ───────────────────────────────
        final java.awt.Image cachedImage;
        if (p.tieneImagen()) {
            java.awt.Image img = null;
            try {
                img = javax.imageio.ImageIO.read(
                        new java.io.ByteArrayInputStream(p.getImagenBytes()));
            } catch (java.io.IOException ex) { }
            cachedImage = img;
        } else {
            cachedImage = null;
        }

        javax.swing.JPanel imgPanel = new javax.swing.JPanel(new java.awt.GridBagLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                g2.setColor(p.getColor());
                g2.fillRoundRect(0, 0, w, h, 18, 18);
                if (cachedImage != null) {
                    g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, 18, 18));
                    g2.drawImage(cachedImage, 0, 0, w, h, this);
                }
                g2.dispose();
            }
        };
        imgPanel.setOpaque(false);
        imgPanel.setPreferredSize(new java.awt.Dimension(175, 115));

        javax.swing.JLabel lblEmoji = new javax.swing.JLabel(p.getEmoji(), javax.swing.SwingConstants.CENTER);
        lblEmoji.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 52));
        if (cachedImage == null) {
            imgPanel.add(lblEmoji);
        }

        // ── INFO ──────────────────────────────────────────────────────
        javax.swing.JPanel info = new javax.swing.JPanel();
        info.setLayout(new javax.swing.BoxLayout(info, javax.swing.BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        // NOMBRE DE LAS TARJETAS
        javax.swing.JLabel lblNombre = new javax.swing.JLabel(
                "<html><body style='width:130px;font-size:12px;font-weight:bold;color:#281905'>"
                + p.getNombre() + "</body></html>"
        );
        lblNombre.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        javax.swing.JLabel lblDesc = new javax.swing.JLabel(
                "<html><body style='width:155px;font-size:10px;color:#9A7A52'>"
                + p.getDescripcion() + "</body></html>"
        );
        lblDesc.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        javax.swing.JLabel lblPrecio = new javax.swing.JLabel(String.format("S/ %.2f", p.getPrecio()));
        lblPrecio.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        lblPrecio.setForeground(CAFE_CLARO);
        lblPrecio.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        // ── CONTADOR: [−] [campo] [+] ─────────────────────────────────
        final javax.swing.JTextField txtCantidad = new javax.swing.JTextField("1", 3);

        javax.swing.JPanel contador = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 3, 0));
        contador.setOpaque(false);
        contador.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        javax.swing.JLabel lblCant = new javax.swing.JLabel("Cant:");
        lblCant.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        lblCant.setForeground(new java.awt.Color(140, 100, 50));

        javax.swing.JButton menos = new javax.swing.JButton("-") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? new java.awt.Color(140, 95, 15) : getModel().isRollover() ? DORADO_CLARO : DORADO);
                g2.fillOval(0, 0, getWidth(), getHeight());
                // Dibujar el texto encima
                g2.setColor(java.awt.Color.WHITE);
                g2.setFont(getFont());
                java.awt.FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        //Botón menos
        menos.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
        menos.setForeground(java.awt.Color.WHITE);
        menos.setFocusPainted(false);
        menos.setBorderPainted(false);
        menos.setContentAreaFilled(false);
        menos.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        // tamaño de los circulos
        menos.setPreferredSize(new java.awt.Dimension(30, 30));
        //centrado
        menos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        menos.setVerticalAlignment(javax.swing.SwingConstants.CENTER);

        txtCantidad.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        txtCantidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCantidad.setPreferredSize(new java.awt.Dimension(36, 26));
        txtCantidad.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(DORADO_BORDE, 1),
                javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)
        ));
        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });

        javax.swing.JButton mas = new javax.swing.JButton("+") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? new java.awt.Color(140, 95, 15) : getModel().isRollover() ? DORADO_CLARO : DORADO);
                g2.fillOval(0, 0, getWidth(), getHeight());
                // Dibujar el texto encima
                g2.setColor(java.awt.Color.WHITE);
                g2.setFont(getFont());
                java.awt.FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };

        //Boton más
        mas.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
        mas.setForeground(java.awt.Color.WHITE);
        mas.setFocusPainted(false);
        mas.setBorderPainted(false);
        mas.setContentAreaFilled(false);
        mas.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        // tamaño del circulo
        mas.setPreferredSize(new java.awt.Dimension(30, 30));
        //centrado
        mas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mas.setVerticalAlignment(javax.swing.SwingConstants.CENTER);

        menos.addActionListener(e -> {
            try {
                int c = Integer.parseInt(txtCantidad.getText());
                if (c > 1) {
                    txtCantidad.setText(String.valueOf(c - 1));
                }
            } catch (NumberFormatException ex) {
                txtCantidad.setText("1");
            }
        });

        mas.addActionListener(e -> {
            try {
                int c = Integer.parseInt(txtCantidad.getText());
                txtCantidad.setText(String.valueOf(c + 1));
            } catch (NumberFormatException ex) {
                txtCantidad.setText("1");
            }
        });

        contador.add(lblCant);
        contador.add(menos);
        contador.add(txtCantidad);
        contador.add(mas);

        // ── BOTÓN AGREGAR ─────────────────────────────────────────────
        javax.swing.JButton btnAgregar = Botones.crear("+ Agregar", DORADO, DORADO_CLARO);
        btnAgregar.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        btnAgregar.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 32));
        if (!disponible) {
            tarjeta.setBackground(new java.awt.Color(215, 215, 215));
            imgPanel.setBackground(new java.awt.Color(170, 170, 170));

            lblNombre.setText("<html><body style='width:130px;font-size:12px;font-weight:bold;color:#777777'>"
                    + p.getNombre() + "</body></html>");

            lblDesc.setText("<html><body style='width:155px;font-size:10px;color:#999999'>"
                    + p.getDescripcion() + "</body></html>");

            lblPrecio.setForeground(new java.awt.Color(120, 120, 120));
            lblCant.setForeground(new java.awt.Color(150, 150, 150));

            menos.setEnabled(false);
            mas.setEnabled(false);
            txtCantidad.setEnabled(false);

            btnAgregar.setEnabled(false);
            btnAgregar.setText("No disponible");
            btnAgregar.setForeground(new java.awt.Color(120, 120, 120));
        }

        btnAgregar.addActionListener(e -> {

            if (!disponible) {
                return;
            }

            if (ventanaPago == null) {
                ventanaPago = new listaMenu(this);
            }

            int cantidad;

            try {
                cantidad = Integer.parseInt(txtCantidad.getText());

                if (cantidad < 1) {
                    cantidad = 1;
                } else if (cantidad > 999) {
                    cantidad = 999;
                }

            } catch (NumberFormatException ex) {
                cantidad = 1;
            }

            if (cantidad > p.getStock()) {
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        "Stock insuficiente. Solo hay " + p.getStock() + " unidades disponibles.",
                        "Stock agotado", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (int i = 0; i < cantidad; i++) {
                ventanaPago.agregarProducto(new Producto(p.getNombre(), p.getPrecio()));
            }

            txtCantidad.setText("1");

            actualizarBadgeCarrito(cantidad);
        });

        info.add(javax.swing.Box.createVerticalGlue());
        info.add(lblNombre);
        info.add(javax.swing.Box.createVerticalStrut(3));
        info.add(lblDesc);
        info.add(javax.swing.Box.createVerticalStrut(8));
        info.add(lblPrecio);
        info.add(javax.swing.Box.createVerticalStrut(8));
        info.add(contador);
        info.add(javax.swing.Box.createVerticalStrut(4));
        info.add(btnAgregar);
        info.add(javax.swing.Box.createVerticalGlue());

        tarjeta.add(imgPanel, java.awt.BorderLayout.NORTH);
        tarjeta.add(info, java.awt.BorderLayout.CENTER);
        return tarjeta;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Busqueda;
    private javax.swing.JPanel Café;
    private javax.swing.JButton CerrarSesion;
    private javax.swing.JButton Compra;
    private javax.swing.JPanel Emparedados;
    private javax.swing.JTabbedPane JTabbedPane;
    private javax.swing.JPanel Postres;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    // End of variables declaration//GEN-END:variables

}
