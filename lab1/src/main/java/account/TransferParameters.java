package account;

import java.util.UUID;

public record TransferParameters(UUID clientID, UUID accountID, Double money) { }
