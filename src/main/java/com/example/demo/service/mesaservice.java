package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Mesa;

@Service
public interface mesaservice {

    public List<Mesa> mostrarmesalibre(int capacidad, LocalDateTime fecha);

}
