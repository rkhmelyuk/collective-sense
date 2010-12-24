package collectivesense.similarity;

import collectivesense.Item;
import collectivesense.ItemValue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-23
 */
public class PearsonSimilarity implements Similarity{

    public BigDecimal calculate(Item left, Item right) {
        int n = 0;

        final MathContext context = new MathContext(10, RoundingMode.HALF_UP);

        BigDecimal sum1 = BigDecimal.ZERO;
        BigDecimal sum2 = BigDecimal.ZERO;
        BigDecimal sum1Sq = BigDecimal.ZERO;
        BigDecimal sum2Sq = BigDecimal.ZERO;
        BigDecimal pSum = BigDecimal.ZERO;

        for (ItemValue value1 : left.getValues()) {
            for (ItemValue value2 : right.getValues()) {
                if (value1.getName().equals(value2.getName())) {
                    BigDecimal rate1 = value1.getRate();
                    BigDecimal rate2 = value2.getRate();

                    sum1 = sum1.add(rate1);
                    sum2 = sum2.add(rate2);
                    sum1Sq = sum1Sq.add(rate1.multiply(rate1));
                    sum2Sq = sum2Sq.add(rate2.multiply(rate2));
                    pSum = pSum.add(rate1.multiply(rate2));
                    n++;
                }
            }
        }

        if (n == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal bn = new BigDecimal(n);
        BigDecimal sub1 = sum1Sq.subtract(sum1.multiply(sum1).divide(bn, context));
        BigDecimal sub2 = sum2Sq.subtract(sum2.multiply(sum2.divide(bn, context)));
        double den = Math.sqrt(sub1.multiply(sub2).doubleValue());
        if (den == 0) {
            return BigDecimal.ZERO;
        }

        return pSum.subtract(sum1.multiply(sum2).divide(bn, context))
                .divide(new BigDecimal(den), context);
    }

    private BigDecimal toBigDecimal(Object value, MathContext context) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Double) {
            return new BigDecimal((Double) value, context);
        }
        if (value instanceof Integer) {
            return new BigDecimal((Integer) value, context);
        }
        return new BigDecimal(value.toString(), context);
    }
}
