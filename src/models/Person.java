package models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Person {
    private final String name;
    private final Map<String, Map<Person, Double>> personExpensesMap;
    private Double deposit;

    public Person(String name) {
        this.name = name;
        deposit = 0.0;
        personExpensesMap = new HashMap<>();
    }

    public Map<String, Map<Person, Double>> getPersonExpensesMap() {
        return personExpensesMap;
    }
    public void setDeposit(){
        for (String expenseName : personExpensesMap.keySet()){
            Map<Person, Double> debtorMap = personExpensesMap.get(expenseName);
            for (Person debtor : debtorMap.keySet()){
                deposit = deposit+ debtorMap.get(debtor);
            }
        }
    }
    public void setDeposit(Double inDeposit){
        deposit = inDeposit;
    }

    public Double getDeposit() {
        return deposit;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
