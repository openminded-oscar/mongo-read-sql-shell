package co.oleh.mongoreadsqlshell.models.entities;

import co.oleh.mongoreadsqlshell.models.Address;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "realtyObjects")
@Getter
@Setter
public class RealtyObject {
    private String id;
    private Double totalArea;
    private Double livingArea;
    private BigDecimal price;
    private Address address;
}
