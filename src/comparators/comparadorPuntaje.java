/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparators;

import Mundo.Equipo;
import java.util.Comparator;

/**
 *
 * @author Santiago Caro
 */
public class comparadorPuntaje implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Equipo e1=(Equipo) o1;
        Equipo e2=(Equipo) o2;
        int i=Integer.parseInt(String.valueOf(e1.getPuntaje()));
        int j=Integer.parseInt(String.valueOf(e2.getPuntaje()));
        return i-j;
    }
    
}
