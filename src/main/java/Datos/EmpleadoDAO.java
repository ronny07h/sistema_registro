package Datos;

import Model.Empleado;
import Util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    // Método para agregar un nuevo empleado
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

    // Método para listar todos los empleados
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

    // Método para eliminar un empleado por ID
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

    // Método para actualizar los datos de un empleado
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
                empleadoExistente.setNombre(empleado.getNombre());
                empleadoExistente.setApellido(empleado.getApellido());
                empleadoExistente.setCorreo(empleado.getCorreo());
                empleadoExistente.setCedula(empleado.getCedula());
                empleadoExistente.setFecha_de_nacimiento(empleado.getFecha_de_nacimiento());
                empleadoExistente.setEdad(empleado.getEdad());
                empleadoExistente.setDireccion(empleado.getDireccion());
                empleadoExistente.setSalario(empleado.getSalario());
                empleadoExistente.setTurno(empleado.getTurno());
                empleadoExistente.setFechaContratacion(empleado.getFechaContratacion());
                empleadoExistente.setRoles(empleado.getRoles());
                empleadoExistente.setClave(empleado.getClave());

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

    // Método para buscar un empleado por su cédula
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

    
 public double calcularSalarioAcumuladoPorCedula(String cedula) {
    EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
    try {
        Empleado empleado = em.createQuery(
                "SELECT e FROM Empleado e WHERE e.cedula = :cedula", Empleado.class)
                .setParameter("cedula", cedula)
                .getSingleResult();

        if (empleado.getFechaContratacion() == null) {
            System.err.println("El empleado no tiene fecha de contratación registrada");
            return -1;
        }

        LocalDate fechaInicio = empleado.getFechaContratacion();
        LocalDate fechaActual = LocalDate.now();

        if (fechaInicio.isAfter(fechaActual)) {
            System.err.println("La fecha de contratación es futura: " + fechaInicio);
            return -1;
        }

        long diasTrabajados = ChronoUnit.DAYS.between(fechaInicio, fechaActual);

        // Asumimos que el salario mensual es por 30 días
        double salarioDiario = empleado.getSalario() / 30.0;
        double salarioAcumulado = diasTrabajados * salarioDiario;

        // Para debug/log
        System.out.println("Días trabajados: " + diasTrabajados);
        System.out.println("Salario diario: " + salarioDiario);
        System.out.println("Salario acumulado: " + salarioAcumulado);

        return salarioAcumulado;

    } catch (NoResultException e) {
        System.err.println("No se encontró empleado con cédula: " + cedula);
        return -1;
    } catch (Exception e) {
        System.err.println("Error al calcular salario: " + e.getMessage());
        return -1;
    } finally {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}

}
