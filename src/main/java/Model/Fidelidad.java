package Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;



@Entity
@Table(name = "fidelidad")

public class Fidelidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    
    @OneToOne
    @JoinColumn(name = "id_cleinte", nullable = false, unique = true)
    private Cliente cliente;
    
    private int puntosAcumulado;
    
    private int puntosGastados;
    
    private LocalDate fechaDeAfiliacion;

    public Fidelidad() {
    }

    public Fidelidad(int Id, Cliente cliente, int puntosAcumulado, int puntosGastados, LocalDate fechaDeAfiliacion) {
        this.Id = Id;
        this.cliente = cliente;
        this.puntosAcumulado = puntosAcumulado;
        this.puntosGastados = puntosGastados;
        this.fechaDeAfiliacion = fechaDeAfiliacion;
    }

    public Fidelidad(Cliente cliente, int puntosAcumulado, int puntosGastados, LocalDate fechaDeAfiliacion) {
        this.cliente = cliente;
        this.puntosAcumulado = puntosAcumulado;
        this.puntosGastados = puntosGastados;
        this.fechaDeAfiliacion = fechaDeAfiliacion;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getPuntosAcumulado() {
        return puntosAcumulado;
    }

    public void setPuntosAcumulado(int puntosAcumulado) {
        this.puntosAcumulado = puntosAcumulado;
    }

    public int getPuntosGastados() {
        return puntosGastados;
    }

    public void setPuntosGastados(int puntosGastados) {
        this.puntosGastados = puntosGastados;
    }

    public LocalDate getFechaDeAfiliacion() {
        return fechaDeAfiliacion;
    }

    public void setFechaDeAfiliacion(LocalDate fechaDeAfiliacion) {
        this.fechaDeAfiliacion = fechaDeAfiliacion;
    }

    

    @Override
    public String toString() {
        return "Fidelidad{" + "Id=" + Id + ", cliente=" + cliente + ", puntosAcumulado=" + puntosAcumulado + ", puntosGastados=" + puntosGastados + ", fechaDeAfiliacion=" + fechaDeAfiliacion + '}';
    }
    
    
    
    
    
}