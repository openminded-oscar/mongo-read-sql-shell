package co.oleh.mongoreadsqlshell.models.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "car")
@Getter
@Setter
public class Car {
    private String registrationCountry;
    private String countryNumber;
    private LocalDate manufacturingDate;
}
