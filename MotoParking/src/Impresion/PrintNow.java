package Impresion;

import GUI.Conection;
import Negocio.Cupo;
import Utilidades.Auxi;
import Negocio.CobroMensual;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import Impresion.LineaRecibo;
import Negocio.Configuraciones;

public class PrintNow {

    private static List<LineaRecibo> recibo = null;

    public static void printCard(final List<LineaRecibo> Bill) throws PrinterException {
        final PrinterJob job = PrinterJob.getPrinterJob();
        Printable contentToPrint = new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(new File("log6464.png"));
                } catch (IOException e) {
                }
                pageFormat.setOrientation(PageFormat.PORTRAIT);
                Paper pPaper = pageFormat.getPaper();
                pPaper.setImageableArea(15, 0, 150, pPaper.getHeight());
                pageFormat.setPaper(pPaper);
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.setFont(new Font("Times New Roman", Font.BOLD, 12));
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                } //Only one page
                int y = 10;
                g2d.drawImage(img, 36, y, null);
                y += 84;
                for (LineaRecibo next : Bill) {
                    g2d.setFont(new Font("Times New Roman", next.mod, next.size));
                    g2d.drawString(next.linea, 0, y);
                    y = y + 15;
                }
                return PAGE_EXISTS;
            }
        };
        PageFormat pageFormat = new PageFormat();
        pageFormat.setOrientation(PageFormat.PORTRAIT);
        Paper pPaper = pageFormat.getPaper();
        pPaper.setImageableArea(15, 0, 150, pPaper.getHeight());
        pageFormat.setPaper(pPaper);
        job.setPrintable(contentToPrint, pageFormat);
        //boolean don = job.printDialog();
        try {
            job.print();
        } catch (PrinterException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void encabezado() {
        addLinea("    MotoParqueo 259", Font.BOLD, 14);
        addLinea("            Luz Stella Garcia Campos", Font.PLAIN, 8);
        addLinea("    39554400-2" + " " + "Regimen Simplificado", Font.PLAIN, 8);
        try{
            String polizaReg = Conection.getConfiguraciones().findConfiguraciones("polizaReg").getValor(),
                    polizaHead = Conection.getConfiguraciones().findConfiguraciones("polizaHeader").getValor();
            addLinea(polizaHead, Font.PLAIN, 8);
            addLinea(polizaReg, Font.PLAIN, 8);
        }catch(Exception e){
            e.printStackTrace();
        }
        addLinea("");
    }
    
    public static void pie() {
        addLinea("Contacto:");
        Configuraciones contacto = Conection.getConfiguraciones().findConfiguraciones("contacto");
        StringTokenizer token = new StringTokenizer(contacto.getValor(), "//");
        while(token.hasMoreTokens()){
            addLinea(token.nextToken());
        }        
        addLinea("Cll. 9 #2-59");
    }

    public static void imprimirReciboEntrada(Cupo cupo) {
        String horaE = Auxi.formaterHora(cupo.getCupoPK().getIngreso()), horaCierre, cierreSize;
        try{
            horaCierre = Conection.getConfiguraciones().findConfiguraciones("horaCierre").getValor();
            cierreSize = Conection.getConfiguraciones().findConfiguraciones("cierreSize").getValor();
        }catch(Exception e){
            e.printStackTrace();
            horaCierre = "";
            cierreSize = "0";
        }
        recibo = new ArrayList<LineaRecibo>();
        encabezado();
        if (new GregorianCalendar().get(GregorianCalendar.HOUR_OF_DAY) >= 12) {
            if (new GregorianCalendar().get(GregorianCalendar.HOUR_OF_DAY) < 19) {
                addLinea("Buenas Tardes", Font.BOLD, 14);
            } else {
                addLinea("Buenas Noches", Font.BOLD, 14);
            }
        } else {
            addLinea("Buenos Dias", Font.BOLD, 14);
        }
        addLinea("");
        addLinea(Auxi.formaterFecha(cupo.getCupoPK().getIngreso()) + "       " + String.valueOf(horaE), Font.BOLD, 14);
        addLinea("Placa:", Font.BOLD, 14);
        addLinea(String.valueOf(cupo.getPlaca().getPlaca()), Font.BOLD, 14);
        addLinea("");
        addLinea("Recibo: " + String.valueOf(cupo.getCupoPK().getConsecutivo()), Font.BOLD, 16);
        if (cupo.getLocker() != null) {
            addLinea("Casco(s): " + String.valueOf(cupo.getLocker().getIdentificador()) + "-" + String.valueOf(cupo.getLocker().getAlojamiento()), Font.BOLD, 14);
        } else {
            addLinea("Ningun Casco", Font.BOLD, 14);
        }
        pie();
        addLinea("Hoy servicio hasta", Font.BOLD, Integer.parseInt(cierreSize));
        addLinea(horaCierre, Font.BOLD, Integer.parseInt(cierreSize)+4);
        try {
            printCard(recibo);
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    public static void imprimirReciboSalida(Cupo cupo, long cobro) {
        String horaE = Auxi.formaterHora(cupo.getCupoPK().getIngreso()), horaCierre, cierreSize;
        String horaS = Auxi.formaterHora(cupo.getSalida());
        try{
            horaCierre = Conection.getConfiguraciones().findConfiguraciones("horaCierre").getValor();
            cierreSize = Conection.getConfiguraciones().findConfiguraciones("cierreSize").getValor();
        }catch(Exception e){
            e.printStackTrace();
            horaCierre = "";
            cierreSize = "0";
        }
        recibo = new ArrayList<LineaRecibo>();
        encabezado();
        if (new GregorianCalendar().get(GregorianCalendar.HOUR_OF_DAY) >= 12) {
            if (new GregorianCalendar().get(GregorianCalendar.HOUR_OF_DAY) < 19) {
                addLinea("Buenas Tardes", Font.BOLD, 14);
            } else {
                addLinea("Buenas Noches", Font.BOLD, 14);
            }
        } else {
            addLinea("Buenos Dias", Font.BOLD, 14);
        }
        addLinea("");
        addLinea(Auxi.formaterFecha(cupo.getCupoPK().getIngreso()), Font.BOLD, 14);
        addLinea("Placa:", Font.BOLD, 14);
        addLinea(String.valueOf(cupo.getPlaca().getPlaca()), Font.BOLD, 14);
        if (cupo.getLocker() != null) {
                addLinea("Casco(s): " + String.valueOf(cupo.getLocker().getIdentificador()), Font.BOLD, 14);
            } else {
                addLinea("Ningun Casco", Font.BOLD, 14);
            }
        addLinea(horaE + "---->" + horaS, Font.BOLD, 14);
        addLinea(String.format("%d:%d", cupo.getHoras(), cupo.getMinutos()), Font.BOLD, 14);
        addLinea("Total: $" + cobro, Font.BOLD, 14);
        pie();
        addLinea("Hoy servicio hasta", Font.BOLD, Integer.parseInt(cierreSize));
        addLinea(horaCierre, Font.BOLD, Integer.parseInt(cierreSize)+4);
        try {
            printCard(recibo);
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    private static void addLinea(String linea) {
        recibo.add(new LineaRecibo(linea));
    }

    private static void addLinea(String linea, int mod, int size) {
        recibo.add(new LineaRecibo(linea, mod, size));
    }

    public static void printReciboMensual(CobroMensual cupo) {
        String fechaAnt = Auxi.formaterFecha(cupo.getDesde());
        long mensualidad = cupo.getCobro();
        recibo = new ArrayList<LineaRecibo>();
        encabezado();
        addLinea(Auxi.formaterFecha(new GregorianCalendar().getTime()), Font.BOLD, 14);
        String nombre = "";
        StringTokenizer token = new StringTokenizer(cupo.getUsuarioMensual().getNombre(), " ");
        int i = 2;
        do {
            if (i > 0) {
                nombre = nombre.concat(token.nextToken());
                nombre = nombre.concat(" ");
                i--;
            } else {
                i = 2;
                addLinea(nombre, Font.BOLD, 10);
                nombre = "";
            }
        } while (token.hasMoreTokens());
        if (!nombre.equals("")) {
            addLinea(nombre, Font.BOLD, 10);
        }
        addLinea("Ha pagado a:");
        addLinea("Luz Stella Garcia Campos");
        addLinea("$" + String.valueOf(mensualidad), Font.BOLD, 10);
        addLinea("Por servicio de parqueadero de:");
        addLinea("moto placa:");
        addLinea(cupo.getUsuarioMensual().getPlaca(), Font.BOLD, 10);
        addLinea("Desde:");
        addLinea(fechaAnt, Font.BOLD, 10);
        addLinea("Hasta:");
        addLinea(Auxi.formaterFecha(cupo.getHasta()), Font.BOLD, 10);
        addLinea("");
        pie();
        try {
            printCard(recibo);
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    public static void printResumenDia(Date fecha, String first, String last, String total, List<CobroMensual> mensualidades) {
        recibo = new ArrayList<LineaRecibo>();
        long totalMensual = 0;
        encabezado();
        addLinea(Auxi.formaterFecha(fecha), Font.BOLD, 14);
        addLinea("Desde:", Font.BOLD, 14);
        addLinea(first, Font.BOLD, 14);
        addLinea("Hasta:", Font.BOLD, 14);
        addLinea(last, Font.BOLD, 14);
        addLinea("Con un total de:", Font.BOLD, 14);
        addLinea(total, Font.BOLD, 14);
        if(mensualidades.size()>0){
            addLinea("");
            addLinea("Mensualidades:", Font.BOLD, 14);
            for(CobroMensual cobro: mensualidades){
                addLinea(String.format("%s %d", cobro.getCobroMensualPK().getPlaca(), cobro.getCobro()) , Font.PLAIN, 12);
                totalMensual += cobro.getCobro();
            }
            addLinea(String.format("Total: %d", totalMensual), Font.BOLD, 14);
        }
        pie();
        try {
            printCard(recibo);
        } catch (PrinterException e) {
            e.printStackTrace();
        }        
    }
}
