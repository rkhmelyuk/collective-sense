package collectivesense.util

/**
 * Calculates number of words in the text.
 * 
 * @author Ruslan Khmelyuk
 * @created 2010-12-16
 */
class WordsCounter {

    def ignoreWords = ['and', 'the', 'with', 'for', 'a', 'then', 'else', 'but', 'many', 'own', 'other', 'another', 'some', 'more']

    Map<String, Integer> count(String text) {
        def result = [:].withDefault {0}
        text = text.toLowerCase()
        def words = text.findAll(/\w+/)
        words.each {
            if (it.size() > 2 && !ignoreWords.contains(it)) {
                result.put(it, result.get(it) + 1)
            }
        }
        return result
    }

}
