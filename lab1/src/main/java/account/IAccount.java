package account;

import java.util.UUID;

public interface IAccount {
    public void DoCommissionOrAccrual();

    public void DepositMoney(Double moneyAmount);

    public void WithDraw(Double money);

    public void PassDays();

    public void Transfer(UUID clientID, UUID accountID, Double money);

    public void UndoPreviousTransfer();

    public void ChangeTerms(Double defaultAccrualRateForDeposit, Double defaultAccrualRateForDedut,
                            Integer defaultPeriodForDeposit, Double defaultAllowableNegativeBalanceForCredit,
                            Double defaultCommissionForCredit);
}
