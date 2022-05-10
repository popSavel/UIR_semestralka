import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        if(args.length != 6 && args.length != 1){
            System.out.println("Error: Number of arguments must be 6 for creating module!!! program stopped manually");
            System.exit(0);
        }
        String [] classes = readFile(args[0]);
        String [] linesTrain = readFile(args[1]);
        String [] linesTest = readFile(args[2]);
        String featureMethod = args[3];
        String classificationMethod = args[4];
        Sentence [] trainData = new Sentence[linesTrain.length];
        Sentence [] testData = new Sentence[linesTest.length];

        for(int i = 0; i < trainData.length; i++){
            trainData[i] = new Sentence(linesTrain[i]);
        }

        for(int i = 0; i < testData.length; i++){
            testData[i] = new Sentence(linesTest[i]);
        }

        String [] vocabulary = makeVocabulary(trainData);

        ClassificationClass [] classificationClasses = createClasses(classes, trainData.length);

        feature(featureMethod, trainData, vocabulary);

        Classificator classificator = null;

        switch (classificationMethod){
            case "bayes":
                classificator = new N_Bayes(trainData, testData, vocabulary);
                break;
            case "k_nn":
                classificator = new K_NN(trainData, testData, vocabulary);
                feature(featureMethod, testData, vocabulary);
                break;
            default:
                System.out.println("Error: Invalid classification method parameter given, process stopped manually!!!");
                System.exit(0);
            }

        classificator.classify();
        printResults(testData);
    }





    private static void feature(String featureMethod, Sentence[] data, String[] vocabulary) {
        Feature feature = null;

        Classificator classificator = null;

        switch (featureMethod){
            case "tf_idf":
                feature = new TF_IDF(data, vocabulary);
                break;
            case "bow":
                feature = new BagOfWords(data, vocabulary);
                break;
            case "df":
                feature= new DF(data, vocabulary);
                break;
            default:
                System.out.println("Error: Invalid feature method parameter given, process stopped manually!!!");
                System.exit(0);
        }
        feature.feature();
    }

    private static ClassificationClass[] createClasses(String[] classes, int length) {
        ClassificationClass [] result = new ClassificationClass[classes.length];
        for(int i = 0;  i < result.length; i++){
            result[i] = new ClassificationClass(classes[i], length);
        }
        return result;
    }

    private static void printResults(Sentence[] testData) {
        int correct = 0;
        for(int i = 0; i < testData.length; i++){
            if(testData[i].classifiedAs.equals(testData[i].type)){
                correct++;
            }
        }
        double result = correct/(double)testData.length;
        System.out.println("accuracy: " + result*100 );
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
