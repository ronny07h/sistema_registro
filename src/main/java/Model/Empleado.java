package Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "empleado")
public class Empleado extends Persona {

    public enum Rol {
        CAJERO,
        ADMINISTRADOR,
        CLIENTE 
    }

    private String direccionemp;
    private Rol rol;
    private double salario;

    @OneToOne(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Persona usuario; 

    public Empleado() {
    }

    public Empleado(String direccion, Rol rol, double salario, String nombre, String apellido, String correo, String cedula, LocalDate fecha_de_nacimiento, int edad) {
        super(nombre, apellido, correo, cedula, fecha_de_nacimiento, edad);
        this.direccionemp = direccion;
        this.rol = rol;
        this.salario = salario;
    }

    public Empleado(String direccion, Rol rol, double salario, Persona usuario, String nombre, String apellido, String correo, String cedula, LocalDate fecha_de_nacimiento, int edad) {
        super(nombre, apellido, correo, cedula, fecha_de_nacimiento, edad);
        this.direccionemp = direccion;
        this.rol = rol;
        this.salario = salario;
        this.usuario = usuario;
    }

    public String getDireccion() {
        return direccionemp;
    }

    public void setDireccion(String direccion) {
        this.direccionemp = direccion;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
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

    @Override
    public String toString() {
        return "Empleado{" + 
               "direccion=" + direccionemp + 
               ", rol=" + rol + 
               ", salario=" + salario + 
               ", usuario=" + usuario + 
               '}';
    }
}