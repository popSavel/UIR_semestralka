import java.util.ArrayList;

public class Sentence {

    String [] words;

    String type;

    String classifiedAs;

    double [] vector;


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
            if(word.length()>0) {
                if (word.charAt((word.length() - 1)) == '?') {
                    char newWord = word.charAt(word.length() - 1);


                    word = word.substring(0, word.length() - 1);
                    word = strip(word);
                    result.add(word);
                    result.add(Character.toString(newWord));
                } else {
                    if(isValid(word)) {
                        word = strip(word);
                        result.add(word);
                    }
                }
            }

        }
        return result;
    }

    /**
     * ze slova word odstraní interpunkčí zmanénka na konci a na začátku
     */
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

    /**
     * @return zda se v této větě vyskytuje slovo currWord
     */
    public boolean contains(String currWord) {
        for(int i =0; i < this.words.length; i++){
            if(this.words[i].equals(currWord)){
                return true;
            }
        }
        return false;
    }

    /**
     * @return zda je slovo word platné a dá se považovat za slovo
     */
    public boolean isValid(String word){
        if(word.length() > 0 && (!word.equals("."))
                            && (!word.equals("-"))
                            && (!word.equals("?"))
                            && (!word.equals("!"))
                            && (!word.equals(":"))
                            && (!word.equals(","))){
            return true;
        }else{
            return false;
        }
    }
}
