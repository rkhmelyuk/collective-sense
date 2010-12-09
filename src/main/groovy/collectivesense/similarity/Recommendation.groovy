package collectivesense.similarity

import collectivesense.Item
import collectivesense.ItemValue

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-09
 */
class Recommendation {

    Map<ItemValue, BigDecimal> recommend(Item item, List<Item> items, Similarity similarity) {
        def totals = [:]
        def simSums = [:]

        for (eachItem in items) {
            if (eachItem.name != item.name) {
                def sim = similarity.calculate(item, eachItem)
                if (sim <= 0) {
                    continue
                }

                eachItem.values.each { value ->
                    if (!item.containsValueWithName(value.name)) {
                        totals[value.name] = totals.get(value.name, 0) + value.rate * sim
                        simSums[value.name] = simSums.get(value.name, 0) + sim
                    }
                }
            }
        }

        def result = [:]
        totals.collect { key, value ->
            result[key] = (value / simSums[key])
        }

        return result
    }

}
