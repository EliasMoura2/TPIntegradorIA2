package som;

import java.util.ArrayList;

public class SOM {

    private ArrayList<Double[]> pesos = new ArrayList<Double[]>();
    private Double alfaInicial;
    private int numeroDeIteraciones;
    private Double lambda;// es un valor constante que hace que el radio y el factor de aprendizaje se reduzcan no linealmente
    private int tipoFuncionDeV;// este valor indica si se va a usar funcion de vecindad nula o gaussiana
    private  Double radioInicial=0.0;

    public SOM(int numeroDeIteraciones, ArrayList<Double[]> InicializacionDePesos, Double factorDeAprendizaje,int funVec) {
        this.alfaInicial = factorDeAprendizaje;
        this.tipoFuncionDeV = funVec;
        // se completa la matriz de pesos con los pesos iniciales
        for (int i = 0; i < InicializacionDePesos.size(); i++) {
            Double[] get = InicializacionDePesos.get(i);
            this.pesos.add(get);
        }
        this.radioInicial=CalcularRadioInicial();// funcion que calcula la distancia euclidiana maxima entre todos los patrones de entrada
                                                 // y establece como radio inicial a dicha distancia
        this.numeroDeIteraciones = numeroDeIteraciones;
        this.lambda = numeroDeIteraciones / 2*(Math.log1p(radioInicial));// es un valor constante que hace que el
                                                                        // radio y el factor de aprendizaje se reduzcan no linealmente
    }

    public void entrenar(ArrayList<Double[]> patronesEntrada) {// este metodo contiene las etapas de competencia y actualizacion de la red
        Double distanciaEuclidiana = 0.0;// variable que almacena la distancia euclidiana calculada en cada iteracion
        Double distanciaEuclidianaMin = 50000.0; // variable que almacena la distancia euclidiana minima para cada fase de competencia
        int ganadora = 0;// variable que almacena el indice de la neurona ganadora en cada fase de competencia
        int neuronaCalulada = 0; // variable que almacena el indice de la neurona que se esta evaluando
        
        for (int p = 0; p < numeroDeIteraciones; p++) {// bucle que define la cantidad de epocas ( se establece desde IGU)
            for (int i = 0; i < patronesEntrada.size(); i++) {// sacamos uno a uno los patrones de entrada para realizar la competencia
                                                              //y asi determinar que neurona de la capa de salida resulta ganadora por cada patron
                Double[] unPatron = patronesEntrada.get(i);
                for (int j = 0; j < this.getPesos().size(); j++) {// por cada neurona de la capa de salida
                    for (int k = 0; k < unPatron.length; k++) {
                        distanciaEuclidiana = distanciaEuclidiana + Math.pow(unPatron[k] - this.getPesos().get(j)[k], 2);
                        // se calcula la distancia euclidea
                    }
                    if (distanciaEuclidiana < distanciaEuclidianaMin) {// si la distancia es mejor 
                        distanciaEuclidianaMin = distanciaEuclidiana;// se almacena como distancia minima 
                        ganadora = neuronaCalulada;// y se almacena el indice de la neurona correspondiente a la distancia minima
                    }
                    neuronaCalulada++;// se incrementa el indice de la neurona evaluada 
                    distanciaEuclidiana = 0.0;// se reinicia la distancia euclidiana
                }
                neuronaCalulada = 0;// se reinicia la neurona calculada
                distanciaEuclidianaMin = 50000.0;// se establece una distancia eclidea minima alta para reiniciar las distancias
                // termina comeptencia para el patron [i] : queda definida la neurona ganadora
                if(tipoFuncionDeV==1){// opcion que se determina en IGU
                actualizarPesosSimple(ganadora, unPatron,p);// actualiza los pesos de la neurona (funcion de vecindad nula)
                }else if(tipoFuncionDeV==2){// opcion que se determina en IGU
                actualizarPesosFV(p, ganadora, unPatron);// actualiza los pesos de las neuronas (funcion de vecindad gaussiana)
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
       Double funcionVecindad = 0.0;
       Double radio = radioInicial * Math.pow(Math.E, -(p / this.lambda));
       Double  alfa = this.alfaInicial * Math.pow(Math.E, -(p / this.lambda));

        for (int i = 0; i < this.pesos.size(); i++) {// para cada neurona de la capa de salida
            Double[] get = this.pesos.get(i);
            d = calcularDistanciaConBMU(ganadora, get);// calculamos la distancia con la ganadora
            d = Math.sqrt(d);// se aplica raiz cuadrada por que la funcion anterior no lo hace
            for (int j = 0; j < get.length; j++) {
               funcionVecindad = (Math.pow(Math.E, -((Math.pow(d, 2)) / (2 * Math.pow(radio, 2)))));// vecindad gaussiana
               this.pesos.get(i)[j]= this.pesos.get(i)[j] + (funcionVecindad * alfa * (unPatron[j] - this.pesos.get(i)[j]));// actualizacion
            }
        }
    }

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

    private void actualizarPesosSimple(int ganadora, Double[] unPatron,int p) {
      
        Double alfa = this.alfaInicial /(p+1);// utiliza un alfa variable de la forma (alfaInicial / t) --> t = numero de iteracion
        
        for (int i = 0; i < this.getPesos().get(ganadora).length; i++) {
            this.pesos.get(ganadora)[i] = this.getPesos().get(ganadora)[i] + alfa * (unPatron[i] - this.getPesos().get(ganadora)[i]);
        }
    }

    private Double CalcularRadioInicial() {
        
        Double distanciaMax = 0.0;
        Double distancia = 0.0;
        for (int i = 0; i < this.pesos.size(); i++) {
            for (int j = 0; j < this.pesos.size(); j++) { 
                
                Double[] vec = this.pesos.get(j);
                distancia = calcularDistanciaConBMU(i, vec);
                distancia = Math.sqrt(distancia);
                if(distancia >= distanciaMax){
                    distanciaMax = distancia;
                }
            }
        }
        return distanciaMax;
    }

}
