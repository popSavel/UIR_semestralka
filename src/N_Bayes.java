import java.util.ArrayList;

public class N_Bayes {

    Sentence [] trainData;

    ClassificationClass [] classes;
    public N_Bayes(Sentence[] trainData) {
        this.trainData = trainData;
        sortData(this.trainData);
    }

    private void sortData(Sentence[] trainData) {
        ArrayList<ClassificationClass> classes = new ArrayList<ClassificationClass>();
        for(int i = 0; i < trainData.length; i++){
            Sentence curr = trainData[i];
            if(!classExist(classes, curr)){
                classes.add(new ClassificationClass(curr.type));
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
