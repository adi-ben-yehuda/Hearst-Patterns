import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Adi Ben Yehuda 211769757
 * @since 2022-06-13
 */
public class DiscoverHypernym {
    /**
     * The function will search all the possible hypernyms of the input lemma
     * and print them to the console
     *
     * @param args contains two arguments: (1) the absolute path to the
     *             directory of the corpus and (2) a lemma.
     */
    public static void main(String[] args) throws IOException {
        String pathCorpus = null, lemma = null, s;
        BufferedReader reader = null;
        HypernymAndHyponymByLemma relations = new HypernymAndHyponymByLemma();

        // TODO: change it at the end
        // The path to the directory of the corpus
        pathCorpus = "C:\\Users\\adiby\\Documents\\test";
        // The path to the output file
        lemma = "Arabic";
        File folder = new File(pathCorpus);
        File[] files = folder.listFiles();

        for (File file : files) {
            // TODO: remove it at the end
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(file.getName() + " " + dtf.format(now));

            try {
                // Open the file
                reader = new BufferedReader(new FileReader(
                        pathCorpus + "\\" + file.getName()));

                // Read each line from the file.
                while ((s = reader.readLine()) != null) {
                        /* Find and aggregate hypernym relations that match the
                         Hearst patterns using regular expressions */
                    relations.checkAndAddByLemma(s, lemma);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }

        relations.printByLemma(lemma);

    }
}
