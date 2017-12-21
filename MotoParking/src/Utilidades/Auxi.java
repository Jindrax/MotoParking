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
import java.util.regex.Pattern;
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
        String moto = "[A-Za-z][A-Za-z][A-Za-z][0-9][0-9][A-Za-z]?";
        String carro = "[A-Za-z][A-Za-z][A-Za-z][0-9][0-9][0-9]";
        String cedula = "(C|c)(C|c)[0-9]+";
        String ticket = "[0-9]+";
        if(Pattern.matches(moto, ingreso)){
            if(Conection.getUsuarioMensual().findUsuarioMensual(ingreso)!=null){
                return 5;
            }else{
                return 1;
            }
        }
        if(Pattern.matches(carro, ingreso)){
            if(Conection.getUsuarioMensual().findUsuarioMensual(ingreso)!=null){
                return 5;
            }else{
                return 2;
            }
        }
        if(Pattern.matches(cedula, ingreso)){
            if(Conection.getUsuarioMensual().findUsuarioMensual(ingreso)!=null){
                return 5;
            }else{
                return 3;
            }
        }
        if(Pattern.matches(ticket, ingreso)){
            return 4;
        }
        return 0;
    }

    public static Cupo calcularTiempoMoto(Cupo cupo) {
        long gracia = 0;
        try{
            gracia = Long.valueOf(Conection.getConfiguraciones().findConfiguraciones("gracia").getValor());
        }catch(Exception e){
            e.printStackTrace();
        }
        gracia *= 1000*60;
        Interval intervalo = new Interval(cupo.getSalida().getTime(), cupo.getSalida().getTime());
        if(cupo.getSalida().getTime()-gracia > cupo.getCupoPK().getIngreso().getTime()){
            intervalo = new Interval(cupo.getCupoPK().getIngreso().getTime(), cupo.getSalida().getTime() - gracia);
        }
        long horas = intervalo.toDuration().getStandardHours();
        long minutos = intervalo.toDuration().getStandardMinutes();
        minutos -= horas * 60;
        Configuraciones mediaHora, unaHora, porHora;
        int tipo = selector(cupo.getPlaca().getPlaca());
        switch(tipo){
            case 1:
                mediaHora = Conection.getConfiguraciones().findConfiguraciones("mediaHoraMoto");
                unaHora = Conection.getConfiguraciones().findConfiguraciones("unaHoraMoto");
                porHora = Conection.getConfiguraciones().findConfiguraciones("porHoraMoto");
                break;
            case 2:
                mediaHora = Conection.getConfiguraciones().findConfiguraciones("mediaHoraCarro");
                unaHora = Conection.getConfiguraciones().findConfiguraciones("unaHoraCarro");
                porHora = Conection.getConfiguraciones().findConfiguraciones("porHoraCarro");
                break;
            case 3:
                mediaHora = Conection.getConfiguraciones().findConfiguraciones("mediaHoraMoto");
                unaHora = Conection.getConfiguraciones().findConfiguraciones("unaHoraMoto");
                porHora = Conection.getConfiguraciones().findConfiguraciones("porHoraMoto");
                break;
            default:
                mediaHora = new Configuraciones("mhDef", "0");
                unaHora = new Configuraciones("uhDef", "0");
                porHora = new Configuraciones("phDef", "0");
                break;                
        }        
        cupo.setHoras(horas);
        cupo.setMinutos(minutos);
        if (minutos == 0) {
            cupo.setCobroSugerido(0);
        } else {
            if (horas == 0) {
                if (minutos < 30) {
                    cupo.setCobroSugerido(Long.parseLong(mediaHora.getValor()));
                } else {
                    cupo.setCobroSugerido(Long.parseLong(unaHora.getValor()));
                }
            } else if (minutos < 30) {
                if(tipo == 2){
                    cupo.setCobroSugerido((Long.parseLong(porHora.getValor()) * horas) + Long.parseLong(mediaHora.getValor()));
                }else{
                    cupo.setCobroSugerido(Long.parseLong(porHora.getValor()) * (horas + 1));                    
                }
            } else {
                cupo.setCobroSugerido(Long.parseLong(porHora.getValor()) * (horas + 1));
            }
        }      
        return cupo;
    }

    public static String[] calcularTiempoMotoTentativo(Cupo cupo) {
        long gracia = 0, entrada = cupo.getCupoPK().getIngreso().getTime(), salida = new Date().getTime() + 1;
        try{
            gracia = Long.valueOf(Conection.getConfiguraciones().findConfiguraciones("gracia").getValor());
        }catch(Exception e){
            e.printStackTrace();
        }
        gracia *= 1000*60;
        Interval intervalo = new Interval(salida, salida);        
        if(salida-gracia > entrada){
            intervalo = new Interval(entrada, salida - gracia);
        }
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
        int tipo = selector(cupo.getPlaca().getPlaca());
        switch(tipo){
            case 1:
                mediaHora = Conection.getConfiguraciones().findConfiguraciones("mediaHoraMoto");
                unaHora = Conection.getConfiguraciones().findConfiguraciones("unaHoraMoto");
                porHora = Conection.getConfiguraciones().findConfiguraciones("porHoraMoto");
                break;
            case 2:
                mediaHora = Conection.getConfiguraciones().findConfiguraciones("mediaHoraCarro");
                unaHora = Conection.getConfiguraciones().findConfiguraciones("unaHoraCarro");
                porHora = Conection.getConfiguraciones().findConfiguraciones("porHoraCarro");
                break;
            case 3:
                mediaHora = Conection.getConfiguraciones().findConfiguraciones("mediaHoraMoto");
                unaHora = Conection.getConfiguraciones().findConfiguraciones("unaHoraMoto");
                porHora = Conection.getConfiguraciones().findConfiguraciones("porHoraMoto");
                break;
            default:
                mediaHora = new Configuraciones("mhDef", "0");
                unaHora = new Configuraciones("uhDef", "0");
                porHora = new Configuraciones("phDef", "0");
                break;                
        }
        cupo.setHoras(horas);
        cupo.setMinutos(minutos);
        if (minutos == 0) {
            cobro = 0;
        } else {
            if (horas == 0) {
                if (minutos < 30) {
                    cobro = Long.parseLong(mediaHora.getValor());
                } else {
                    cobro = Long.parseLong(unaHora.getValor());
                }
            } else if (minutos < 30) {
                if(tipo == 2){
                    cobro = (Long.parseLong(porHora.getValor()) * horas) + Long.parseLong(mediaHora.getValor());
                }else{
                    cobro = Long.parseLong(porHora.getValor()) * (horas + 1);                    
                }
            } else {
                cobro = Long.parseLong(porHora.getValor()) * (horas + 1);
            }
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
