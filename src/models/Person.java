package models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Person {
    private String name;
    private Map<String, Map<Person, Double>> personExpencesMap;
    Double deposit;

    public Person(String name) {
        this.name = name;
        deposit = 0.0;
        personExpencesMap = new HashMap<>();
    }

    public Map<String, Map<Person, Double>> getPersonExpencesMap() {
        return personExpencesMap;
    }
    public void setDeposit(){
        for (String expenseName : personExpencesMap.keySet()){
            Map<Person, Double> debtorMap = personExpencesMap.get(expenseName);
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
