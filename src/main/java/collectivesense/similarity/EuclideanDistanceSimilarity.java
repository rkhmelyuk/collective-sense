package collectivesense.similarity;

import collectivesense.Item;
import collectivesense.ItemValue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-24
 */
public class EuclideanDistanceSimilarity implements Similarity {

    public BigDecimal calculate(Item left, Item right) {
        if (!left.containsSimilarValues(right)) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (ItemValue value1 : left.getValues()) {
            ItemValue value2 = right.getValueByName(value1.getName());
            if (value2 != null) {
                BigDecimal val = value1.getRate().subtract(value2.getRate());
                sum = sum.add(val.multiply(val));
            }
        }

        final BigDecimal one = new BigDecimal(1);
        return one.divide(one.add(sum), new MathContext(10, RoundingMode.HALF_UP));
    }
}
