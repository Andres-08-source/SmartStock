package com.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerta")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Integer idAlerta;

    @ManyToOne
    @JoinColumn(name = "id_lote")
    private Lote lote;

    @Column(name = "fecha_alerta", insertable = false, updatable = false)
    private LocalDateTime fechaAlerta;

    @NotNull
    @Positive
    @Column(name = "dias_previos", nullable = false)
    private Integer diasPrevios;

    // La BD define el ENUM con estos valores exactos: 'Pendiente','Enviada','Atendida'
    @Column(name = "estado_alerta", nullable = false, length = 20)
    private String estadoAlerta = "Pendiente";

    public Alerta() {}

    public Alerta(Lote lote, Integer diasPrevios) {
        this.lote = lote;
        this.diasPrevios = diasPrevios;
    }

    public Integer getIdAlerta() { return idAlerta; }
    public void setIdAlerta(Integer idAlerta) { this.idAlerta = idAlerta; }
    public Lote getLote() { return lote; }
    public void setLote(Lote lote) { this.lote = lote; }
    public LocalDateTime getFechaAlerta() { return fechaAlerta; }
    public Integer getDiasPrevios() { return diasPrevios; }
    public void setDiasPrevios(Integer diasPrevios) { this.diasPrevios = diasPrevios; }
    public String getEstadoAlerta() { return estadoAlerta; }
    public void setEstadoAlerta(String estadoAlerta) { this.estadoAlerta = estadoAlerta; }
}