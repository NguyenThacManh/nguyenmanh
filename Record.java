public class Record {
    private String word;
    private String meaning;

    public Record() { }

    public Record(String word, String def) {
        this.word = word;
        this.meaning = def;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String def) {
        this.meaning = def;
    }
}