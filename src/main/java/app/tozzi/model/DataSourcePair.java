package app.tozzi.model;

import lombok.Data;

/**
 * @param <A>
 * @param <B>
 *
 * @author Biagio Tozzi
 */
@Data
public class DataSourcePair<A, B> {

    private A elementA;
    private B elementB;

    public boolean isComplete() {
        return this.elementA != null && this.elementB != null;
    }

}
