import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class WordSearchTracker {
    private Map<String, Map<LocalDate, AtomicInteger>> searchMap;

    public WordSearchTracker() {
        searchMap = new ConcurrentHashMap<>();
    }

    public void trackWordSearch(String word) {
        LocalDate currentDate = LocalDate.now();
        searchMap.computeIfAbsent(word, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(currentDate, k -> new AtomicInteger())
                .incrementAndGet();
    }

    public int getSearchCount(String word, LocalDate date) {
        return searchMap.getOrDefault(word, Map.of()).getOrDefault(date, new AtomicInteger()).get();
    }

    public void setSearchCount(String word, LocalDate date, int count) {
        searchMap.computeIfAbsent(word, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(date, k -> new AtomicInteger())
                .set(count);
    }

    public Map<String, Map<LocalDate, AtomicInteger>> getSearchMap() {
        return searchMap;
    }
}
