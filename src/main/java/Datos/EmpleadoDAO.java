package Datos;

import Model.Empleado;
import Util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    public int agregarEmpleado(Empleado empleadoAgregar) {
        if (empleadoAgregar == null) {
            throw new IllegalArgumentException("El empleado a agregar no puede ser nulo.");
        }

        int result = 0;
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            // Verificar si ya existe un empleado con la misma cédula
            Empleado empleadoExiste = em.createQuery(
                    "SELECT e FROM Empleado e WHERE e.cedula = :cedula", Empleado.class)
                    .setParameter("cedula", empleadoAgregar.getCedula())
                    .getSingleResult();

            if (empleadoExiste != null) {
                System.out.println("El empleado ya está registrado.");
                return result;
            }
        } catch (NoResultException ex) {
            try {
                em.getTransaction().begin();
                em.persist(empleadoAgregar);
                em.getTransaction().commit();
                result = 1; // Éxito
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                System.err.println("Error al agregar empleado: " + e.getMessage());
            }
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
        return result;
    }

    public List<Empleado> listarEmpleados() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        List<Empleado> lista = new ArrayList<>();

        try {
            lista = em.createQuery("SELECT e FROM Empleado e", Empleado.class).getResultList();
        } catch (Exception ex) {
            System.err.println("Error al listar empleados: " + ex.getMessage());
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
        return lista;
    }

    public boolean eliminarEmpleado(int id) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        boolean eliminado = false;

        try {
            em.getTransaction().begin();
            Empleado empleadoEliminar = em.find(Empleado.class, id);

            if (empleadoEliminar != null) {
                em.remove(empleadoEliminar);
                em.getTransaction().commit();
                eliminado = true;
                System.out.println("Empleado eliminado con éxito.");
            } else {
                em.getTransaction().rollback();
                System.out.println("No se encontró el empleado con ID: " + id);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al eliminar empleado: " + e.getMessage());
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
        return eliminado;
    }

    public boolean actualizarEmpleado(Empleado empleado) {
        if (empleado == null || empleado.getId() <= 0) {
            throw new IllegalArgumentException("El empleado a actualizar no puede ser nulo y debe tener un ID válido.");
        }

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        boolean actualizado = false;

        try {
            em.getTransaction().begin();
            Empleado empleadoExistente = em.find(Empleado.class, empleado.getId());

            if (empleadoExistente != null) {
                // Actualizar campos de Persona (heredados)
                empleadoExistente.setNombre(empleado.getNombre());
                empleadoExistente.setApellido(empleado.getApellido());
                empleadoExistente.setCorreo(empleado.getCorreo());
                empleadoExistente.setCedula(empleado.getCedula());
                empleadoExistente.setFecha_de_nacimiento(empleado.getFecha_de_nacimiento());
                empleadoExistente.setEdad(empleado.getEdad());

                // Actualizar campos específicos de Empleado
                empleadoExistente.setDireccion(empleado.getDireccion());
                empleadoExistente.setSalario(empleado.getSalario());
                empleadoExistente.setTurno(empleado.getTurno());
                empleadoExistente.setFechaContratacion(empleado.getFechaContratacion());
                empleadoExistente.setRoles(empleado.getRoles());

                em.getTransaction().commit();
                actualizado = true;
                System.out.println("Empleado actualizado con éxito.");
            } else {
                em.getTransaction().rollback();
                System.out.println("No se encontró el empleado con ID: " + empleado.getId());
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al actualizar empleado: " + e.getMessage());
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
        return actualizado;
    }

    public Empleado buscarPorCedula(String cedula) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        Empleado empleado = null;

        try {
            empleado = em.createQuery(
                    "SELECT e FROM Empleado e WHERE e.cedula = :cedula", Empleado.class)
                    .setParameter("cedula", cedula)
                    .getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("No se encontró un empleado con cédula: " + cedula);
        } catch (Exception e) {
            System.err.println("Error al buscar empleado: " + e.getMessage());
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
        return empleado;
    }
}