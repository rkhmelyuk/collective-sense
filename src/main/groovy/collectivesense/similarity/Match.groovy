package collectivesense.similarity

import collectivesense.Item

/**
 * Gets the matches for specified item with other items in the list.
 * 
 * @author Ruslan Khmelyuk
 * @created 2010-12-09
 */
class Match {

    Map<Item, BigDecimal> match(Item item, List<Item> items, Similarity similarity) {
        def result = [:]

        for (eachItem in items) {
            if (eachItem.name != item.name) {
                result[eachItem] = similarity.calculate(item, eachItem)
            }
        }

        return result
    }
}
