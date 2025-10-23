
package com.mycompany.simuladornequi;

// CuentaNequi.java
import java.util.ArrayList;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

class CuentaNequi extends CuentaBancaria {
    
    private static final double TASA_4X1000 = 0.004;
    
    private Saldo saldo;
    private List<Transaccion> transacciones;
    
    public CuentaNequi(String numeroCuenta) {
        super(numeroCuenta);
        this.saldo = new Saldo(0.0);
        this.transacciones = new ArrayList<>();
    }

    public void agregarSaldo(double cantidad) {
        if (cantidad > 0) {
            saldo.agregar(cantidad);
            transacciones.add(new Transaccion("Deposito de saldo", cantidad, "DEPOSITO"));
            System.out.println("Saldo agregado: $" + cantidad + ". Nuevo saldo: $" + saldo.getMonto());
        } else {
            System.out.println("Error: La cantidad debe ser positiva.");
        }
    }

    @Override
    public boolean transferir(double monto, CuentaBancaria destino) {
        if (monto > 0) {
            double impuesto = monto * TASA_4X1000;
            double total = monto + impuesto;
            
            if (saldo.getMonto() >= total) {
                saldo.restar(total);
                
                if (destino instanceof CuentaNequi) {
                    ((CuentaNequi) destino).saldo.agregar(monto);
                } else {
                    destino.saldoBase += monto;
                }
                
                String desc = "Transferencia a " + destino.getNumeroCuenta() + 
                             " (Impuesto 4x1000: $" + String.format("%.2f", impuesto) + ")";
                transacciones.add(new Transaccion(desc, -monto, "TRANSFERENCIA"));
                System.out.println("Transferencia exitosa de $" + monto + 
                                 " (impuesto: $" + String.format("%.2f", impuesto) + 
                                 "). Nuevo saldo: $" + saldo.getMonto());
                return true;
            } else {
                System.out.println("Error: Fondos insuficientes (incluyendo impuesto 4x1000).");
            }
        } else {
            System.out.println("Error: El monto debe ser positivo.");
        }
        return false;
    }

    public boolean retirar(double monto) {
        if (monto > 0) {
            double impuesto = monto * TASA_4X1000;
            double total = monto + impuesto;
            
            if (saldo.getMonto() >= total) {
                saldo.restar(total);
                String desc = "Retiro de efectivo (Impuesto 4x1000: $" + 
                             String.format("%.2f", impuesto) + ")";
                transacciones.add(new Transaccion(desc, -monto, "RETIRO"));
                System.out.println("Retiro exitoso de $" + monto + 
                                 " (impuesto: $" + String.format("%.2f", impuesto) + 
                                 "). Nuevo saldo: $" + saldo.getMonto());
                return true;
            } else {
                System.out.println("Error: Fondos insuficientes (incluyendo impuesto 4x1000).");
            }
        } else {
            System.out.println("Error: El monto debe ser positivo.");
        }
        return false;
    }

    public boolean donar(double monto, String causa) {
        if (monto > 0) {
            double impuesto = monto * TASA_4X1000;
            double total = monto + impuesto;
            
            if (saldo.getMonto() >= total) {
                saldo.restar(total);
                String desc = "Donacion a '" + causa + "' (Impuesto 4x1000: $" + 
                             String.format("%.2f", impuesto) + ")";
                transacciones.add(new Transaccion(desc, -monto, "DONACION"));
                System.out.println("Donacion exitosa de $" + monto + " a '" + causa + 
                                 "' (impuesto: $" + String.format("%.2f", impuesto) + 
                                 "). Nuevo saldo: $" + saldo.getMonto());
                return true;
            } else {
                System.out.println("Error: Fondos insuficientes (incluyendo impuesto 4x1000).");
            }
        } else {
            System.out.println("Error: El monto debe ser positivo.");
        }
        return false;
    }

    public void mostrarHistorial() {
        System.out.println("Historial de transacciones para cuenta " + numeroCuenta + ":");
        if (transacciones.isEmpty()) {
            System.out.println("No hay transacciones.");
        } else {
            for (Transaccion t : transacciones) {
                System.out.println(t);
            }
        }
    }

