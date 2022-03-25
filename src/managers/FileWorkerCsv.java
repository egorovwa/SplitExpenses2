package managers;

import models.Person;
import models.Transaction;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileWorkerCsv implements FileWorker {

    private Map<String, Person> personMap;
    private final String pathInputFile;
    private final String pathOutPutFile;
    public FileWorkerCsv(String inputFilePath, String outPutFilePath) {
        personMap = new HashMap<>();
        this.pathInputFile =inputFilePath;
        this.pathOutPutFile = outPutFilePath;
    }

    @Override
    public Map<String, Person> readFile() {
        personMap = setPersonMap();
        return personMap;
    }

    @Override
    public void writeFile(List<Transaction> transactionList) {
        String[][] arrayToWrite = setArrayToWrite(transactionList);
        try {
            WriteArrayToFile(arrayToWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteArrayToFile(String[][] arrayToWrite) throws IOException {
        Path path = Paths.get(pathOutPutFile);
        if (Files.exists(path)){
            Files.delete(path);
            Files.createFile(path);
        }
        Writer writer = new FileWriter(String.valueOf(path),StandardCharsets.UTF_8,true);
        for (int i = 0; i < arrayToWrite.length; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < arrayToWrite.length; j++) {
                if (arrayToWrite[i][j] != null){
                    stringBuilder.append(arrayToWrite[i][j])
                            .append(",");
                }else {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            stringBuilder.append("\n");
            writer.write(stringBuilder.toString());
        }
        writer.close();
    }

    private String[][] setArrayToWrite(List<Transaction> transactionList) {
        Map<String, Integer> toArray = getToArrayMap(transactionList);
        String[][] lineArray = new String[toArray.size() + 1][toArray.size() + 1];
        lineArray[0][0] = "";
        for (Transaction transaction : transactionList) {
            int toIndex = toArray.get(transaction.getTo().toString());
            int fromIndex = toArray.get(transaction.getFrom().toString());
            String sum;
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
        String expenseName = line[1];
        Map<String, Map<Person, Double>> personExpensesMap = person.getPersonExpensesMap();
        Map<Person, Double> debtorMap = new HashMap<>();
        for (int i = 2; i < fistString.length; i++) {
            String debtorName = fistString[i];
            String debtStr = line[i];
            if (!debtStr.equals("") && !debtStr.equals(" ")) {
                Double debt = Double.parseDouble(debtStr);
                if (personMap.containsKey(debtorName)) {
                    Person debtor = personMap.get(debtorName);
                    debtorMap.put(debtor, debt);
                }
            }
        }
        personExpensesMap.put(expenseName, debtorMap);
    }

    private List<String[]> getFileStrings() {
        List<String[]> fileStrings = new ArrayList<>();
        try (Reader reader = new FileReader(pathInputFile, StandardCharsets.UTF_8)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            while (bufferedReader.ready()) {
                String lineStr = bufferedReader.readLine();
                if (lineStr.endsWith(",")) {
                    lineStr += " ";
                }
                String[] fileLine = lineStr.split(",");
                fileStrings.add(fileLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileStrings;
    }

}
