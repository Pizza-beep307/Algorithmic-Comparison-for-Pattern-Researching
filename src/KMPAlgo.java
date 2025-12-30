import java.util.ArrayList;
import java.util.Arrays;

class KMPAlgo {

    long cpt = 0;

    void principal() {
        testGenerateRandomText();
        testGenerateRepetitiveText();
        testKmpAlgo();
        testKmpAlgoEfficiency();
    }

    /**
     * Searches for all occurrences of a specific pattern within a text using the Knuth-Morris-Pratt (KMP) algorithm.
     * This algorithm relies on a pre-computed prefix table (Pi table) to skip unnecessary comparisons in the text
     * when a mismatch occurs. We assume that the 'text' and 'pattern' parameters are verified (not null/empty) 
     * inside the method via nested checks.
     * @param text - The text (list of characters) in which to perform the search
     * @param pattern - The pattern to search for (String)
     * @return A list containing the starting indices of the matches found (1-based index), or null in case of error
     */
    ArrayList<Integer> kmpAlgo(ArrayList<Character> text, String pattern) {
        cpt = 0;
        ArrayList<Integer> indices = null;

        if (text != null) {
            if (text.size() > 0) {
                if (pattern != null) {
                    if (pattern.length() > 0) {
                        if (pattern.length() <= text.size()) {
                            indices = new ArrayList<Integer>(); // 2 op
                            
                            int n = text.size();
                            int m = pattern.length();

                            int[] prefixTable = computePrefixTable(pattern); // m op

                            // Step 2: Search Phase
                            int matchCount = 0; // Number of characters matched so far (q)

                            // Loop through the text
                            for (int i = 0; i < n; i++) {
                                // While characters don't match and we have matched some before,
                                // we fallback using the table to avoid re-scanning text
                                while (matchCount > 0 && pattern.charAt(matchCount) != text.get(i)) {
                                    cpt++; // Count comparison
                                    matchCount = prefixTable[matchCount - 1];
                                }

                                cpt++; // Count the comparison for the next check
                                if (pattern.charAt(matchCount) == text.get(i)) {
                                    matchCount++; // Next character matches
                                }

                                if (matchCount == m) {
                                    // Complete match found
                                    // Formula: Current index 'i' - length 'm' + 1 (to get start) + 1 (for 1-based index convention)
                                    indices.add(i - m + 2);

                                    // Prepare for next match (overlap is allowed in KMP)
                                    matchCount = prefixTable[matchCount - 1];
                                }
                            }

                        } else {
                            System.err.println("kmpAlgo(): Error: pattern is longer than the text");
                        }
                    } else {
                        System.err.println("kmpAlgo(): Error: pattern is empty");
                    }
                } else {
                    System.err.println("kmpAlgo(): Error: pattern is null");
                }
            } else {
                System.err.println("kmpAlgo(): Error: text is empty");
            }
        } else {
            System.err.println("kmpAlgo(): Error: text is null");
        }

        return indices;
    }

    /**
     * Computes the Prefix Function Table (also called Pi Table or fp).
     * For each index 'q' in the pattern, it calculates the length of the longest proper prefix of pattern[0..q]
     * that is also a suffix of pattern[0..q]. This allows the algorithm to shift more than 1 step.
     * @param pattern - The pattern to analyze
     * @return The prefix table as an int array
     */
    int[] computePrefixTable(String pattern) {
        int[] pi = null;

        if (pattern != null) {
            if (pattern.length() > 0) {
                int m = pattern.length();
                pi = new int[m];
                
                // Initialization: The border of a single char string is always 0
                pi[0] = 0;
                
                int k = 0; // Length of the previous longest prefix

                // Loop from the second character to the end
                for (int q = 1; q < m; q++) {
                    // While we cannot expand the current prefix, fallback to the previous best prefix
                    while (k > 0 && pattern.charAt(k) != pattern.charAt(q)) {
                        k = pi[k - 1];
                    }
                    
                    // If characters match, we can extend the prefix length by 1
                    if (pattern.charAt(k) == pattern.charAt(q)) {
                        k++;
                    }
                    
                    pi[q] = k;
                }
            } else {
                System.err.println("computePrefixTable(): Error: pattern is empty");
            }
        } else {
            System.err.println("computePrefixTable(): Error: pattern is null");
        }
        
        return pi;
    }

    /**
     * Test of computePrefixTable()
     */
    void testComputePrefixTable() {
        System.out.println("\n--- Test of computePrefixTable() ---");

        System.out.println("Test: Subject Example 'ABABACA'");
        // Note: In standard KMP implementation (0-based), the table is [0, 0, 1, 2, 3, 0, 1]
        int[] expectedSubject = {0, 0, 1, 2, 3, 0, 1};
        testCasComputePrefixTable("ABABACA", expectedSubject);

        System.out.println("Test: Normal Case 'AAAAA'");
        // Should be 0, 1, 2, 3, 4 (each step increases prefix length)
        int[] expectedA = {0, 1, 2, 3, 4};
        testCasComputePrefixTable("AAAAA", expectedA);
        
        System.out.println("Test: Normal Case 'ABCDE'");
        // Should be all 0 (no prefix is a suffix)
        int[] expectedABC = {0, 0, 0, 0, 0};
        testCasComputePrefixTable("ABCDE", expectedABC);

        System.out.println("Test: Error Null");
        testCasComputePrefixTable(null, null);

        System.out.println("Test: Error Empty");
        testCasComputePrefixTable("", null);
    }

