package collectivesense.cluster

import collectivesense.test.ArticleRecord
import collectivesense.util.WordsCounter

/**
 * @author Ruslan Khmelyuk
 * @created 2010-12-16
 */
class ArticleMatrixBuilder {

    Map build(List<ArticleRecord> records) {
        def words = [:]
        def wordsCount = [:].withDefault { 0 }

        def wordsCounter = new WordsCounter()
        def index = 0
        for (record in records) {
            def articleWords = wordsCounter.count(record.title)
            words[record.title] = articleWords

            articleWords.each { entry ->
                wordsCount[entry.key]++
            }

            if (++index == 10000) {
                break
            }
        }

        def regardWords = []
        wordsCount.each { word, count ->
            def rel = (float) count / 10000//records.size()
            if (rel > 0.01 && rel <= 0.4) {
                regardWords << word
            }
        }


        def result = [:].withDefault { 0 }

        words.each { title, articleWords ->
            def vector = regardWords.collect { articleWords[it] }
            result[title] = vector
        }

        return result
    }

    Map revert(List<ArticleRecord> records) {
        def words = [:]
        def wordsCount = [:].withDefault { 0 }

        def wordsCounter = new WordsCounter()
        def index = 0
        for (record in records) {
            def articleWords = wordsCounter.count(record.description)
            words[record.title] = articleWords

            articleWords.each { entry ->
                wordsCount[entry.key]++
            }

            if (++index == 1000) {
                break
            }
        }

        def regardWords = []
        wordsCount.each { word, count ->
            def rel = (float) count / 1000//records.size()
            if (rel > 0.05 && rel < 0.5) {
                regardWords << word
            }
        }


        def result = [:].withDefault { 0 }

        regardWords.each { word ->
            def vector = words.collect { key, value -> value[word] }
            result[word] = vector

        }

        return result
    }
}
