package Datos;

import Model.Empleado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

public class AutenticacionDAO {

    public Empleado IniciarSesionUsuarioClave(String usuario, String clave) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("RegistroPu");
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                "SELECT e FROM Empleado e WHERE e.cedula = :user AND e.clave = :contrasena", Empleado.class)
                .setParameter("user", usuario)
                .setParameter("contrasena", clave)
                .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
            emf.close();
        }
    }
}
