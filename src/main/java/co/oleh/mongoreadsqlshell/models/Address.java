package co.oleh.mongoreadsqlshell.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Address {
    private String city;
    private String country;
    private String postalCode;
    private String region;
    private String street;
    private String streetNumber;
}
