import java.util.ArrayList;

public class ClassificationClass {

    String name;
    int trainDataSize;

    //věty patřící do této klasifikační třídy
    ArrayList <Sentence> data;

    double [] probabilities;

    public ClassificationClass(String type, int trainSize) {
        this.name = type;
        this.data = new ArrayList<Sentence>();
        this.trainDataSize = trainSize;
    }

    public void addSentence(Sentence sentence) {
        data.add(sentence);
    }

    /**
     * do vektoru probabilities spočítá pravdděpodobnosti slov pro tuto klasifikační třídu
     * @param vocabulary
     */
    public void countProbabilities(String[] vocabulary) {
        this.probabilities = new double[vocabulary.length];
        double totalProb = 0;
        for(int i = 0; i < this.data.size(); i++){
            Sentence curr = data.get(i);
            for (int j = 0; j < vocabulary.length; j++){
                probabilities[j] += curr.vector[j];
                totalProb += curr.vector[j];
            }
        }
        double denominator = totalProb + vocabulary.length;
        for(int i = 0 ; i < probabilities.length; i++){
            double numerator = probabilities[i] + 1;
            probabilities[i] = numerator/denominator;
        }
    }

    /**
     * @return pravděpodobnost, že Sentence curr patří do této klasifikační třídy
     */
    public double countProbability(Sentence curr, String[] vocabulary) {
        double result = data.size()/(double)trainDataSize;

        for(int i = 0; i < vocabulary.length; i++) {
            if (curr.contains(vocabulary[i])) {
                    result *= probabilities[i];
            }
        }
        return result;
    }
}
