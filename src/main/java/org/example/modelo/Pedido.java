package org.example.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Pedido {
    private int id_pedido;
    private LocalDate fecha_pedido;
    private int cantidad;
    private int id_producto;
    private String nombreProducto;
}
