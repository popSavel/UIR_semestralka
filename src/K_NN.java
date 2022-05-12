import java.util.ArrayList;

public class K_NN implements Classificator{

    Sentence [] trainData;

    Sentence [] testData;

    String [] vocabulary;

    int K = 3;
    public K_NN(Sentence[] trainData, Sentence[] testData, String[] vocabulary) {
        this.trainData = trainData;
        this.testData = testData;
        this.vocabulary = vocabulary;
    }

    public String[] classify() {
        String [] result = new String[this.testData.length];
        Sentence [] nearest = new Sentence[this.K];
        for(int i = 0; i < result.length; i++){
            nearest = findNearest(this.testData[i]);
            result[i] = mostNeighbours(nearest);
            this.testData[i].classifiedAs = result[i];
        }
        return result;
    }

    @Override
    public String classifyInput(Sentence input) {
        Sentence [] nearest = findNearest(input);
        return mostNeighbours(nearest);
    }

    /**
     * @param nearest pole k nejbližších sousedů
     * @return klasifikační třída s největším zastoupením v poli nearest
     */
    private String mostNeighbours(Sentence[] nearest) {
        ArrayList<ClassificationClass> classes = new ArrayList<ClassificationClass>();
        for(int i = 0; i < nearest.length; i++){
            if(!contain(classes, nearest[i])){
                classes.add(new ClassificationClass(nearest[i].type, 0));
            }
            addNeighbor(classes, nearest[i]);
        }
        return hasMost(classes);
    }

    /**
     * @return typ klasifikační třídy, která má nejvíce prvků
     */
    private String hasMost(ArrayList<ClassificationClass> classes) {
        String result = "";
        int count = 0;
        for(int i = 0; i < classes.size(); i++){
            if(classes.get(i).data.size() > count){
                count = classes.get(i).data.size();
                result = classes.get(i).name;
            }
        }
        return result;
    }

    /**
     * přidá souseda do klasifikační třídy
     */
    private void addNeighbor(ArrayList<ClassificationClass> classes, Sentence sentence) {
        for(int i = 0; i < classes.size(); i++){
            if(classes.get(i).name.equals(sentence.type)){
                classes.get(i).addSentence(sentence);
            }
        }
    }

    /**
     * @return zda v arraylistu classes existuje třída stejného typu jako je sentence
     */
    private boolean contain(ArrayList<ClassificationClass> classes, Sentence sentence) {
        for(int i = 0; i < classes.size(); i++){
            if(classes.get(i).equals(sentence.type)){
                return true;
            }
        }
        return false;
    }

    /**
     * @return pole k nejbližších sousedů prvku current
     * prochází pole a aktuální prvek vždy porovnává s tím nejvzdálenějším z těch co jsou zatím nejbližší
     */
    private Sentence[] findNearest(Sentence current) {
        Sentence [] result = new Sentence[this.K];
        int farthestIndex = 0;
        for(int i = 0; i < this.K; i++){
            result[i] = this.trainData[i];
        }
        farthestIndex = getFarthest(result, current);
        for(int i = 5; i < this.trainData.length; i++){
            if(getDistance(trainData[i], current) < getDistance(result[farthestIndex], current)){
                result[farthestIndex] = trainData[i];
                farthestIndex = getFarthest(result, current);
            }
        }
        return result;
    }

    /**
     * @return prvek z pole train, který má největší euklidovskou vzdálenost od prvku current
     */
    private int getFarthest(Sentence[] train, Sentence current) {
        int result = 0;
        Sentence farthest = train[0];
        for(int i = 1; i < train.length; i++){
            if(getDistance(farthest, current) < getDistance(train[i], current)){
                farthest = train[i];
                result = i;
            }
        }
        return result;
    }

    /**
     * @return euklidovskoá vzdálenost first a second prvku
     */
    private double getDistance(Sentence first, Sentence second) {
        double distance = 0;
        for(int i = 0; i < first.vector.length; i++){
            double difference = first.vector[i] - second.vector[i];
            distance += difference * difference;
        }
        return Math.sqrt(distance);
    }
}
