package org.example.dao;

import org.example.conexion.ConexionBD;
import org.example.modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoDAO {
    // METODO 1: INSERTAR PRODUCTO
    public void insertarProducto(Producto producto) {
        // Usamos '?' para evitar inyección SQL. Es una Sentencia Preparada.
        String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";

        // El try-with-resources (los paréntesis) cierra la conexión automáticamente al terminar.
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Sustituimos los '?' por los datos reales del objeto Producto (índice 1, 2, 3)
            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecio());
            pstmt.setInt(3, producto.getStock());

            // executeUpdate() se usa para INSERT, UPDATE o DELETE
            pstmt.executeUpdate();
            System.out.println("Producto guardado.");

        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());
        }
    }

    // METODO 2: CONSULTAR PRODUCTO POR ID
    public Producto obtenerProducto(int id) {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            // executeQuery() se usa para SELECT y devuelve un ResultSet (como una tabla virtual)
            ResultSet rs = pstmt.executeQuery();

            // rs.next() avanza a la siguiente fila. Si devuelve true, es que ha encontrado el producto.
            if (rs.next()) {
                return new Producto(
                        rs.getInt("id_producto"), // Sacamos el dato de la columna por su nombre
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }
        return null; // Si no lo encuentra o hay error, devolvemos null
    }

    // METODO 3: ACTUALIZAR STOCK
    public void actualizarStock(int idProducto, int nuevoStock) {
        String sql = "UPDATE productos SET stock = ? WHERE id_producto = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, nuevoStock);
            pstmt.setInt(2, idProducto);

            // executeUpdate() devuelve el número de filas afectadas.
            int filas = pstmt.executeUpdate();
            if (filas > 0) System.out.println("Stock actualizado.");
            else System.out.println("Producto no encontrado.");

        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }
}
