package managers;

import models.Person;
import models.Transaction;

import java.util.*;

public class ExpenseManager {
    Map<String, Person> personMap;
    FileWorker fileWorker;
    Double avgSum;

    public ExpenseManager(FileWorker fileWorker) {
        this.fileWorker = fileWorker;
    }

    public static void main(String[] args) {
        FileWorker fileWorker = new FileWorkerCsv();
        ExpenseManager expenseManager = new ExpenseManager(fileWorker);
        expenseManager.loadMap();
        expenseManager.writeFile();

    }

    public void loadMap() {
        personMap = fileWorker.readFile();
    }

    public void writeFile() {
        List<Person> personDepositList = setPersonDepositList();
        List<Transaction> transactionList = setTransactionList(personDepositList);
        System.out.println(avgSum);
         printTransationList(transactionList);
         fileWorker.writeFile(transactionList);

    }

    private static void printPersonMap(ExpenseManager fileWorkerCsv) {
        for (String name : fileWorkerCsv.personMap.keySet()) {
            System.out.println(name);
            Person person = fileWorkerCsv.personMap.get(name);
            System.out.println(person.getDeposit());
            Map<String, Map<Person, Double>> personExp = person.getPersonExpencesMap();
            for (String expence : personExp.keySet()) {
                System.out.println(expence);
                Map<Person, Double> debtorMap = personExp.get(expence);
                for (Person debtor : debtorMap.keySet()) {
                    System.out.println(debtor.getName() + " " + debtorMap.get(debtor));
                }
            }
        }
    }


    private void printTransationList(List<Transaction> transactionList) {
        System.out.println(transactionList);
    }

    private List<Transaction> setTransactionList(List<Person> personDepositList) {
        List<Transaction> transactionList = new ArrayList<>();
while (personDepositList.get(0).getDeposit()!=avgSum){
    printPersonDepositLuist(personDepositList);
    Person debtor = personDepositList.get(0);
    Person recipient = personDepositList.get(personDepositList.size()-1);
    Double recipientDeposit = recipient.getDeposit()-avgSum;
    Double debtorDebt = debtor.getDeposit()-avgSum;
    Double remaining = recipientDeposit+debtorDebt;
    Transaction transaction = null;
    if (remaining == 0){
         transaction = new Transaction(debtor,recipient,recipientDeposit);
        debtor.setDeposit(avgSum);
        recipient.setDeposit(avgSum);
        transactionList.add(transaction);
    }else if (remaining> 0){
         transaction = new Transaction(debtor,recipient,debtorDebt*-1);
        debtor.setDeposit(avgSum);
        recipient.setDeposit(recipient.getDeposit()-debtorDebt);
        transactionList.add(transaction);
    }else if (remaining< 0){
         transaction = new Transaction(debtor,recipient,recipientDeposit);
        recipient.setDeposit(avgSum);
        debtor.setDeposit(debtor.getDeposit()+recipientDeposit);
        transactionList.add(transaction);
    }
    System.out.println(transaction);
    sortDebtorList(personDepositList);
}

           /* while (!personDepositList.isEmpty()) {
                ListIterator<Person> personListIterator = personDepositList.listIterator();
                printPersonDepositLuist(personDepositList);
                Transaction transaction;
                double transactionSum;
                while (personListIterator.hasNext()){
                Person person = personListIterator.next();
                Double debtorDebt = person.getDeposit() - avgSum;
                if (debtorDebt != 0) {
                    if (debtorDebt < 0) {
                        Person recipient = personDepositList.get(personDepositList.size() - 1);
                        double recipientDeposit = recipient.getDeposit() - avgSum;
                        transactionSum = recipientDeposit + debtorDebt;
                        if (transactionSum > 0) {
                            transaction = new Transaction(person, recipient, transactionSum);
                            transactionList.add(transaction);
                            recipient.setDeposit(recipient.getDeposit() - transactionSum);
                            personListIterator.remove();

                        } else if (transactionSum < 0) {
                            double remaning = transactionSum * -1;
                            transaction = new Transaction(person, recipient, person.getDeposit());
                            transactionList.add(transaction);
                            person.setDeposit(remaning);
                            recipient.setDeposit(avgSum);
                        }
                    } else break;
                }
                }
                sortDebtorList(personDepositList);
            }*/


        return transactionList;
    }

    private void printPersonDepositLuist(List<Person> personDepositList) {
        for (Person person : personDepositList) {
            System.out.print(person.getName() + " " + person.getDeposit() + "; ");
            System.out.println("");
        }
    }

    private List<Person> setPersonDepositList() {
        List<Person> depositList = new ArrayList<>();
        double allExpencesum = 0;
        for (String name : personMap.keySet()) {
            Person person = personMap.get(name);
            person.setDeposit();
            depositList.add(person);
            allExpencesum += person.getDeposit();

        }
        sortDebtorList(depositList);
        avgSum = allExpencesum / personMap.size();
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
