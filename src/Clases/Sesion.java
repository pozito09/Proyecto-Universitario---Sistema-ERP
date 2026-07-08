package Clases;

public class Sesion {
    public static int id;
    public static String nombre;
    public static String rol;

    public static void iniciar(int id, String nombre, String rol) {
        Sesion.id = id;
        Sesion.nombre = nombre;
        Sesion.rol = rol;
    }

    public static void cerrar() {
        id = 0;
        nombre = null;
        rol = null;
    }
}
