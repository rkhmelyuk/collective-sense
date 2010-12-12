package collectivesense.similarity

import collectivesense.Item
import collectivesense.ItemValue
import collectivesense.Rated

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-12
 */
class RecommendationItems {

    List<Rated> recommendationItems(List<Item> items, Item item, Map<Item, List<Rated>> matches) {
        def values = [:]
        def scores = [:]
        def totalSims = [:]

        for (value in item.values) {
            def rates = matches.findResult{ key, val ->
                if (key.name == value.name) {
                    return val
                }
                return null
            }
            for (rate in rates) {
                def name = rate.item.name
                if (item.containsValueWithName(name)) {
                    continue
                }

                values[name] = rate.item
                totalSims[name] = totalSims.get(name, 0) + rate.rate
                scores[name] = scores.get(name, 0) + value.rate * rate.rate
            }
        }

        def result = scores.collect { key, value ->
            new Rated<ItemValue>(item: values[key], rate: value / totalSims[key])
        }

        result.sort().reverse()
    }
}