    public double getSaldo() {
        return saldo.getMonto();
    }
    
    public void agregarTransaccion(Transaccion transaccion) {
        transacciones.add(transaccion);
    }
    
    public List<Transaccion> getTransacciones() {
        return new ArrayList<>(transacciones);
    }

    /*
    // NUEVO: Método para imprimir factura usando iText
    public void imprimirFactura(Transaccion transaccion, Usuario usuario, String nombreArchivo) {
        Document documento = new Document(PageSize.A4);
        
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            documento.open();
            
            // Colores corporativos de Nequi (morado)
            BaseColor colorNequi = new BaseColor(102, 45, 145);
            BaseColor colorGris = new BaseColor(128, 128, 128);
            
            // ========== ENCABEZADO ==========
            Paragraph encabezado = new Paragraph();
            encabezado.setAlignment(Element.ALIGN_CENTER);
            
            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, colorNequi);
            Chunk titulo = new Chunk("NEQUI", fuenteTitulo);
            encabezado.add(titulo);
            encabezado.add(Chunk.NEWLINE);
            
            Font fuenteSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, colorGris);
            encabezado.add(new Chunk("Comprobante de Transaccion", fuenteSubtitulo));
            documento.add(encabezado);
            
            documento.add(new Paragraph(" "));
            
            // Línea separadora
            LineSeparator linea = new LineSeparator();
            linea.setLineColor(colorNequi);
            documento.add(new Chunk(linea));
            documento.add(new Paragraph(" "));
            
            // ========== INFORMACIÓN DE LA TRANSACCIÓN ==========
            PdfPTable tablaInfo = new PdfPTable(2);
            tablaInfo.setWidthPercentage(100);
            tablaInfo.setSpacingBefore(10f);
            tablaInfo.setSpacingAfter(10f);
            
            Font fuenteEtiqueta = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font fuenteValor = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            
            // Número de transacción
            agregarCeldaFactura(tablaInfo, "Numero de Transaccion:", fuenteEtiqueta);
            agregarCeldaFactura(tablaInfo, transaccion.getNumeroTransaccion(), fuenteValor);
            
            // Fecha y hora
            agregarCeldaFactura(tablaInfo, "Fecha y Hora:", fuenteEtiqueta);
            agregarCeldaFactura(tablaInfo, transaccion.getFechaFormateada(), fuenteValor);
            
            // Tipo de transacción
            agregarCeldaFactura(tablaInfo, "Tipo de Operacion:", fuenteEtiqueta);
            agregarCeldaFactura(tablaInfo, transaccion.getTipo(), fuenteValor);
            
            documento.add(tablaInfo);
            
            // ========== INFORMACIÓN DEL USUARIO ==========
            documento.add(new Paragraph(" "));
            Font fuenteSeccion = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, colorNequi);
            Paragraph seccionUsuario = new Paragraph("DATOS DEL USUARIO", fuenteSeccion);
            documento.add(seccionUsuario);
            documento.add(new Paragraph(" "));
            
            PdfPTable tablaUsuario = new PdfPTable(2);
            tablaUsuario.setWidthPercentage(100);
            
            agregarCeldaFactura(tablaUsuario, "Nombre:", fuenteEtiqueta);
            agregarCeldaFactura(tablaUsuario, usuario.getNombre(), fuenteValor);
            
            agregarCeldaFactura(tablaUsuario, "Cedula:", fuenteEtiqueta);
            agregarCeldaFactura(tablaUsuario, usuario.getCedula(), fuenteValor);
            
            agregarCeldaFactura(tablaUsuario, "Numero de Cuenta:", fuenteEtiqueta);
            agregarCeldaFactura(tablaUsuario, numeroCuenta, fuenteValor);
            
            documento.add(tablaUsuario);
            
            // ========== DETALLES DE LA TRANSACCIÓN ==========
            documento.add(new Paragraph(" "));
            Paragraph seccionDetalle = new Paragraph("DETALLE DE LA TRANSACCION", fuenteSeccion);
            documento.add(seccionDetalle);
            documento.add(new Paragraph(" "));
            
            PdfPTable tablaDetalle = new PdfPTable(2);
            tablaDetalle.setWidthPercentage(100);
            
            agregarCeldaFactura(tablaDetalle, "Descripcion:", fuenteEtiqueta);
            agregarCeldaFactura(tablaDetalle, transaccion.getDescripcion(), fuenteValor);
            
            // Calcular impuesto si aplica
            double montoTransaccion = Math.abs(transaccion.getMonto());
            double impuesto = 0;
            
            if (!transaccion.getTipo().equals("DEPOSITO")) {
                impuesto = montoTransaccion * TASA_4X1000;
            }
            
            agregarCeldaFactura(tablaDetalle, "Monto:", fuenteEtiqueta);
            agregarCeldaFactura(tablaDetalle, "$" + String.format("%.2f", montoTransaccion), fuenteValor);
            
            if (impuesto > 0) {
                agregarCeldaFactura(tablaDetalle, "Impuesto 4x1000:", fuenteEtiqueta);
                agregarCeldaFactura(tablaDetalle, "$" + String.format("%.2f", impuesto), fuenteValor);
            }
            
            documento.add(tablaDetalle);
            
            // ========== TOTAL ==========
            documento.add(new Paragraph(" "));
            PdfPTable tablaTotal = new PdfPTable(2);
            tablaTotal.setWidthPercentage(100);
            
            Font fuenteTotal = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, colorNequi);
            PdfPCell celdaTotalEtiqueta = new PdfPCell(new Phrase("TOTAL:", fuenteTotal));
            celdaTotalEtiqueta.setBorder(Rectangle.NO_BORDER);
            celdaTotalEtiqueta.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaTotalEtiqueta.setPadding(5);
            tablaTotal.addCell(celdaTotalEtiqueta);
            
            double totalTransaccion = montoTransaccion + impuesto;
            PdfPCell celdaTotalValor = new PdfPCell(new Phrase("$" + String.format("%.2f", totalTransaccion), fuenteTotal));
            celdaTotalValor.setBorder(Rectangle.NO_BORDER);
            celdaTotalValor.setHorizontalAlignment(Element.ALIGN_LEFT);
            celdaTotalValor.setPadding(5);
            tablaTotal.addCell(celdaTotalValor);
            
            documento.add(tablaTotal);
            
            // ========== SALDO ACTUAL ==========
            documento.add(new Paragraph(" "));
            PdfPTable tablaSaldo = new PdfPTable(2);
            tablaSaldo.setWidthPercentage(100);
            
            Font fuenteSaldo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            agregarCeldaFactura(tablaSaldo, "Saldo Disponible:", fuenteSaldo);
            agregarCeldaFactura(tablaSaldo, "$" + String.format("%.2f", saldo.getMonto()), fuenteSaldo);
            
            documento.add(tablaSaldo);
            
            // ========== PIE DE PÁGINA ==========
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph(" "));
            documento.add(new Chunk(linea));
            
            Paragraph piePagina = new Paragraph();
            piePagina.setAlignment(Element.ALIGN_CENTER);
            Font fuentePie = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, colorGris);
            piePagina.add(new Chunk("Este documento es un comprobante valido de la transaccion realizada", fuentePie));
            piePagina.add(Chunk.NEWLINE);
            piePagina.add(new Chunk("Generado el: " + 
                java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), 
                fuentePie));
            piePagina.add(Chunk.NEWLINE);
            piePagina.add(new Chunk("Nequi - Tu plata sin tarjeta", fuentePie));
            documento.add(piePagina);
            
            System.out.println("\n✓ Factura generada exitosamente: " + nombreArchivo);
            
        } catch (Exception e) {
            System.err.println("Error al generar la factura: " + e.getMessage());
            e.printStackTrace();
        } finally {
            documento.close();
        }
    }
    
    // Método auxiliar para agregar celdas a las tablas
    private void agregarCeldaFactura(PdfPTable tabla, String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBorder(Rectangle.NO_BORDER);
        celda.setPadding(5);
        tabla.addCell(celda);
    }
}

*/
