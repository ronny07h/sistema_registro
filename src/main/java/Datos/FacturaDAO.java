package Datos;

import Model.Factura;
import Util.JpaUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class FacturaDAO {
    

    public int RegistrarFactura(Factura facturaAgregar) {
        EntityManager entityManager = null;
        try {
            
            entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();

            entityManager.getTransaction().begin();
            entityManager.persist(facturaAgregar);
            entityManager.getTransaction().commit();
            return 0;
        } catch (Exception ex) {
            
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Error de sesion de trabajo: " + ex.getMessage());
            ex.printStackTrace(); 
            return 1;
        } finally {
           
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    public Factura obtenerFacturaCompletaPorId(int idFactura) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("""
                    SELECT f FROM Factura f
                    JOIN FETCH f.persona
                    LEFT JOIN FETCH f.detalles d
                    LEFT JOIN FETCH d.producto
                    WHERE f.id = :idFactura
                    """, Factura.class)
                    .setParameter("idFactura", idFactura)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}