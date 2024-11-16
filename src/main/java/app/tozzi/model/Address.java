package app.tozzi.model;

import lombok.Builder;
import lombok.Data;

/**
 * Mail/PEC Address
 *
 * @author Biagio Tozzi
 */
@Data
@Builder
public class Address {

    private String email;
    private String name;

}
