package managers;

import models.Person;
import models.Transaction;

import java.util.*;

public class ExpenseManager {
    private Map<String, Person> personMap;
    private final FileWorker fileWorker;
    private Double avgSum;

    public ExpenseManager(FileWorker fileWorker) {
        this.fileWorker = fileWorker;
    }

    public void loadFile() {
        personMap = fileWorker.readFile();
    }

    public void writeFile() {
        List<Person> personDepositList = setPersonDepositList();
        List<Transaction> transactionList = setTransactionList(personDepositList);
        fileWorker.writeFile(transactionList);
    }

    private List<Transaction> setTransactionList(List<Person> personDepositList) {
        List<Transaction> transactionList = new ArrayList<>();
        while (personDepositList.get(0).getDeposit() != avgSum) {
            Person debtor = personDepositList.get(0);
            Person recipient = personDepositList.get(personDepositList.size() - 1);
            Double recipientDeposit = recipient.getDeposit() - avgSum;
            Double debtorDebt = debtor.getDeposit() - avgSum;
            double remaining = recipientDeposit + debtorDebt;
            Transaction transaction;
            if (remaining == 0) {
                transaction = new Transaction(debtor, recipient, recipientDeposit);
                debtor.setDeposit(avgSum);
                recipient.setDeposit(avgSum);
                transactionList.add(transaction);
            } else if (remaining > 0) {
                transaction = new Transaction(debtor, recipient, debtorDebt * -1);
                debtor.setDeposit(avgSum);
                recipient.setDeposit(recipient.getDeposit() - debtorDebt);
                transactionList.add(transaction);
            } else if (remaining < 0) {
                transaction = new Transaction(debtor, recipient, recipientDeposit);
                recipient.setDeposit(avgSum);
                debtor.setDeposit(debtor.getDeposit() + recipientDeposit);
                transactionList.add(transaction);
            }
            sortDebtorList(personDepositList);
        }
        return transactionList;
    }

    private List<Person> setPersonDepositList() {
        List<Person> depositList = new ArrayList<>();
        double allExpenseSum = 0;
        for (String name : personMap.keySet()) {
            Person person = personMap.get(name);
            person.setDeposit();
            depositList.add(person);
            allExpenseSum += person.getDeposit();

        }
        sortDebtorList(depositList);
        avgSum = allExpenseSum / personMap.size();
        return depositList;
    }

    private void sortDebtorList(List<Person> depositList) {
        depositList.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return Double.compare(o1.getDeposit(), o2.getDeposit());
            }
        });
    }


}
