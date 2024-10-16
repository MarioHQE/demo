package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.platorepository;
import com.example.demo.entity.Plato;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class platoimpl implements platoservice {

    @Autowired
    platorepository platodao;

    @Override
    public ArrayList<Plato> traerplatos() {
        ArrayList<Plato> listaplato = (ArrayList<Plato>) platodao.findAll();
        return listaplato;
    }

}
