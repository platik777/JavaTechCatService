package account;

import bank.Bank;
import lombok.Data;
import lombok.NoArgsConstructor;
import transaction.ITransaction;
import transaction.Transfer;

import java.util.Calendar;
import java.util.Date;
import java.util.Stack;
import java.util.UUID;

@Data
@NoArgsConstructor
public class DepositAccount implements IAccount {
    private UUID accountID;
    private Calendar dateOfOpeningDeposit;
    private Integer periodOfDepositInMonths;
    private Calendar dateOfEndingDeposit;
    private Double accrualRate;
    private Double currentBalance;
    private Double currentAccrual;
    private ITransaction transfer;
    private Double limits;
    private Stack<TransferParameters> transferLogger;

    public DepositAccount(UUID accountID, Integer day, Integer month, Integer year, Integer periodOfDepositInMonths, Double currentBalance, Double accrualRate, Bank bank, Double limits) {
        dateOfOpeningDeposit = new Calendar.Builder().setDate(year, month - 1, day).build();
        this.periodOfDepositInMonths = periodOfDepositInMonths;
        this.currentBalance = currentBalance;
        this.accrualRate = accrualRate;
        this.accountID = accountID;
        dateOfEndingDeposit = new Calendar.Builder().setDate(
                year + (month - 1 + periodOfDepositInMonths) / 12,
                (month - 1 + periodOfDepositInMonths) % 12,
                day).build();
        currentAccrual = 0.0;
        transfer = new Transfer(bank);
        this.limits = limits;
    }

    @Override
    public void PassDays() {
        currentAccrual += currentBalance * (1 + accrualRate / 365);
    }

    @Override
    public void Transfer(UUID clientID, UUID accountID, Double money) {
        if (money > limits) {
            System.out.printf("You can't transfer more than %f because of yor limits: %f.", money, limits);
            return;
        }
        transferLogger.push(new TransferParameters(clientID, accountID, money));
        transfer.Do(this, money, clientID, accountID);
    }

    @Override
    public void UndoPreviousTransfer() {
        TransferParameters temp = transferLogger.pop();
        transfer.Undo(this, temp.money(), temp.clientID(), temp.accountID());
    }

    @Override
    public void ChangeTerms(Double defaultAccrualRateForDeposit, Double defaultAccrualRateForDedut,
                            Integer defaultPeriodForDeposit, Double defaultAllowableNegativeBalanceForCredit,
                            Double defaultCommissionForCredit) {
        accrualRate = defaultAccrualRateForDeposit;
        periodOfDepositInMonths = defaultPeriodForDeposit;
        dateOfEndingDeposit.set(dateOfOpeningDeposit.get(Calendar.YEAR) + (dateOfOpeningDeposit.get(Calendar.MONTH) - 1 + periodOfDepositInMonths) / 12,
                (dateOfOpeningDeposit.get(Calendar.MONTH) - 1 + periodOfDepositInMonths) % 12,
                dateOfOpeningDeposit.get(Calendar.DATE));
    }

    @Override
    public void DoCommissionOrAccrual() {
        currentBalance += currentAccrual;
        currentAccrual = 0.0;
    }

    @Override
    public void DepositMoney(Double moneyAmount) {
        currentBalance += moneyAmount;
    }

    @Override
    public void WithDraw(Double money) {
        if (money > limits) {
            System.out.printf("You can't withdraw more than %f because of yor limits: %f.", money, limits);
            return;
        }
        if (new Date().getTime() < dateOfEndingDeposit.getTimeInMillis()) {
            System.out.println("You can't withdraw from deposit before end of term");
            return;
        }
        currentBalance -= money;
    }
}
