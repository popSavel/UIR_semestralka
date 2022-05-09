import java.util.ArrayList;

public class TF_IDF implements Feature{

    Sentence [] sentences;

    String [] vocabulary;

    double [] idf;
    public TF_IDF(Sentence[] trainData, String[] vocabulary) {
        this.sentences = trainData;
        this.vocabulary = vocabulary;
    }

    private double[] makeIdf() {
        double [] result = new double[this.vocabulary.length];
        for(int i = 0; i < vocabulary.length; i++){
            String currWord = vocabulary[i];
            double occurences = 0;
            for(int j = 0; j < sentences.length; j++){
                if(sentences[j].contains(currWord)){
                    occurences++;
                }
            }
            result[i] = Math.log(sentences.length/occurences);
        }
        return result;
    }

    private void tf() {
        for(int i = 0; i < sentences.length; i++){
            Sentence curr = sentences[i];
            double length = curr.words.length;
            double [] tf_idf = new double[vocabulary.length];
            for(int j = 0; j < vocabulary.length; j++){
                int occurenses = occurence(curr.words, vocabulary[j]);
                Double tf = occurenses/length;
                tf_idf[j] = tf * this.idf[j];
            }
            curr.vector = tf_idf;
        }
    }

    private int occurence(String[] words, String word) {
        int result = 0;
        for(int i = 0; i < words.length; i++){
            if(words[i].equals(word)){
                result++;
            }
        }
        return result;
    }

    @Override
    public void feature() {
        this.idf = makeIdf();
        tf();
    }
}
