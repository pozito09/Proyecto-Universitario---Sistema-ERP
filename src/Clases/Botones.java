package Clases;

import java.awt.*;
import javax.swing.*;
// ── DESCRIPCIÓN: Utilidad para crear botones redondeados con efecto de cambio de color al pasar el mouse ──
public class Botones {

    // ── DESCRIPCIÓN: Crea un botón redondeado con color de fondo y color de hover personalizado ──
    public static JButton crear(String texto, Color fondo, Color hover) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? fondo.darker() : getModel().isRollover() ? hover : fondo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 36));
        return btn;
    }

    // ── DESCRIPCIÓN: Crea un botón redondeado usando una versión más clara del color de fondo como hover por defecto ──
    public static JButton crear(String texto, Color fondo) {
        return crear(texto, fondo, fondo.brighter());
    }
}
