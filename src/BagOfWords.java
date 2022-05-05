import java.util.ArrayList;

public class BagOfWords {

    Sentence[] sentences;

    String [] vocabulary;
    public BagOfWords(Sentence[] trainData) {
        this.sentences = trainData;
        this.vocabulary = makeVocabulary();
        for(int i = 0; i < this.sentences.length; i++){
            this.sentences[i].bowVector = makeVector(this.sentences[i]);
        }
    }

    private int[] makeVector(Sentence sentence) {
        int [] result = new int[vocabulary.length];
        for(int i = 0; i < vocabulary.length; i++){
            String word = vocabulary[i];
            result[i] = occurence(word, sentence);
        }
        return result;
    }

    private int occurence(String word, Sentence sentence) {
        int result = 0;
        for(int i = 0; i < sentence.words.length; i++){
            if(sentence.words[i].equals(word)){
                result++;
            }
        }
        return result;
    }

    public String[] makeVocabulary(){
        ArrayList<String> vocabulary = new ArrayList<String>();
        for(int i = 0; i < this.sentences.length; i++){
            String [] words =  this.sentences[i].words;
            for(int j = 0; j < words.length; j++){
                String word = words[j];

                if(!inVocabulary(vocabulary, word)){
                    vocabulary.add(word);
                }
            }
        }
        String [] result = new String[vocabulary.size()];
        for (int i = 0; i < result.length ; i++) {
            result[i] = vocabulary.get(i);
        }
        return result;
    }

    private boolean inVocabulary(ArrayList<String> vocabulary, String word) {
        for(int i = 0; i < vocabulary.size(); i++){
            if(vocabulary.get(i).equals(word)){
                return true;
            }
        }
        return false;
    }
}
