package collectivesense.cluster;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-21
 */
public interface RowsDistance {

    BigDecimal calculate(List row1, List row2);

}
