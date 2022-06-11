import java.io.*;
import java.util.Map;

public class CreateHypernymDatabase {
    public static void main(String[] args) throws IOException {
        String pathCorpus = null, outputFile = null;

        BufferedReader reader = null;
        PrintWriter out = null;

        try {
            // The path to the directory of the corpus
            pathCorpus = "C:\\Users\\adiby\\Documents\\test.txt";
            // The path to the output file
            outputFile = "src\\out.txt";

            // Open the file
            reader = new BufferedReader(new FileReader(pathCorpus));
            String s;
            HypernymAndHyponym relations = new HypernymAndHyponym();

            // Read each line from the file.
            while ((s = reader.readLine()) != null) {
                /* Find and aggregate hypernym relations that match the Hearst
                 patterns using regular expressions */
                relations.checkAndAdd(s);
            }

            // Save the predicted relations in a file.
            out = new PrintWriter(new FileWriter(outputFile));
            out.println(relations.GetAllRelations());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
