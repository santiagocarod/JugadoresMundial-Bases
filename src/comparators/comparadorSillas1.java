/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparators;

import Mundo.Equipo;
import Mundo.Sillas;
import java.util.Comparator;

/**
 *
 * @author Santiago Caro
 */
public class comparadorSillas1 implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Sillas e1=(Sillas) o1;
        Sillas e2=(Sillas) o2;
        int i=Integer.parseInt(String.valueOf(e1.getSillasPK().getNumfila()));
        int j=Integer.parseInt(String.valueOf(e2.getSillasPK().getNumfila()));
        return i-j;
    }
}
