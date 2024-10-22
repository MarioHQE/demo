package com.example.demo.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(name = "Plato.findById", query = "SELECT p From Plato p WHERE p.id_plato =:id")
@Entity
@Data
@Table(name = "Plato")
@DynamicInsert
@DynamicUpdate
public class Plato {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_plato")
    private int id_plato;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "foto")
    private String foto;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "precio")
    private double precio;

}
