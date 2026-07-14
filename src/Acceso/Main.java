package Acceso;

// ── DESCRIPCIÓN: Punto de entrada de la aplicación. Crea y muestra la ventana de Login. ──
public class Main {
    public static void main(String[] args) {
        Login login = new Login();
        login.setVisible(true);
        login.setLocationRelativeTo(null);
    }
}