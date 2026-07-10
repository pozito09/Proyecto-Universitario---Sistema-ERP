package Clases;

public class GuardarSesion {
    public static int id;
    public static String nombre;
    public static String rol;
    public static String nombres;
    public static String apellidos;

    public static void iniciar(int id, String nombre, String rol, String nombres, String apellidos) {
        GuardarSesion.id = id;
        GuardarSesion.nombre = nombre;
        GuardarSesion.rol = rol;
        GuardarSesion.nombres = nombres != null ? nombres : "";
        GuardarSesion.apellidos = apellidos != null ? apellidos : "";
    }

    public static String nombreCompleto() {
        String n = (nombres != null ? nombres : "").trim();
        String a = (apellidos != null ? apellidos : "").trim();
        return (n + " " + a).trim();
    }

    public static void cerrar() {
        id = 0;
        nombre = null;
        rol = null;
        nombres = null;
        apellidos = null;
    }
}
