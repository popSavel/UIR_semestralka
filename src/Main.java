import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        String [] linesTrain = readFile(args[0]);
        String [] linesTest = readFile(args[1]);
        Sentence[] trainData = new Sentence[linesTrain.length];
        Sentence[] testData = new Sentence[linesTest.length];

        for(int i = 0; i < trainData.length; i++){
            trainData[i] = new Sentence(linesTrain[i]);
        }

        for(int i = 0; i < testData.length; i++){
            testData[i] = new Sentence(linesTest[i]);
        }

        String [] vocabulary = makeVocabulary(trainData);

        BagOfWords bow = new BagOfWords(trainData, vocabulary);
        TF_IDF tf_idf = new TF_IDF(trainData, vocabulary);


        N_Bayes bayes = new N_Bayes(trainData, testData, vocabulary);

        printResults(testData);

    }

    private static void printResults(Sentence[] testData) {
        int correct = 0;
        for(int i = 0; i < testData.length; i++){
            if(testData[i].classifiedAs.equals(testData[i].type)){
                correct++;
            }
        }
        double result = correct/(double)testData.length;
        System.out.println("results: " + result*100 );
    }

    public static String[] makeVocabulary(Sentence[] data){
        ArrayList<String> vocabulary = new ArrayList<String>();
        for(int i = 0; i < data.length; i++){
            String [] words =  data[i].words;
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

    public static boolean inVocabulary(ArrayList<String> vocabulary, String word) {
        for(int i = 0; i < vocabulary.size(); i++){
            if(vocabulary.get(i).equals(word)){
                return true;
            }
        }
        return false;
    }

    private  static String[] readFile(String fileName) {
        Path path  = Path.of(fileName);
        ArrayList <String> lines = new ArrayList<String>();
        try {
            Scanner sc = new Scanner(path);
            while(sc.hasNextLine()){
                lines.add(sc.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Error loading input file, program ended manually");
            System.exit(0);
        }
        String [] result = new String[lines.size()];
        for(int i = 0; i < result.length; i++){
            result[i] = lines.get(i);
        }

        return result;
    }

}
