package Negocio;

import Datos.FacturaDAO;
import Datos.PersonaDAO;
import Datos.ProductoDAO;
import Model.Factura;
import Model.Persona;
import Model.Producto;


public class FacturaServicio {
    
    private final FacturaDAO facturaDao;
    private final ProductoDAO productoDao;
    private final PersonaDAO personaDao;
    
    
      public FacturaServicio(){
        this.personaDao = new PersonaDAO();
        this.productoDao = new ProductoDAO();
        this.facturaDao = new FacturaDAO();
    }
    
    
    public Persona BuscarPersonaPorCedula(String cedula) {
        Persona personaEncontrada = this.personaDao.buscarPorCedula(cedula);
        return personaEncontrada;
    }
    
    
    public Producto BuscarProductoPorCodigo(String codigo){
        Producto productoEncontrado = this.productoDao.buscarPorCodigo(codigo);
        return productoEncontrado;
    }
    
    
    public Factura obtenerFacturaCompletaPorId(int idFactura){
        return this.facturaDao.obtenerFacturaCompletaPorId(idFactura);
    }
    
    
    public int RegistrarNuevaFactura(Factura nuevaFactura){
        return this.facturaDao.RegistrarFactura(nuevaFactura);
    }
}
