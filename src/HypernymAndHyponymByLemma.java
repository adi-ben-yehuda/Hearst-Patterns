import java.util.*;

public class HypernymAndHyponymByLemma extends HypernymAndHyponymByData {
    private Map hypernyms;

    public HypernymAndHyponymByLemma() {
        super();
        this.hypernyms = new HashMap();
    }

    public void checkAndAddByLemma(String data, String lemma) {
        if (data.contains(lemma) || data.contains(lemma.toLowerCase())) {
            checkAndAdd(data);
        }
    }

    /**
     * The function sorts the hypernyms in descending order according to (x).
     */
    private void sortHypernyms() {
        LinkedHashMap<String, Integer> reverseOrder = new LinkedHashMap<>();
        List<Map.Entry<String, Integer>> hypernymsList = new
                ArrayList<>(hypernyms.entrySet());
        hypernymsList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Convert the Map.Entry to LinkedHashMap
        for (Map.Entry<String, Integer> hypernym : hypernymsList) {
            reverseOrder.put(hypernym.getKey(), hypernym.getValue());
        }

        hypernyms = reverseOrder;
    }

    public void printByLemma(String lemma) {
        boolean isExist = false;
        String hypernym;
        HashMap hyponyms;

        Set keys = super.getRelations().keySet();
        Iterator i = keys.iterator();

        while (i.hasNext()) {
            hypernym = i.next().toString();
            hyponyms = (HashMap) super.getRelations().get(hypernym);

            if (hyponyms.containsKey(lemma.toLowerCase())) {
                this.hypernyms.put(hypernym, hyponyms.get(lemma.toLowerCase()));
                isExist = true;
            } else if (hyponyms.containsKey(lemma)) {
                isExist = true;
                this.hypernyms.put(hypernym, hyponyms.get(lemma));
            }
        }

        sortHypernyms();

        Object[] hypernymsArr = hypernyms.keySet().toArray();

        for (int j = 0; j < hypernymsArr.length; j++) {
            System.out.println(hypernymsArr[j] + ": (" + hypernyms.get(hypernymsArr[j]) + ")");
        }

        if (!isExist) {
            System.out.println("The lemma doesn't appear in the corpus.");
        }
    }

}
