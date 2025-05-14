package Negocio;

import Datos.EmpleadoDAO;
import Model.Empleado;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class EmpleadoServicio {
    private final EmpleadoDAO empleadoDAO;
    
    public EmpleadoServicio() {
        this.empleadoDAO = new EmpleadoDAO();
    }
    
    public List<Empleado> listarEmpleados() {
        return empleadoDAO.listarEmpleados();
    }
    
    public int agregarNuevoEmpleado(Empleado empleado) {
       
        if (empleado.getEdad() == 0 && empleado.getFecha_de_nacimiento() != null) {
            empleado.setEdad(calcularEdad(empleado.getFecha_de_nacimiento()));
        }
        return empleadoDAO.agregarEmpleado(empleado);
    }
    
    public boolean eliminarEmpleado(int id) {
        return empleadoDAO.eliminarEmpleado(id);
    }
    
    public boolean actualizarEmpleado(Empleado empleado) {
        // Actualiza la edad si la fecha de nacimiento cambió
        if (empleado.getFecha_de_nacimiento() != null) {
            empleado.setEdad(calcularEdad(empleado.getFecha_de_nacimiento()));
        }
        return empleadoDAO.actualizarEmpleado(empleado);
    }
    
    public Empleado buscarPorCedula(String cedula) {
        return empleadoDAO.buscarPorCedula(cedula);
    }
    

    public List<Empleado> buscarPorTurno(String turno) {
        return empleadoDAO.listarEmpleados().stream()
                .filter(e -> e.getTurno().equalsIgnoreCase(turno))
                .toList();
    }
    
    public boolean asignarRol(int idEmpleado, String rol) {
        Empleado empleado = empleadoDAO.buscarPorCedula(String.valueOf(idEmpleado));
        if (empleado != null) {
            empleado.getRoles().add(rol);
            return empleadoDAO.actualizarEmpleado(empleado);
        }
        return false;
    }
    
    public static int calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return 0;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}