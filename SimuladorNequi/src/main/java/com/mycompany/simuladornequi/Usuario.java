
package com.mycompany.simuladornequi;


// Usuario.java
class Usuario {
    private String nombre;
    private String cedula;
    private CuentaNequi cuenta;
    
    public Usuario(String nombre, String cedula, CuentaNequi cuenta) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.cuenta = cuenta;
    }
    
    public void mostrarInfo() {
        System.out.println("Usuario: " + nombre + " (Cedula: " + cedula + ")");
        System.out.println("Saldo actual: $" + cuenta.getSaldo());
        cuenta.mostrarHistorial();
    }
    
    public CuentaNequi getCuenta() {
        return cuenta;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getCedula() {
        return cedula;
    }
}
