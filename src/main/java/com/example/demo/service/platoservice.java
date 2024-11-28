package com.example.demo.service;

import java.io.IOException;
import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Plato;

import jakarta.servlet.http.HttpSession;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public interface platoservice {

    public ArrayList<Plato> traerplatos();

    public ResponseEntity<String> guardar(MultipartFile imagen, String nombre, String descripcion, String precio)
            throws S3Exception, AwsServiceException, SdkClientException, IOException;

    public ResponseEntity<String> actualizar(String id, String nombre, String descripcion, String precio,
            MultipartFile imagen, HttpSession sesion);

    public ResponseEntity<String> eliminar(String id);
}
