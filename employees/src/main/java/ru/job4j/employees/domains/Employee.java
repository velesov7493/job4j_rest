package ru.job4j.employees.domains;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "tz_employees")
public class Employee {

    @Id
    @SequenceGenerator(
            name = "employeesIdSeq",
            sequenceName = "tz_employees_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeesIdSeq")
    private int id;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    private String inn;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "employmentdate", updatable = false)
    private Date employmentDate;
    @Transient
    private Set<Account> accounts;

    public Employee() {
        employmentDate = new Date();
        accounts = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public Date getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(Date employmentDate) {
        this.employmentDate = employmentDate;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account value) {
        accounts.add(value);
    }

    public void deleteAccount(Account value) {
        accounts.remove(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return
                id == employee.id
                && Objects.equals(firstName, employee.firstName)
                && Objects.equals(lastName, employee.lastName)
                && Objects.equals(inn, employee.inn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, inn);
    }
}