package transaction;

import account.IAccount;
import bank.Bank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Transfer implements ITransaction {
    private Bank bank;
    @Override
    public void Do(@NotNull IAccount selfAccount, Double money, UUID otherClientID, UUID otherAccountID) {
        selfAccount.WithDraw(money);
        bank.getClients().get(otherClientID).getAccounts().get(otherAccountID).DepositMoney(money);
    }

    @Override
    public void Undo(@NotNull IAccount selfAccount, Double money, UUID otherClientID, UUID otherAccountID) {
        bank.getClients().get(otherClientID).getAccounts().get(otherAccountID).WithDraw(money);
        selfAccount.DepositMoney(money);
    }
}
