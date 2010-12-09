package collectivesense.similarity

import collectivesense.Item

/**
 * Used to calculated similarity between two items.
 * 
 * @author Ruslan Khmelyuk
 * @created 2010-12-09
 */
public interface Similarity {

    BigDecimal calculate(Item left, Item right)

}