import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class WordDateSearch {
    private Map<LocalDate, Map<String, Integer>> wordSearch;
    private static WordDateSearch instance = null;


    private WordDateSearch(String file) {
        try {
            wordSearch = loadFromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static WordDateSearch createInstance() {
        return new WordDateSearch();
    }
    public static WordDateSearch getInstance(String file) {
        if (instance == null) {
            synchronized (WordDateSearch.class) {
                if (instance == null) {
                    instance = new WordDateSearch(file);
                }
            }
        }
        return instance;
    }

    public static WordDateSearch getInstance() {
        if (instance == null) {
            synchronized (WordDateSearch.class) {
                if (instance == null) {
                    instance = new WordDateSearch();
                }
            }
        }
        return instance;
    }

    private WordDateSearch() {
        wordSearch = new HashMap<>();
    }

    public void addWordDateSearch(String word) {
        LocalDate today = LocalDate.now();
        wordSearch.computeIfAbsent(today, k -> new HashMap<>());
        wordSearch.get(today).merge(word, 1, Integer::sum);
    }

    public Map<LocalDate, Map<String, Integer>> getWordDateSearch() {
        return this.wordSearch;
    }

    public void setWordDateSearch(Map<LocalDate, Map<String, Integer>> wordDateSearch) {
        this.wordSearch = wordDateSearch;
    }

    public Map<LocalDate, Map<String, Integer>> getWordFrequencyMap() {
        Map<LocalDate, Map<String, Integer>> wordFrequencyMap = new HashMap<>();
        for (Map.Entry<LocalDate, Map<String, Integer>> dateEntry : wordSearch.entrySet()) {
            LocalDate date = dateEntry.getKey();
            Map<String, Integer> frequencyMap = new HashMap<>(dateEntry.getValue());
            wordFrequencyMap.put(date, frequencyMap);
        }
        return wordFrequencyMap;
    }

    public Map<LocalDate, Map<String, Integer>> filterRangeDate(String date1, String date2) throws DateTimeParseException {
        LocalDate startDate = LocalDate.parse(date1);
        LocalDate endDate = LocalDate.parse(date2);

        Map<LocalDate, Map<String, Integer>> result = new HashMap<>();
        for (Map.Entry<LocalDate, Map<String, Integer>> entry : wordSearch.entrySet()) {
            LocalDate date = entry.getKey();
            if (date.isEqual(startDate) || date.isEqual(endDate) || (date.isAfter(startDate) && date.isBefore(endDate))) {
                result.put(date, new HashMap<>(entry.getValue()));
            }
        }
        return result;
    }

    public void saveToFile(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<LocalDate, Map<String, Integer>> entry : wordSearch.entrySet()) {
                LocalDate date = entry.getKey();
                Map<String, Integer> wordMap = entry.getValue();
                writer.write(date.toString());
                writer.newLine();
                for (Map.Entry<String, Integer> wordEntry : wordMap.entrySet()) {
                    String word = wordEntry.getKey();
                    int count = wordEntry.getValue();
                    writer.write(word + ":" + count);
                    writer.newLine();
                }
            }
        }
    }

    private Map<LocalDate, Map<String, Integer>> loadFromFile(String fileName) throws IOException {
        Map<LocalDate, Map<String, Integer>> wordDateSearch = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LocalDate date = LocalDate.parse(line);
                Map<String, Integer> wordMap = new HashMap<>();
                while ((line = reader.readLine()) != null && !line.isEmpty()) {
                    String[] parts = line.split(":");
                    String word = parts[0];
                    int count = Integer.parseInt(parts[1]);
                    wordMap.put(word, count);
                }
                wordDateSearch.put(date, wordMap);
            }
        }
        return wordDateSearch;
    }
}
