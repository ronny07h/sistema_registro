/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Datos.AutenticacionDAO;
import Model.Empleado;

/**
 *
 * @author Ronny
 */
public class LoginServicio {
    
    private final AutenticacionDAO autenticacionDAO;
    
   
    public LoginServicio(){
        this.autenticacionDAO = new AutenticacionDAO();
    }
    
    
    public boolean LoginUsuarioClave(String usuario, String clave){
        Empleado empleadoLogeado = (Empleado) this.autenticacionDAO.IniciarSesionUsuarioClave(usuario, clave);
        return empleadoLogeado != null;
    }
}
