package Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "detalle_factura")
public class DetalleFactura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int cantidad;
    
    @Column(nullable = false)
    private float precioUnitario;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

    
    public DetalleFactura() {
    
    }
    
    
    public DetalleFactura(int cantidad, float precioUnitario, Producto producto) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.producto = producto;
    }
    
    
    public DetalleFactura(int id, int cantidad, float precioUnitario, Producto producto) {
        this.id = id;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.producto = producto;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    @Override
    public String toString() {
        return "DetalleFactura{" + "id=" + id + ", cantidad=" + cantidad + ", precioUnitario=" + precioUnitario + ", producto=" + producto + ", factura=" + factura + '}';
    }
    
}