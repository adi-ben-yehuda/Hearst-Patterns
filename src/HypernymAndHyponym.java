import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HypernymAndHyponym {
    private Map relations;
    private String[] regexes;

    public HypernymAndHyponym() {
        this.relations = new HashMap();
        this.regexes = new String[8];
        initRegexes();
    }

    private void initRegexes() {
        // NP {,} such as NP {, NP, ..., {and|or} NP}
        this.regexes[0] = "<np>[^><]*<\\/np> such as (((<np>[^><]*<\\/np> ?" +
                ",? ?)+ (and|or) <np>[^><]*<\\/np>)|<np>[^><]*<\\/np>)";
        // such NP as NP {, NP, ..., {and|or} NP}
        this.regexes[1] = "such <np>[^><]*<\\/np> as (((<np>[^><]*<\\/np> ?," +
                "? ?)+ (and|or) <np>[^><]*<\\/np>)|<np>[^><]*<\\/np>)";
        // NP {,} including NP {, NP, ..., {and|or} NP}
        this.regexes[2] = "<np>[^><]*<\\/np> including (((<np>[^><]*<\\/np> ?" +
                ", ? ?)+ (and|or) <np>[^><]*<\\/np>)|<np>[^><]*<\\/np>)";
        // NP {,} especially NP {, NP, ..., {and|or} NP}
        this.regexes[3] = "<np>[^><]*<\\/np> especially (((<np>[^><]*<\\/np>" +
                " ?, ? ?)+ (and|or) <np>[^><]*<\\/np>)|<np>[^><]*<\\/np>)";
        // NP {,} which is (a\an\null) NP
        this.regexes[4] = "(((<np>[^><]*<\\/np> ?,? ?)+ (and|or) <np>[^><]*<\\/" +
                "np>)|(<np>[^><]*<\\/np>)) which is (a?(an)? ?)<np>[^><]*<\\/np>";
        // NP {,} which is an example of (a\an\null) NP
        this.regexes[5] = "(((<np>[^><]*<\\/np> ?,? ?)+ (and|or) <np>[^><]*<" +
                "\\/np>)|(<np>[^><]*<\\/np>)) which is an example of (a?(an)" +
                "? ?)<np>[^><]*<\\/np>";
        // NP {,} which is a kind of (a\an\null) NP
        this.regexes[6] = "(((<np>[^><]*<\\/np> ?,? ?)+ (and|or) <np>[^><]*<" +
                "\\/np>)|<np>[^><]*<\\/np>) which is a kind of (a?(an)? ?)<np" +
                ">[^><]*<\\/np>";
        // NP {,} which is a class of (a\an\null) NP
        this.regexes[7] = "(((<np>[^><]*<\\/np> ?,? ?)+ (and|or) <np>[^><]*<" +
                "\\/np>)|<np>[^><]*<\\/np>) which is a class of (a?(an)? ?)" +
                "<np>[^><]*<\\/np>";
    }

    private List<String> extractWords(String sentence) {
        List<String> words = new ArrayList<>();
        String extractRegex = "[ a-z]*<\\/np>";
        Pattern pattern = Pattern.compile(extractRegex);
        Matcher m = pattern.matcher(sentence);

        while (m.find()) {
            String text = m.group(0).toLowerCase();
            words.add(text.substring(0, text.length() - 5));
        }
        return words;
    }

    private void add(String hypernym, String hyponym) {
        // That is, the hypernym doesn't exist in the relations map.
        if (!this.relations.containsKey(hypernym)) {
            Map temp = new HashMap<>();
            temp.put(hyponym, 1);
            this.relations.put(hypernym, temp);
        } else {
            // That is, the hypernym exists in the relations map.
            Map temp = (Map) this.relations.get(hypernym);
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

    private void sortHyponyms() {
        LinkedHashMap<String, Integer> reverseOrder;
        List<Map.Entry<String, Integer>> values;

        // Convert the relations map to be set.
        Set<Map.Entry<String, Map<String, Integer>>> relation = relations.entrySet();

        for (Map.Entry<String, Map<String, Integer>> keyAndValues : relation) {
            reverseOrder = new LinkedHashMap<>();
            values = new ArrayList<>(keyAndValues.getValue().entrySet());
            /* Sort the list of co-hyponyms according to (x) in descending
             order. If two hyponyms have the same number of relations, Sort
             them alphabetically. */
            values.sort(Map.Entry.comparingByKey());
            values.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            // Convert the Map.Entry to LinkedHashMap
            for (Map.Entry<String, Integer> hyponym: values) {
                String key = hyponym.getKey();
                Integer value = hyponym.getValue();
                reverseOrder.put(key, value);
            }

            relations.replace(keyAndValues.getKey(), reverseOrder);
        }
    }

    private void sortHypernyms() {
        LinkedHashMap<String, Map<String, Integer>> relationsMap = new
                LinkedHashMap<>();
        List<Map.Entry<String, Map<String, Integer>>> relationsSort = new
                ArrayList<>(relations.entrySet());
        // The hypernyms should be sorted alphabetically.
        relationsSort.sort(Map.Entry.comparingByKey());

        // Convert the Map.Entry to LinkedHashMap
        for (Map.Entry<String, Map<String, Integer>> hypernym: relationsSort) {
            String key = hypernym.getKey();
            Map<String, Integer> value = hypernym.getValue();
            relationsMap.put(key, value);
        }

        relations = relationsMap;
    }

    public void checkAndAdd(String data) {
        for (int i = 0; i < regexes.length; i++) {
            String suchAsPattern = regexes[i];
            Pattern pattern = Pattern.compile(suchAsPattern);
            Matcher m = pattern.matcher(data);

            while (m.find()) {
                List<String> words = extractWords(m.group(0).toLowerCase());
                // The first NP is the hypernym and the other NPs are hyponyms.
                if (i <= 3) {
                    for (int j = 1; j < words.size(); j++) {
                        add(words.get(0), words.get(j));
                    }
                } else if (i >= 4) {
                    // The first NP is the hyponym and the second in a hypernym.
                    for (int j = 0; j < words.size() - 1; j++) {
                        add(words.get(words.size() - 1), words.get(j));
                    }
                }
            }
        }

    }

    public String GetAllRelations() {
        String data = "";
        Map values = null;

        sortHypernyms();
        sortHyponyms();

        for (int i = 0; i < relations.size(); i++) {
            values = (Map) relations.get(relations.keySet().toArray()[i]);

            // Ignore hypernyms that have less than 3 distinct hyponyms.
            if (values.size() > 2) {
                data += relations.keySet().toArray()[i] + ": ";
                data += values.keySet().toArray()[0] + " (" +
                        values.get(values.keySet().toArray()[0]) + ")";
                for (int j = 1; j < values.keySet().toArray().length; j++) {
                    data += ", " + values.keySet().toArray()[j] + " (" +
                            values.get(values.keySet().toArray()[j]) + ")";
                }

                data += "\n";
            }
        }

        return data;
    }

}
