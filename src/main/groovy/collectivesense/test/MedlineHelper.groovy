package collectivesense.test

/**
 * Reads MedLine file and extracts need information.
 *
 * @author Ruslan Khmelyuk
 * @created 2010-12-16
 */
class MedlineHelper {

    List<ArticleRecord> readXml(String fileName) {
        def document = new XmlParser().parse(new File(fileName))

        document.MedlineCitation.collect { article ->
            new ArticleRecord(
                    id: article.PMID.text(),
                    title: article.Article.ArticleTitle.text(),
                    description: article.Article.Abstract.AbstractText.text()
            )
        }
    }

    void writeText(String fileName, List<ArticleRecord> records) {
        def file = new File(fileName)
        file.withOutputStream {out ->
            records.each {
                out << it.id
                out << '\n'
                out << it.title
                out << '\n'
                out << it.description
                out << '\n'
            }
        }
    }

    List<ArticleRecord> readText(String fileName) {
        def result = []

        def record = null, index = 0
        new File(fileName).eachLine { line ->
            index++
            if (index == 1) {
                record = new ArticleRecord()
                record.id = line
            }
            else if (index == 2) {
                record.title = line
            }
            else if (index == 3) {
                record.description = line
                result << record
                index = 0
            }
        }

        return result
    }

    static void main(String... args) {
        def helper = new MedlineHelper()
        /*def records = helper.readXml('/home/ruslan/projects/collectivesense/medline11n0357.xml')
        helper.writeText('/home/ruslan/projects/collectivesense/records.txt', records)*/

        def records = helper.readText('/home/ruslan/projects/collectivesense/records.txt')
        println records.size()
    }

}
