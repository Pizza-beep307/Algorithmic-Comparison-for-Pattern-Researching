import java.util.ArrayList;
import java.util.Arrays;

class RabinKarpAlgo {

    long cpt = 0;

    void principal() {
        testGenerateRandomText();
        testGenerateRepetitiveText();
        testRabinKarpAlgo();
        testRabinKarpAlgoEfficiency();
    }

    /**
     * Searches for all occurrences of a specific pattern within a text using the Rabin-Karp algorithm.
     * This algorithm uses a rolling hash function to compare the pattern's "fingerprint" with the 
     * fingerprint of the current text window.
     * Use Formula (4): hach(t+1) = { [ hach(t) - ( t[k] * b^(m-1) ) ] * b } + t[k+m]
     * @param text - The text (list of characters) in which to perform the search
     * @param pattern - The pattern to search for (String)
     * @return A list containing the starting indices of the matches found (1-based index), or null in case of error
     */
    ArrayList<Integer> rabinKarpAlgo(ArrayList<Character> text, String pattern) {
        cpt = 0;
        final int BASE = 101; 
        ArrayList<Integer> indices = null;

        if (text != null) {
            if (text.size() > 0) {
                if (pattern != null) {
                    if (pattern.length() > 0) {
                        if (pattern.length() <= text.size()) {
                            indices = new ArrayList<Integer>(); // 2 op
                            
                            int n = text.size();
                            int m = pattern.length();
                            
                            // Use long to prevent overflow as much as possible (though standard RK uses modulo)
                            long patternHash = 0;
                            long textHash = 0;
                            long highOrderPower = 1; // Represents b^(m-1)

                            // 1. Pre-compute the high order power: b^(m-1)
                            for (int i = 0; i < m - 1; i++) {
                                highOrderPower = highOrderPower * BASE;
                            }

                            // 2. Calculate the initial hash for the pattern and the first window of text
                            // Using the polynomial formula: c0*b^(m-1) + ... + cm*b^0
                            for (int i = 0; i < m; i++) {
                                patternHash = (patternHash * BASE) + pattern.charAt(i);
                                textHash = (textHash * BASE) + text.get(i);
                            }

                            // 3. Slide the window over the text
                            for (int i = 0; i <= n - m; i++) {
                                
                                // Step A: Compare Hash Values
                                // This is a fast integer comparison (1 op)
                                if (patternHash == textHash) {
                                    // Step B: If hashes match, verify characters (Handling collisions)
                                    boolean match = true;
                                    int j = 0;
                                    while (j < m && match) {
                                        cpt++; // Count character comparison
                                        if (text.get(i + j) != pattern.charAt(j)) {
                                            match = false;
                                        }
                                        j++;
                                    }
                                    
                                    if (match) {
                                        indices.add(i + 1); // 1-based index
                                    }
                                } else {
                                    cpt++; 
                                }

                                // Step C: Calculate the hash for the NEXT window (Rolling Hash)
                                // Only if we are not at the very end
                                if (i < n - m) {
                                    // Remove leading character: text[i]
                                    // Add trailing character: text[i + m]
                                    // Formula (4): newHash = (oldHash - val_out * b^(m-1)) * b + val_in
                                    
                                    char charOut = text.get(i);
                                    char charIn = text.get(i + m);
                                    
                                    textHash = (textHash - (charOut * highOrderPower)) * BASE + charIn;
                                }
                            }

                        } else {
                            System.err.println("rabinKarpAlgo(): Error: pattern is longer than the text");
                        }
                    } else {
                        System.err.println("rabinKarpAlgo(): Error: pattern is empty");
                    }
                } else {
                    System.err.println("rabinKarpAlgo(): Error: pattern is null");
                }
            } else {
                System.err.println("rabinKarpAlgo(): Error: text is empty");
            }
        } else {
            System.err.println("rabinKarpAlgo(): Error: text is null");
        }

        return indices;
    }

