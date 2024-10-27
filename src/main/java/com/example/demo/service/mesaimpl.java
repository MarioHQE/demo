package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.mesarepository;
import com.example.demo.entity.Mesa;

@Service
public class mesaimpl implements mesaservice {

    @Autowired
    private mesarepository mesadao;

    @Override
    public List<Mesa> mostrarmesalibre(int Capacidad, LocalDateTime fecha) {

        return mesadao.mesalibre(Capacidad, fecha);

    }

}
