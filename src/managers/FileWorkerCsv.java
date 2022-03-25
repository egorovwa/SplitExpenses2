package managers;

import models.Person;
import models.Transaction;

import java.io.*;
import java.util.*;

public class FileWorkerCsv implements FileWorker {
    private final String PATH_INPUT_FILE = "src/Files/input.csv";
    Map<String, Person> personMap;

    public FileWorkerCsv() {
        personMap = new HashMap<>();
    }


    @Override
    public Map<String, Person> readFile() {
        personMap = setPersonMap();
        return personMap;
    }

    @Override
    public void writeFile(List<Transaction> transactionList) {
        List<String> listForWrite = setListForWrite(transactionList);
        System.out.println("list for write");
        System.out.println(listForWrite);
        String[][] arrayToWrite = setArryToWrite(transactionList);
    }

    private List<String> setListForWrite(List<Transaction> transactionList) {
        List<String> listForWrite = new ArrayList<>();


        return listForWrite;
    }

    private String[][] setArryToWrite(List<Transaction> transactionList) {
        Map<String, Integer> toArray = getToArrayMap(transactionList);
        String[][] lineArray = new String[toArray.size() + 1][toArray.size() + 1];
        lineArray[0][0] = ",";

        for (Transaction transaction : transactionList) {
            int toIndex = toArray.get(transaction.getTo().toString());
            int fromIndex = toArray.get(transaction.getFrom().toString());
            String sum = " ";
            sum = String.valueOf(transaction.getSum());
            lineArray[fromIndex][toIndex] = sum;
            lineArray[fromIndex][0] = transaction.getFrom().toString();
            lineArray[0][fromIndex] = lineArray[fromIndex][0];
            lineArray[0][toIndex] = transaction.getTo().toString();
            lineArray[toIndex][0] = lineArray[0][toIndex];
        }
        printLineArray(lineArray);
       return lineArray;
    }

    private void printLineArray(String[][] lineArray) {
        for (int i = 0; i < lineArray.length; i++) {
            for (int j = 0; j < lineArray.length; j++) {
                System.out.print(lineArray[i][j] + " ");

            }
            System.out.println("");

        }
    }

    private Map<String, Integer> getToArrayMap(List<Transaction> transactionList) {
        Map<String, Integer> toArray = new HashMap<>();
        int i = 1;
        for (Transaction transaction : transactionList) {
            if (!toArray.containsKey(transaction.getTo().toString())) {
                toArray.put(transaction.getTo().toString(), i);
                i++;
            }
            if (!toArray.containsKey((transaction.getFrom().toString()))) {
                toArray.put(transaction.getFrom().toString(), i);
                i++;
            }
        }
        return toArray;
    }


    private Map<String, Person> setPersonMap() {

        List<String[]> fileStrings = getFileStrings();
        String[] fistString = fileStrings.get(0);
        for (int i = 2; i < fistString.length; i++) {
            Person person = new Person(fistString[i]);
            personMap.put(person.getName(), person);
        }
        for (int i = 1; i < fileStrings.size(); i++) {
            String[] line = fileStrings.get(i);
            if (personMap.containsKey(line[0])) {
                Person person = personMap.get(line[0]);
                setPersonExpenseMap(person, fistString, line);

            }
        }
        return personMap;
    }

    private void setPersonExpenseMap(Person person, String[] fistString, String[] line) {
        String expenceName = line[1];           // TODO: 23.03.2022 exception приделать на равенство строк
        Map<String, Map<Person, Double>> personExpencesMap = person.getPersonExpencesMap();
        Map<Person, Double> debtorMap = new HashMap<>();        // TODO: 23.03.2022 Случай повтора сроки
        for (int i = 2; i < fistString.length; i++) {
            String debtorName = fistString[i];
            String debtStr = line[i];
            if (!debtStr.equals("") && !debtStr.equals(" ")) {
                Double debt = Double.parseDouble(debtStr);
                if (personMap.containsKey(debtorName)) {
                    Person dedtor = personMap.get(debtorName);
                    debtorMap.put(dedtor, debt);
                }
            }
        }
        personExpencesMap.put(expenceName, debtorMap);

    }

    private List<String[]> getFileStrings() {
        List<String[]> fileStrings = new ArrayList<>();
        try (Reader reader = new FileReader(PATH_INPUT_FILE)) {      // TODO: 22.03.2022 обработать exception
            BufferedReader bufferedReader = new BufferedReader(reader);
            while (bufferedReader.ready()) {
                String lineStr = bufferedReader.readLine();
                if (lineStr.substring(lineStr.length() - 1).equals(",")) {
                    lineStr += " ";
                }
                String[] fileline = lineStr.split(",");
                fileStrings.add(fileline);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileStrings;
    }


    private void printArray(List<String> array) {
        for (String string : array) {
            System.out.println(string);
        }
    }
}
