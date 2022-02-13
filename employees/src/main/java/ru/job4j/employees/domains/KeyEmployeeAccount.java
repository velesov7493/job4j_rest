package ru.job4j.employees.domains;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tr_employees_accounts")
public class KeyEmployeeAccount {

    @Id
    @SequenceGenerator(
            name = "employeesAccountsIdSeq",
            sequenceName = "tr_employees_accounts_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeesAccountsIdSeq")
    private int id;
    @Column(name = "id_employee")
    private int employeeId;
    @Column(name = "id_account")
    private int accountId;

    public static KeyEmployeeAccount of(int employeeId, int accountId) {
        KeyEmployeeAccount result = new KeyEmployeeAccount();
        result.accountId = accountId;
        result.employeeId = employeeId;
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeyEmployeeAccount that = (KeyEmployeeAccount) o;
        return
                employeeId == that.employeeId
                && accountId == that.accountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, accountId);
    }
}
