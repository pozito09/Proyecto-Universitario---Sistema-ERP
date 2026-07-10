package Finanzas;

import static Clases.Colores.*;
import Clases.ConexionBD;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ModuloFinanciero extends JFrame {

    private JLabel lblIngresos;
    private JLabel lblGastos;
    private JLabel lblMargenBruto;

    private DefaultTableModel modelo;

    public ModuloFinanciero() {

        setTitle("CAFÉ COMETA - FINANZAS");
        setSize(1200, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(100, 70));

        JLabel titulo = new JLabel("MÓDULO FINANCIERO");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setBorder(new EmptyBorder(10,20,10,20));

        header.add(titulo, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout());

        //---------------------------------
        // TARJETAS KPI
        //---------------------------------

        JPanel tarjetas = new JPanel(
                new GridLayout(1,3,15,15));

        tarjetas.setBorder(
                new EmptyBorder(15,15,15,15));

        lblIngresos = new JLabel("S/ 0.00");
        lblGastos = new JLabel("S/ 0.00");
        lblMargenBruto = new JLabel("S/ 0.00");

        tarjetas.add(
                crearTarjeta(
                        "INGRESOS",
                        lblIngresos,
                        DORADO_CLARO));

        tarjetas.add(
                crearTarjeta(
                        "GASTOS",
                        lblGastos,
                        ROJO));

        tarjetas.add(
                crearTarjeta(
                        "MARGEN BRUTO",
                        lblMargenBruto,
                        DORADO));

        centro.add(tarjetas,
                BorderLayout.NORTH);

        //---------------------------------
        // TABLA
        //---------------------------------

        String columnas[] = {
            "Fecha",
            "Descripción",
            "Ingreso",
            "Egreso"
        };

        modelo = new DefaultTableModel(
                columnas,
                0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabla = new JTable(modelo);

        tabla.setRowHeight(28);
        tabla.getTableHeader().setBackground(CABECERA_TABLA);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(SELECCION_TABLA);
        tabla.setGridColor(GRILLA_TABLA);

        JScrollPane scroll =
                new JScrollPane(tabla);

        centro.add(scroll,
                BorderLayout.CENTER);

        add(centro,
                BorderLayout.CENTER);

        //---------------------------------
        // BOTONES
        //---------------------------------

        JPanel botones = new JPanel();

        JButton btnActualizar =
                new JButton("Actualizar");

        btnActualizar.addActionListener(e -> {
            cargarDatos();
        });

        botones.add(btnActualizar);

        add(botones,
                BorderLayout.SOUTH);

        cargarDatos();
    }

    private JPanel crearTarjeta(
            String titulo,
            JLabel lblValor,
            Color color){

        JPanel panel = new JPanel();

        panel.setBackground(color);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS));

        JLabel lblTitulo =
                new JLabel(titulo);

        lblTitulo.setForeground(Color.WHITE);

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18));

        lblTitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        lblValor.setForeground(
                Color.WHITE);

        lblValor.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24));

        lblValor.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblValor);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void cargarDatos() {

        try (Connection cn = ConexionBD.conectar()) {

            modelo.setRowCount(0);

            //---------------------------------
            // INGRESOS (PEDIDOS)
            //---------------------------------

            double ingresos = 0;

            String sqlIngresos =
                    "SELECT SUM(total) total FROM pedidos WHERE pagado = TRUE";

            try (PreparedStatement ps1 = cn.prepareStatement(sqlIngresos);
                 ResultSet rs1 = ps1.executeQuery()) {
                if(rs1.next()) {
                    ingresos = rs1.getDouble("total");
                }
            }

            //---------------------------------
            // GASTOS (COMPRAS)
            //---------------------------------

            double gastos = 0;

            String sqlGastos =
                    "SELECT SUM(total) total FROM compras WHERE estado != 'Anulado'";

            try (PreparedStatement ps2 = cn.prepareStatement(sqlGastos);
                 ResultSet rs2 = ps2.executeQuery()) {
                if(rs2.next()) {
                    gastos = rs2.getDouble("total");
                }
            }

            //---------------------------------
            // KPI
            //---------------------------------

            double margenBruto =
                    ingresos - gastos;

            lblIngresos.setText(
                    "S/ " + String.format("%.2f", ingresos));

            lblGastos.setText(
                    "S/ " + String.format("%.2f", gastos));

            lblMargenBruto.setText(
                    "S/ " + String.format("%.2f", margenBruto));

            //---------------------------------
            // MOVIMIENTOS DE VENTAS
            //---------------------------------

            String sqlVentas =
                    "SELECT fecha,total FROM pedidos WHERE pagado = TRUE ORDER BY fecha DESC LIMIT 10";

            try (PreparedStatement ps3 = cn.prepareStatement(sqlVentas);
                 ResultSet rs3 = ps3.executeQuery()) {
                while(rs3.next()) {
                    modelo.addRow(new Object[]{
                        rs3.getString("fecha"),
                        "Venta",
                        rs3.getDouble("total"),
                        "-"
                    });
                }
            }

            //---------------------------------
            // MOVIMIENTOS DE COMPRAS
            //---------------------------------

            String sqlCompras =
                    "SELECT fecha,total FROM compras WHERE estado != 'Anulado' ORDER BY fecha DESC LIMIT 10";

            try (PreparedStatement ps4 = cn.prepareStatement(sqlCompras);
                 ResultSet rs4 = ps4.executeQuery()) {
                while(rs4.next()) {
                    modelo.addRow(new Object[]{
                        rs4.getString("fecha"),
                        "Compra",
                        "-",
                        rs4.getDouble("total")
                    });
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage());
        }
    }

    private static ModuloFinanciero instancia;

    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new ModuloFinanciero();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
