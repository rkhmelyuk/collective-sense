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
        for (record in records) {
            def articleWords = wordsCounter.count(record.title + ' ' + record.description)
            words.put(record.title, articleWords)

            articleWords.each { key, value ->
                wordsCount.put(key, value + 1)
            }
        }

        def regardWords = []
        wordsCount.each { word, count ->
            def rel = (float) count / records.size()
            if (rel >= 0.1 && rel <= 0.5) {
                regardWords << word
            }
        }

        def result = [:].withDefault { 0 }

        words.each { title, articleWords ->
            def vector = regardWords.collect { articleWords[it] }
            result.put(title, vector)
        }

        return result
    }

    Map revert(List<ArticleRecord> records) {
        def words = [:]
        def wordsCount = [:].withDefault { 0 }

        def wordsCounter = new WordsCounter()
        def index = 0
        for (record in records) {
            def articleWords = wordsCounter.count(record.title + ' ' + record.description)
            words[record.title] = articleWords

            articleWords.each { entry ->
                wordsCount[entry.key]++
            }
        }

        def regardWords = []
        wordsCount.each { word, count ->
            def rel = (float) count / records.size()
            if (rel > 0.1 && rel <= 0.5) {
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