    /**
     * Test of rabinKarpAlgo()
     */
    void testRabinKarpAlgo() {
        System.out.println("\n--- Test of rabinKarpAlgo() ---");

        ArrayList<Character> text;
        String pattern;

        System.out.println("Test : Normal Case : single match");
        text = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd', 'e'));
        pattern = "c";
        testCasRabinKarpAlgo(text, pattern, new ArrayList<>(Arrays.asList(3)));

        System.out.println("Test : Normal Case : Multiple matches");
        text = new ArrayList<>(Arrays.asList('a', 'b', 'a', 'b', 'a', 'b'));
        pattern = "ab";
        testCasRabinKarpAlgo(text, pattern, new ArrayList<>(Arrays.asList(1, 3, 5)));

        System.out.println("Test : Normal Case : No match");
        text = new ArrayList<>(Arrays.asList('x', 'y', 'z'));
        pattern = "a";
        testCasRabinKarpAlgo(text, pattern, new ArrayList<>());

        System.out.println("Test : Limit Case : Pattern as long as the text");
        text = new ArrayList<>(Arrays.asList('T', 'E', 'X', 'T'));
        pattern = "TEXT";
        testCasRabinKarpAlgo(text, pattern, new ArrayList<>(Arrays.asList(1)));
        
        System.out.println("Test : Subject Example 'abr... bra'");
        // Text: abracadabra, Pattern: aba -> indices 1, 8
        // abracadabra
        // 12345678901
        text = new ArrayList<>(Arrays.asList('a','b','r','a','c','a','d','a','b','r','a'));
        pattern = "aba";
        testCasRabinKarpAlgo(text, pattern, null); // Just to run it, manually check collision logic if needed
        // Actually let's test a real match
        pattern = "abra";
        testCasRabinKarpAlgo(text, pattern, new ArrayList<>(Arrays.asList(1, 8)));


        System.out.println("Test : Error Case : Pattern longer than text");
        text = new ArrayList<>(Arrays.asList('A'));
        pattern = "ABC";
        testCasRabinKarpAlgo(text, pattern, null);

        System.out.println("Test : Error Case : Pattern null");
        testCasRabinKarpAlgo(text, null, null);
    }

    /**
     * Tests a specific case of rabinKarpAlgo()
     * @param text - The text to search in
     * @param pattern - The pattern to search for
     * @param expected - The list of expected starting indices
     */
    void testCasRabinKarpAlgo(ArrayList<Character> text, String pattern, ArrayList<Integer> expected) {
        ArrayList<Integer> resObtenu = rabinKarpAlgo(text, pattern);
        if (expected != null) {
            boolean succes = resObtenu.equals(expected);

            if (succes) {
                System.out.println(resObtenu);
                System.out.println("Test successful for pattern \"" + pattern + "\"\n");
            } else {
                System.err.println("Test failed for pattern \"" + pattern + "\"\n");
                System.err.println("Expected: " + expected + " Got: " + resObtenu + "\n");
            }

        } else {
            if (resObtenu == expected) {
                System.out.println("Test successful\n");
            } else {
                System.err.println("Test failed\n");
            }
        }
    }

    /**
     * Efficiency test of the Rabin-Karp algorithm()
     */
    void testRabinKarpAlgoEfficiency() {
        System.out.println("\n=== Efficacity Test of Rabin-Karp Algorithm ===");

        // Normal Case (Random Text)
        System.out.println("\n=== Normal Case with Random text ===");

        ArrayList<Character> text;
        String pattern = "ABCDE";
        int n;
        long t1, t2, diffT;
        double cptOverN;

        n = (int) Math.pow(10, 6);

        for (int i = 0; i < 6; i++) {
            text = generateRandomText(n);
            System.out.println("Length of the text n: " + n);

            cpt = 0;
            t1 = System.currentTimeMillis();
            rabinKarpAlgo(text, pattern);
            t2 = System.currentTimeMillis();

            diffT = t2 - t1;
            System.out.println("Tps = " + diffT + " ms");

            cptOverN = (double) cpt / n;
            System.out.println("cpt / n = " + cptOverN); 
            System.out.println("-----------------------------------");
            n = n * 2;
        }

        // Repetitive Text (Worst Case Analysis)
        // With pattern "AAAAB" and text "AAAAA...", 
        // Hash("AAAAA") is different from Hash("AAAAB") thanks to the positional power.
        // So Rabin-Karp should remain fast (O(n)) because it won't trigger the character check often.
        System.out.println("\n=== Repetitive text Case ===");

        String worstCasePattern = "AAAAB";
        int m = worstCasePattern.length();

        n = (int) Math.pow(10, 6);

        for (int i = 0; i < 6; i++) {
            text = generateRepetitiveText(n);
            System.out.println("Length of the text n: " + n);

            cpt = 0;
            t1 = System.currentTimeMillis();
            rabinKarpAlgo(text, worstCasePattern);
            t2 = System.currentTimeMillis();

            diffT = t2 - t1;
            System.out.println("Tps = " + diffT + " ms");

            cptOverN = (double) cpt / n; 
            System.out.println("cpt / n = " + cptOverN); 
            System.out.println("-----------------------------------");
            n = n * 2;
        }
    }

