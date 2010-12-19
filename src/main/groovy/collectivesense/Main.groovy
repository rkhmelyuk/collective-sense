package collectivesense

import collectivesense.cluster.ArticleMatrixBuilder
import collectivesense.cluster.HierarchicalCluster
import collectivesense.cluster.PearsonRowsDistance
import collectivesense.similarity.EuclideanDistanceSimilarity
import collectivesense.similarity.PearsonSimilarity
import collectivesense.similarity.RecommendationItems
import collectivesense.similarity.SimilarItems
import collectivesense.test.MedlineHelper

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-09
 */
class Main {

    static void main(String... args) {
        //similarity()
        cluster()
    }

    private static def cluster() {
        def helper = new MedlineHelper()
        def records = helper.readText('/home/ruslan/projects/collectivesense/records.txt')
        def matrix = new ArticleMatrixBuilder().build(records)

        def cluster =
                //new KCluster()
                new HierarchicalCluster()
        def distance =
                //new TanamotoRowsDistance()
                //new EuclideanRowsDistance()
                new PearsonRowsDistance()
        def result = cluster.cluster(matrix, distance, 4)

        cluster.print(matrix, result)
    }

    private static def similarity() {
        def users = [
                'John Doe': [test1: 3, test2: 4, test3: 4],
                'Mark White': [test1: 2, test2: 3, test3: 4, test4: 5, test8: 1, test7: 3],
                'Mary Black': [test1: 3, test2: 4, test3: 5, test4: 4, test5: 3, test6: 5],
                'John Smith': [test1: 1, test2: 4, test3: 3, test4: 1, test5: 5, test6: 1]
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

        /*def matches = new Match().match(persons.first(), persons, 5, pearson)
        matches.each {
            println "$it.item.name: $it.rate"
        }*/

        println '------------------------------------'

        /*def recommendations = new Recommendation().recommend(persons.first(), persons, pearson)
        recommendations.each {
            println "$it.item.name: $it.rate"
        }*/

        def similarItems = new SimilarItems().similarItems(stuff, 5, pearson)
        println similarItems[stuff.first()]

        def recommendedItems = new RecommendationItems().recommendationItems(persons, persons.first(), similarItems)
        recommendedItems.each {
            println "$it.item.name: $it.rate"
        }
    }
}
