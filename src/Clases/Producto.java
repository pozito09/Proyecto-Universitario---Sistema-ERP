package Clases;

public class Producto {

    private String nombre;
    private double precio;
    private int stock;
    private String categoria;
    private String descripcion;
    private String emoji;
    private java.awt.Color color;
    private byte[] imagenBytes;

    // Constructor simple (para carrito)
    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    // Constructor con imagen real y stock
    public Producto(String nombre, double precio, String categoria,
            String descripcion, String emoji, java.awt.Color color,
            byte[] imagenBytes, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.emoji = emoji;
        this.color = color;
        this.imagenBytes = imagenBytes;
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEmoji() {
        return emoji;
    }

    public java.awt.Color getColor() {
        return color;
    }

    public byte[] getImagenBytes() {
        return imagenBytes;
    }

    public int getStock() {
        return stock;
    }

    public boolean tieneImagen() {
        return imagenBytes != null && imagenBytes.length > 0;
    }

}
