package app.tozzi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {

    private String email;
    private String name;

}
