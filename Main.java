package search;

import java.io.*;
import java.util.*;


class Main {

    private Map<String, List<Integer>> searchMap;

    private static BufferedReader fileReader;

    private final BufferedReader consoleReader;

    //typed by user data, by which program search people
    private String data;

    //number of people, which user can type in
    private int numOfPeople;

    //container for all people
    private final List<String> people;

    //array of all information
    private static String[][] information;

    //people which was founded by data
    private static List<String> foundedPeople;

    //number of command in user menu
    private static int command;

    public Main() throws FileNotFoundException {
        numOfPeople = 0;
        foundedPeople = new ArrayList<>();
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
        command = -1;
        people = new ArrayList<>();
        searchMap = new HashMap<>();
    }

    //getters and setters

    //-----------------------------//

    public int getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(int numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

//-----------------------------//

    //start of the program. Calls method "process()",
    //all logic described here
    public static void main(String[] args) throws IOException {
        File file = new File(args[1]);
        FileReader fr = new FileReader(file);
        fileReader = new BufferedReader(fr);

        new Main().process();
    }

    //main method
    private void process() throws IOException {

        int countLines = -1;

        //counting people in our file
        String s = fileReader.readLine();
        people.add(s);
        countLines++;
        while (s != null) {
            s = fileReader.readLine();
            people.add(s);
            countLines++;
        }
        setNumOfPeople(countLines);
        people.remove(people.size() - 1);

        information = new String[getNumOfPeople()][3];
        //reading information from "people" to "information"
        for (int i = 0; i < getNumOfPeople(); i++) {
            //array of data about people, separated with space
            String[] words = people.get(i).split(" ");
            for (int j = 0; j < words.length; j++) {
                information[i][j] = words[j];
            }
        }
        assembleMap();
        while (true) {
            showUserMenu();
            s = consoleReader.readLine();
            command = Integer.parseInt(s);
            switch (command) {
                case 1:
                    find();
                    break;
                case 2:
                    showAllPeople();
                    break;
                case 0:
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Incorrect option! Try again.");
            }
        }
    }

    private void find() throws IOException {
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String s = consoleReader.readLine();
        System.out.println("Enter a name or email to search all suitable people.");
        String s1 = consoleReader.readLine();
        switch (s) {
            case ("ALL"):
                List<Integer> listALL = findALL(s1);
                show(listALL);
                break;
            case ("ANY"):
                List<Integer> listANY = findANY(s1);
                show(listANY);
                break;
            case ("NONE"):
                List<Integer> listNONE = findNONE(s1);
                show(listNONE);
                break;
            default:
                find();
                break;
        }
    }

    //If the strategy is NONE, the program should print lines that do not contain words from the query at all
    private List<Integer> findNONE(String s1) throws IOException {
        List<Integer> resList = new ArrayList<>();
        List<Integer> listMatches = findANY(s1);

        for (int i = 0; i < people.size(); i++) {
            if (!listMatches.contains(i)) {
                resList.add(i);
            }
        }
        return resList;
    }

    //If the strategy is ANY, the program should print the lines containing at least one word from the query.
    private List<Integer> findANY(String s) throws IOException {
        List<Integer> list = new ArrayList<>();

        String[] arr = s.split(" ");
        for (String value : arr) {
            if (searchMap.get(value.toLowerCase(Locale.ROOT)) != null) {
                list.addAll(searchMap.get(value.toLowerCase(Locale.ROOT)));
            }
        }
        return list;
    }

    private void show(List<Integer> list) {
        if (!list.isEmpty()) {
            System.out.println(list.size() + " persons found:");
            for (Integer i : list) {
                System.out.println(people.get(i));
            }
            return;
        }
        System.out.println("No matching people found.");
    }

    //If the strategy is ALL, the program should print lines containing all the words from the query.
    private List<Integer> findALL(String s) throws IOException {

        List<Integer> result;
        List<Integer> res2;

        String[] arr = s.split(" ");

        result = findANY(arr[0]);

        for (int i = 1; i < arr.length; i++) {
            res2 = findANY(arr[i]);

            result.retainAll(res2);
        }

        return result;
    }


    private void assembleMap() {
        for (int i = 0; i < people.size(); i++) {
            String[] strings = people.get(i).split(" ");
            for (String key : strings) {
                List<Integer> values = searchMap.get(key.toLowerCase(Locale.ROOT));
                if (values == null) {
                    values = new ArrayList<>();
                }
                values.add(i);
                searchMap.put(key.toLowerCase(Locale.ROOT), values);
            }
        }
    }

    //displays all the people, that user typed in
    private void showAllPeople() {
        StringBuilder sb = new StringBuilder();

        System.out.println("=== List of people ===");
        for (int i = 0; i < getNumOfPeople(); i++) {
            for (int j = 0; j < 3; j++) {
                if (information[i][j] != null) {
                    sb.append(information[i][j]).append(" ");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            System.out.println(sb);
            sb.delete(0, sb.length());
        }
    }

    //search for specified data in array "people"
    private void searchInformation() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < getNumOfPeople(); i++) {
            for (int j = 0; j < 3; j++) {
                Optional<String> optional = Optional.ofNullable(information[i][j]);
                if (optional.isPresent()) {
                    String inf = optional.get().toLowerCase(Locale.ROOT);
                    String d = getData().toLowerCase(Locale.ROOT);
                    if (inf.contains(d)) {
                        foundedPeople.add(people.get(i));
                        j = 3;
                    }
                }
            }
        }

        if (foundedPeople.isEmpty()) {
            System.out.println("No matching people found.");
        } else {
            System.out.println("Found people:");
            for (String str : foundedPeople) {
                System.out.println(str);
            }
            foundedPeople.clear();
        }

    }

    //displays user menu
    private void showUserMenu() {
        System.out.println("=== Menu ===");
        System.out.println("1. Find a person.");
        System.out.println("2. Print all data.");
        System.out.println("0. Exit.");
    }
}
