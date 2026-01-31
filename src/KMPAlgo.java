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
	 * Searches for the 'pattern' in the 'text' using the KMP Algorithm.
	 * @param text - The text (ArrayList<Character>)
	 * @param pattern - The search pattern (String)
	 * @return ArrayList<Integer> - The starting indices of the matches, null if error
	*/
	ArrayList<Integer> kmpAlgo(ArrayList<Character> text, String pattern) {
		cpt = 0;
		ArrayList<Integer> indices = null;

		if (text != null) {
			if (text.size() > 0) {
				if (pattern != null) {
					if (pattern.length() > 0) {
						if (pattern.length() <= text.size()) {
							indices = new ArrayList<>();
							int n = text.size();
							int m = pattern.length();
							// Precompute the Longest Prefix Suffix (LPS) array
							int[] lpsList = longestPrefixSuffix(pattern);
							int i = 0; // Index for text
							int j = 0; // Index for pattern

							while (i < n) {
								cpt++;

								if (pattern.charAt(j) == text.get(i)) {
									// Match: move both pointers forward
									j++;
									i++;
								}

								if (j == m) {
									// Complete match found
									indices.add((i - j) + 1); // Record index (1-based)
									j = lpsList[j - 1]; // Use LPS to determine the next optimal position for j
								} 
								else if (i < n && pattern.charAt(j) != text.get(i)) {
									// Mismatch detected

									if (j != 0) {
										// Key KMP step: Use LPS to skip characters without backtracking i
										j = lpsList[j - 1];
									} else {
										// If j is at start we simply move to the next character in text
										i++;
									}
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
	* Computes the Longest Prefix Suffix (LPS) table for the KMP algorithm.
	* @param pattern - The pattern to analyze (string)
	* @return The computed LPS table (int[])
	*/
	int[] longestPrefixSuffix(String pattern) {
		int[] lpsList = null;

		if (pattern != null) {
			if (pattern.length() > 0) {
				int m = pattern.length();
				int j = 0;
				int i = 1;
				lpsList = new int[m];
				lpsList[0] = 0;

				while (i < m) {
					cpt ++;
					if (pattern.charAt(i) == pattern.charAt(j)) {
						j ++;
						lpsList[i] = j;
						i ++;
					} else {
						if (j != 0) {
							j = lpsList[j - 1];
						} else {
							lpsList[i] = 0;
							i ++;
						}
					}
				}
			} else {
				System.err.println("LongestPrefixSuffix(): Error: pattern is empty");	
			}
		} else {
			System.err.println("LongestPrefixSuffix(): Error: pattern is null");
		}
		
		return lpsList;
	}
	
	/**
	 * Test of longestPrefixSuffix()
	*/
	void testLongestPrefixSuffix() {
		System.out.println("\n=== Test of longestPrefixSuffix() ===");
		
		System.out.println("Test 1: Subject Example 'ABABACA'");
		int[] expectedSubject = {0, 0, 1, 2, 3, 0, 1};
		testCasLongestPrefixSuffix("ABABACA", expectedSubject);

		System.out.println("\nTest 2: Normal Case 'AAAAA'");
		int[] expectedA = {0, 1, 2, 3, 4};
		testCasLongestPrefixSuffix("AAAAA", expectedA);

		System.out.println("\nTest 3: Normal Case 'ABCDE'");
		int[] expectedABC = {0, 0, 0, 0, 0};
		testCasLongestPrefixSuffix("ABCDE", expectedABC);

		System.out.println("\nTest 4: Error Null");
		testCasLongestPrefixSuffix(null, null);

		System.out.println("\nTest 5: Error Empty");
		testCasLongestPrefixSuffix("", null);
	}

	/**
	* Tests a specific case of computePrefixTable()
	* @param pattern - The pattern to analyze
	* @param expected - The expected int array
	*/
	void testCasLongestPrefixSuffix(String pattern, int[] expected) {
		int[] result = longestPrefixSuffix(pattern);

		if (expected != null) {
			System.out.println("result: " + result);	
			if (result != null && Arrays.equals(result, expected)) { // utilisation de Array.equals pour comparer 2 int[] au lieu de faire une boucle 
				System.out.println("Test successful\n");
			} else {
				System.err.println("Test failed for pattern " + pattern);
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
				System.out.println("Test successful for pattern \"" + pattern + "\" in \"" + text + "\n");
			} else {
				System.err.println("Test failed for pattern \"" + pattern + "\"\n");
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
	* Efficiency test of the KMP algorithm()
	*/
	void testKmpAlgoEfficiency() {
		System.out.println("\n=== Efficacity Test of KMP Algorithm ===");

		// Normal Case (Random Text)
		System.out.println("\n=== Normal Case with Random text ===");
		

		ArrayList<Character> text;
		String pattern = "ABCDE";
		int m = pattern.length();
		int n;
		long t1, t2, diffT;
		double cptOverN;

		n = (int) Math.pow(10, 6);

		for (int i = 0; i < 6; i++) {
			text = generateRandomText(n);
			System.out.println("Length of the text n: " + n);
			System.out.println("Length of the pattern m: " + m + "(" + pattern + ")");

			cpt = 0;
			t1 = System.currentTimeMillis();
			kmpAlgo(text, pattern);
			t2 = System.currentTimeMillis();

			diffT = t2 - t1;
			System.out.println("Tps = " + diffT + " ms");

			cptOverN = (double) cpt / (n+m);
			System.out.println("cpt / n = " + cptOverN); 
			System.out.println("-----------------------------------");
			n = n * 2;
		}

		// Repetitive Text (Worst case for Naive, but Linear for KMP)
		System.out.println("\n=== Repetitive text Case (KMP Strength) ===");

		String worstCasePattern = "AAAAB";
		m = worstCasePattern.length();

		n = (int) Math.pow(10, 6);

		for (int i = 0; i < 6; i++) {
			text = generateRepetitiveText(n);
			System.out.println("Length of the text n: " + n);
			System.out.println("Length of the pattern m: " + m + "(" + worstCasePattern + ")");

			cpt = 0;
			t1 = System.currentTimeMillis();
			kmpAlgo(text, worstCasePattern);
			t2 = System.currentTimeMillis();

			diffT = t2 - t1;
			System.out.println("Tps = " + diffT + " ms");

			cptOverN = (double) cpt / (n+m);

			System.out.println("cpt / n = " + cptOverN); 
			System.out.println("-----------------------------------");
			n = n * 2;
		}
	}

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
