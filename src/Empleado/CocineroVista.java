package Empleado;

import static Clases.Colores.*;
import Acceso.Login;
import Clases.ConexionBD;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CocineroVista extends JFrame {

    private JTable tablaPedidos;
    private JTable tablaDetalle;

    private DefaultTableModel modeloPedidos;
    private DefaultTableModel modeloDetalle;

    private JButton btnPreparando;
    private JButton btnListo;
    private JButton btnCerrarSesion;
    private JLabel lblTitulo;

    private Timer timer;

    public CocineroVista() {
        initComponents();
        cargarPedidos();

        timer = new Timer(5000, e -> cargarPedidos());
        timer.start();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
            }
        });
    }

    private void initComponents() {

        setTitle("CAFÉ COMETA - PANEL DE COCINERO");
        setSize(1000, 700);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(FONDO);

        lblTitulo = new JLabel("PEDIDOS DE COCINA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel lblUsuario = new JLabel("Cocinero: " + Clases.GuardarSesion.nombreCompleto(), SwingConstants.RIGHT);
        lblUsuario.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblUsuario.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(FONDO);
        panelNorte.add(lblTitulo, BorderLayout.CENTER);
        panelNorte.add(lblUsuario, BorderLayout.SOUTH);

        principal.add(panelNorte, BorderLayout.NORTH);

        modeloPedidos = new DefaultTableModel(
                new String[]{
                    "ID",
                    "Cliente",
                    "Fecha",
                    "Estado 1 (Pago)",
                    "Estado 2 (Prep.)",
                    "Creado por"
                }, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaPedidos = new JTable(modeloPedidos);
        tablaPedidos.getTableHeader().setBackground(CABECERA_TABLA);
        tablaPedidos.getTableHeader().setForeground(Color.WHITE);
        tablaPedidos.setSelectionBackground(SELECCION_TABLA);
        tablaPedidos.setGridColor(GRILLA_TABLA);

        JScrollPane spPedidos = new JScrollPane(tablaPedidos);

        modeloDetalle = new DefaultTableModel(
                new String[]{
                    "Producto",
                    "Cantidad",
                    "Precio",
                    "Subtotal"
                }, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaDetalle = new JTable(modeloDetalle);
        tablaDetalle.getTableHeader().setBackground(CABECERA_TABLA);
        tablaDetalle.getTableHeader().setForeground(Color.WHITE);
        tablaDetalle.setSelectionBackground(SELECCION_TABLA);
        tablaDetalle.setGridColor(GRILLA_TABLA);

        JScrollPane spDetalle = new JScrollPane(tablaDetalle);

        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                spPedidos,
                spDetalle
        );

        split.setDividerLocation(300);

        principal.add(split, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new BorderLayout());
        panelBotones.setBackground(FONDO);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelIzquierdo.setBackground(FONDO);
        btnPreparando = new JButton("PREPARANDO");
        btnPreparando.setBackground(DORADO);
        btnPreparando.setForeground(Color.WHITE);
        btnPreparando.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPreparando.setFocusPainted(false);
        btnPreparando.setBorderPainted(false);
        btnPreparando.setContentAreaFilled(false);
        btnPreparando.setOpaque(true);
        btnPreparando.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnListo = new JButton("LISTO");
        btnListo.setBackground(VERDE);
        btnListo.setForeground(Color.WHITE);
        btnListo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnListo.setFocusPainted(false);
        btnListo.setBorderPainted(false);
        btnListo.setContentAreaFilled(false);
        btnListo.setOpaque(true);
        btnListo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panelIzquierdo.add(btnPreparando);
        panelIzquierdo.add(btnListo);

        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelDerecho.setBackground(FONDO);
        btnCerrarSesion = new JButton("CERRAR SESIÓN");
        btnCerrarSesion.setBackground(CAFE);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setContentAreaFilled(false);
        btnCerrarSesion.setOpaque(true);
        btnCerrarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panelDerecho.add(btnCerrarSesion);

        panelBotones.add(panelIzquierdo, BorderLayout.WEST);
        panelBotones.add(panelDerecho, BorderLayout.EAST);

        principal.add(panelBotones, BorderLayout.SOUTH);

        add(principal);

        tablaPedidos.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int fila = tablaPedidos.getSelectedRow();

                if (fila != -1) {

                    int idPedido = Integer.parseInt(
                            tablaPedidos.getValueAt(fila, 0).toString()
                    );

                    cargarDetalle(idPedido);
                }
            }
        });

        btnPreparando.addActionListener(e -> actualizarEstado("Preparando"));

        btnListo.addActionListener(e -> actualizarEstado("Listo"));

        btnCerrarSesion.addActionListener(e -> {
            dispose();
            Login login = new Login();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
        });
    }

    private void cargarPedidos() {

        // Guardar selección actual
        int filaSeleccionada = tablaPedidos.getSelectedRow();
        int idSeleccionado = -1;
        if (filaSeleccionada != -1) {
            idSeleccionado = Integer.parseInt(tablaPedidos.getValueAt(filaSeleccionada, 0).toString());
        }

        modeloPedidos.setRowCount(0);

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT id, nombre_cliente, fecha, estado_cocina, estado_pago, modificado, "
                     + "COALESCE(nombre_usuario_crea, '') AS creador "
                + "FROM pedidos "
                + "WHERE estado_cocina != 'Listo' AND estado_pago != 'Anulado' "
                + "ORDER BY id ASC");
             ResultSet rs = ps.executeQuery()) {

            int fila = 0;
            boolean encontrado = false;
            while (rs.next()) {

                String estadoPago = rs.getString("estado_pago");

                String estadoPrep = rs.getString("estado_cocina");
                if (rs.getBoolean("modificado")) {
                    estadoPrep = estadoPrep + " (Modificado)";
                }

                modeloPedidos.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre_cliente"),
                    rs.getTimestamp("fecha"),
                    estadoPago,
                    estadoPrep,
                    rs.getString("creador")
                });

                if (rs.getInt("id") == idSeleccionado) {
                    tablaPedidos.setRowSelectionInterval(fila, fila);
                    encontrado = true;
                }
                fila++;
            }

            if (!encontrado) {
                modeloDetalle.setRowCount(0);
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this, "Error al cargar pedidos:\n" + ex.getMessage());
        }
    }

    private void cargarDetalle(int idPedido) {

        modeloDetalle.setRowCount(0);

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT nombre_producto, cantidad, precio, subtotal "
                     + "FROM detalle_pedido "
                     + "WHERE id_pedido = ?")) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    modeloDetalle.addRow(new Object[]{
                        rs.getString("nombre_producto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio"),
                        rs.getDouble("subtotal")
                    });
                }
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this, "Error al cargar detalle:\n" + ex.getMessage());
        }
    }

    private void actualizarEstado(String estado) {

        int fila = tablaPedidos.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un pedido"
            );

            return;
        }

        int idPedido = Integer.parseInt(
                tablaPedidos.getValueAt(fila, 0).toString()
        );

        String estadoActual = modeloPedidos.getValueAt(fila, 4).toString();
        // Quitar sufijo "(Modificado)" para comparar
        String estadoBase = estadoActual.replace(" (Modificado)", "");

        if ("Listo".equals(estadoBase)) {
            JOptionPane.showMessageDialog(this, "Este pedido ya está listo.");
            return;
        }

        if ("Preparando".equals(estado)) {
            if (!"Pendiente".equals(estadoBase)) {
                JOptionPane.showMessageDialog(this, "Solo se puede marcar Preparando desde Pendiente.");
                return;
            }
            if ("Preparando".equals(estadoBase)) {
                JOptionPane.showMessageDialog(this, "El pedido ya está en preparación.");
                return;
            }
        } else {
            if (!"Preparando".equals(estadoBase)) {
                JOptionPane.showMessageDialog(this, "Solo se puede marcar Listo desde Preparando.");
                return;
            }
        }

        String origen = "Preparando".equals(estado) ? estadoBase : "Preparando";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE pedidos "
                     + "SET estado_cocina=?, id_usuario_prepara=?, nombre_usuario_prepara=?, modificado=FALSE "
                     + "WHERE id=? AND estado_cocina=?")) {

            ps.setString(1, estado);
            ps.setInt(2, Clases.GuardarSesion.id);
            ps.setString(3, Clases.GuardarSesion.nombre);
            ps.setInt(4, idPedido);
            ps.setString(5, origen);

            int filas = ps.executeUpdate();

            if (filas == 0) {
                JOptionPane.showMessageDialog(this,
                        "Otro cocinero ya actualizó este pedido. Recargando...");
            }

            cargarPedidos();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this, "Error al actualizar estado:\n" + ex.getMessage());
        }
    }
}
