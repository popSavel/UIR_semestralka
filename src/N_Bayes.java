import java.util.ArrayList;

public class N_Bayes {

    Sentence [] trainData;

    Sentence [] testData;

    String [] vocabulary;

    String [] result;

    ClassificationClass [] classes;
    public N_Bayes(Sentence[] trainData, Sentence[] testData, String[] vocabulary) {
        this.trainData = trainData;
        this.testData = testData;
        this.vocabulary = vocabulary;
        sortData(this.trainData);
        train();
        result = classify(this.testData);
    }

    private String[] classify(Sentence[] testData) {
        String [] result = new String[testData.length];
        for(int i = 0; i < result.length; i++){
            Sentence curr = testData[i];
            double [] probabilities = new double[classes.length];
            for(int j = 0; j < probabilities.length; j++){
                probabilities[j] = classes[j].countProbability(curr, vocabulary);
            }
            int largest = largest(probabilities);
            curr.classifiedAs = classes[largest].name;
        }
        return result;
    }

    private int largest(double[] probabilities) {
        int result = 0;
        double largest = 0;
        for(int i = 0; i < probabilities.length; i++){
            if(probabilities[i] > largest){
                largest = probabilities[i];
                result = i;
            }
        }
        return result;
    }

    private void train() {
        for(int i = 0; i < classes.length; i++){
            ClassificationClass curr = classes[i];
            curr.countProbabilities(vocabulary);
        }
    }

    private void sortData(Sentence[] trainData) {
        ArrayList<ClassificationClass> classes = new ArrayList<ClassificationClass>();
        for(int i = 0; i < trainData.length; i++){
            Sentence curr = trainData[i];
            if(!classExist(classes, curr)){
                classes.add(new ClassificationClass(curr.type, trainData.length));
            }
            addSentence(curr, classes);
        }
        this.classes = new ClassificationClass[classes.size()];
        for(int i = 0; i < classes.size(); i++){
            this.classes[i] = classes.get(i);
        }
    }

    private void addSentence(Sentence curr, ArrayList<ClassificationClass> classes) {
        for(int i = 0; i < classes.size(); i++){
            if(classes.get(i).name.equals(curr.type)){
                classes.get(i).addSentence(curr);
            }
        }
    }

    private boolean classExist(ArrayList<ClassificationClass> classes, Sentence curr) {
        if(classes.size() < 1){
            return false;
        }else{
            for(int i = 0; i < classes.size(); i++){
                if(classes.get(i).name.equals(curr.type)){
                    return true;
                }
            }
            return false;
        }
    }
}
