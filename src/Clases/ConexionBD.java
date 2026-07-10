package Clases;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/cafecometa";
    private static final String USER = "root";
    private static final String PASSWORD = "root123";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "Error de conexión: " + e.getMessage(),
                "Error BD", javax.swing.JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}

