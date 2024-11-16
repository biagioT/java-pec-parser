package app.tozzi.model;

import lombok.Builder;
import lombok.Data;

/**
 * Mail/PEC header
 *
 * @author Biagio Tozzi
 */
@Data
@Builder
public class Header {

    private String key;
    private String value;
}
