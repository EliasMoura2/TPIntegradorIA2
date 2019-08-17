package som;

import java.util.ArrayList;

public class SOM {

    private ArrayList<Double[]> pesos = new ArrayList<Double[]>();
    private int cantidadNeuronasSalida;
    private Double factorDeAprendizaje;

    public SOM(int cantidadNeuronasSalida, ArrayList<Double[]> InicializacionDePesos,Double factorDeAprendizaje) {
        this.cantidadNeuronasSalida = cantidadNeuronasSalida;
        this.factorDeAprendizaje = factorDeAprendizaje;
        for (int i = 0; i < InicializacionDePesos.size(); i++) {
            Double[] get = InicializacionDePesos.get(i);
            this.pesos.add(get);
        }
    }

    public void entrenar(ArrayList<Double[]> patronesEntrada) {
        Double distanciaEuclidiana = 0.0;
        Double distanciaEuclidianaMin = 50000.0;
        int ganadora = 0;
        int neuronaCalulada = 0;

        for (int i = 0; i < patronesEntrada.size(); i++) {
            // sacamos uno a uno los patrones de entrada para realizar la competencia
            //y asi determinar que neurona de la capa de salida resulta ganadora por cada patron
            Double[] unPatron = patronesEntrada.get(i);

            for (int j = 0; j < this.getPesos().size(); j++) {
                for (int k = 0; k < unPatron.length; k++) {
                    distanciaEuclidiana = distanciaEuclidiana + unPatron[k] - this.getPesos().get(j)[k];
                }
                if (distanciaEuclidiana < distanciaEuclidianaMin) {
                    distanciaEuclidianaMin = distanciaEuclidiana;
                    ganadora = neuronaCalulada;
                }
                neuronaCalulada++;
                distanciaEuclidiana = 0.0;
            }
                neuronaCalulada=0;
            // termina comeptencia para el patron 1 : se debe definir ganadora y actualizar los pesos
            //el metodo actualizar utiiza la neurona ganadora y el patron usado en la competencia para 
            // actualizar los pesos ----> por el momento se concidera funcion de vecindad nula
            actualizarPesos(ganadora,unPatron);

        }

    }

    public int clasificar() {

        return 5;
    }

    private void actualizarPesos(int ganadora, Double[] unPatron) {

        for (int i = 0; i < this.getPesos().get(ganadora).length; i++) {
            this.pesos.get(ganadora)[i]= this.getPesos().get(ganadora)[i]+ this.factorDeAprendizaje * (unPatron[i]-this.getPesos().get(ganadora)[i]);
        }
        System.out.println(this.pesos.get(ganadora)[0]+"//"+this.pesos.get(ganadora)[1]);
                
    }

    /**
     * @return the pesos
     */
    public ArrayList<Double[]> getPesos() {
        return pesos;
    }

}
