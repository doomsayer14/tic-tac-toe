package search;

import java.util.Scanner;

public class Main {

    private final Scanner scanner;

    //line, that user type in console
    private String inputLine;

    //array of words in "inputLine", separated with space
    private String[] words;

    //word, that we have to search for
    private String specWord;

    public Main() {
        scanner = new Scanner(System.in);
        inputLine = "";
        specWord = "";
    }

    //getters and setters

    //-----------------------------//
    public String getInputLine() {
        return inputLine;
    }

    public void setInputLine(String inputLine) {
        this.inputLine = inputLine;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }

    public String getSpecWord() {
        return specWord;
    }

    public void setSpecWord(String specWord) {
        this.specWord = specWord;
    }
    //-----------------------------//

    //start of the program. Calls method "process()",
    //all logic described here
    public static void main(String[] args) {
        new Main().process();
    }

    //main method
    private void process() {
        setInputLine(scanner.nextLine());
        setWords(getInputLine().split(" "));
        setSpecWord(scanner.next());
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(getSpecWord())) {
                System.out.println(++i);
                return;
            }
        }
        System.out.println("Not found");
    }
}


