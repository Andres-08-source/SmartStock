package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Esta clase remplaza a un simple Main.java.
 * En vez de ejecutar una prueba una sola vez y terminar,
 * @SpringBootApplication levanta un servidor web que queda "escuchando" peticiones HTTP indefinidamente (hasta que lo detengas).

 * Al correr esto, Spring Boot escanea automáticamente todos los paquetes com.smartstock
 * y arma el sistema solo, gracias a las anotaciones (@Entity, @Repository, @Service, @RestController).
 *
 * Este servira para pruebas localmente
 */

@SpringBootApplication
public class SmartstockBackApp {
    public static void main(String[] args) {
        SpringApplication.run(SmartstockBackApp.class, args);
        System.out.println("✔ Backend SmartStock iniciado correctamente.");
    }
}
