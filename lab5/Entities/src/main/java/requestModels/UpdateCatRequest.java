package requestModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Cat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCatRequest {
    private Integer id;
    private Cat cat;
}
