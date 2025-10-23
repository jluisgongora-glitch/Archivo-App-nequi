
package com.mycompany.simuladornequi;

// SimuladorNequi.java
class SimuladorNequi {
    public static void main(String[] args) {
        // Creación de cuentas
        CuentaNequi miCuenta = new CuentaNequi("123456789");
        CuentaNequi cuentaDestino = new CuentaNequi("987654321");
        
        // Crear usuario
        Usuario usuario = new Usuario("Joseph Gongora", "12345678", miCuenta);
        
        // Realizar operaciones
        System.out.println("=== SIMULADOR NEQUI ===\n");
        
        miCuenta.agregarSaldo(100000.0);
        Transaccion deposito = miCuenta.getTransacciones().get(miCuenta.getTransacciones().size() - 1);
        miCuenta.imprimirFactura(deposito, usuario, "factura_deposito.pdf");
        
        miCuenta.agregarTransaccion(new Transaccion("Compra en tienda", -50000.0, "COMPRA"));
        
        if (miCuenta.transferir(20000.0, cuentaDestino)) {
            System.out.println("Transferencia exitosa a " + cuentaDestino.getNumeroCuenta());
            Transaccion transferencia = miCuenta.getTransacciones().get(miCuenta.getTransacciones().size() - 1);
            miCuenta.imprimirFactura(transferencia, usuario, "factura_transferencia.pdf");
        }
        
        miCuenta.retirar(10000.0);
        Transaccion retiro = miCuenta.getTransacciones().get(miCuenta.getTransacciones().size() - 1);
        miCuenta.imprimirFactura(retiro, usuario, "factura_retiro.pdf");
        
        miCuenta.donar(5000.0, "Fundacion Ayuda Infantil");
        Transaccion donacion = miCuenta.getTransacciones().get(miCuenta.getTransacciones().size() - 1);
        miCuenta.imprimirFactura(donacion, usuario, "factura_donacion.pdf");
        
        // Mostrar resumen
        System.out.println("\n=== RESUMEN DE LA CUENTA ===");
        usuario.mostrarInfo();
        System.out.println("\nSaldo final en cuenta destino: $" + cuentaDestino.getSaldo());
    }
}