
package com.mycompany.simuladornequi;

// Transaccion.java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Transaccion {
    private String descripcion;
    private double monto;
    private String tipo;
    private LocalDateTime fecha;
    private String numeroTransaccion;
    
    public Transaccion(String descripcion, double monto, String tipo) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.tipo = tipo;
        this.fecha = LocalDateTime.now();
        this.numeroTransaccion = generarNumeroTransaccion();
    }
    
    private String generarNumeroTransaccion() {
        return "TRX" + System.currentTimeMillis();
    }
    
    @Override
    public String toString() {
        return tipo + ": " + descripcion + " - Monto: $" + monto + 
               " [" + fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "]";
    }
    
    public double getMonto() {
        return monto;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public String getFechaFormateada() {
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
    
    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }
}



