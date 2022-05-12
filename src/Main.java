import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        if(args.length != 6 && args.length != 1){
            System.out.println("Error: Number of arguments must be 6 for creating module, or 1 for running existing module!!! program stopped manually");
            System.exit(0);
        }else{
            if(args.length == 6){
                String [] classes = readFile(args[0]);
                String [] linesTrain = readFile(args[1]);
                String [] linesTest = readFile(args[2]);
                String featureMethod = args[3];
                String classificationMethod = args[4];
                String outputFile = args[5];
                createModule(classes, linesTrain, linesTest, featureMethod, classificationMethod, outputFile);
            }else{
                String modul = args[0];
                runModule(modul);
            }
        }
    }

    /**
     * spuštění s jedním parametrem
     * připraví klasifikátor a příznakový algoritmus
     * zobrazí jednoduché gui, kam uživatel zadá text, klasifikátor ho zařadí a vypíše výsledek do gui
     * @param modul název modulu
     */
    private static void runModule(String modul) {
        String [] lines = readFile(modul);
        String featureType = lines[0];
        String classificationMethod = lines[1];

        int classesLength  = Integer.parseInt(lines[2]);

        String [] classes = new String[classesLength];

        for(int i = 0; i < classesLength; i++){
            classes[i] = lines[i + 3];
        }

        Sentence [] data = new Sentence[lines.length - (classesLength + 3)];

        for(int i = 0; i < lines.length - (classesLength + 3); i++){
            String line = lines[i + (classesLength + 3)];
            data[i] = new Sentence(line);
        }

        String [] vocabulary = makeVocabulary(data);

        createClasses(classes, lines.length - (classesLength + 3));

        feature(featureType, data, vocabulary);

        Classificator classificator = createClassificator(classificationMethod, data, vocabulary);

        JFrame gui = new JFrame();
        gui.setTitle("UIR 2022 - klasifikace dialogových aktů");
        gui.setSize(800, 200);

        JPanel panel = new JPanel();
        panel.setSize(new Dimension(800, 200));

        TextField textArea = new TextField( 50);

        Label fMethod = new Label("Příznakový algoritmus: " + featureType);
        Label cMethod = new Label("Klasifikační algoroitmus: " + classificationMethod);

        Label resultLabel = new Label("Výsledek: ");
        TextField resultField = new TextField();
        resultField.setColumns(15);
        resultField.setEditable(false);


        Button klasifikace = new Button("Klasifikuj");
        Button delete = new Button("Vymaž text");

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });

        klasifikace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textArea.getText();
                text.strip();
                Sentence toClassify = new Sentence(text);
                if(classificationMethod.equals("k_nn")){
                    Sentence [] guiText = new Sentence[1];
                    guiText[0] = toClassify;
                    feature(featureType, guiText, vocabulary);
                }
                String result = classificator.classifyInput(toClassify);
                resultField.setText(result);
                gui.repaint();
            }
        });

        JPanel tf = new JPanel();
        tf.add(new Label("Zadejte text: "));
        tf.add(textArea);

        JPanel buttons = new JPanel();
        buttons.add(fMethod);
        buttons.add(cMethod);
        buttons.add(delete);
        buttons.add(klasifikace);
        buttons.add(resultLabel);
        buttons.add(resultField);

        panel.setLayout(new BorderLayout());
        panel.add(tf, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.AFTER_LAST_LINE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        gui.add(panel);

        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setLocationRelativeTo(null);
        gui.setResizable(false);
        gui.setVisible(true);
    }

    /**
     * @return vytvoří klasifikátor typu classificatiomMethod
     */
    private static Classificator createClassificator(String classificationMethod, Sentence[] data, String[] vocabulary) {
        Classificator classificator = null;
        if(classificationMethod.equals("bayes")){
            classificator = new N_Bayes(data, null ,vocabulary);
        }else{
            classificator = new K_NN(data, null, vocabulary);
        }
        return classificator;
    }

    /**
     * spuštění se 6 parametry
     * klasifikuje testovací data, vypíše přesnost do konzole a vytvoří modul
     */
    private static void createModule(String[] classes, String[] linesTrain, String[] linesTest, String featureMethod, String classificationMethod, String outputFile) {
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
        printOutput(outputFile, trainData, featureMethod, classificationMethod, classes);
    }

    /**
     * vypíše do souboru s názvem outputFile všechna potřebná data pro spuštění s jedním parametrem
     */
    private static void printOutput(String outputFile, Sentence[] trainData, String featureMethod, String classificationMethod, String[] classes) {
        try{
            PrintWriter writer = new PrintWriter(outputFile);
            writer.println(featureMethod);
            writer.println(classificationMethod);
            writer.println(classes.length);
            for(int i = 0; i < classes.length; i++){
                writer.println(classes[i]);
            }
            for(int i = 0; i < trainData.length; i++){
                Sentence curr = trainData[i];
                writer.print(curr.type + " ");
                for(int j = 0; j < curr.words.length; j++){
                    writer.print(curr.words[j] + " ");
                }
                writer.println();
            }
            System.out.println("Modul " + outputFile + " byl vytvořen.");
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error loading output file: make sure it ends with .txt");
        }
    }

    /**
     * vytvoří příznakové vektory prvkům z pole data, algoritmem featureMethod
     */
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

    /**
     * @return pole prvků typu ClassificationClass, prvek za každý typ z pole classes
     */
    private static ClassificationClass[] createClasses(String[] classes, int length) {
        ClassificationClass [] result = new ClassificationClass[classes.length];
        for(int i = 0;  i < result.length; i++){
            result[i] = new ClassificationClass(classes[i], length);
        }
        return result;
    }

    /**
     * pro prvky z pole testData spočítá kolik z nich má stejný typ věty s typem klasifikační třídy do které byl zařazen
     * úspěšnost vypíše v procentech
     */
    private static void printResults(Sentence[] testData) {
        int correct = 0;
        for(int i = 0; i < testData.length; i++){
            if(testData[i].classifiedAs.equals(testData[i].type)){
                correct++;
            }
        }
        double result = correct/(double)testData.length;
        System.out.format("Program klasifikoval testovací data s přesností %.2f procent", result * 100);
        System.out.println();
    }

    /**
     * @return seznam všech možných slov co se vyskytují v trénovacích datech
     */
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

    /**
     * @return zda je prvek word v listu vocabulary
     */
    public static boolean inVocabulary(ArrayList<String> vocabulary, String word) {
        for(int i = 0; i < vocabulary.size(); i++){
            if(vocabulary.get(i).equals(word)){
                return true;
            }
        }
        return false;
    }

    /**
     * @return pole řádků souboru fileName
     */
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
