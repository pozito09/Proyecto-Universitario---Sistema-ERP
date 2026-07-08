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

    public CocineroVista() {
        initComponents();
        cargarPedidos();

        Timer timer = new Timer(5000, e -> cargarPedidos());
        timer.start();
    }

    private void initComponents() {

        setTitle("CAFECOMETA - PANEL DE COCINA");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(FONDO);

        lblTitulo = new JLabel("PEDIDOS DE COCINA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));

        JLabel lblUsuario = new JLabel("Cocinero: " + Clases.Sesion.nombre, SwingConstants.RIGHT);
        lblUsuario.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblUsuario.setBorder(BorderFactory.createEmptyBorder(15,10,5,10));

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
                    "Estado",
                    "Creado por"
                },0
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
                },0
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

        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panelIzquierdo.setBackground(FONDO);
        btnPreparando = new JButton("PREPARANDO");
        btnListo = new JButton("LISTO");
        panelIzquierdo.add(btnPreparando);
        panelIzquierdo.add(btnListo);

        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
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

            if(!e.getValueIsAdjusting()){

                int fila = tablaPedidos.getSelectedRow();

                if(fila != -1){

                    int idPedido = Integer.parseInt(
                            tablaPedidos.getValueAt(fila,0).toString()
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

    private void cargarPedidos(){

        modeloPedidos.setRowCount(0);

        try{

            Connection con = ConexionBD.conectar();

            String sql =
                    "SELECT id,nombre_cliente,fecha,estado, "
                    + "COALESCE(nombre_usuario_crea,'') AS creador "
                    + "FROM pedidos "
                    + "WHERE estado NOT IN ('Listo','Completado') "
                    + "ORDER BY id ASC";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                modeloPedidos.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre_cliente"),
                    rs.getTimestamp("fecha"),
                    rs.getString("estado"),
                    rs.getString("creador")
                });
            }

        }catch(Exception ex){

            JOptionPane.showMessageDialog(this, "Error al cargar pedidos:\n" + ex.getMessage());
        }
    }

    private void cargarDetalle(int idPedido){

        modeloDetalle.setRowCount(0);

        try{

            Connection con = ConexionBD.conectar();

            String sql =
                    "SELECT nombre_producto,cantidad,precio,subtotal "
                    + "FROM detalle_pedido "
                    + "WHERE id_pedido=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1,idPedido);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                modeloDetalle.addRow(new Object[]{
                    rs.getString("nombre_producto"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio"),
                    rs.getDouble("subtotal")
                });
            }

        }catch(Exception ex){

            JOptionPane.showMessageDialog(this, "Error al cargar detalle:\n" + ex.getMessage());
        }
    }

    private void actualizarEstado(String estado){

        int fila = tablaPedidos.getSelectedRow();

        if(fila == -1){

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un pedido"
            );

            return;
        }

        int idPedido = Integer.parseInt(
                tablaPedidos.getValueAt(fila,0).toString()
        );

        try{

            Connection con = ConexionBD.conectar();

            String sql =
                    "UPDATE pedidos "
                    + "SET estado=?, id_usuario_prepara=?, nombre_usuario_prepara=? "
                    + "WHERE id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1,estado);
            ps.setInt(2, Clases.Sesion.id);
            ps.setString(3, Clases.Sesion.nombre);
            ps.setInt(4,idPedido);

            ps.executeUpdate();

            cargarPedidos();
            modeloDetalle.setRowCount(0);

        }catch(Exception ex){

            JOptionPane.showMessageDialog(this, "Error al actualizar estado:\n" + ex.getMessage());
        }
    }
}