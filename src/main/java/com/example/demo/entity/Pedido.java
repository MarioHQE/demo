package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "Pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_pedido")
    private int id_pedido;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", foreignKey = @ForeignKey(name = "usuario_id"))
    private Usuario id_usuario;

    @ManyToMany
    @JoinTable(name = "pedido_plato", joinColumns = @JoinColumn(name = "id_pedido"), inverseJoinColumns = @JoinColumn(name = "id_plato"), foreignKey = @ForeignKey(name = "pedido_id"), inverseForeignKey = @ForeignKey(name = "plato_id"))
    private List<Plato> id_plato;
    @Column(name = "fecha")
    private LocalDateTime fecha;
    @Column(name = "total")
    private double total;
    @Column(name = "estado")
    private String estado;

    public void calcularTotal() {
        this.total = id_plato.stream().mapToDouble(Plato::getPrecio).sum();
    }

}
