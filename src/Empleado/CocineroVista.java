package Empleado;

import static Clases.Colores.*;
import Acceso.Login;
import Clases.Auditoria;
import Clases.ConexionBD;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// ── DESCRIPCIÓN: Panel del cocinero. Muestra pedidos pendientes y permite cambiar su estado de cocina (Pendiente -> Preparando -> Listo). ──
public class CocineroVista extends JFrame {

    private JTable tablaPedidos;
    private JTable tablaDetalle;

    private DefaultTableModel modeloPedidos;
    private DefaultTableModel modeloDetalle;

    private JButton btnPreparando;
    private JButton btnListo;
    private JButton btnAnular;
    private JButton btnCerrarSesion;
    private JLabel lblTitulo;

    private Timer timer;

    // ── DESCRIPCIÓN: Inicializa la vista, carga pedidos, y configura un timer de 5 segundos para auto-refrescar. ──
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

    // ── DESCRIPCIÓN: Construye la interfaz con tabla de pedidos, tabla de detalle, botones Preparando/Listo, y cerrar sesión. ──
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

        btnAnular = new JButton("ANULAR PEDIDO");
        btnAnular.setBackground(ROJO);
        btnAnular.setForeground(Color.WHITE);
        btnAnular.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAnular.setFocusPainted(false);
        btnAnular.setBorderPainted(false);
        btnAnular.setContentAreaFilled(false);
        btnAnular.setOpaque(true);
        btnAnular.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panelIzquierdo.add(btnAnular);

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

        btnAnular.addActionListener(e -> anularPedido());

        btnCerrarSesion.addActionListener(e -> {
            dispose();
            Login login = new Login();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
        });
    }

    // ── DESCRIPCIÓN: Carga todos los pedidos que no están Listo ni Anulados, manteniendo la selección actual. ──
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

    // ── DESCRIPCIÓN: Muestra los productos del pedido seleccionado en la tabla de detalle. ──
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

    // ── DESCRIPCIÓN: Cambia el estado de cocina del pedido seleccionado validando la transición correcta (Pendiente->Preparando, Preparando->Listo). ──
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

    // ── DESCRIPCIÓN: Anula un pedido desde cocina: revierte stock, insumos, marca como Anulado y registra en auditoría. ──
    private void anularPedido() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para anular.");
            return;
        }

        int idPedido = Integer.parseInt(tablaPedidos.getValueAt(fila, 0).toString());
        String cliente = tablaPedidos.getValueAt(fila, 1).toString();
        String estadoPago = tablaPedidos.getValueAt(fila, 3).toString();

        if ("Pagado".equals(estadoPago)) {
            JOptionPane.showMessageDialog(this, "Este pedido ya fue pagado. Anúlalo desde la vista de Ventas.");
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this,
                "¿Anular pedido #" + idPedido + " del cliente " + cliente + "?\nSe revertirá el stock de productos e insumos.",
                "Confirmar Anulación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (conf != JOptionPane.YES_OPTION) return;

        try (Connection con = ConexionBD.conectar()) {
            con.setAutoCommit(false);

            // Revertir stock de productos
            try (PreparedStatement psRevertir = con.prepareStatement(
                    "UPDATE productos SET stock = stock + ? WHERE nombre = ?")) {
                try (PreparedStatement psDetalle = con.prepareStatement(
                        "SELECT nombre_producto, cantidad FROM detalle_pedido WHERE id_pedido = ?")) {
                    psDetalle.setInt(1, idPedido);
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
                String sqlReceta = "SELECT r.id_insumo, r.cantidad FROM recetas r JOIN productos p ON r.id_producto = p.id WHERE p.nombre = ?";
                String sqlRevertirInsumo = "UPDATE insumos SET stock = stock + ? WHERE id = ?";
                try (PreparedStatement psReceta = con.prepareStatement(sqlReceta);
                     PreparedStatement psRevertirInsumo = con.prepareStatement(sqlRevertirInsumo)) {
                    try (PreparedStatement psDetalle = con.prepareStatement(
                            "SELECT nombre_producto, cantidad FROM detalle_pedido WHERE id_pedido = ?")) {
                        psDetalle.setInt(1, idPedido);
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

            // Marcar como Anulado
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE pedidos SET estado_pago='Anulado', estado_cocina=NULL WHERE id=?")) {
                ps.setInt(1, idPedido);
                ps.executeUpdate();
            }

            con.commit();
            Auditoria.anular("pedidos", idPedido, "Pedido #" + idPedido + " anulado desde cocina por " + Clases.GuardarSesion.nombreCompleto());
            cargarPedidos();
            modeloDetalle.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Pedido #" + idPedido + " anulado correctamente.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al anular pedido:\n" + ex.getMessage());
        }
    }
}
