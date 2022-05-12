public interface Classificator {

    /*
    společné metody obou klasifikátorů
    classify zařadí trénovací data
    classifyInput zařadí pouze jeden prvek ze vstupu
     */

    public String [] classify();

    public String classifyInput(Sentence input);
}
