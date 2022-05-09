import java.util.ArrayList;

public class BagOfWords implements Feature{

    Sentence[] sentences;

    String [] vocabulary;
    public BagOfWords(Sentence[] trainData, String[] vocabulary) {
        this.sentences = trainData;
        this.vocabulary = vocabulary;
    }

    public double[] makeVector(Sentence sentence) {
        double [] result = new double[vocabulary.length];
        for(int i = 0; i < vocabulary.length; i++){
            String word = vocabulary[i];
            result[i] = occurence(word, sentence);
        }
        return result;
    }

    public int occurence(String word, Sentence sentence) {
        int result = 0;
        for(int i = 0; i < sentence.words.length; i++){
            if(sentence.words[i].equals(word)){
                result++;
            }
        }
        return result;
    }

    @Override
    public void feature() {
        for(int i = 0; i < this.sentences.length; i++){
            this.sentences[i].vector = makeVector(this.sentences[i]);
        }
    }
}
