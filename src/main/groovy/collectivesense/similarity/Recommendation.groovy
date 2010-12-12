package collectivesense.similarity

import collectivesense.Item
import collectivesense.ItemValue
import collectivesense.Rated

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-09
 */
class Recommendation {

    List<Rated> recommend(Item item, List<Item> items, Similarity similarity) {
        def values = [:]
        def totals = [:]
        def simSums = [:]

        for (eachItem in items) {
            if (eachItem.name != item.name) {
                def sim = similarity.calculate(item, eachItem)
                if (sim <= 0) {
                    continue
                }

                eachItem.values.each { value ->
                    def name = value.name
                    if (!item.containsValueWithName(name)) {
                        values[name] = value
                        totals[name] = totals.get(name, 0) + value.rate * sim
                        simSums[name] = simSums.get(name, 0) + sim
                    }
                }
            }
        }

        def result = totals.collect { key, value ->
            def sim = simSums[key]
            sim ? new Rated<ItemValue>(item: values[key], rate: value / sim) : null
        }

        result.grep { it }.sort().reverse()
    }

}
