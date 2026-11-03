package org.example;

import org.example.dao.PedidoDAO;
import org.example.dao.ProductoDAO;
import org.example.modelo.Pedido;
import org.example.modelo.Producto;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProductoDAO prodDAO = new ProductoDAO();
        PedidoDAO pedDAO = new PedidoDAO();
        int opcion = 0;

        // Bucle principal: se repite hasta que el usuario elija la opción 6
        while (opcion != 6) {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1. Insertar producto");
            System.out.println("2. Consultar producto por id");
            System.out.println("3. Actualizar stock");
            System.out.println("4. Insertar pedido");
            System.out.println("5. Listado de pedidos");
            System.out.println("6. Salir");
            System.out.print("Elige: ");

            opcion = sc.nextInt();
            sc.nextLine(); // Limpiamos el buffer del Scanner

            switch (opcion) {
                case 1:
                    // Pedimos los datos y llamamos al DAO
                    System.out.print("Nombre: "); String nom = sc.nextLine();
                    System.out.print("Precio: "); double pre = sc.nextDouble();
                    System.out.print("Stock: "); int st = sc.nextInt();
                    prodDAO.insertarProducto(new Producto(0, nom, pre, st));
                    break;

                case 2:
                    System.out.print("ID: "); int id = sc.nextInt();
                    Producto p = prodDAO.obtenerProducto(id);
                    if (p != null) System.out.println("Encontrado: " + p); // Lombok genera el toString
                    else System.out.println("No existe.");
                    break;

                case 3:
                    System.out.print("ID: "); int idAct = sc.nextInt();
                    System.out.print("Nuevo stock: "); int nSt = sc.nextInt();
                    prodDAO.actualizarStock(idAct, nSt);
                    break;

                case 4:
                    System.out.print("ID del producto: "); int idPed = sc.nextInt();
                    System.out.print("Cantidad: "); int cant = sc.nextInt();
                    // LocalDate.now() coge la fecha actual del sistema
                    pedDAO.insertarPedido(idPed, cant, LocalDate.now());
                    break;

                case 5:
                    List<Pedido> pedidos = pedDAO.listarPedidos();
                    System.out.println("\n--- LISTA DE PEDIDOS ---");
                    if (pedidos.isEmpty()) {
                        System.out.println("No hay pedidos.");
                    } else {
                        // Recorremos la lista y mostramos el nombreProducto que viene del JOIN
                        for (Pedido ped : pedidos) {
                            System.out.println("ID Pedido: " + ped.getId_pedido() +
                                    " | Fecha: " + ped.getFecha_pedido() +
                                    " | Producto: " + ped.getNombreProducto() +
                                    " | Cantidad: " + ped.getCantidad());
                        }
                    }
                    break;

                case 6:
                    System.out.println("Adiós.");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
}
