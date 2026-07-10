package Caja;

import static Clases.Colores.*;
import Clases.Botones;
import Clases.ConexionBD;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ControlCaja extends JFrame {

    private JLabel lblInicial, lblVentas, lblEsperado, lblDiferencia;
    private JLabel lblFechaActualizacion;
    private JButton btnAbrir, btnCerrar, btnActualizar;
    private JTable tabla;
    private DefaultTableModel modelo;

    public ControlCaja() {
        setTitle("CAFÉ COMETA - CAJA");
        setSize(1000, 650);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        add(crearHeader(), BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(FONDO);
        centro.add(crearKPIs(), BorderLayout.NORTH);
        centro.add(crearTabla(), BorderLayout.CENTER);

        add(centro, BorderLayout.CENTER);
        add(crearFooter(), BorderLayout.SOUTH);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(100, 70));

        JLabel titulo = new JLabel("CONTROL DE CAJA");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setBorder(new EmptyBorder(10, 20, 10, 20));
        header.add(titulo, BorderLayout.WEST);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 16));
        botones.setOpaque(false);

        btnAbrir = Botones.crear("Abrir Caja", VERDE);
        btnAbrir.addActionListener(e -> abrirCaja());
        botones.add(btnAbrir);

        btnCerrar = Botones.crear("Cerrar Caja", ROJO, ROJO_HOVER);
        btnCerrar.addActionListener(e -> cerrarCaja());
        botones.add(btnCerrar);

        header.add(botones, BorderLayout.EAST);
        return header;
    }

    private JPanel crearKPIs() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 5, 15));
        panel.setBackground(FONDO);

        lblInicial = crearTarjeta(panel, "MONTO INICIAL", DORADO);
        lblVentas = crearTarjeta(panel, "VENTAS DEL DÍA", DORADO_CLARO);
        lblEsperado = crearTarjeta(panel, "MONTO ESPERADO", CAFE_CLARO);
        lblDiferencia = crearTarjeta(panel, "DIFERENCIA", TEXTO);

        return panel;
    }

    private JLabel crearTarjeta(JPanel contenedor, String titulo, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValor = new JLabel("S/ 0.00");
        lblValor.setForeground(Color.WHITE);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblValor);
        panel.add(Box.createVerticalGlue());

        contenedor.add(panel);
        return lblValor;
    }

    private JScrollPane crearTabla() {
        modelo = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        modelo.setColumnIdentifiers(new Object[]{"ID", "Apertura", "Cierre", "Inicial", "Final", "Ventas", "Diferencia", "Estado"});
        tabla = new JTable(modelo);
        tabla.getTableHeader().setBackground(CABECERA_TABLA);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setRowHeight(24);
        tabla.setSelectionBackground(SELECCION_TABLA);
        tabla.setGridColor(GRILLA_TABLA);
        return new JScrollPane(tabla);
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(FONDO);
        footer.setBorder(new EmptyBorder(5, 15, 5, 15));

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnActualizar.setBackground(DORADO);
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> cargarDatos());
        footer.add(btnActualizar, BorderLayout.WEST);

        lblFechaActualizacion = new JLabel("");
        lblFechaActualizacion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFechaActualizacion.setForeground(TEXTO);
        footer.add(lblFechaActualizacion, BorderLayout.EAST);

        return footer;
    }

    // ============================================
    // DATOS
    // ============================================
    private void cargarDatos() {
        cargarKPIs();
        cargarHistorial();
        actualizarBotones();
        lblFechaActualizacion.setText("Última actualización: " + new java.util.Date());
    }

    private void cargarKPIs() {
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement()) {

            // Buscar caja abierta
            ResultSet rs = st.executeQuery(
                    "SELECT id, COALESCE(monto_inicial,0) mi, COALESCE(monto_final,0) mf "
                    + "FROM apertura_caja WHERE estado='Abierta' ORDER BY id DESC LIMIT 1 FOR UPDATE");

            if (rs.next()) {
                int cajaId = rs.getInt("id");
                double inicial = rs.getDouble("mi");
                double ventas = getVentasDelDia(con, cajaId);
                double esperado = inicial + ventas;
                lblInicial.setText("S/ " + String.format("%,.2f", inicial));
                lblVentas.setText("S/ " + String.format("%,.2f", ventas));
                lblEsperado.setText("S/ " + String.format("%,.2f", esperado));
                lblDiferencia.setText("—");
                lblDiferencia.setForeground(TEXTO);
            } else {
                lblInicial.setText("—");
                lblVentas.setText("—");
                lblEsperado.setText("—");
                lblDiferencia.setText("—");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar KPIs: " + e.getMessage());
        }
    }

    private double getVentasDelDia(Connection con, int idApertura) {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT COALESCE(SUM(total),0) total FROM pedidos WHERE id_apertura=? AND pagado = TRUE")) {
            ps.setInt(1, idApertura);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getDouble("total") : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private void cargarHistorial() {
        String sql = "SELECT id, fecha_apertura, fecha_cierre, monto_inicial, monto_final, "
                + "estado FROM apertura_caja ORDER BY id DESC";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            modelo.setRowCount(0);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                double inicial = rs.getDouble("monto_inicial");
                double ventas = getVentasPorCaja(con, id, rs.getTimestamp("fecha_apertura"), rs.getTimestamp("fecha_cierre"));
                double montoFinal = rs.getDouble("monto_final");
                boolean cerrada = rs.wasNull() || rs.getString("estado").equals("Abierta");
                String difText;
                if (!cerrada) {
                    double dif = montoFinal - (inicial + ventas);
                    difText = "S/ " + String.format("%,.2f", dif);
                } else {
                    difText = "—";
                }
                modelo.addRow(new Object[]{
                    id,
                    rs.getTimestamp("fecha_apertura"),
                    rs.getTimestamp("fecha_cierre") != null ? rs.getTimestamp("fecha_cierre") : "—",
                    "S/ " + String.format("%,.2f", inicial),
                    cerrada ? "—" : "S/ " + String.format("%,.2f", montoFinal),
                    "S/ " + String.format("%,.2f", ventas),
                    difText,
                    rs.getString("estado")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage());
        }
    }

    private double getVentasPorCaja(Connection con, int idApertura, Timestamp desde, Timestamp hasta) {
        if (desde == null) return 0;
        String sql;
        if (hasta != null) {
            sql = "SELECT COALESCE(SUM(total),0) FROM pedidos WHERE id_apertura=? AND fecha BETWEEN ? AND ? AND pagado = TRUE";
        } else {
            sql = "SELECT COALESCE(SUM(total),0) FROM pedidos WHERE id_apertura=? AND fecha >= ? AND pagado = TRUE";
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idApertura);
            ps.setTimestamp(2, desde);
            if (hasta != null) ps.setTimestamp(3, hasta);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private void actualizarBotones() {
        boolean hayAbierta = hayCajaAbierta();
        btnAbrir.setEnabled(!hayAbierta);
        btnCerrar.setEnabled(hayAbierta);
    }

    // ============================================
    // ACCIONES
    // ============================================
    private void abrirCaja() {
        String input = JOptionPane.showInputDialog(this,
                "Ingrese el monto inicial en caja:", "Abrir Caja", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.trim().isEmpty()) return;

        try {
            double monto = Double.parseDouble(input.trim());
            if (monto < 0) {
                JOptionPane.showMessageDialog(this, "El monto no puede ser negativo.");
                return;
            }

            try (Connection con = ConexionBD.conectar()) {
                con.setAutoCommit(false);
                try (Statement st = con.createStatement();
                     ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM apertura_caja WHERE estado='Abierta'")) {
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        con.rollback();
                        JOptionPane.showMessageDialog(this, "Ya existe una caja abierta. Cierre la caja actual antes de abrir una nueva.");
                        return;
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO apertura_caja(monto_inicial, estado) VALUES (?, 'Abierta')")) {
                    ps.setDouble(1, monto);
                    ps.executeUpdate();
                }
                con.commit();
            }

            JOptionPane.showMessageDialog(this, "Caja abierta con S/ " + String.format("%,.2f", monto));
            cargarDatos();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto válido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al abrir caja: " + e.getMessage());
        }
    }

    private void cerrarCaja() {
        if (!hayCajaAbierta()) {
            JOptionPane.showMessageDialog(this, "No hay una caja abierta.");
            return;
        }

        String input = JOptionPane.showInputDialog(this,
                "Ingrese el monto final contado en caja:", "Cerrar Caja", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.trim().isEmpty()) return;

        try {
            double montoFinal = Double.parseDouble(input.trim());
            if (montoFinal < 0) {
                JOptionPane.showMessageDialog(this, "El monto no puede ser negativo.");
                return;
            }

            try (Connection con = ConexionBD.conectar()) {
                // Obtener caja activa
                PreparedStatement psGet = con.prepareStatement(
                        "SELECT id, monto_inicial FROM apertura_caja WHERE estado='Abierta' ORDER BY id DESC LIMIT 1");
                ResultSet rs = psGet.executeQuery();
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "No se encontró caja abierta.");
                    return;
                }
                int id = rs.getInt("id");
                double inicial = rs.getDouble("monto_inicial");
                double ventas = getVentasDelDia(con, id);
                double esperado = inicial + ventas;
                double diferencia = montoFinal - esperado;

                // Actualizar
                PreparedStatement psUpd = con.prepareStatement(
                        "UPDATE apertura_caja SET monto_final=?, fecha_cierre=NOW(), estado='Cerrada' WHERE id=?");
                psUpd.setDouble(1, montoFinal);
                psUpd.setInt(2, id);
                psUpd.executeUpdate();

                String difMsg;
                if (Math.abs(diferencia) < 0.01) {
                    difMsg = "Cuadra perfectamente ✅";
                } else if (diferencia > 0) {
                    difMsg = "Sobra S/ " + String.format("%,.2f", diferencia) + " ⚠️";
                } else {
                    difMsg = "Falta S/ " + String.format("%,.2f", Math.abs(diferencia)) + " ❌";
                }

                JOptionPane.showMessageDialog(this,
                        "CAJA CERRADA\n"
                        + "Inicial: S/ " + String.format("%,.2f", inicial) + "\n"
                        + "Ventas: S/ " + String.format("%,.2f", ventas) + "\n"
                        + "Esperado: S/ " + String.format("%,.2f", esperado) + "\n"
                        + "Real: S/ " + String.format("%,.2f", montoFinal) + "\n"
                        + difMsg,
                        "Cierre de Caja", JOptionPane.INFORMATION_MESSAGE);
            }

            cargarDatos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cerrar caja: " + e.getMessage());
        }
    }

    // ============================================
    // UTILIDADES
    // ============================================
    public static boolean hayCajaAbierta() {
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT COUNT(*) FROM apertura_caja WHERE estado='Abierta'")) {
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static int getCajaActivaId() {
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT id FROM apertura_caja WHERE estado='Abierta' ORDER BY id DESC LIMIT 1")) {
            return rs.next() ? rs.getInt("id") : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private static ControlCaja instancia;

    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new ControlCaja();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
