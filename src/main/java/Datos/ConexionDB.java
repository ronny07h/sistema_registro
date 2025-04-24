package Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/persona_database";
    private static final String USER = "root";
    private static final String PASSWORD = "Incipio123";

    public static Connection AbrirConexion() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            System.out.println("Error de la coneccion:" + ex.getMessage());
            return null;
        }
    }

    public static void CerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                    System.out.println("Error al cerrar la coneccion: " + ex.getMessage());
            }
        }

    }

}
