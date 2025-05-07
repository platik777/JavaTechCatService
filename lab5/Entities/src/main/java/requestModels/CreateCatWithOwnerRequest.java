package requestModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Cat;
import models.OwnerDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCatWithOwnerRequest {
    OwnerDto ownerDto;
    Cat cat;
}
