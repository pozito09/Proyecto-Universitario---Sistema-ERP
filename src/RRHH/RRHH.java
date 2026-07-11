package RRHH;

import static Clases.Colores.*;
import Clases.Botones;
import Clases.ConexionBD;
import Clases.PasswordUtil;
import Clases.FixedDocument;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import Clases.Auditoria;

public class RRHH extends JFrame {

    private DefaultTableModel modelo;
    private JTable tabla;

    private JLabel lblTotal;
    private JLabel lblAdmins;
    private JLabel lblEmpleados;
    private JLabel lblCajeros;
    private JLabel lblCocineros;

    public RRHH() {

        setTitle("CAFÉ COMETA - RECURSOS HUMANOS");
        setSize(1200, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CAFE);
        header.setPreferredSize(new Dimension(100, 70));

        JLabel titulo = new JLabel("GESTIÓN DE RECURSOS HUMANOS");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setBorder(new EmptyBorder(10, 20, 10, 20));

        header.add(titulo, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // CENTRO
        JPanel centro = new JPanel(new BorderLayout());

        // KPIs
        JPanel tarjetas = new JPanel(
                new GridLayout(1, 5, 12, 12));

        tarjetas.setBorder(
                new EmptyBorder(15, 15, 15, 15));

        lblTotal = new JLabel("0");
        lblAdmins = new JLabel("0");
        lblEmpleados = new JLabel("0");
        lblCajeros = new JLabel("0");
        lblCocineros = new JLabel("0");

        tarjetas.add(crearTarjeta(
                "TOTAL",
                lblTotal,
                DORADO));

        tarjetas.add(crearTarjeta(
                "ADMIN/JEFE",
                lblAdmins,
                DORADO_CLARO));

        tarjetas.add(crearTarjeta(
                "EMPLEADOS",
                lblEmpleados,
                CAFE_CLARO));

        tarjetas.add(crearTarjeta(
                "CAJEROS",
                lblCajeros,
                CAFE_CLARO));

        tarjetas.add(crearTarjeta(
                "COCINEROS",
                lblCocineros,
                TEXTO));

        centro.add(tarjetas, BorderLayout.NORTH);

        // TABLA

        String columnas[] = {
            "ID",
            "Usuario",
            "Nombres",
            "Apellidos",
            "Rol",
            "Teléfono",
            "Estado"
        };

        modelo = new DefaultTableModel(
                columnas,
                0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modelo);

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

        // BOTONES

        JPanel botones = new JPanel();

        JButton btnNuevo =
                Botones.crear("Nuevo Empleado", VERDE);

        JButton btnEditar =
                Botones.crear("Editar", DORADO, DORADO_CLARO);

        JButton btnEliminar =
                Botones.crear("Eliminar", ROJO, ROJO_HOVER);

        JButton btnActualizar =
                Botones.crear("Actualizar", DORADO, DORADO_CLARO);

        btnNuevo.addActionListener(e -> dialogoNuevoUsuario());
        btnEditar.addActionListener(e -> dialogoEditarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnActualizar.addActionListener(e -> cargarUsuarios());

        botones.add(btnNuevo);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);

        add(botones,
                BorderLayout.SOUTH);

        cargarUsuarios();
    }

    private void dialogoNuevoUsuario() {
        JDialog dlg = new JDialog(this, "Nuevo Usuario", true);
        dlg.getContentPane().setBackground(FONDO);
        dlg.setLayout(new BorderLayout());

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(FONDO);
        formulario.setBorder(new EmptyBorder(30, 50, 10, 50));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 5, 10, 5);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNombres = new JTextField(15);
        txtNombres.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNombres.setPreferredSize(new Dimension(200, 32));
        txtNombres.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 160, 80), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JTextField txtApellidos = new JTextField(15);
        txtApellidos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtApellidos.setPreferredSize(new Dimension(200, 32));
        txtApellidos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 160, 80), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JTextField txtUsuario = new JTextField(15);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setPreferredSize(new Dimension(200, 32));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 160, 80), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JPasswordField txtContrasena = new JPasswordField(15);
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContrasena.setPreferredSize(new Dimension(200, 32));
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 160, 80), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JTextField txtTelefono = new JTextField(15);
        txtTelefono.setDocument(new FixedDocument(9, true, true));
        txtTelefono.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTelefono.setPreferredSize(new Dimension(200, 32));
        txtTelefono.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 160, 80), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JComboBox<String> cmbRol = new JComboBox<>(new String[]{"EMPLEADO", "ADMINISTRATIVO", "JEFE", "CAJERO", "COCINERO"});
        cmbRol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbRol.setBackground(Color.WHITE);

        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        formulario.add(crearLabelForm("Nombres:"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(txtNombres, g);

        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        formulario.add(crearLabelForm("Apellidos:"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(txtApellidos, g);

        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        formulario.add(crearLabelForm("Usuario:"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(txtUsuario, g);

        g.gridx = 0; g.gridy = 3; g.weightx = 0;
        formulario.add(crearLabelForm("Contraseña:"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(txtContrasena, g);

        g.gridx = 0; g.gridy = 4; g.weightx = 0;
        formulario.add(crearLabelForm("Teléfono:"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(txtTelefono, g);

        g.gridx = 0; g.gridy = 5; g.weightx = 0;
        formulario.add(crearLabelForm("Rol:"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(cmbRol, g);

        JButton btnGuardar = Botones.crear("Guardar", DORADO, DORADO_CLARO);
        btnGuardar.setPreferredSize(new Dimension(130, 36));
        btnGuardar.addActionListener(ev -> {
            String nombres = txtNombres.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String contrasena = new String(txtContrasena.getPassword()).trim();
            String telefono = txtTelefono.getText().trim();
            String rol = (String) cmbRol.getSelectedItem();
            if (usuario.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Usuario y contraseña son obligatorios.");
                return;
            }
            if (contrasena.length() < 7) {
                JOptionPane.showMessageDialog(dlg, "La contraseña debe tener al menos 7 caracteres.");
                return;
            }
            try (Connection con = ConexionBD.conectar();
                 PreparedStatement psCheck = con.prepareStatement("SELECT COUNT(*) FROM usuarios WHERE usuario=?")) {
                psCheck.setString(1, usuario);
                ResultSet rsCheck = psCheck.executeQuery();
                if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(dlg, "Ya existe un usuario con ese nombre.");
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Error al verificar usuario: " + ex.getMessage());
                return;
            }
            try (Connection con = ConexionBD.conectar();
                 PreparedStatement ps = con.prepareStatement("INSERT INTO usuarios (usuario, contraseña, nombres, apellidos, telefono, rol) VALUES (?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, usuario);
                ps.setString(2, PasswordUtil.hash(contrasena));
                ps.setString(3, nombres.isEmpty() ? "" : nombres);
                ps.setString(4, apellidos.isEmpty() ? "" : apellidos);
                ps.setString(5, telefono.isEmpty() ? null : telefono);
                ps.setString(6, rol);
                ps.executeUpdate();
                try (ResultSet rsKeys = ps.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        Auditoria.crear("usuarios", rsKeys.getInt(1), "Usuario: " + usuario);
                    }
                }
                JOptionPane.showMessageDialog(dlg, "Usuario creado correctamente.");
                dlg.dispose();
                cargarUsuarios();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Error: " + ex.getMessage());
            }
        });

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        botones.setBackground(FONDO);
        botones.add(btnGuardar);

        dlg.add(formulario, BorderLayout.CENTER);
        dlg.add(botones, BorderLayout.SOUTH);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void dialogoEditarUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla.");
            return;
        }
        int id = (int) modelo.getValueAt(fila, 0);
        String usuarioActual = modelo.getValueAt(fila, 1).toString();
        String nombresActual = modelo.getValueAt(fila, 2) != null ? modelo.getValueAt(fila, 2).toString() : "";
        String apellidosActual = modelo.getValueAt(fila, 3) != null ? modelo.getValueAt(fila, 3).toString() : "";
        String rolActual = modelo.getValueAt(fila, 4).toString();
        String telefonoActual = modelo.getValueAt(fila, 5) != null ? modelo.getValueAt(fila, 5).toString() : "";

        JDialog dlg = new JDialog(this, "Editar Usuario", true);
        dlg.getContentPane().setBackground(FONDO);
        dlg.setLayout(new BorderLayout());

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(FONDO);
        formulario.setBorder(new EmptyBorder(30, 50, 10, 50));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 5, 10, 5);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNombres = new JTextField(nombresActual, 15);
        txtNombres.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNombres.setPreferredSize(new Dimension(200, 32));
        txtNombres.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 160, 80), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JTextField txtApellidos = new JTextField(apellidosActual, 15);
        txtApellidos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtApellidos.setPreferredSize(new Dimension(200, 32));
        txtApellidos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 160, 80), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JTextField txtUsuario = new JTextField(usuarioActual, 15);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setPreferredSize(new Dimension(200, 32));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 160, 80), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JPasswordField txtContrasena = new JPasswordField(15);
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContrasena.setPreferredSize(new Dimension(200, 32));
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 160, 80), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JTextField txtTelefono = new JTextField(15);
        txtTelefono.setDocument(new FixedDocument(9, true, true));
        txtTelefono.setText(telefonoActual);
        txtTelefono.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTelefono.setPreferredSize(new Dimension(200, 32));
        txtTelefono.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 160, 80), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JComboBox<String> cmbRol = new JComboBox<>(new String[]{"EMPLEADO", "ADMINISTRATIVO", "JEFE", "CAJERO", "COCINERO"});
        cmbRol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbRol.setBackground(Color.WHITE);
        cmbRol.setSelectedItem(rolActual);

        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        formulario.add(crearLabelForm("Nombres:"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(txtNombres, g);

        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        formulario.add(crearLabelForm("Apellidos:"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(txtApellidos, g);

        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        formulario.add(crearLabelForm("Usuario:"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(txtUsuario, g);

        g.gridx = 0; g.gridy = 3; g.weightx = 0;
        formulario.add(crearLabelForm("Contraseña:"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(txtContrasena, g);

        g.gridx = 0; g.gridy = 4; g.weightx = 0;
        formulario.add(crearLabelForm("Teléfono:"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(txtTelefono, g);

        g.gridx = 0; g.gridy = 5; g.weightx = 0;
        formulario.add(crearLabelForm("Rol:"), g);
        g.gridx = 1; g.weightx = 1;
        formulario.add(cmbRol, g);

        JButton btnGuardar = Botones.crear("Guardar Cambios", DORADO, DORADO_CLARO);
        btnGuardar.setPreferredSize(new Dimension(170, 36));
        btnGuardar.addActionListener(ev -> {
            String nombres = txtNombres.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String contrasena = new String(txtContrasena.getPassword()).trim();
            String telefono = txtTelefono.getText().trim();
            String rol = (String) cmbRol.getSelectedItem();
            if (usuario.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "El usuario no puede estar vacío.");
                return;
            }
            if (!contrasena.isEmpty() && contrasena.length() < 7) {
                JOptionPane.showMessageDialog(dlg, "La contraseña debe tener al menos 7 caracteres.");
                return;
            }
            try (Connection con = ConexionBD.conectar()) {
                PreparedStatement ps;
                if (!contrasena.isEmpty()) {
                    ps = con.prepareStatement("UPDATE usuarios SET usuario=?, contraseña=?, nombres=?, apellidos=?, telefono=?, rol=? WHERE id=?");
                    ps.setString(1, usuario);
                    ps.setString(2, PasswordUtil.hash(contrasena));
                    ps.setString(3, nombres.isEmpty() ? "" : nombres);
                    ps.setString(4, apellidos.isEmpty() ? "" : apellidos);
                    ps.setString(5, telefono.isEmpty() ? null : telefono);
                    ps.setString(6, rol);
                    ps.setInt(7, id);
                } else {
                    ps = con.prepareStatement("UPDATE usuarios SET usuario=?, nombres=?, apellidos=?, telefono=?, rol=? WHERE id=?");
                    ps.setString(1, usuario);
                    ps.setString(2, nombres.isEmpty() ? "" : nombres);
                    ps.setString(3, apellidos.isEmpty() ? "" : apellidos);
                    ps.setString(4, telefono.isEmpty() ? null : telefono);
                    ps.setString(5, rol);
                    ps.setInt(6, id);
                }
                ps.executeUpdate();
                Auditoria.editar("usuarios", id, "Usuario: " + usuario);
                JOptionPane.showMessageDialog(dlg, "Usuario actualizado correctamente.");
                dlg.dispose();
                cargarUsuarios();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Error: " + ex.getMessage());
            }
        });

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        botones.setBackground(FONDO);
        botones.add(btnGuardar);

        dlg.add(formulario, BorderLayout.CENTER);
        dlg.add(botones, BorderLayout.SOUTH);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void eliminarUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla.");
            return;
        }
        int id = (int) modelo.getValueAt(fila, 0);
        String nombreUsuario = modelo.getValueAt(fila, 1).toString();
        int conf = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar al usuario \"" + nombreUsuario + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (conf != JOptionPane.YES_OPTION) return;

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement("DELETE FROM usuarios WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            Auditoria.eliminar("usuarios", id, nombreUsuario);
            JOptionPane.showMessageDialog(this, "Usuario eliminado.");
            cargarUsuarios();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private JLabel crearLabelForm(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(TEXTO);
        return lbl;
    }

    private JPanel crearTarjeta(
            String titulo,
            JLabel lblValor,
            Color color) {

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

    private void cargarUsuarios() {

        try (Connection cn = ConexionBD.conectar();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM usuarios");
             ResultSet rs = ps.executeQuery()) {

            modelo.setRowCount(0);

            int total = 0;
            int admins = 0;
            int empleados = 0;
            int cajeros = 0;
            int cocineros = 0;

            while (rs.next()) {

                String rol =
                        rs.getString("rol");

                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("usuario"),
                    rs.getString("nombres") != null ? rs.getString("nombres") : "",
                    rs.getString("apellidos") != null ? rs.getString("apellidos") : "",
                    rol,
                    rs.getString("telefono") != null ? rs.getString("telefono") : "",
                    "Activo"
                });

                total++;

                if (rol != null) {

                    switch (rol.toUpperCase()) {
                        case "ADMINISTRATIVO":
                        case "JEFE":
                            admins++;
                            break;
                        case "EMPLEADO":
                            empleados++;
                            break;
                        case "CAJERO":
                            cajeros++;
                            break;
                        case "COCINERO":
                            cocineros++;
                            break;
                    }
                }
            }

            lblTotal.setText(
                    String.valueOf(total));

            lblAdmins.setText(
                    String.valueOf(admins));

            lblEmpleados.setText(
                    String.valueOf(empleados));

            lblCajeros.setText(
                    String.valueOf(cajeros));

            lblCocineros.setText(
                    String.valueOf(cocineros));

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage());

        }
    }

    private static RRHH instancia;

    public static void abrir() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new RRHH();
        }
        instancia.setVisible(true);
        instancia.toFront();
        instancia.requestFocus();
    }
}
