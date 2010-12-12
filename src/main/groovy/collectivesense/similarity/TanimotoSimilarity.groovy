package collectivesense.similarity

import collectivesense.Item

/**
 * Reference
 * <a href="http://habrahabr.ru/blogs/algorithm/104901/">http://habrahabr.ru/blogs/algorithm/104901/</a>.
 *
 * @author Ruslan Khmelyuk
 * @created 2010-12-12
 */
class TanimotoSimilarity implements Similarity {

    BigDecimal calculate(Item left, Item right) {
        def leftCount = left.values.size()
        def rightCount = right.values.size()

        def leftValueNames = left.values.name
        def rightValueNames = right.values.name
        leftValueNames.retainAll(rightValueNames)
        def commonCount = leftValueNames.size()

        return commonCount / (leftCount + rightCount - commonCount)
    }
}