    /**
     * Tests a specific case of computePrefixTable()
     * @param pattern - The pattern to analyze
     * @param expected - The expected int array
     */
    void testCasComputePrefixTable(String pattern, int[] expected) {
        int[] result = computePrefixTable(pattern);

        if (expected != null) {
            if (result != null && Arrays.equals(result, expected)) {
                System.out.println("Test successful\n");
            } else {
                System.err.println("Test failed for pattern " + pattern);
                System.err.println("Expected: " + Arrays.toString(expected));
                System.err.println("Got:      " + Arrays.toString(result) + "\n");
            }
        } else {
            if (result == null) {
                System.out.println("Test successful (null)\n");
            } else {
                System.err.println("Test failed (expected null)\n");
            }
        }
    }

    /**
     * Test of kmpAlgo()
     */
    void testKmpAlgo() {
        System.out.println("\n--- Test of kmpAlgo() ---");

        ArrayList<Character> text;
        String pattern;

        System.out.println("Test : Normal Case : single match");
        text = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd', 'e'));
        pattern = "c";
        testCasKmpAlgo(text, pattern, new ArrayList<>(Arrays.asList(3)));

        System.out.println("Test : Normal Case : Multiple matches (Overlapping)");
        // Text: ABABA... Pattern: ABA -> Matches at 1, 3, 5
        text = new ArrayList<>(Arrays.asList('A', 'B', 'A', 'B', 'A', 'B', 'A'));
        pattern = "ABA";
        testCasKmpAlgo(text, pattern, new ArrayList<>(Arrays.asList(1, 3, 5)));

        System.out.println("Test : Normal Case : No match");
        text = new ArrayList<>(Arrays.asList('x', 'y', 'z'));
        pattern = "a";
        testCasKmpAlgo(text, pattern, new ArrayList<>());

        System.out.println("Test : Limit Case : Pattern as long as the text");
        text = new ArrayList<>(Arrays.asList('T', 'E', 'X', 'T'));
        pattern = "TEXT";
        testCasKmpAlgo(text, pattern, new ArrayList<>(Arrays.asList(1)));
        
        System.out.println("Test : Error Case : Pattern longer than text");
        text = new ArrayList<>(Arrays.asList('A'));
        pattern = "ABC";
        testCasKmpAlgo(text, pattern, null);

        System.out.println("Test : Error Case : Pattern null");
        testCasKmpAlgo(text, null, null);
    }

    /**
     * Tests a specific case of kmpAlgo()
     * @param text - The text to search in
     * @param pattern - The pattern to search for
     * @param expected - The list of expected starting indices
     */
    void testCasKmpAlgo(ArrayList<Character> text, String pattern, ArrayList<Integer> expected) {
        ArrayList<Integer> resObtenu = kmpAlgo(text, pattern);
        if (expected != null) {
            boolean succes = resObtenu.equals(expected);

            if (succes) {
                System.out.println(resObtenu);
                System.out.println("Test successful for pattern \"" + pattern + "\"\n");
            } else {
                System.err.println("Test failed for pattern \"" + pattern + "\"\n");
            }

        } else {
            if (resObtenu == expected) {
                System.out.println("Test successful (null)\n");
            } else {
                System.err.println("Test failed (expected null)\n");
            }
        }
    }

    /**
     * Efficiency test of the KMP algorithm()
     */
    void testKmpAlgoEfficiency() {
        System.out.println("\n=== Efficacity Test of KMP Algorithm ===");

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
            kmpAlgo(text, pattern);
            t2 = System.currentTimeMillis();

            diffT = t2 - t1;
            System.out.println("Tps = " + diffT + " ms");

            cptOverN = (double) cpt / n;
            System.out.println("cpt / n = " + cptOverN); // Should be very stable around 1.0
            System.out.println("-----------------------------------");
            n = n * 2;
        }

        // Repetitive Text (Worst case for Naive, but Linear for KMP)
        // Pattern "AAAAB" in "AAAAAAAA..."
        // KMP avoids re-scanning 'A's thanks to the prefix table.
        System.out.println("\n=== Repetitive text Case (KMP Strength) ===");

        String worstCasePattern = "AAAAB";
        int m = worstCasePattern.length();

        n = (int) Math.pow(10, 6);

        for (int i = 0; i < 6; i++) {
            text = generateRepetitiveText(n);
            System.out.println("Length of the text n: " + n);

            cpt = 0;
            t1 = System.currentTimeMillis();
            kmpAlgo(text, worstCasePattern);
            t2 = System.currentTimeMillis();

            diffT = t2 - t1;
            System.out.println("Tps = " + diffT + " ms");

            cptOverN = (double) cpt / n; 
            // Unlike Naive algorithm where this ratio increases, for KMP it stays constant.
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
     * Executes various test cases including normal, limit, and error cases.
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
     * Verifies if the output is not null (for valid sizes) and has the correct size.
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
                if (res.size() < 15) { // the goal is to display reasonable text size
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
     * This method is used to simulate the "worst case" scenario for string searching algorithms.
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
     * Executes various test cases including normal, limit, and error cases.
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
                while (i < size && success) { // verifying the content
                    if (res.get(i) != 'A') {
                        success = false;
                    }
                    i ++;
                }
                if (res.size() < 15) { // the goal is to display reasonable text size
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
