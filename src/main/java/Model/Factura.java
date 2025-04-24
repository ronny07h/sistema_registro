package Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id")
    private Persona persona;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<DatalleFactura> detalle;

    public Factura() {
    }

    public Factura(Persona persona, List<DatalleFactura> detalle) {

        this.persona = persona;
        this.detalle = detalle;
    }

    public Factura(int id, Persona persona, List<DatalleFactura> detalle) {
        this.id = id;
        this.persona = persona;
        this.detalle = detalle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public List<DatalleFactura> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DatalleFactura> detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "Factura{" + "id=" + id + ", persona=" + persona + ", detalle=" + detalle + '}';
    }

}
