import Model.Empleado;


import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.transaction.Transactional;



public class EmpleadoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    
    @Transactional
    public Empleado guardar(Empleado empleado) {
        entityManager.persist(empleado);
        return empleado;
    }

    
    @Transactional
    public Empleado actualizar(Empleado empleado) {
        return entityManager.merge(empleado);
    }

    
    @Transactional
    public void eliminar(String cedula) {
        Empleado empleado = buscarPorCedula(cedula);
        if (empleado != null) {
            entityManager.remove(empleado);
        }
    }

   
     public Empleado buscarPorCedula(String cedula) {
        try {
            return entityManager.createQuery("SELECT e FROM Empleado e WHERE e.cedula = :cedula", Empleado.class)
                    .setParameter("cedula", cedula)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}