package bank;

import account.CreditAccount;
import account.DebutAccount;
import account.DepositAccount;
import account.IAccount;
import client.AccountType;
import client.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bank {
    private UUID bankID;
    private String name;
    private Map<UUID, Client> clients;
    private  Map<UUID, Client> notificationSubscribers;

    private Double defaultAccrualRateForDeposit;
    private Double defaultAccrualRateForDedut;
    private Integer defaultPeriodForDeposit;
    private Double defaultAllowableNegativeBalanceForCredit;
    private Double defaultComissionForCredit;

    public Bank(UUID bankID, String name) {
        this.bankID = bankID;
        this.name = name;
        defaultAccrualRateForDedut = 3.65;
        defaultAccrualRateForDeposit = 15.0;
        defaultPeriodForDeposit = 24;
        defaultAllowableNegativeBalanceForCredit = 200000.0;
        defaultComissionForCredit = 10000.0;
    }
    public void DoCommissionOrAccrual() {
        for (Map.Entry<UUID, Client> entry : clients.entrySet())
            for (Map.Entry<UUID, IAccount> entry1 : entry.getValue().getAccounts().entrySet())
                entry1.getValue().DoCommissionOrAccrual();
    }

    public void ChangeTermsForClients(Double defaultAccrualRateForDeposit, Double defaultAccrualRateForDedut,
                                      Integer defaultPeriodForDeposit, Double defaultAllowableNegativeBalanceForCredit,
                                      Double defaultComissionForCredit) {
        if (defaultPeriodForDeposit != null) this.defaultPeriodForDeposit = defaultPeriodForDeposit;
        if (defaultAccrualRateForDedut != null) this.defaultAccrualRateForDedut = defaultAccrualRateForDedut;
        if (defaultAccrualRateForDeposit != null) this.defaultAccrualRateForDeposit = defaultAccrualRateForDeposit;
        if (defaultComissionForCredit != null) this.defaultComissionForCredit = defaultComissionForCredit;
        if (defaultAllowableNegativeBalanceForCredit != null) this.defaultAllowableNegativeBalanceForCredit = defaultAllowableNegativeBalanceForCredit;
        for (Map.Entry<UUID, Client> entry : clients.entrySet()) {
            entry.getValue().ChangeTerms(this.defaultAccrualRateForDeposit, this.defaultAccrualRateForDedut,
                    this.defaultPeriodForDeposit, this.defaultAllowableNegativeBalanceForCredit,
                    this.defaultComissionForCredit,
                    notificationSubscribers.containsKey(entry.getKey()));
        }
    }

    public void UnsubscribeFromNotifications(UUID clientID) {
        notificationSubscribers.remove(clientID);
    }
    public void AddNewClient(@NotNull String name, @NotNull String surname, String adress, String pasport) {

    }

    public void CreateAccount(@NotNull AccountType accountType, UUID accountID) {
        IAccount newAccount;
        UUID newAccountID = UUID.randomUUID();
        Calendar rightnow = Calendar.getInstance();
        Double limits = clients.get(accountID).getLimitsForAccount();
        switch (accountType) {
            case DebutAccount : {
                newAccount = new DebutAccount(newAccountID, 0.0,
                        defaultAccrualRateForDedut, this, limits);
                break;
            }
            case DepositAccount : {
                newAccount = new DepositAccount(newAccountID, rightnow.get(Calendar.DAY_OF_MONTH),
                        rightnow.get(Calendar.MONTH), rightnow.get(Calendar.YEAR), defaultPeriodForDeposit,
                        0.0, defaultAccrualRateForDeposit, this, limits);
                break;
            }
            case CreditAccount : {
                newAccount = new CreditAccount(newAccountID, defaultAllowableNegativeBalanceForCredit, 0.0,
                        defaultComissionForCredit, this, limits);
                break;
            }
            default:
                System.out.println("Incorrect account type");
                return;
        }
        clients.get(accountID).getAccounts().put(newAccountID, newAccount);
    }
}
