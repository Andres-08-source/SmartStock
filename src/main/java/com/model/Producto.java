package com.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Column(name = "nombre_producto", nullable = false, length = 80)
    private String nombreProducto;

    @NotNull(message = "El producto debe tener un precio")
    @Positive(message = "El valor debe ser mayor a 0")
    @Column(name = "precio")
    private Integer precio;

    @Column(name = "descripcion", length = 150)
    private String descripcion;

    @NotBlank(message = "La unidad de medida es obligatoria")
    @Column(name = "unidad_medida", nullable = false, length = 30)
    private String unidadMedida;

    @NotNull(message = "El stock mínimo es obligatorio")
    @PositiveOrZero(message = "El stock mínimo no puede ser negativo")
    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo;

    // Tiene DEFAULT CURRENT_TIMESTAMP en la BD, por eso no se envía desde el cliente
    @Column(name = "fecha_ingreso", insertable = false, updatable = false)
    private LocalDateTime fechaIngreso;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    public Producto() {}

    public Producto(String nombreProducto, String descripcion, String unidadMedida, Categoria categoria, Integer precio, Integer stockMinimo) {
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.categoria = categoria;
        this.precio = precio;
        this.stockMinimo = stockMinimo;
    }

    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public LocalDateTime getFechaIngreso() { return fechaIngreso; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public Integer getPrecio() { return precio; }
    public void setPrecio(Integer precio) { this.precio = precio; }
    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
}