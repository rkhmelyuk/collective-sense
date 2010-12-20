package collectivesense.cluster;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-20
 */
public class PearsonRowsDistance implements RowsDistance {

    public BigDecimal calculate(List row1, List row2) {
        int n = 0;

        final MathContext context = new MathContext(10, RoundingMode.HALF_UP);

        BigDecimal sum1 = BigDecimal.ZERO;
        BigDecimal sum2 = BigDecimal.ZERO;
        BigDecimal sum1Sq = BigDecimal.ZERO;
        BigDecimal sum2Sq = BigDecimal.ZERO;
        BigDecimal pSum = BigDecimal.ZERO;

        for (int i = 0; i < row1.size(); i++) {
            BigDecimal rate1 = toBigDecimal(row1.get(i), context);
            BigDecimal rate2 = toBigDecimal(row2.get(i), context);

            sum1 = sum1.add(rate1);
            sum2 = sum2.add(rate2);
            sum1Sq = sum1Sq.add(rate1.multiply(rate1));
            sum2Sq = sum2Sq.add(rate2.multiply(rate2));
            pSum = pSum.add(rate1.multiply(rate2));
            n++;
        }

        if (n == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal bn = new BigDecimal(n);
        BigDecimal left = sum1Sq.subtract(sum1.multiply(sum1).divide(bn, context));
        BigDecimal right = sum2Sq.subtract(sum2.multiply(sum2.divide(bn, context)));
        double den = Math.sqrt(left.multiply(right).doubleValue());
        if (den == 0) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(1, context).subtract(pSum
                .subtract(sum1.multiply(sum2).divide(bn, context))
                .divide(new BigDecimal(den), context));
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
