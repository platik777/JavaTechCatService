import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Cat;
import ru.platik777.entities.Color;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Cat> cats = new ArrayList<>();
        cats.add(new Cat("Kot", LocalDate.of(2024, 10, 9), "breed", 100, Color.BLACK));
        cats.add(new Cat("Kit", LocalDate.of(2024, 9, 2), "breed", 100, Color.BLACK));

        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(cats));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
