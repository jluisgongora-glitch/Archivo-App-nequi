
package com.mycompany.simuladornequi;


class Saldo {
    private double monto;
    
    public Saldo(double montoInicial) {
        this.monto = montoInicial;
    }
    
    public void agregar(double cantidad) {
        if (cantidad > 0) {
            monto += cantidad;
        }
    }
    
    public void restar(double cantidad) {
        if (cantidad > 0 && monto >= cantidad) {
            monto -= cantidad;
        }
    }
    
    public double getMonto() {
        return monto;
    }
}