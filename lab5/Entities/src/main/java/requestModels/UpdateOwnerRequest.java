package requestModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.OwnerDetails;
import models.OwnerDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOwnerRequest {
    Integer id;
    OwnerDto updateOwnerDetails;
}
