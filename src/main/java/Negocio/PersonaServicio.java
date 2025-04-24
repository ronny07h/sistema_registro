package Negocio;

import Datos.PersonaDAO;
import Model.Persona;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class PersonaServicio {
    private final PersonaDAO personaDAO;
    
    public PersonaServicio() {
        this.personaDAO = new PersonaDAO();
    }
    
    public List<Persona> listarPersonas() {
        return personaDAO.listarPersonas();
    }
    
    public int agregarNuevaPersona(Persona persona) {
        return personaDAO.verificarAgregarPersona(persona);
    }
    
    public boolean eliminarPersona(int id) {
        return personaDAO.eliminarPersona(id);
    }
    
    public boolean actualizar(Persona persona) {
        return personaDAO.actualizarPersona(persona);
    }
    
    public Persona buscarPorCedula(String cedula) {
        return personaDAO.buscarPorCedula(cedula);
    }
    
    public static int calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return 0;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}