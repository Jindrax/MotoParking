/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilidades;

import Negocio.Locker;
import java.util.Comparator;

/**
 *
 * @author Santiago
 */
public class CustomComparator implements Comparator{

    @Override
    public int compare(Object t, Object t1) {
        String s1 = ((Locker)t).getIdentificador();
        String s2 = ((Locker)t1).getIdentificador();
        int i=0;
        while(!s1.equals(s2)){
            if(s1.length() < s2.length()){
                return -1;
            }else if(s1.length() > s2.length()){
                return 1;
            }else if(s1.charAt(i) < s2.charAt(i)){
                return -1;
            }else if(s1.charAt(i) > s2.charAt(i)){
                return 1;
            }
            i++;
        }
        return 0;
    }
    
}
