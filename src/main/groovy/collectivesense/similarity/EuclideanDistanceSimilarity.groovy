package collectivesense.similarity

import collectivesense.Item

/**
 * Calculates similarity using Euclidean Distance.
 *
 * @author Ruslan Khmelyuk
 * @created 2010-12-08
 */
class EuclideanDistanceSimilarity implements Similarity{

    BigDecimal calculate(Item left, Item right) {
        if (!left.containsSimilarValues(right)) {
            return 0
        }

        def sum = 0
        for (def value1 in left.values) {
            def value2 = right.getValueByName(value1.name)

            sum += (value1.rate - value2.rate) ** 2
        }

        return 1 / (1 + sum)
    }

}