    // --- GENERATORS ---

    /**
     * Generates a random text composed of uppercase letters (A-Z).
     * @param size - The size of the text to generate. Assumed to be a positive integer (>= 0).
     * @return The text as an ArrayList of Characters
     */
    ArrayList<Character> generateRandomText(int size) {
        ArrayList<Character> text = null;
        if (size >= 0) {
            text = new ArrayList<>(size);
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            for (int i = 0; i < size; i++) {
                int index = (int) (Math.random() * alphabet.length());
                text.add(alphabet.charAt(index));
            }
        } else {
            System.err.println("generateRandomText(): Error: size negative");
        }
        return text;
    }

    /**
     * Test of generateRandomText()
     */
    void testGenerateRandomText() {
        System.out.println("\n--- Test of generateRandomText() ---");

        int size;
        
        System.out.println("Test : Normal Case : Size positive");
        size = 6;
        testCasGenerateRandomText(size);

        System.out.println("Test : Limit Case : Size equals zero");
        size = 0;
        testCasGenerateRandomText(size);

        System.out.println("Test : Limit Case : Huge Size (will not be displayed)");
        size = 10000;
        testCasGenerateRandomText(size);

        System.out.println("Test : Error Case : Negativ Size");
        size = -5;
        testCasGenerateRandomText(size);
    }
    
    /**
     * Tests a specific case of generateRandomText().
     * @param size - The requested size
     */
    void testCasGenerateRandomText(int size) {
        ArrayList<Character> res = generateRandomText(size);
        boolean success = false;
        
        if (res == null) {
            if (size < 0) {
                success = true;
            }
        } else {
            if (res.size() == size) {
                success = true;
                if (res.size() < 15) { 
                    System.out.println(res);
                }
            }
        }
        
        if (success) {
            System.out.println("Test successful\n");
        } else {
            System.err.println("Test failed\n");
        }
    }
            
    /**
     * Generates a repetitive text composed only of the character 'A'.
     * @param size - The size of the text to generate. Assumed to be a positive integer (>= 0).
     * @return The text as an ArrayList of Characters
     */
    ArrayList<Character> generateRepetitiveText(int size) {
        ArrayList<Character> text = null;
        if (size >= 0) {
            text = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                text.add('A');
            }
        } else {
            System.err.println("generateRepetitiveText(): Error: size is negativ");
        }
        return text;
    }

    /**
     * Test of generateRepetitiveText()
     */
    void testGenerateRepetitiveText() {
        System.out.println("\n--- Test of generateRepetitiveText() ---");

        int size;

        System.out.println("Test : Normal Case : Size positive");
        size = 6;
        testCasGenerateRepetitiveText(size);

        System.out.println("Test : Limit Case : Size equals zero");
        size = 0;
        testCasGenerateRepetitiveText(size);
 
        System.out.println("Test : Limit Case : Huge Size (will not be displayed)");
        size = 10000;
        testCasGenerateRepetitiveText(size);

        System.out.println("Test : Error Case : Negativ Size");
        size = -5;
        testCasGenerateRepetitiveText(size);
    }
    
    /**
     * Tests a specific case of generateRepetitiveText().
     * @param size - The requested size
     */
    void testCasGenerateRepetitiveText(int size) {
        ArrayList<Character> res = generateRepetitiveText(size);
        boolean success = false;
        
        if (res == null) {
            if (size < 0) {
                success = true;
            } 
        } else {
            if (res.size() == size) {
                success = true;
                int i = 0;
                while (i < size && success) { 
                    if (res.get(i) != 'A') {
                        success = false;
                    }
                    i ++;
                }
                if (res.size() < 15) { 
                    System.out.println(res);
                }
            }
        }
        
        if (success) {
            System.out.println("Test successful\n");
        } else {
            System.err.println("Test failed\n");
        }
    }
}
