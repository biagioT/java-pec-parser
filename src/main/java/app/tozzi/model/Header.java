package app.tozzi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Header {

    private String key;
    private String value;
}
