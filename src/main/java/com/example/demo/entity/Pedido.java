package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@DynamicInsert
@DynamicUpdate
@NamedQuery(name = "Pedido.findPedidobyCorreo", query = "SELECT DISTINCT p FROM Pedido p INNER JOIN p.id_usuario u INNER JOIN p.pedidoPlatos pp INNER JOIN pp.plato pl WHERE u.email = :correo ORDER BY p.id_pedido ")
@Table(name = "Pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_pedido")
    public int id_pedido;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", foreignKey = @ForeignKey(name = "usuario_id"))
    public Usuario id_usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<PedidoPlato> pedidoPlatos;

    @Column(name = "fecha")
    public LocalDateTime fecha;

    @Column(name = "total")
    public double total;

    @Column(name = "estado")
    public String estado;

    public void calcularTotal() {
        this.total = pedidoPlatos.stream()
                .mapToDouble(pp -> pp.getPlato().getPrecio() * pp.getCantidad())
                .sum();
    }

}