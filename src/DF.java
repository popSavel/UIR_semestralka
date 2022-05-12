public class DF implements Feature{

    Sentence[] sentences;

    String [] vocabulary;
    public DF(Sentence[] trainData, String[] vocabulary) {
        this.sentences = trainData;
        this.vocabulary = vocabulary;
    }

    /**
     * spočítá kolikrát se každý prvek ze slovníku vyskytuje v trénovacích datech
     * @return výsledný vektor s dokumentovou frekvencí všech prvků slovníku
     */
    private double[] countDf() {
        int totalWords = 0;
        double [] result = new double[this.vocabulary.length];
        for(int i = 0; i < result.length; i++){
            result[i] = 0;
        }
        for(int i = 0; i < this.sentences.length; i++){
            Sentence curr = this.sentences[i];
            totalWords += curr.words.length;
            for(int j = 0; j < curr.words.length; j++){
                int vocabIndex = vocabIndex(curr.words[j]);
                result[vocabIndex]++;
            }
        }
        for(int i = 0; i < result.length; i++){
            result[i] /= totalWords;
        }
        return result;
    }

    /**
     * @return index slova word ve slovníku
     */
    private int vocabIndex(String word) {
        for (int i = 0; i < this.vocabulary.length; i++){
            if(this.vocabulary[i].equals(word)){
                return i;
            }
        }
        return 0;
    }

    public double[] makeVector(Sentence sentence, double[] df) {
        double [] result = new double[vocabulary.length];
        for(int i = 0; i < sentence.words.length; i++){
            String word = sentence.words[i];
            result[vocabIndex(word)] = df[vocabIndex(word)];
        }
        return result;
    }

    @Override
    public void feature() {
        double [] df = countDf();
        for(int i = 0; i < this.sentences.length; i++){
            this.sentences[i].vector = makeVector(this.sentences[i], df);
        }
    }
}
