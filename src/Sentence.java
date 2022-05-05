import java.util.ArrayList;

public class Sentence {

    String [] words;

    String type;

    int [] bowVector;

    double [] tf_idf;

    public Sentence(String line) {
        String [] sentence = line.split(" ");
        type = sentence[0];
        ArrayList<String> parts = init(sentence);

        this.words = new String [parts.size()];
        for(int i = 0; i < this.words.length; i++){
            words[i] = parts.get(i);
        }



    }

    private ArrayList<String> init(String[] sentence) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 1; i < sentence.length; i++){
            String word = sentence[i];
            if(word.charAt((word.length() - 1)) == '?'){
                char newWord = word.charAt(word.length() - 1);



                word = word.substring(0, word.length() - 1);
                word = strip(word);
                result.add(word);
                result.add(Character.toString(newWord));
            }else{
                word = strip(word);
                result.add(word);
            }

        }
        return result;
    }

    private String strip(String word) {
        if(word.length() < 1){
            return word;
        }
        while (((word.charAt((word.length() - 1)) == ',') ||
                (word.charAt((word.length() - 1)) == '.') ||
                (word.charAt((word.length() - 1)) == '?') ||
                (word.charAt((word.length() - 1)) == '!') ||
                (word.charAt((word.length() - 1)) == ':') ||
                (word.charAt((word.length() - 1)) == '-')) && (word.length() > 1 )) {
            word = word.substring(0, word.length() - 1);
        }

        while (((word.charAt(0) == '.') ||
                (word.charAt(0) == '-')) && (word.length() > 1 )) {
            word = word.substring(1);
        }

        return word;
    }

    public boolean contains(String currWord) {
        for(int i =0; i < this.words.length; i++){
            if(this.words[i].equals(currWord)){
                return true;
            }
        }
        return false;
    }
}
