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
    
    /**
     * Calcula el salario acumulado por cédula del empleado
     * @param cedula Cédula del empleado
     * @return Salario acumulado o -1 si hay error
     */
    public double calcularSalarioAcumuladoPorCedula(String cedula) {
        return empleadoDAO.calcularSalarioAcumuladoPorCedula(cedula);
    }
    
    /**
     * Método mejorado para mostrar información completa del cálculo
     * @param cedula Cédula del empleado
     * @return String con el resultado formateado o mensaje de error
     */
    public String calcularSalarioCompleto(String cedula) {
        double salarioAcumulado = empleadoDAO.calcularSalarioAcumuladoPorCedula(cedula);
        if (salarioAcumulado < 0) {
            return "Error al calcular el salario acumulado";
        }
        
        Empleado empleado = empleadoDAO.buscarPorCedula(cedula);
        if (empleado == null) {
            return "No se pudo obtener información del empleado";
        }
        
        return String.format("""
            ===== RESUMEN SALARIAL =====
            Empleado: %s %s
            Cédula: %s
            Fecha contratación: %s
            Salario mensual: $%,.2f
            Salario acumulado: $%,.2f
            Tiempo trabajado: %s
            """,
            empleado.getNombre(),
            empleado.getApellido(),
            empleado.getCedula(),
            empleado.getFechaContratacion(),
            empleado.getSalario(),
            salarioAcumulado,
            obtenerTiempoTrabajadoPorCedula(cedula));
    }
    
    public static int calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return 0;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
    
    /**
     * Obtiene el tiempo trabajado buscando por cédula
     * @param cedula Cédula del empleado
     * @return String con el tiempo trabajado formateado
     */
    public String obtenerTiempoTrabajadoPorCedula(String cedula) {
        Empleado empleado = empleadoDAO.buscarPorCedula(cedula);
        if (empleado == null) {
            return "Empleado no encontrado";
        }
        
        if (empleado.getFechaContratacion() == null) {
            return "No tiene fecha de contratación registrada";
        }
        
        LocalDate fechaContratacion = empleado.getFechaContratacion();
        LocalDate fechaActual = LocalDate.now();
        
        if (fechaContratacion.isAfter(fechaActual)) {
            return "Fecha de contratación futura";
        }
        
        Period periodo = Period.between(fechaContratacion, fechaActual);
        return String.format("%d años, %d meses, %d días", 
                           periodo.getYears(), 
                           periodo.getMonths(), 
                           periodo.getDays());
    }
}