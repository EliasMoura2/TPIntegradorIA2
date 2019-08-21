package som;

import java.util.ArrayList;

public class SOM {

    private ArrayList<Double[]> pesos = new ArrayList<Double[]>();
    private Double factorDeAprendizaje;
    private int numeroDeIteraciones;
    private Double lamda;// es un valor constante que hace que el radio y el factor de aprendizaje se reduzcan no linealmente
    private int funcionDeV;

    public SOM(int numeroDeIteraciones, ArrayList<Double[]> InicializacionDePesos, Double factorDeAprendizaje,int funVec) {
        this.factorDeAprendizaje = factorDeAprendizaje;
        this.funcionDeV = funVec;
        for (int i = 0; i < InicializacionDePesos.size(); i++) {
            Double[] get = InicializacionDePesos.get(i);

            this.pesos.add(get);
        }
        this.numeroDeIteraciones = numeroDeIteraciones;
        this.lamda = numeroDeIteraciones / (Math.log1p(numeroDeIteraciones));
    }

    public void entrenar(ArrayList<Double[]> patronesEntrada) {
        Double distanciaEuclidiana = 0.0;
        Double distanciaEuclidianaMin = 50000.0;
        int ganadora = 0;
        int neuronaCalulada = 0;
        for (int p = 0; p < numeroDeIteraciones; p++) {
            for (int i = 0; i < patronesEntrada.size(); i++) {
                // sacamos uno a uno los patrones de entrada para realizar la competencia
                //y asi determinar que neurona de la capa de salida resulta ganadora por cada patron
                Double[] unPatron = patronesEntrada.get(i);

                for (int j = 0; j < this.getPesos().size(); j++) {
                    for (int k = 0; k < unPatron.length; k++) {
                        distanciaEuclidiana = distanciaEuclidiana + Math.pow(unPatron[k] - this.getPesos().get(j)[k], 2);
                    }
                    if (distanciaEuclidiana < distanciaEuclidianaMin) {
                        distanciaEuclidianaMin = distanciaEuclidiana;
                        ganadora = neuronaCalulada;
                    }
                    neuronaCalulada++;
                    distanciaEuclidiana = 0.0;
                }
                neuronaCalulada = 0;
                // termina comeptencia para el patron 1 : se debe definir ganadora y actualizar los pesos
                //el metodo actualizar utiiza la neurona ganadora y el patron usado en la competencia para 
                // actualizar los pesos ----> por el momento se concidera funcion de vecindad nula
                distanciaEuclidianaMin = 50000.0;
                if(funcionDeV==1){
                actualizarPesosSimple(ganadora, unPatron);
                }else if(funcionDeV==2){
                actualizarPesosFV(p, ganadora, unPatron);
                }
                
                

            }
        }

    }

    public int clasificar() {

        return 5;
    }

    private void actualizarPesosFV(int p, int ganadora, Double[] unPatron) {
        // la variable "p" represanta el numero de iteracion
        Double d;// esta variable representa la distancia entre el nodo que se va a actualizar y em BMU
        Double radio;
        Double funcionVecindad = 0.0;

        radio = numeroDeIteraciones * Math.pow(Math.E, -(p / this.lamda));
        Double  facAprendizaje = this.factorDeAprendizaje * Math.pow(Math.E, -(p / this.lamda));
        
        
        
        for (int i = 0; i < pesos.size(); i++) {
            Double[] get = pesos.get(i);
            d = calcularDistanciaConBMU(ganadora, get);
            for (int j = 0; j < get.length; j++) {
                Double double1 = get[j];
                
                funcionVecindad = (Math.pow(Math.E, -((Math.pow(d, 2)) / (2 * Math.pow(radio, 2)))));
                double1 = double1 + (funcionVecindad * facAprendizaje * (unPatron[j] - double1));
                get[j]=double1;
            }
        }
    }

    /**
     * @return the pesos
     */
    public ArrayList<Double[]> getPesos() {
        return pesos;
    }

    private Double calcularDistanciaConBMU(int ganadora, Double[] get) {
        Double distancia = 0.0;
        for (int i = 0; i < pesos.get(ganadora).length; i++) {
            distancia = distancia + Math.pow(pesos.get(ganadora)[i] - get[i], 2);
        }
        return distancia;
    }

    private void actualizarPesosSimple(int ganadora, Double[] unPatron) {
        for (int i = 0; i < this.getPesos().get(ganadora).length; i++) {
            this.pesos.get(ganadora)[i] = this.getPesos().get(ganadora)[i] + this.factorDeAprendizaje * (unPatron[i] - this.getPesos().get(ganadora)[i]);
        }
    }

}
