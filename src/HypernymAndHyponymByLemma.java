import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Comparator;

/**
 * @author Adi Ben Yehuda
 * @since 2022-06-19
 */
public class HypernymAndHyponymByLemma extends HypernymAndHyponymByData {
    private Map hypernyms;

    /**
     * The function builds a new HypernymAndHyponymByLemma object.
     */
    public HypernymAndHyponymByLemma() {
        super();
        this.hypernyms = new HashMap();
    }

    /**
     * The function sorts the hypernyms in descending order according to (x).
     */
    private void sortHypernyms() {
        LinkedHashMap<String, Integer> reverseOrder = new LinkedHashMap<>();
        List<Map.Entry<String, Integer>> hypernymsList = new
                ArrayList<>(hypernyms.entrySet());
        hypernymsList.sort(Map.Entry.comparingByKey());
        hypernymsList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Convert the Map.Entry to LinkedHashMap
        for (Map.Entry<String, Integer> hypernym : hypernymsList) {
            reverseOrder.put(hypernym.getKey(), hypernym.getValue());
        }

        hypernyms = reverseOrder;
    }

    /**
     * The function finds the NPs that exist in the data (when the lemma
     * exists in the data) and adds them to the hypernyms map.
     *
     * @param data
     * @param lemma
     */
    public void checkAndAddByLemma(String data, String lemma) {
        if (data.contains(lemma)) {
            checkAndAdd(data);
        }
    }

    /**
     * The function prints all the hypernym and number of the lemma to a file.
     * where the number corresponds to the number of occurrences of the
     * relations (across all possible patterns) in the corpus.
     *
     * @param lemma
     */
    public void printByLemma(String lemma) {
        boolean isExist = false;
        String hypernym;
        HashMap hyponyms;

        // Convert the relations to set.
        Set keys = super.getRelations().keySet();
        Iterator i = keys.iterator();

        while (i.hasNext()) {
            hypernym = i.next().toString();
            hyponyms = (HashMap) super.getRelations().get(hypernym);

            if (hyponyms.containsKey(lemma)) {
                isExist = true;
                this.hypernyms.put(hypernym, hyponyms.get(lemma));
            }
        }

        sortHypernyms();

        // Convert the map to array.
        Object[] hypernymsArr = hypernyms.keySet().toArray();

        /* Print all the possible hypernyms of the input lemma and the number
         of occurrences of the relations. */
        for (int j = 0; j < hypernymsArr.length; j++) {
            System.out.println(hypernymsArr[j] + ": ("
                    + hypernyms.get(hypernymsArr[j]) + ")");
        }

        // That is, the input lemma doesn't appear in the corpus.
        if (!isExist) {
            System.out.println("The lemma doesn't appear in the corpus.");
        }
    }

}
