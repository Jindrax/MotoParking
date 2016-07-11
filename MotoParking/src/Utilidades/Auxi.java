/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilidades;

import GUI.Conection;
import Negocio.Configuraciones;
import Negocio.Cupo;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 *
 * @author santiago pc
 */
public class Auxi {

    private static boolean esNumero(char l) {
        if (l > 47 && l < 58) {
            return true;
        }
        return false;
    }

    public static int selector(String ingreso) {
        try {
            char inicio = ingreso.charAt(0),
                    fin = ingreso.charAt(ingreso.length() - 1);
            if (ingreso.length() > 6) {
                return 1;
            }
            if (!esNumero(inicio) && !esNumero(fin)) {
                if(Conection.getUsuarioMensual().findUsuarioMensual(ingreso)!=null){
                    return 5;
                }
                return 1;
            }
            if (ingreso.length()==5){
                if(Conection.getUsuarioMensual().findUsuarioMensual(ingreso)!=null){
                    return 5;
                }
                return 1;
            }
            if (!esNumero(inicio) && esNumero(fin)) {
                return 2;
            }
            return 3;
        } catch (StringIndexOutOfBoundsException ex) {
            return 4;
        }
    }

    public static void calcularTiempoMoto(Cupo cupo) {
        Interval intervalo = new Interval(cupo.getCupoPK().getIngreso().getTime(), cupo.getSalida().getTime());
        long horas = intervalo.toDuration().getStandardHours();
        long minutos = intervalo.toDuration().getStandardMinutes();
        minutos -= horas * 60;
        Configuraciones mediaHora, unaHora, porHora;
        mediaHora = Conection.getConfiguraciones().findConfiguraciones("mediaHoraMoto");
        unaHora = Conection.getConfiguraciones().findConfiguraciones("unaHoraMoto");
        porHora = Conection.getConfiguraciones().findConfiguraciones("porHoraMoto");
        cupo.setHoras(horas);
        cupo.setMinutos(minutos);
        if (horas == 0) {
            if (minutos < 30) {
                cupo.setCobroSugerido(Long.parseLong(mediaHora.getValor()));
            } else {
                cupo.setCobroSugerido(Long.parseLong(unaHora.getValor()));
            }
        } else if (minutos < 30) {
            cupo.setCobroSugerido(Long.parseLong(porHora.getValor()) * (horas + 1));
        } else {
            cupo.setCobroSugerido(Long.parseLong(porHora.getValor()) * (horas + 1));
        }
    }

    public static String[] calcularTiempoMotoTentativo(Cupo cupo) {
        Interval intervalo = new Interval(cupo.getCupoPK().getIngreso().getTime(), new Date().getTime() + 1);
        Period period = intervalo.toPeriod();
        PeriodFormatter minutesAndSeconds = new PeriodFormatterBuilder()
                .printZeroAlways()
                .appendHours()
                .appendSeparator(":")
                .appendMinutes()
                .toFormatter();
        String result = minutesAndSeconds.print(period);
        long horas = intervalo.toDuration().getStandardHours();
        long minutos = intervalo.toDuration().getStandardMinutes();
        long cobro = 0;
        minutos -= horas * 60;
        Configuraciones mediaHora, unaHora, porHora;
        mediaHora = Conection.getConfiguraciones().findConfiguraciones("mediaHoraMoto");
        unaHora = Conection.getConfiguraciones().findConfiguraciones("unaHoraMoto");
        porHora = Conection.getConfiguraciones().findConfiguraciones("porHoraMoto");
        cupo.setHoras(horas);
        cupo.setMinutos(minutos);
        if (horas == 0) {
            if (minutos < 30) {
                cobro = Long.parseLong(mediaHora.getValor());
            } else {
                cobro = Long.parseLong(unaHora.getValor());
            }
        } else if (minutos < 30) {
            cobro = Long.parseLong(porHora.getValor()) * (horas + 1);
        } else {
            cobro = Long.parseLong(porHora.getValor()) * (horas + 1);
        }
        String retorno[] = {result, String.valueOf(cobro)};
        return retorno;
    }

    public static String formaterHora(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        return format.format(date);
    }

    public static String formaterFecha(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
        return format.format(date);
    }
}
