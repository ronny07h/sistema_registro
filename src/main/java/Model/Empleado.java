package Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empleado")
public class Empleado extends Persona {

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private double salario;

    @Column(nullable = false)
    private String turno;

    @Column(name = "fecha_contratacion", nullable = false)
    private LocalDate fechaContratacion;

    @Column
    private String clave;      

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "empleado_roles", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "rol")
    private List<String> roles = new ArrayList<>();

    @OneToOne(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Persona usuario;

    public Empleado() {
    }

    public Empleado(int id, String nombre, String apellido, String correo, String cedula,
            LocalDate fecha_de_nacimiento, int edad, String direccion, double salario,
            String turno, LocalDate fechaContratacion) {
        super(nombre, apellido, correo, cedula, fecha_de_nacimiento, edad);
        this.direccion = direccion;
        this.salario = salario;
        this.turno = turno;
        this.fechaContratacion = fechaContratacion;
    }

    public Empleado(String nombre, String apellido, String correo, String cedula,
            LocalDate fecha_de_nacimiento, int edad, String direccion, double salario,
            String turno, LocalDate fechaContratacion, List<String> roles, Persona usuario) {
        super(nombre, apellido, correo, cedula, fecha_de_nacimiento, edad);
        this.direccion = direccion;
        this.salario = salario;
        this.turno = turno;
        this.fechaContratacion = fechaContratacion;
        this.roles = roles;
        this.usuario = usuario;
    }

    // GETTER Y SETTER NUEVO PARA CLAVE (ÚNICO CAMBIO ADICIONAL)
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    /* 
     * EL RESTO DE TU CÓDIGO PERMANECE EXACTAMENTE IGUAL 
     * (mismos getters, setters y métodos que ya tenías)
     */
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void addRol(String rol) {
        if (!roles.contains(rol)) {
            roles.add(rol);
        }
    }

    public void removeRol(String rol) {
        roles.remove(rol);
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public Persona getUsuario() {
        return usuario;
    }

    public void setUsuario(Persona usuario) {
        this.usuario = usuario;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    @Override
    public String toString() {
        return "Empleado{"
                + "direccion=" + direccion
                + ", salario=" + salario
                + ", turno=" + turno
                + ", fechaContratacion=" + fechaContratacion
                + ", roles=" + roles
                + ", usuario=" + usuario
                + '}';
    }

    public boolean hasRol(String rol) {
        return roles.contains(rol);
    }
}
