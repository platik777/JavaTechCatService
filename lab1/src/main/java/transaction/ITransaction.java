package transaction;

import account.IAccount;

import java.util.UUID;

public interface ITransaction {
    public void Do(IAccount selfAccount, Double money, UUID otherClientID, UUID otherAccountID);
    public void Undo(IAccount selfAccount, Double money, UUID otherClientID, UUID otherAccountID);
}
