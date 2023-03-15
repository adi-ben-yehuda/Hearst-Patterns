import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Comparator;

/**
 * @author Adi Ben Yehuda
 * @since 2022-06-12
 */
public class HypernymAndHyponymByData {
    private Map relations;
    private Pattern extractPattern;
    private Pattern[] regexesPatterns;

    /**
     * The function builds a new HypernymAndHyponym object.
     */
    public HypernymAndHyponymByData() {
        this.relations = new HashMap();
        initRegexes();

        String extractRegex = "[^<>]*<\\/np>";
        extractPattern = Pattern.compile(extractRegex);
    }

    /**
     * The function initializes the regular expressions that find the
     * hypernym and hyponym in the text.
     */
    private void initRegexes() {
        String[] regexes = new String[3];

        // NP {,} such as NP {, NP, ..., {and|or} NP}
        // NP {,} including NP {, NP, ..., {and|or} NP}
        // NP {,} especially NP {, NP, ..., {and|or} NP}
        regexes[0] = "<np>[^<]+<\\/np>( ,)? (such as|including|especially) "
                + "(((<np>[^<]+<\\/np>( ,)? )+(and |or )?<np>[^><]+<\\/np>)|"
                + "<np>[^<]+<\\/np>)";

        // such NP as NP {, NP, ..., {and|or} NP}
        regexes[1] = "such <np>[^<]+<\\/np> as (((<np>[^<]+<\\/np>( ,)? )+"
                + "(and |or )?<np>[^><]+<\\/np>)|<np>[^<]+<\\/np>)";

        // NP {,} which is (a\an\null) NP
        // NP {,} which is an example of (a\an\null) NP
        // NP {,} which is a kind of (a\an\null) NP
        // NP {,} which is a class of (a\an\null) NP
        regexes[2] = "<np>[^<]+<\\/np>( ,)? which is ((an example|a kind|a "
                + "class)? of )?<np>[^<]+<\\/np>";

        this.regexesPatterns = new Pattern[3];
        for (int i = 0; i < regexes.length; i++) {
            regexesPatterns[i] = Pattern.compile(regexes[i]);
        }
    }

    /**
     * The function gets sentence and extract the NPs from it.
     *
     * @param sentence
     * @return all the NPs that exist in the sentence.
     */
    private List<String> extractWords(String sentence) {
        List<String> words = new ArrayList<>();
        Matcher m = extractPattern.matcher(sentence);

        while (m.find()) {
            String text = m.group(0);
            // Adds the NPs to the words list.
            words.add(text.substring(0, text.length() - 5));
        }
        return words;
    }

    /**
     * The function adds the hypernym and hyponym to the relations map.
     *
     * @param hypernym
     * @param hyponym
     */
    private void add(String hypernym, String hyponym) {
        Map temp;

        // That is, the hypernym doesn't exist in the relations map.
        if (!this.relations.containsKey(hypernym)) {
            temp = new HashMap<>();
            temp.put(hyponym, 1); // Add the hyponym to the temp map.
            this.relations.put(hypernym, temp);
        } else {
            // That is, the hypernym exists in the relations map.
            temp = (Map) this.relations.get(hypernym);
            // That is, the hyponym exists in the map of the hypernym.
            if (temp.containsKey(hyponym)) {
                temp.replace(hyponym, (int) temp.get(hyponym) + 1);
            } else {
                // That is, the hyponym doesn't exist in the map of the hypernym.
                temp.put(hyponym, 1);
            }
            this.relations.replace(hypernym, temp);
        }
    }

    /**
     * The function sorts the list of co-hyponyms according to (x) in
     * descending order. If two hyponyms have the same number of relations,
     * sort them alphabetically.
     */
    private void sortHyponyms() {
        LinkedHashMap<String, Integer> reverseOrder;
        List<Map.Entry<String, Integer>> values;

        // Convert the relations map to be set.
        Set<Map.Entry<String, Map<String, Integer>>> relation =
                relations.entrySet();

        for (Map.Entry<String, Map<String, Integer>> keyAndValues : relation) {
            // Ignore hypernyms that have less than 3 distinct hyponyms.
            if (keyAndValues.getValue().size() > 2) {
                reverseOrder = new LinkedHashMap<>();
                values = new ArrayList<>(keyAndValues.getValue().entrySet());
                /* Sort the list of co-hyponyms according to (x) in descending
                order. If two hyponyms have the same number of relations, Sort
                them alphabetically. */
                values.sort(Map.Entry.comparingByKey());
                values.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

                // Convert the Map.Entry to LinkedHashMap
                for (Map.Entry<String, Integer> hyponym : values) {
                    reverseOrder.put(hyponym.getKey(), hyponym.getValue());
                }

                relations.replace(keyAndValues.getKey(), reverseOrder);
            }
        }
    }

    /**
     * The function sorts the hypernyms alphabetically.
     */
    private void sortHypernyms() {
        LinkedHashMap<String, Map<String, Integer>> relationsMap = new
                LinkedHashMap<>();
        List<Map.Entry<String, Map<String, Integer>>> relationsSort = new
                ArrayList<>(relations.entrySet());
        // The hypernyms should be sorted alphabetically.
        relationsSort.sort(Map.Entry.comparingByKey());

        // Convert the Map.Entry to LinkedHashMap
        for (Map.Entry<String, Map<String, Integer>> hypernym : relationsSort) {
            relationsMap.put(hypernym.getKey(), hypernym.getValue());
        }

        relations = relationsMap;
    }

    /**
     * The function finds the NPs that exist in the data and adds them to
     * the relations map.
     *
     * @param data
     */
    public void checkAndAdd(String data) {
        String hypernym;

        for (int i = 0; i < regexesPatterns.length; i++) {
            Matcher m = regexesPatterns[i].matcher(data);

            while (m.find()) {
                // Gets all the NPs that exist in the date.
                List<String> words = extractWords(m.group(0));
                int wordsSize = words.size();

                // The first NP is the hypernym and the other NPs are hyponyms.
                if (i < 2) {
                    hypernym = words.get(0);
                    for (int j = 1; j < wordsSize; j++) {
                        add(hypernym, words.get(j));
                    }
                } else {
                    // The first NP is the hyponym and the second in a hypernym.
                    for (int j = 0; j < wordsSize - 1; j++) {
                        add(words.get(wordsSize - 1), words.get(j));
                    }
                }
            }
        }
    }

    /**
     * The function prints all the hypernym and hyponym to a file.
     *
     * @param out the file to print to.
     */
    public void printToFile(PrintWriter out) {
        Map values;
        Object[] valuesArr;

        sortHypernyms();
        sortHyponyms();

        Object[] relationsArr = relations.keySet().toArray();

        for (int i = 0; i < relations.size(); i++) {
            values = (Map) relations.get(relationsArr[i]);

            // Ignore hypernyms that have less than 3 distinct hyponyms.
            if (values.size() > 2) {
                valuesArr = values.keySet().toArray();
                out.print(relationsArr[i] + ": " + valuesArr[0] + " ("
                        + values.get(valuesArr[0]) + ")");

                for (int j = 1; j < valuesArr.length; j++) {
                    out.print(", " + valuesArr[j] + " ("
                            + values.get(valuesArr[j]) + ")");
                }

                if (i != relations.size() - 1) {
                    out.println("");
                }
            }
        }
    }

    /**
     * The function returns the relations map.
     *
     * @return relations map.
     */
    public Map getRelations() {
        return relations;
    }
}


