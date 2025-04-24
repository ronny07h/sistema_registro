
package Util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


public class JpaUtil {

    private static final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("RegistroPu");

    public static EntityManagerFactory getEntityManagerFactory(){
        return emf ;
    }

    public static void Cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

}
