
package som;

import Vista.JFramePrincipal;
import java.util.ArrayList;
import javax.swing.JFrame;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

public class main {

    public static void main(String[] args) {
        //JFrame principal = new JFramePrincipal();
       // principal.setVisible(true);
        ArrayList<Double[]> inicializacion = new ArrayList<Double[]>();
        ArrayList<Double[]> Entrada = new ArrayList<Double[]>();

        inicializacion.add(new Double[]{0.5,-0.3});
        inicializacion.add(new Double[]{-0.5,0.8});
        inicializacion.add(new Double[]{-0.9,-0.7});
        inicializacion.add(new Double[]{-0.2,-0.8});
        
            Entrada.add(new Double[]{-0.54,0.36});
            Entrada.add(new Double[]{0.16,0.7});
            Entrada.add(new Double[]{-0.8,-0.18});
            Entrada.add(new Double[]{-0.36,-0.52});
            Entrada.add(new Double[]{-0.64,0.46});
            Entrada.add(new Double[]{-0.40,0.34});
            Entrada.add(new Double[]{-0.54,0.36});
       
       SOM red = new SOM(2, inicializacion, 1.0);
       red.entrenar(Entrada);
       
       
                   System.out.println("------------------------------------");
            System.out.println("PESOS FINALES:");
        for (int i = 0; i < red.getPesos().size(); i++) {
            Double[] get = red.getPesos().get(i);

            System.out.println(get[0]+"//"+get[1]);
        }
    }
    
}
