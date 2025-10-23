
package com.mycompany.simuladornequi;

// CuentaBancaria.java
import java.util.ArrayList;
import java.util.List;

public class CuentaBancaria {
    protected String numeroCuenta;
    protected double saldoBase;
    
    public CuentaBancaria(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
        this.saldoBase = 0.0;
    }
    
    public boolean transferir(double monto, CuentaBancaria destino) {
        if (monto > 0 && saldoBase >= monto) {
            saldoBase -= monto;
            destino.saldoBase += monto;
            return true;
        }
        return false;
    }
    
    public String getNumeroCuenta() {
        return numeroCuenta;
    }
    
    public double getSaldoBase() {
        return saldoBase;
    }
}

