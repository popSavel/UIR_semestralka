import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        String [] lines = readFile(args[0]);
        Sentence[] trainData = new Sentence[lines.length];
        for(int i = 0; i < trainData.length; i++){
            trainData[i] = new Sentence(lines[i]);
        }

        BagOfWords bow = new BagOfWords(trainData);
        TF_IDF tf_idf = new TF_IDF(trainData, bow.vocabulary);

        N_Bayes bayes = new N_Bayes(trainData);

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
