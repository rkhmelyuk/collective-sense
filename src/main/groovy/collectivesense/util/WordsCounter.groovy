package collectivesense.util

/**
 * Calculates number of words in the text.
 * 
 * @author Ruslan Khmelyuk
 * @created 2010-12-16
 */
class WordsCounter {

    Map<String, Integer> count(String text) {
        def result = [:].withDefault {0}
        text = text.toLowerCase()
        def words = text.findAll(/\w+/)
        words.each {
            if (it.size() > 2) {
                result[it]++
            }
        }
        return result
    }

}
