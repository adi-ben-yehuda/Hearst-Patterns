import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Adi Ben Yehuda 211769757
 * @since 2022-06-12
 */
public class CreateHypernymDatabase {
    /**
     * The function will read all the files in the directory, find and
     * aggregate hypernym relations that match the Hearst patterns using
     * regular expressions, and save them in a txt file.
     *
     * @param args contains two arguments: (1) the path to the directory of
     *             the corpus and (2) the path to the output file.
     */
    public static void main(String[] args) throws IOException {
        String pathCorpus = null, outputFile = null, s;
        BufferedReader reader = null;
        PrintWriter out = null;
        HypernymAndHyponymByData relations = new HypernymAndHyponymByData();

        try {
            // TODO: change it at the end
            // The path to the directory of the corpus
            pathCorpus = "C:\\Users\\adiby\\Documents\\corpus";
            // The path to the output file
            outputFile = "hypernym_db.txt";
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
                        relations.checkAndAdd(s);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                }
            }

            // Save the predicted relations in a file.
            out = new PrintWriter(new FileWriter(outputFile));
            relations.printToFile(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}

