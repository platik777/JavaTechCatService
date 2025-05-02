package client;
import account.CreditAccount;
import account.DebutAccount;
import account.DepositAccount;
import account.IAccount;
import bank.Bank;
import lombok.Data;
import notifiers.INotifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

@Data
public class Client {
    private UUID clientID;
    private Map<UUID, IAccount> accounts;
    private Double defaultAccrualRateForDeposit;
    private Double defaultAccrualRateForDedut;
    private Integer defaultPeriodForDeposit;
    private Double defaultAllowableNegativeBalanceForCredit;
    private Double defaultComissionForCredit;

    private String name;
    private String surname;
    private String address;
    private String passport;

    private ArrayList<INotifier> notifiDevices;
    private Double limitsForAccount;
    private Bank bank;

    public Client(@NotNull String name, @NotNull String surname, String address, String passport, Bank bank) {
        limitsForAccount = address != null && passport != null ? 1000000.0 : 50000.0;
        this.clientID = UUID.randomUUID();
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.passport = passport;
        this.bank = bank;
    }

    public void SetAdress(@NotNull String address) {
        this.address = address;
        limitsForAccount = passport != null ? 1000000.0 : 50000.0;
    }

    public void SetPasport(@NotNull String passport) {
        this.passport = passport;
        limitsForAccount = address != null ? 1000000.0 : 50000.0;
    }

    public void AddNotifier(INotifier notifier) {
        notifiDevices.add(notifier);
    }

    public void ChangeTerms(Double defaultAccrualRateForDeposit, Double defaultAccrualRateForDedut,
                                 Integer defaultPeriodForDeposit, Double defaultAllowableNegativeBalanceForCredit,
                                 Double defaultComissionForCredit, Boolean isSubscriber) {
        this.defaultPeriodForDeposit = defaultPeriodForDeposit;
        this.defaultAccrualRateForDedut = defaultAccrualRateForDedut;
        this.defaultAccrualRateForDeposit = defaultAccrualRateForDeposit;
        this.defaultComissionForCredit = defaultComissionForCredit;
        this.defaultAllowableNegativeBalanceForCredit = defaultAllowableNegativeBalanceForCredit;
        for (Map.Entry<UUID, IAccount> entry : accounts.entrySet()) {
            entry.getValue().ChangeTerms(defaultAccrualRateForDeposit, defaultAccrualRateForDedut,
                    defaultPeriodForDeposit, defaultAllowableNegativeBalanceForCredit, defaultComissionForCredit);

        }
        if (isSubscriber) {
            for (INotifier notifier : notifiDevices) {
                notifier.GetNotifyOnDevice();
            }
        }
    }
}
