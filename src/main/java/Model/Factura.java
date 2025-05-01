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
    @JoinColumn(name = "idpersona")
    private Persona persona;
    
    
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<DetalleFactura> detalles;
    
    
    public Factura (){
        
    }
    
    public Factura (Persona persona, List<DetalleFactura> detalles){
        this.persona = persona;
        this.detalles = detalles;
        
        for(DetalleFactura d: detalles){
            d.setFactura(this);
        }
    }
    
    
    public Factura (int id, Persona persona, List<DetalleFactura> detalles){
        this.id = id;
        this.persona = persona;
        this.detalles = detalles;
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

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Factura{" + "id=" + id + ", persona=" + persona + ", detalles=" + detalles + '}';
    }
    
    
}