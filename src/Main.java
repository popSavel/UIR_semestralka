import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        String [] lines = readFile(args[0]);
        Sentence [] sentences = new Sentence[lines.length];
        for(int i = 0; i < sentences.length; i++){
            sentences[i] = new Sentence(lines[i]);
        }

        BagOfWords bow = new BagOfWords(sentences);

        for(int i = 0; i < bow.vocabulary.length; i++){
            System.out.println(bow.vocabulary[i]);
        }

        for(int i = 0; i < 10; i++){
            Sentence curr = sentences[i];
            System.out.print(curr.type + ": ");
            for(int j = 0; j < curr.bowVector.length; j++){
                System.out.print(curr.bowVector[j] + ", ");
            }
            System.out.println();
        }
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
