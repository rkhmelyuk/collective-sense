package collectivesense.similarity

import collectivesense.Item
import collectivesense.Rated

/**
 * Prepare similar items.
 * 
 * @author Ruslan Khmelyuk
 * @created 2010-12-12
 */
class SimilarItems {

    Map<Item, List<Rated>> similarItems(List<Item> items, int n, Similarity similarity) {
        def result = [:]

        def match = new Match()
        for (Item item in items) {
            def matches = match.match(item, items, n, similarity)
            result[item] = matches
        }

        return result
    }
}
