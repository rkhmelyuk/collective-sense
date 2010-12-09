package collectivesense

import collectivesense.similarity.EuclideanDistanceSimilarity
import collectivesense.similarity.Match
import collectivesense.similarity.PearsonSimilarity
import collectivesense.similarity.Recommendation

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-09
 */
class Main {

    static void main(String... args) {
        def users = [
                'John Doe': [test1: 3, test2: 4, test3: 4],
                'Mark White': [test1: 2, test2: 3, test3: 4, test4: 5],
                'Mary Black': [test1: 3, test2: 4, test3: 5, test4: 4, test5: 3],
                'John Smith': [test1: 1, test2: 4, test3: 3, test4: 1, test5: 5]
        ]

        def persons = users.collect { entry ->
            new Item(name: entry.key, values: entry.value.collect { new ItemValue(it.key, it.value)})
        }

        def stuff = [] as List<Item>
        users.each { entry ->
            entry.value.each { valueEntry ->
                def item = stuff.find { it.name == valueEntry.key }
                if (item == null) {
                    item = new Item(name: valueEntry.key, values: [])
                    stuff << item
                }
                item.values << new ItemValue(entry.key, valueEntry.value)
            }
        }

        def pearson = new PearsonSimilarity()
        def euclidean = new EuclideanDistanceSimilarity()

        def matches = new Match().match(persons.first(), persons, pearson)
        matches.each {
            println "$it.key.name: $it.value"
        }

        println '------------------------------------'

        def recommendations = new Recommendation().recommend(persons.first(), persons, pearson)
        recommendations.each {
            println "$it.key: $it.value"
        }
    }
}
