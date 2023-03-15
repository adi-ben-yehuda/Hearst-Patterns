import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;

/**
 * @author Adi Ben Yehuda
 * @since 2022-06-13
 */
public class DiscoverHypernym {
    /**
     * The function will search all the possible hypernyms of the input lemma
     * and print them to the console.
     *
     * @param args contains two arguments: (1) the absolute path to the
     *             directory of the corpus and (2) a lemma.
     */
    public static void main(String[] args) throws IOException {
        String pathCorpus, lemma = null, s;
        BufferedReader reader = null;
        HypernymAndHyponymByLemma relations = new HypernymAndHyponymByLemma();

        try {
            pathCorpus = args[0]; // The path to the directory of the corpus.
            lemma = args[1]; // The path to the output file.
            File folder = new File(pathCorpus);
            File[] files = folder.listFiles();

            for (File file : files) {
                // Open the file.
                reader = new BufferedReader(new FileReader(
                        pathCorpus + "\\" + file.getName()));

                // Read each line from the file.
                while ((s = reader.readLine()) != null) {
                        /* Find and aggregate hypernym relations that match the
                         Hearst patterns using regular expressions. */
                    relations.checkAndAddByLemma(s.toLowerCase(), lemma.toLowerCase());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        relations.printByLemma(lemma);
    }
}
