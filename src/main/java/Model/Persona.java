package Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;


@Entity
@Table(name = "usuario")

public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;
    @Column(nullable = false)
    private String correo;
    @Column(nullable = false)
    private String cedula;
    @Column(nullable = false)
    private LocalDate fecha_de_nacimiento;
    @Column
    private int edad;

    public Persona() {
    }

    public Persona(int id) {
        this.id = id;
    }
   
    public Persona(String nombre, String apellido, String correo, String cedula, LocalDate fecha_de_nacimiento, int edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.cedula = cedula;
        this.fecha_de_nacimiento = fecha_de_nacimiento;
        this.edad = edad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public LocalDate getFecha_de_nacimiento() {
        return fecha_de_nacimiento;
    }

    public void setFecha_de_nacimiento(LocalDate fecha_de_nacimiento) {
        this.fecha_de_nacimiento = fecha_de_nacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    

    @Override
    public String toString() {
        return "Persona{" + "id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", correo=" + correo + ", cedula=" + cedula + ", fecha_de_nacimiento=" + fecha_de_nacimiento + ", edad=" + edad + '}';
    }

    
}
