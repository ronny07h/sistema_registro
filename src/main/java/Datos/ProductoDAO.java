package Datos;

import Model.Producto;
import Util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    
    public int verificarAgregarProducto(Producto productoAgregar) {
        int result = 0;
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            Producto productoExiste = em.createQuery(
                    "SELECT p FROM Producto p WHERE p.codigo = :codigo", Producto.class)
                    .setParameter("codigo", productoAgregar.getCodigo())
                    .getSingleResult();
            if (productoExiste != null) {
                System.out.println("YA EXISTE EL PRODUCTO CON EL CÓDIGO: " + productoAgregar.getCodigo());
                return result;
            }
        } catch (NoResultException ex) {
            em.getTransaction().begin();
            em.persist(productoAgregar);  
            em.getTransaction().commit();
            result = 1;  
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al agregar producto: " + ex.getMessage());
        } finally {
            em.close();
        }
        return result;
    }

    
    public List<Producto> listarProductos() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        List<Producto> lista = new ArrayList<>();
        
        try {
            lista = em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
        } catch (Exception ex) {
            System.out.println("Error al obtener lista de productos: " + ex.getMessage());
        } finally {
            em.close();
        }
        return lista;
    }

    
    public Producto buscarPorCodigo(String codigo) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        Producto producto = null;

        try {
            producto = em.createQuery(
                    "SELECT p FROM Producto p WHERE p.codigo = :codigo", Producto.class)
                    .setParameter("codigo", codigo)
                    .getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("No se encontró un producto con el código: " + codigo);
        } catch (Exception e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
        } finally {
            em.close();
        }

        return producto;
    }

    
    public Producto buscarPorId(int id) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        Producto producto = null;

        try {
            producto = em.find(Producto.class, id);
            if (producto == null) {
                System.out.println("No se encontró un producto con el ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar producto por ID: " + e.getMessage());
        } finally {
            em.close();
        }

        return producto;
    }

    
    public boolean actualizarProducto(Producto producto) {
        if (producto == null || producto.getId() <= 0) {
            throw new IllegalArgumentException("El producto a actualizar no puede ser nulo y debe tener un ID válido.");
        }

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        boolean actualizado = false;

        try {
            em.getTransaction().begin();

            Producto productoExistente = em.find(Producto.class, producto.getId());

            if (productoExistente != null) {
                productoExistente.setCodigo(producto.getCodigo());
                productoExistente.setNombre(producto.getNombre());
                productoExistente.setDescripcion(producto.getDescripcion());
                productoExistente.setPrecio(producto.getPrecio());
                productoExistente.setStock(producto.getStock());
                
                em.getTransaction().commit();
                actualizado = true;
                System.out.println("Producto actualizado con éxito.");
            } else {
                em.getTransaction().rollback();
                System.out.println("No se encontró el producto con ID: " + producto.getId());
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al actualizar producto: " + e.getMessage());
        } finally {
            em.close();
        }

        return actualizado;
    }

    
    public boolean eliminarProducto(int id) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        boolean eliminado = false;

        try {
            em.getTransaction().begin();

            Producto productoEliminar = em.find(Producto.class, id);

            if (productoEliminar != null) {
                em.remove(productoEliminar);
                em.getTransaction().commit();
                eliminado = true;
                System.out.println("Producto eliminado con éxito.");
            } else {
                em.getTransaction().rollback();
                System.out.println("No se encontró el producto con ID: " + id);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al eliminar producto: " + e.getMessage());
        } finally {
            em.close();
        }

        return eliminado;
    }

   
    public List<Producto> buscarProductos(String criterio) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        List<Producto> resultados = new ArrayList<>();
        
        try {
            resultados = em.createQuery(
                "SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE :criterio OR LOWER(p.descripcion) LIKE :criterio", 
                Producto.class)
                .setParameter("criterio", "%" + criterio.toLowerCase() + "%")
                .getResultList();
        } catch (Exception e) {
            System.err.println("Error al buscar productos: " + e.getMessage());
        } finally {
            em.close();
        }
        
        return resultados;
    }

    
    public boolean actualizarStock(int idProducto, int nuevoStock) {
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        boolean actualizado = false;
        
        try {
            em.getTransaction().begin();
            
            Producto producto = em.find(Producto.class, idProducto);
            
            if (producto != null) {
                producto.setStock(nuevoStock);
                em.getTransaction().commit();
                actualizado = true;
                System.out.println("Stock actualizado con éxito.");
            } else {
                em.getTransaction().rollback();
                System.out.println("No se encontró el producto con ID: " + idProducto);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al actualizar stock: " + e.getMessage());
        } finally {
            em.close();
        }
        
        return actualizado;
    }
}