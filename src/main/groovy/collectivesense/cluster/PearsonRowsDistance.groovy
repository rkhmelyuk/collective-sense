package collectivesense.cluster

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-18
 */
class PearsonRowsDistance implements RowsDistance {

    BigDecimal calculate(List row1, List row2) {
        def n = 0
        def sum1 = 0.0
        def sum2 = 0.0
        def sum1Sq = 0.0
        def sum2Sq = 0.0
        def pSum = 0.0
        for (int i = 0; i < row1.size(); i++) {
            def rate1 = row1[i]
            def rate2 = row2[i]

            sum1 += rate1
            sum2 += rate2
            sum1Sq += rate1 ** 2
            sum2Sq += rate2 ** 2
            pSum += rate1 * rate2
            n++
        }

        if (n == 0) {
            return 0
        }

        def den = Math.sqrt((double) (sum1Sq - (sum1 ** 2) / n) * (sum2Sq - (sum2 ** 2) / n))
        if (den == 0) {
            return 0
        }

        return 1 - (pSum - (sum1 * sum2 / n)) / den
    }

}
