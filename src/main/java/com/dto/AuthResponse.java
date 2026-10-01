package com.dto;

/**
 * Respuesta de la API al cliente:
 * si la autenticación fue exitosa o no manda un mensaje explicativo.
 */
public class AuthResponse {

    private boolean autenticado;
    private String mensaje;

    public AuthResponse() {}

    public AuthResponse(boolean autenticado, String mensaje) {
        this.autenticado = autenticado;
        this.mensaje = mensaje;
    }

    public boolean isAutenticado() { return autenticado; }
    public void setAutenticado(boolean autenticado) { this.autenticado = autenticado; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
