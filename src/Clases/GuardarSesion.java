package Clases;

// ── DESCRIPCIÓN: Almacena la sesión del usuario logueado (id, nombre, apellidos) ──
public class GuardarSesion {
    public static int id;
    public static String nombre;
    public static String nombres;
    public static String apellidos;

    // ── DESCRIPCIÓN: Guarda los datos de la sesión del usuario actual (id, nombre, nombres, apellidos) ──
    public static void iniciar(int id, String nombre, String nombres, String apellidos) {
        GuardarSesion.id = id;
        GuardarSesion.nombre = nombre;
        GuardarSesion.nombres = nombres != null ? nombres : "";
        GuardarSesion.apellidos = apellidos != null ? apellidos : "";
    }

    // ── DESCRIPCIÓN: Devuelve el nombre completo del usuario (nombres + apellidos) ──
    public static String nombreCompleto() {
        String n = (nombres != null ? nombres : "").trim();
        String a = (apellidos != null ? apellidos : "").trim();
        return (n + " " + a).trim();
    }
}
