package account;

import bank.Bank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import transaction.ITransaction;
import transaction.Transfer;

import java.util.Stack;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebutAccount implements IAccount {
    private UUID accountID;
    private Double currentBalance;
    private Double accrualRate;
    private Double currentAccrual;
    private ITransaction transfer;
    private Stack<TransferParameters> transferLogger;
    Double limits;

    public DebutAccount(UUID accountID, Double currentBalance, Double accrualRate, Bank bank, Double limits) {
        this.accountID = accountID;
        this.accrualRate = accrualRate;
        this.currentBalance = currentBalance;
        transfer = new Transfer(bank);
        this.limits = limits;
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
        if (currentBalance - money < 0) {
            System.out.println("You can't withdraw more money than you have on your account");
            return;
        }
        currentBalance -= money;
    }

    @Override
    public void PassDays() {
        currentAccrual += currentBalance + currentAccrual;
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
        accrualRate = defaultAccrualRateForDedut;
    }
}
