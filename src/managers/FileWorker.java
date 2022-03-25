package managers;

import models.Person;
import models.Transaction;

import java.util.List;
import java.util.Map;

public interface FileWorker {
    Map<String, Person> readFile();
    void writeFile(List<Transaction> transactionList);
}
