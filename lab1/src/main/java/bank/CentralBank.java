package bank;

import client.Client;
import lombok.Data;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Data
public class CentralBank {
    private Map<UUID, Bank> banks;

    public void Subscribe(UUID bankID, Bank bank) {
        banks.put(bankID, bank);
    }

    public void NotifyAboutCommissionOrAccrual() {
        for(Map.Entry<UUID, Bank> entry : banks.entrySet()) {
            entry.getValue().DoCommissionOrAccrual();
        }
    }

    public void CreateBank(String name) {
        UUID bankID = UUID.randomUUID();
        banks.put(bankID, new Bank(bankID, name));
    }

    public void Transfer(UUID senderBankID, UUID senderClientID, UUID senderAccountID, Double money,
                         UUID receiverBankID, UUID receiverClientID, UUID receiverAccountID) {
        banks.get(senderBankID).getClients().get(senderClientID).getAccounts().get(senderAccountID).WithDraw(money);
        banks.get(receiverBankID).getClients().get(receiverClientID).getAccounts().get(receiverAccountID).DepositMoney(money);
    }
}
