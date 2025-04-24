package Negocio;

import Model.Producto;
import Datos.ProductoDAO;
import java.util.List;

public class ProductoService {
    
    private final ProductoDAO productoDAO;
    
    // Constructor
    public ProductoService() {
        this.productoDAO = new ProductoDAO();
    }
    
    // Método para agregar un nuevo producto
    public int agregarNuevoProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        // Validar precio mayor que cero
        if (producto.getPrecio() <= 0) {
            System.out.println("El precio debe ser mayor que cero.");
            return 0;  // Indica que no se pudo agregar el producto
        }
        // Validar que el stock no sea negativo
        if (producto.getStock() < 0) {
            System.out.println("El stock no puede ser negativo.");
            return 0;  // Indica que no se pudo agregar el producto
        }
        // Llamada al DAO para verificar y agregar el producto
        return productoDAO.verificarAgregarProducto(producto);
    }
    
    // Método para listar todos los productos
    public List<Producto> listarProductos() {
        return productoDAO.listarProductos();
    }
    
    // Método para buscar un producto por su código
    public Producto buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de producto no puede estar vacío.");
        }
        return productoDAO.buscarPorCodigo(codigo);
    }
    
    // Método para buscar un producto por su ID
    public Producto buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser mayor que cero.");
        }
        return productoDAO.buscarPorId(id);
    }
    
    // Método para actualizar un producto
    public boolean actualizarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        if (producto.getId() <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser válido.");
        }
        if (producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        
        return productoDAO.actualizarProducto(producto);
    }
    
    // Método para eliminar un producto
    public boolean eliminarProducto(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser mayor que cero.");
        }
        return productoDAO.eliminarProducto(id);
    }
    
    // Método para buscar productos por nombre o descripción
    public List<Producto> buscarProductos(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return listarProductos(); // Si no hay criterio, retornar todos los productos
        }
        return productoDAO.buscarProductos(criterio);
    }
    
    // Método para actualizar el stock de un producto
    public boolean actualizarStock(int idProducto, int nuevoStock) {
        if (idProducto <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser mayor que cero.");
        }
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        
        return productoDAO.actualizarStock(idProducto, nuevoStock);
    }
}