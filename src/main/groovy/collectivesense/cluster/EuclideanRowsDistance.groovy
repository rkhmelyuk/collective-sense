package collectivesense.cluster

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-18
 */
class EuclideanRowsDistance implements RowsDistance {

    BigDecimal calculate(List row1, List row2) {
        def sum = 0
        for (int i = 0; i < row1.size(); i++) {
            sum += (row2[i] - row1[i]) ** 2
        }
        return 1 / (1 + sum)
    }

}
