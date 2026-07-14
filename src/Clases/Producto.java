package Clases;

// ── DESCRIPCIÓN: Modelo de datos que representa un producto del catálogo (nombre, precio, categoría, imagen, stock, etc.) ──
public class Producto {

    private String nombre;
    private double precio;
    private int stock;
    private String categoria;
    private String descripcion;
    private String emoji;
    private java.awt.Color color;
    private byte[] imagenBytes;

    // ── DESCRIPCIÓN: Constructor simple utilizado para agregar productos al carrito de compras ──
    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    // ── DESCRIPCIÓN: Constructor completo que carga los datos del producto desde la base de datos (incluye imagen y stock) ──
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

    // ── DESCRIPCIÓN: Devuelve el nombre del producto ──
    public String getNombre() {
        return nombre;
    }

    // ── DESCRIPCIÓN: Devuelve el precio del producto ──
    public double getPrecio() {
        return precio;
    }

    // ── DESCRIPCIÓN: Devuelve la categoría del producto (ej: Cafés, Emparedados, Postres) ──
    public String getCategoria() {
        return categoria;
    }

    // ── DESCRIPCIÓN: Devuelve la descripción del producto ──
    public String getDescripcion() {
        return descripcion;
    }

    // ── DESCRIPCIÓN: Devuelve el emoji asociado al producto ──
    public String getEmoji() {
        return emoji;
    }

    // ── DESCRIPCIÓN: Devuelve el color asignado al producto (según su categoría) ──
    public java.awt.Color getColor() {
        return color;
    }

    // ── DESCRIPCIÓN: Devuelve la imagen del producto en formato bytes ──
    public byte[] getImagenBytes() {
        return imagenBytes;
    }

    // ── DESCRIPCIÓN: Devuelve la cantidad en stock del producto ──
    public int getStock() {
        return stock;
    }

    // ── DESCRIPCIÓN: Verifica si el producto tiene una imagen válida (bytes no nulos ni vacíos) ──
    public boolean tieneImagen() {
        return imagenBytes != null && imagenBytes.length > 0;
    }

}
