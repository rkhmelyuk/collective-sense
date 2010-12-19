package collectivesense.cluster

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-19
 */
class TanamotoRowsDistance implements RowsDistance {

    BigDecimal calculate(List row1, List row2) {
        def sum1 = 0, sum2 = 0, count = 0
        for (int i = 0; i < row1.size(); i++) {
            boolean flag = false
            if (row1[i]) {
                sum1++
                flag = true
            }
            if (row2[i]) {
                sum2++
                flag = true
            }

            if (flag) {
                count++
            }
        }

        def dif = sum1 + sum2 - count
        if (dif > 0) {
            return 1 - (count / dif)
        }
        return 1
    }

}
