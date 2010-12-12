package collectivesense.similarity

import collectivesense.Item

/**
 * Calculates similarity using Pearson's coefficient.
 *
 * @author Ruslan Khmelyuk
 * @created 2010-12-09
 */
class PearsonSimilarity implements Similarity {

    BigDecimal calculate(Item left, Item right) {
        def n = 0
        def sum1 = 0
        def sum2 = 0
        def sum1Sq = 0
        def sum2Sq = 0
        def pSum = 0
        for (value1 in left.values) {
            for (value2 in right.values) {
                if (value1.name == value2.name) {
                    def rate1 = value1.rate
                    def rate2 = value2.rate

                    sum1 += rate1
                    sum2 += rate2
                    sum1Sq += rate1 ** 2
                    sum2Sq += rate2 ** 2
                    pSum += rate1 * rate2
                    n++
                    break
                }
            }
        }

        if (n == 0) {
            return 0
        }

        def den = Math.sqrt((sum1Sq - (sum1 ** 2) / n) * (sum2Sq - (sum2 ** 2) / n))
        if (den == 0) {
            return 0
        }

        return (pSum - (sum1 * sum2 / n)) / den
    }

}
