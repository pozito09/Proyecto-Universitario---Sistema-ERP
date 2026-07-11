package Administrativo;

import static Clases.Colores.*;

import Dashboard.GerencialV2;
import Ventas.Ventas;
import Productos.GestionProductos;
import Inventario.GestionInventario;
import Compras.GestionCompras;
import Caja.ControlCaja;
import Proveedores.GestionProveedores;
import RRHH.RRHH;
import Finanzas.ModuloFinanciero;
import Reportes.Reportes;
import Acceso.Login;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PanelSelector extends JFrame {

    private static final Color CARD_BG = new Color(255, 255, 245);
    private static final Color CARD_BORDER = new Color(200, 140, 40);

    public PanelSelector() {
        setTitle("CAFE COMETA - SELECCIONAR ÁREA");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 550));

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(FONDO);

        // ── HEADER ──
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(0, 85));
        header.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel titulo = new JLabel("PANEL DE CONTROL");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Seleccione el área a gestionar");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(245, 220, 180));

        JPanel headerText = new JPanel(new BorderLayout());
        headerText.setOpaque(false);
        headerText.add(titulo, BorderLayout.NORTH);
        headerText.add(subtitulo, BorderLayout.SOUTH);

        JButton btnSalir = new JButton("CERRAR SESIÓN");
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSalir.setBackground(DORADO);
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setBorderPainted(false);
        btnSalir.setFocusPainted(false);
        btnSalir.setPreferredSize(new Dimension(160, 40));
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> {
            dispose();
            Login login = new Login();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
        });

        header.add(headerText, BorderLayout.WEST);
        header.add(btnSalir, BorderLayout.EAST);

        main.add(header, BorderLayout.NORTH);

        // ── TARJETAS ──
        JPanel grid = new JPanel(new GridLayout(0, 4, 18, 18));
        grid.setBackground(FONDO);
        grid.setBorder(new EmptyBorder(30, 35, 30, 35));

        AreaCard[] areas = {
            new AreaCard("Dashboard", "Indicadores generales", DORADO, GerencialV2::abrir),
            new AreaCard("Ventas",      "Registro y consulta de ventas", DORADO_CLARO, Ventas::abrir),
            new AreaCard("Productos",   "Catálogo y precios", CAFE_CLARO, GestionProductos::abrir),
            new AreaCard("Inventario",  "Stock y alertas", CAFE, GestionInventario::abrir),
            new AreaCard("Compras",     "Órdenes a proveedores", CAFE, GestionCompras::abrir),
            new AreaCard("Proveedores", "Gestión de proveedores", DORADO, GestionProveedores::abrir),
            new AreaCard("Caja",        "Control de caja diario", VERDE, ControlCaja::abrir),
            new AreaCard("Empresa",     "Datos del negocio", DORADO, DatosEmpresa::abrir),
            new AreaCard("RRHH", "Empleados y usuarios", DORADO_CLARO, RRHH::abrir),
            new AreaCard("Finanzas",    "Ingresos y egresos", CAFE_CLARO, ModuloFinanciero::abrir),
            new AreaCard("Reportes",    "Estadísticas y gráficos", CAFE, Reportes::abrir),
        };

        for (AreaCard c : areas) {
            grid.add(c);
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(FONDO);
        main.add(scroll, BorderLayout.CENTER);

        add(main);
    }

    // ─────────────────────────────────────────────────────────────────
    //  TARJETA DE ÁREA
    // ─────────────────────────────────────────────────────────────────
    private static class AreaCard extends JPanel {

        private final Color accent;

        AreaCard(String nombre, String descripcion, Color accent, Runnable onOpen) {
            this.accent = accent;

            setLayout(new BorderLayout());
            setBackground(CARD_BG);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(CARD_BORDER, 2),
                    new EmptyBorder(12, 15, 12, 15)));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Título
            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblNombre.setForeground(accent);

            // Descripción
            JLabel lblDesc = new JLabel(descripcion);
            lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblDesc.setForeground(new Color(100, 80, 60));

            // Botón
            JButton btnAbrir = new JButton("ABRIR");
            btnAbrir.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btnAbrir.setBackground(accent);
            btnAbrir.setForeground(Color.WHITE);
            btnAbrir.setBorderPainted(false);
            btnAbrir.setFocusPainted(false);
            btnAbrir.setPreferredSize(new Dimension(100, 35));
            btnAbrir.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnAbrir.addActionListener(e -> onOpen.run());

            // Layout interno
            JPanel info = new JPanel(new BorderLayout(5, 5));
            info.setOpaque(false);
            info.add(lblNombre, BorderLayout.NORTH);
            info.add(lblDesc, BorderLayout.CENTER);

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            bottom.setOpaque(false);
            bottom.add(btnAbrir);

            add(info, BorderLayout.CENTER);
            add(bottom, BorderLayout.SOUTH);

            // Click en la tarjeta también abre
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    onOpen.run();
                }
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    setBackground(new Color(255, 255, 230));
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    setBackground(CARD_BG);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(accent);
            g2.fillRect(0, 0, 6, getHeight());
            g2.dispose();
        }
    }
}
