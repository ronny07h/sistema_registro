package Datos;

import Model.Persona;
import Util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {

    public int verificarAgregarPersona(Persona personaAgregar) {
        if (personaAgregar == null) {
            throw new IllegalArgumentException("La persona a agregar no puede ser nula.");
        }

        int result = 0;
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            Persona personaExiste = em.createQuery(
                    "SELECT p FROM Persona p WHERE p.cedula = :numId", Persona.class)
                    .setParameter("numId", personaAgregar.getCedula())
                    .getSingleResult();

            if (personaExiste != null) {
                System.out.println("La persona ya está registrada.");
                return result; // Se retorna 0 porque la persona ya existe
            }
        } catch (NoResultException ex) {
            try {
                em.getTransaction().begin();
                em.persist(personaAgregar);
                em.getTransaction().commit();
                result = 1; // Se indica que la persona fue agregada correctamente
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                System.err.println("Error al agregar persona: " + e.getMessage());
            }
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
        return result;
    }

    public List<Persona> listarPersonas() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        List<Persona> lista = new ArrayList<>();

        try {
            lista = em.createQuery("SELECT p FROM Persona p", Persona.class).getResultList();
        } catch (Exception ex) {
            System.err.println("Error al listar personas: " + ex.getMessage());
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
        return lista;
    }
    // En PersonaDAO.java

    public boolean eliminarPersona(int id) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        boolean eliminado = false;

        try {
            em.getTransaction().begin();

            Persona personaEliminar = em.find(Persona.class, id);

            if (personaEliminar != null) {

                em.remove(personaEliminar);
                em.getTransaction().commit();
                eliminado = true;
                System.out.println("Persona eliminada con éxito.");
            } else {
                em.getTransaction().rollback();
                System.out.println("No se encontró la persona con ID: " + id);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al eliminar persona: " + e.getMessage());
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }

        return eliminado;
    }

    public boolean actualizarPersona(Persona persona) {
        if (persona == null || persona.getId() <= 0) {
            throw new IllegalArgumentException("La persona a actualizar no puede ser nula y debe tener un ID válido.");
        }

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        boolean actualizado = false;

        try {
            em.getTransaction().begin();

            Persona personaExistente = em.find(Persona.class, persona.getId());

            if (personaExistente != null) {

                personaExistente.setNombre(persona.getNombre());
                personaExistente.setApellido(persona.getApellido());
                personaExistente.setCorreo(persona.getCorreo());
                personaExistente.setCedula(persona.getCedula());
                personaExistente.setFecha_de_nacimiento(persona.getFecha_de_nacimiento());
                personaExistente.setEdad(persona.getEdad());
                em.getTransaction().commit();
                actualizado = true;
                System.out.println("Persona actualizada con éxito.");
            } else {
                em.getTransaction().rollback();
                System.out.println("No se encontró la persona con ID: " + persona.getId());
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al actualizar persona: " + e.getMessage());
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }

        return actualizado;
    }

    public Persona buscarPorCedula(String cedula) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        Persona persona = null;

        try {
            persona = em.createQuery(
                    "SELECT p FROM Persona p WHERE p.cedula = :cedula", Persona.class)
                    .setParameter("cedula", cedula)
                    .getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("No se encontró una persona con cédula: " + cedula);
        } catch (Exception e) {
            System.err.println("Error al buscar persona: " + e.getMessage());
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }

        return persona;
    }

}
