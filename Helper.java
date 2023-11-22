import java.nio.charset.StandardCharsets;
import java.text.Normalizer;

class Helper {

    public static String UnicodeToASCII(String input) {
        if (input == null) return "";

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFKD);
        String regex = "[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+";

        try {
            byte[] asciiBytes = normalized.replaceAll(regex, "").getBytes(StandardCharsets.US_ASCII);
            return new String(asciiBytes, StandardCharsets.US_ASCII);
        } catch (Exception e) {
            return "";
        }
    }

    public static int LevenshteinDistance(String str1, String str2) {
        if (str1 == null) str1 = "";
        if (str2 == null) str2 = "";

        int len1 = str1.length();
        int len2 = str2.length();

        int[] dp = new int[len2 + 1];

        for (int j = 0; j <= len2; dp[j] = j++) {}

        for (int i = 1; i <= len1; i++) {
            int prev = i;
            for (int j = 1; j <= len2; j++) {
                int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;
                int temp = dp[j - 1] + cost;
                dp[j - 1] = prev;
                prev = Math.min(Math.min(temp, dp[j] + 1), prev + 1);
            }
            dp[len2] = prev;
        }

        return dp[len2];
    }
}
