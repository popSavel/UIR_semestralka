import java.util.ArrayList;

public class ClassificationClass {

    String name;

    ArrayList <Sentence> data;

    public ClassificationClass(String type) {
        this.name = type;
        this.data = new ArrayList<Sentence>();
    }

    public void addSentence(Sentence sentence) {
        data.add(sentence);
    }
}
