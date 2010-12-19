package collectivesense.cluster

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-18
 */
interface RowsDistance {

    BigDecimal calculate(List row1, List row2)

}
