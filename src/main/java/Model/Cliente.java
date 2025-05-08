package Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;


@Entity
@Table(name = "cliente")
public class Cliente extends Persona {

    private String direccion;

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)

    private Fidelidad fidelidad;

    public Cliente() {
    }

    public Cliente(String direccion, String nombre, String apellido, String correo, String cedula, LocalDate fecha_de_nacimiento, int edad) {
        super(nombre, apellido, correo, cedula, fecha_de_nacimiento, edad);
        this.direccion = direccion;
    }

    public Cliente(String direccion, Fidelidad fidelidad, String nombre, String apellido, String correo, String cedula, LocalDate fecha_de_nacimiento, int edad) {
        super(nombre, apellido, correo, cedula, fecha_de_nacimiento, edad);
        this.direccion = direccion;
        this.fidelidad = fidelidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Fidelidad getFidelidad() {
        return fidelidad;
    }

    public void setFidelidad(Fidelidad fidelidad) {
        this.fidelidad = fidelidad;
    }

    @Override
    public String toString() {
        return "Cliente{" + "direccion=" + direccion + ", fidelidad=" + fidelidad + '}';
    }
    
    

}