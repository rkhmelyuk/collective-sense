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
            def articleWords = wordsCounter.count(record.title)// + ' ' + record.description)
            words.put(record.title, articleWords)

            articleWords.each { key, value ->
                wordsCount.put(key, wordsCount.get(key) + 1)
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
        for (record in records) {
            def articleWords = wordsCounter.count(record.title)
            words.put(record.title, articleWords)

            articleWords.each { key, value ->
                wordsCount.put(key, wordsCount.get(key) + 1)
            }
        }

        def regardWords = []
        wordsCount.each { word, count ->
            def rel = (float) count / records.size()
            if (rel > 0.01 && rel <= 0.2) {
                regardWords << word
            }
        }

        def result = [:].withDefault { 0 }
        for (word in regardWords) {
            def vector = words.collect { key, value -> value[word] }
            result.put(word, vector)
        }

        return result
    }
}
