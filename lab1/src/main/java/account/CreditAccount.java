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
@NoArgsConstructor
@AllArgsConstructor
public class CreditAccount implements IAccount {
    private UUID accountID;
    private Double allowableNegativeBalance;
    private Double currentBalance;
    private Double commission;
    private ITransaction transfer;
    private Stack<TransferParameters> transferLogger;
    Double limits;

    public CreditAccount(UUID accountID, Double allowableNegativeBalance, Double currentBalance, Double commission, Bank bank, Double limits) {
        this.accountID = accountID;
        this.allowableNegativeBalance = allowableNegativeBalance;
        this.currentBalance = currentBalance;
        this.commission = commission;
        this.limits = limits;
        transfer = new Transfer(bank);
    }
    @Override
    public void DoCommissionOrAccrual() {
        if (currentBalance < 0) currentBalance -= commission;
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
        if (currentBalance - money < allowableNegativeBalance) {
            System.out.println("You can't withdraw any more money than you've called in.");
            return;
        }
        currentBalance -= money;
    }

    @Override
    public void PassDays() { }

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
    public void ChangeTerms(Double defaultAccrualRateForDeposit, Double defaultAccrualRateForDedut, Integer defaultPeriodForDeposit, Double defaultAllowableNegativeBalanceForCredit, Double defaultCommissionForCredit) {
        commission = defaultCommissionForCredit;
        allowableNegativeBalance = defaultAllowableNegativeBalanceForCredit;
    }
}
