package org.example.dao;

import org.example.conexion.ConexionBD;
import org.example.modelo.Pedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    // METODO 1: INSERTAR PEDIDO Y REDUCIR STOCK (Las 2 sentencias SQL)
    public void insertarPedido(int idProducto, int cantidad, LocalDate fecha) {

        // 1º Comprobamos si el producto existe para evitar el error del punto 3.b
        ProductoDAO prodDAO = new ProductoDAO();
        if (prodDAO.obtenerProducto(idProducto) == null) {
            System.out.println("ERROR: El producto con ID " + idProducto + " no existe.");
            return; // Si no existe, salimos del metodo y no hacemos nada
        }

        // Si existe, preparamos las DOS sentencias SQL
        String sql1 = "INSERT INTO pedidos (fecha_pedido, cantidad, id_producto) VALUES (?, ?, ?)";
        String sql2 = "UPDATE productos SET stock = stock - ? WHERE id_producto = ?";

        try (Connection conn = ConexionBD.conectar()) {
            // --- SENTENCIA 1: Insertar el pedido ---
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            pstmt1.setDate(1, java.sql.Date.valueOf(fecha)); // Convertimos LocalDate a Date de SQL
            pstmt1.setInt(2, cantidad);
            pstmt1.setInt(3, idProducto);
            pstmt1.executeUpdate();

            // --- SENTENCIA 2: Restar el stock al producto ---
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, cantidad); // Restamos la cantidad pedida
            pstmt2.setInt(2, idProducto);
            pstmt2.executeUpdate();

            System.out.println("Pedido insertado y stock reducido.");

        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        }
    }

    // METODO 2: LISTAR PEDIDOS CON JOIN
    public List<Pedido> listarPedidos() {
        List<Pedido> lista = new ArrayList<>();

        // Usamos JOIN para unir la tabla pedidos con la tabla productos y sacar el nombre.
        String sql = "SELECT p.id_pedido, p.fecha_pedido, p.cantidad, p.id_producto, pr.nombre " +
                "FROM pedidos p JOIN productos pr ON p.id_producto = pr.id_producto";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Creamos el objeto Pedido metiendo también el nombre que viene del JOIN
                Pedido ped = new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getDate("fecha_pedido").toLocalDate(), // Convertimos Date de SQL a LocalDate
                        rs.getInt("cantidad"),
                        rs.getInt("id_producto"),
                        rs.getString("nombre") // Este es el campo extra del JOIN
                );
                lista.add(ped); // Añadimos el pedido a la lista
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return lista; // Devolvemos toda la lista llena
    }
}
