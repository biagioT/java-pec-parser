package app.tozzi.model;

import lombok.Data;

@Data
public class DataSourcePair<A, B> {

    private A elementA;
    private B elementB;

    public boolean isComplete() {
        return this.elementA != null && this.elementB != null;
    }

}
