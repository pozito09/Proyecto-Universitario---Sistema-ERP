package Dashboard;

import static Clases.Colores.*;
import Clases.ConexionBD;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class GerencialV2 extends JFrame {

    private JLabel lblVentas, lblIngresos, lblProductos, lblProveedores;
    private JTable tablaVentas, tablaProductos;
    private DefaultTableModel modeloVentas, modeloProductos;
    private JPanel panelGraficoBarras, panelGraficoCircular;
    private Timer timerAutoRefresh;
    private JLabel lblFechaActualizacion;
    private JButton btnActualizar;

    public GerencialV2() {
        setTitle("CAFÉ COMETA - DASHBOARD GERENCIAL");
        setSize(1500, 850);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        cargarIndicadores();
        cargarUltimasVentas();
        cargarTopProductos();
        cargarGraficoBarras();
        cargarGraficoCircular();
        iniciarActualizacionAutomatica();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (timerAutoRefresh != null && timerAutoRefresh.isRunning()) {
                    timerAutoRefresh.stop();
                }
            }
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(crearPanelDashboard(), BorderLayout.CENTER);
    }

    private JPanel crearPanelDashboard() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(100, 70));

        JLabel titulo = new JLabel("DASHBOARD GERENCIAL");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titulo.setBorder(new EmptyBorder(10, 20, 10, 20));

        btnActualizar = new JButton("ACTUALIZAR");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        header.add(titulo, BorderLayout.WEST);
        header.add(btnActualizar, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(FONDO);

        JPanel panelKPIs = new JPanel(new GridLayout(1, 4, 15, 15));
        panelKPIs.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelKPIs.setBackground(FONDO);
        lblVentas = crearTarjeta(panelKPIs, "VENTAS", DORADO);
        lblIngresos = crearTarjeta(panelKPIs, "INGRESOS", DORADO_CLARO);
        lblProductos = crearTarjeta(panelKPIs, "PRODUCTOS", CAFE_CLARO);
        lblProveedores = crearTarjeta(panelKPIs, "PROVEEDORES", TEXTO);
        centro.add(panelKPIs, BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new GridLayout(2, 2, 10, 10));
        cuerpo.setBorder(new EmptyBorder(10, 15, 10, 15));
        cuerpo.setBackground(FONDO);

        modeloVentas = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        modeloVentas.addColumn("ID");
        modeloVentas.addColumn("CLIENTE");
        modeloVentas.addColumn("FECHA");
        modeloVentas.addColumn("TOTAL");
        tablaVentas = new JTable(modeloVentas);
        JScrollPane spVentas = new JScrollPane(tablaVentas);
        JPanel panelVentas = new JPanel(new BorderLayout());
        panelVentas.setBorder(BorderFactory.createTitledBorder("ÚLTIMAS VENTAS"));
        panelVentas.add(spVentas, BorderLayout.CENTER);

        modeloProductos = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        modeloProductos.addColumn("PRODUCTO");
        modeloProductos.addColumn("CANTIDAD");
        tablaProductos = new JTable(modeloProductos);
        JScrollPane spProductos = new JScrollPane(tablaProductos);
        JPanel panelProductos = new JPanel(new BorderLayout());
        panelProductos.setBorder(BorderFactory.createTitledBorder("TOP PRODUCTOS"));
        panelProductos.add(spProductos, BorderLayout.CENTER);

        panelGraficoBarras = new JPanel(new BorderLayout());
        panelGraficoBarras.setBorder(BorderFactory.createTitledBorder("VENTAS POR PRODUCTO"));

        panelGraficoCircular = new JPanel(new BorderLayout());
        panelGraficoCircular.setBorder(BorderFactory.createTitledBorder("PARTICIPACIÓN DE VENTAS"));

        cuerpo.add(panelVentas);
        cuerpo.add(panelProductos);
        cuerpo.add(panelGraficoBarras);
        cuerpo.add(panelGraficoCircular);
        centro.add(cuerpo, BorderLayout.CENTER);
        panel.add(centro, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        lblFechaActualizacion = new JLabel("Última actualización");
        lblFechaActualizacion.setBorder(new EmptyBorder(5, 15, 5, 15));
        footer.add(lblFechaActualizacion, BorderLayout.WEST);
        panel.add(footer, BorderLayout.SOUTH);

        btnActualizar.addActionListener(e -> {
            cargarIndicadores();
            cargarUltimasVentas();
            cargarTopProductos();
            cargarGraficoBarras();
            cargarGraficoCircular();
        });

        return panel;
    }

    private JLabel crearTarjeta(JPanel contenedor, String titulo, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblValor = new JLabel("0");
        lblValor.setForeground(Color.WHITE);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalGlue());
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblValor);
        panel.add(Box.createVerticalGlue());
        contenedor.add(panel);
        return lblValor;
    }

    private void cargarIndicadores() {
        try (Connection con = ConexionBD.conectar()) {
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) total FROM pedidos WHERE estado_pago = 'Pagado'")) {
                if (rs.next()) lblVentas.setText(rs.getString("total"));
            }
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT SUM(total) total FROM pedidos WHERE estado_pago = 'Pagado'")) {
                if (rs.next()) lblIngresos.setText("S/ " + String.format("%,.2f", rs.getDouble("total")));
            }
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) total FROM productos")) {
                if (rs.next()) lblProductos.setText(rs.getString("total"));
            }
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) total FROM proveedores")) {
                if (rs.next()) lblProveedores.setText(rs.getString("total"));
            }
            lblFechaActualizacion.setText("Última actualización: " + new java.util.Date());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void cargarUltimasVentas() {
        try (Connection con = ConexionBD.conectar()) {
            modeloVentas.setRowCount(0);
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT id, nombre_cliente, fecha, total FROM pedidos WHERE estado_pago = 'Pagado' ORDER BY id DESC LIMIT 10")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        modeloVentas.addRow(new Object[]{rs.getInt("id"), rs.getString("nombre_cliente"), rs.getTimestamp("fecha"), rs.getDouble("total")});
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void cargarTopProductos() {
        try (Connection con = ConexionBD.conectar()) {
            modeloProductos.setRowCount(0);
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT dp.nombre_producto, SUM(dp.cantidad) total "
                    + "FROM detalle_pedido dp INNER JOIN pedidos p ON dp.id_pedido = p.id "
                    + "WHERE p.estado_pago = 'Pagado' "
                    + "GROUP BY dp.nombre_producto ORDER BY total DESC LIMIT 10")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        modeloProductos.addRow(new Object[]{rs.getString("nombre_producto"), rs.getInt("total")});
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void cargarGraficoBarras() {
        try (Connection con = ConexionBD.conectar()) {
            panelGraficoBarras.removeAll();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT dp.nombre_producto, SUM(dp.cantidad) total "
                    + "FROM detalle_pedido dp INNER JOIN pedidos p ON dp.id_pedido = p.id "
                    + "WHERE p.estado_pago = 'Pagado' "
                    + "GROUP BY dp.nombre_producto ORDER BY total DESC LIMIT 10")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        dataset.addValue(rs.getInt("total"), "Ventas", rs.getString("nombre_producto"));
                    }
                }
            }
            JFreeChart chart = ChartFactory.createBarChart("Productos Más Vendidos", "Producto", "Cantidad", dataset);
            panelGraficoBarras.add(new ChartPanel(chart), BorderLayout.CENTER);
            panelGraficoBarras.revalidate();
            panelGraficoBarras.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void cargarGraficoCircular() {
        try (Connection con = ConexionBD.conectar()) {
            panelGraficoCircular.removeAll();
            DefaultPieDataset dataset = new DefaultPieDataset();
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT dp.nombre_producto, SUM(dp.cantidad) total "
                    + "FROM detalle_pedido dp INNER JOIN pedidos p ON dp.id_pedido = p.id "
                    + "WHERE p.estado_pago = 'Pagado' "
                    + "GROUP BY dp.nombre_producto ORDER BY total DESC LIMIT 10")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        dataset.setValue(rs.getString("nombre_producto"), rs.getInt("total"));
                    }
                }
            }
            JFreeChart chart = ChartFactory.createPieChart("Participación de Ventas", dataset, true, true, false);
            panelGraficoCircular.add(new ChartPanel(chart), BorderLayout.CENTER);
            panelGraficoCircular.revalidate();
            panelGraficoCircular.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void iniciarActualizacionAutomatica() {
        timerAutoRefresh = new Timer(30000, e -> {
            cargarIndicadores();
            cargarUltimasVentas();
            cargarTopProductos();
            cargarGraficoBarras();
            cargarGraficoCircular();
        });
        timerAutoRefresh.start();
    }

    private static GerencialV2 instancia;

    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new GerencialV2();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
