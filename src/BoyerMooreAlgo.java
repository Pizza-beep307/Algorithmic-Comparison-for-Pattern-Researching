import java.util.ArrayList;
import java.util.Arrays;

class BoyerMooreAlgo {
	
	long cpt = 0;
	
	void principal() {
		testGenerateRandomText();
		testGenerateRepetitiveText();
		testProcessBadChar();
		testProcessGoodSuffix();
		testBoyerMooreAlgo();
		testBoyerMooreAlgoEfficiency();
	}

	/**
	 * Searches for the 'pattern' in the 'text' using the Boyer-Moore Algorithm.
	 * @param text - The text (ArrayList<Character>)
	 * @param pattern - The search pattern (String)
	 * @return ArrayList<Integer> - The starting indices of the matches, null if error
	 */
	ArrayList<Integer> boyerMooreAlgo(ArrayList<Character> text, String pattern) {
		cpt = 0;
		ArrayList<Integer> indices = null;
		
		if (text != null) {
			if (text.size() > 0) {
				if (pattern != null) {
					if (pattern.length() > 0) {
						if (pattern.length() <= text.size()) {
							indices = new ArrayList<Integer>();
							int n = text.size();
							int m = pattern.length();
							
							// Bad Character Rule
							int[] badCharArray = processBadChar(pattern);// Precomputes the last occurrence of each character
							
							// Good Suffix Rule
							int[] goodSuffixArray = processGoodSuffix(pattern);
							
							int shift = 0; // Align pattern at the start of the text
							
							while (shift <= (n - m)) { // While the pattern stays within text bounds
								int j = m - 1; // Start scanning from the rightmost character of the pattern
								
								// Scan from right to left
								while (j >= 0 && pattern.charAt(j) == text.get(shift + j)) { // While characters match (moving right to left)
									cpt++; // Comparison successful
									j--; // Move left
								}
								
								if (j < 0) {
									// Complete match found
									indices.add(shift + 1); // Add the found index (1-based) to the final list
									
									shift += goodSuffixArray[0]; // Shift pattern past the match using the Good Suffix rule
									
								} else {
									// Case 2: Mismatch at index j
									cpt++;
									
									// D1 Processing Bad Character
									char textChar = text.get(shift + j); // The character in text causing the mismatch
									int charCode;

									if (textChar < 256) {
										charCode = textChar;
									} else {
										charCode = 0; // Safety check to prevent ArrayOutOfBoundsException
									};
									
									int d1 = Math.max(1, j - badCharArray[charCode]);
									
									// D2 Processing Good Suffix
									int d2 = goodSuffixArray[j];
									
									shift += Math.max(d1, d2); // Shift by the maximum of the two results (Bad Char vs Good Suffix)
								}
							}
						} else {
							System.err.println("boyerMooreAlgo(): Error: pattern is longer than the text");
						}	
					} else {
						System.err.println("boyerMooreAlgo(): Error: pattern is empty");
					}
				} else {
					System.err.println("boyerMooreAlgo(): Error: pattern is null");
				}
			} else {
				System.err.println("boyerMooreAlgo(): Error: text is empty");
			}
		} else {
			System.err.println("boyerMooreAlgo(): Error: text is null");
		}

		return indices;
	}

	/**
	 * Computes the jump table for the Bad Character rule (Table 1).
	 * Associates each ASCII character (0-255) with its last occurrence position in the pattern.
	 * @param pattern - The pattern for which to build the table
	 * @return An integer array where the index corresponds to the ASCII code and the value to the position
	 */
	int[] processBadChar(String pattern) {
		int[] table = null;
		
		if (pattern != null) {
			if (pattern.length() > 0) {
				table = new int[256];
				
				// Initialization to -1
				for (int i = 0; i < 256; i++) {
					cpt ++;
					table[i] = -1;
				}

				// Filling with the last position
				for (int i = 0; i < pattern.length(); i++) {
					cpt ++;
					char c = pattern.charAt(i);
					if (c < 256) {
						table[c] = i;
					}
				}
			} else {
				System.err.println("processBadChar(): Error: pattern is empty");
			}
		} else {
			System.err.println("processBadChar(): Error: pattern is null");
		}

		return table;
	}
	
	/**
	 * Test of processBadChar()
	 */
	void testProcessBadChar() {
		System.out.println("\n--- Test of processBadChar() ---");
		
		System.out.println("Test: Normal Case: pattern 'ABC'");
		
		int[] expectedABC = new int[256];
		for (int i = 0; i < 256; i++) {
			expectedABC[i] = -1;
		}
		expectedABC['A'] = 0;
		expectedABC['B'] = 1;
		expectedABC['C'] = 2;
		
		testCasProcessBadChar("ABC", expectedABC);


		System.out.println("Test: Normal Case: Repetitive pattern 'ABA'");
		
		int[] expectedABA = new int[256];
		for (int i = 0; i < 256; i++) {
			expectedABA[i] = -1;
		}
		expectedABA['A'] = 2; // Last position counts
		expectedABA['B'] = 1;
		
		testCasProcessBadChar("ABA", expectedABA);
		
		
		System.out.println("Test: Error Case: Pattern Null");
		testCasProcessBadChar(null, null);

		System.out.println("Test: Error Case: Pattern Empty");
		testCasProcessBadChar("", null);
	}
	
	/**
	 * Tests a specific case of processBadChar()
	 * @param pattern - The pattern
	 * @param expected - The expected table
	 */
	void testCasProcessBadChar(String pattern, int[] expected) {
		int[] result = processBadChar(pattern);
		
		if (expected != null) {
			if (result != null && Arrays.equals(result, expected)) {
				System.out.println("Test successful\n");
			} else {
				System.err.println("Test failed\n");
			}
		} else {
			if (result == null) {
				System.out.println("Test successful\n");
			} else {
				System.err.println("Test failed\n");
			}
		}
	}
	
	/**
	 * Computes the jump array for the Good Suffix rule (Table 2).
	 * This method avoids using 'break' by using boolean flags in loop conditions.
	 * @param pattern - The pattern to analyze
	 * @return An integer array containing the shift amounts
	 */
	int[] processGoodSuffix(String pattern) {
		int[] table = null;
		
		if (pattern != null) {
			if (pattern.length() > 0) {
				int m = pattern.length();
				table = new int[m];
				
				for (int j = m - 1; j >= 0; j--) {
					cpt++;
					int suffixLen = m - 1 - j; // Length of the suffix after mismatch at j
					boolean found = false;
					int shiftAmount = m; // Default shift: whole pattern length
					
					// Case 1: Mismatch at the last character (no suffix to match)
					if (suffixLen == 0) { 
						shiftAmount = 1;
					} else {
						String suffix = pattern.substring(j + 1);
						char charBeforeSuffix = pattern.charAt(j); // The character that caused the mismatch within the pattern
						
						// Case 2: Search for another occurrence of the suffix in the pattern
						// It must not be preceded by the same character 'charBeforeSuffix'
						int k = m - 2;
						while (k >= suffixLen - 1 && !found) {
							cpt++;
							int start = k - suffixLen + 1;
							String sub = pattern.substring(start, k + 1);
							
							if (sub.equals(suffix)) {
								char charBeforeSub = '\0';

								if (start - 1 >= 0) {
									charBeforeSub = pattern.charAt(start - 1);
								}
								// "Strong" Good Suffix Rule: check preceding character
								if (start == 0 || charBeforeSub != charBeforeSuffix) {
									shiftAmount = (m - 1) - k;
									found = true;
								}
							}
							k--;
						}
						// Case 3: Search for a suffix that matches a prefix of the pattern
						if (!found) {
							int p = 1;
							while (p < m && !found) {
				
								int len = m - p;
								if (len < suffixLen) {
									// Check if the end of pattern matches the start of pattern
									boolean prefixMatch = true;
									int i = 0;
									while (i < len && prefixMatch) {
										cpt++;
										if (pattern.charAt(i) != pattern.charAt(m - len + i)) {
											prefixMatch = false;
										}
										i = i + 1;
									}
									
									if (prefixMatch) {
										shiftAmount = p;
										found = true;
									}
								}
								p++;
							}
						}
					}
					table[j] = shiftAmount;
				}
			} else {
				System.err.println("computeGoodSuffixTable(): Error: pattern is empty");
			}
		} else {
			System.err.println("computeGoodSuffixTable(): Error: pattern is null");
		}
		
		return table;
	}

	/**
	 * Test of processGoodSuffix()
	 */
	void testProcessGoodSuffix() {
		System.out.println("\n--- Test of processGoodSuffix() ---");
		
		System.out.println("Test: Simple pattern 'ABC'");
		int[] expABC = {3, 3, 1};
		testCasProcessGoodSuffix("ABC", expABC);
		
		System.out.println("Test: Error Null");
		testCasProcessGoodSuffix(null, null);
		
		System.out.println("Test: Error Empty");
		testCasProcessGoodSuffix("", null);
	}

	/**
	 * Tests a specific case of processGoodSuffix()
	 * @param pattern - The pattern
	 * @param expected - The expected table
	 */
	void testCasProcessGoodSuffix(String pattern, int[] expected) {
		int[] result = processGoodSuffix(pattern);
		
		if (expected != null) {
			if (result != null && Arrays.equals(result, expected)) {
				System.out.println("Test successful\n");
			} else {
				System.err.println("Test failed for pattern " + pattern + "\n");
			}
		} else {
			if (result == null) {
				System.out.println("Test successful\n");
			} else {
				System.err.println("Test failed\n");
			}
		}
	}

	/**
	 * Test of boyerMooreAlgo()
	 */
	void testBoyerMooreAlgo() {
		System.out.println("\n--- Test of boyerMooreAlgo() ---");

		ArrayList<Character> text;
		String pattern;

		System.out.println("Test : Normal Case : single match");
		text = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd', 'e'));
		pattern = "c";
		testCasBoyerMooreAlgo(text, pattern, new ArrayList<>(Arrays.asList(3)));

		System.out.println("Test : Normal Case : Multiple matches");
		text = new ArrayList<>(Arrays.asList('a', 'b', 'a', 'b', 'a', 'b'));
		pattern = "ab";
		testCasBoyerMooreAlgo(text, pattern, new ArrayList<>(Arrays.asList(1, 3, 5)));

		System.out.println("Test : Normal Case : No match");
		text = new ArrayList<>(Arrays.asList('x', 'y', 'z'));
		pattern = "a";
		testCasBoyerMooreAlgo(text, pattern, new ArrayList<>());

		System.out.println("Test : Limit Case : Pattern as long as the text");
		text = new ArrayList<>(Arrays.asList('T', 'E', 'X', 'T'));
		pattern = "TEXT";
		testCasBoyerMooreAlgo(text, pattern, new ArrayList<>(Arrays.asList(1)));

		System.out.println("Test : Error Case : Pattern does not exist");
		text = new ArrayList<>(Arrays.asList('T', 'E', 'X', 'T'));
		pattern = null;
		testCasBoyerMooreAlgo(text, pattern, null);

		System.out.println("Test : Error Case : Pattern is empty");
		text = new ArrayList<>(Arrays.asList('T', 'E', 'X', 'T'));
		pattern = "";
		testCasBoyerMooreAlgo(text, pattern, null);

		System.out.println("Test : Error Case : Pattern longer than the text");
		text = new ArrayList<>(Arrays.asList('T', 'E', 'X', 'T'));
		pattern = "TEXTURE";
		testCasBoyerMooreAlgo(text, pattern, null);

		System.out.println("Test : Error Case : Text does not exist");
		text = null;
		pattern = "srchNull";
		testCasBoyerMooreAlgo(text, pattern, null);

		System.out.println("Test : Error Case : Text is empty");
		text = new ArrayList<>();
		pattern = "srchNull";
		testCasBoyerMooreAlgo(text, pattern, null);
	}
	
	/**
	 * Tests a specific case of boyerMooreAlgo()
	 * @param text - The text
	 * @param pattern - The pattern
	 * @param expected - The list of expected indices
	 */
	void testCasBoyerMooreAlgo(ArrayList<Character> text, String pattern, ArrayList<Integer> expected) {
		ArrayList<Integer> resObtenu = boyerMooreAlgo(text, pattern);
		if (expected != null) {
			boolean succes = resObtenu.equals(expected);
			
			if (succes) {
				System.out.println(resObtenu);
				System.out.println("Test successful for pattern \"" + pattern + "\" in " + text.toString() + "\n");
			} else {
				System.err.println("Test failed for pattern \"" + pattern + "\" in " + text.toString() + "\n");
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
	 * Efficiency Test of the Boyer-Moore Algorithm()
	 */
	void testBoyerMooreAlgoEfficiency() {
		System.out.println("\n=== Efficacity Test of Boyer-Moore Algorithm ===");
		
		System.out.println("\n=== Normal Case with Random text ===");
		
		ArrayList<Character> text;
		String pattern = "ABCDE";
		int n, m;
		long t1, t2, diffT;
		double cptOverN;
		
		n = (int) Math.pow(10, 6);
		m = pattern.length();
		for (int i = 0; i < 6; i++) {
			text = generateRandomText(n);
			System.out.println("Length of the text n: " + n);
			System.out.println("Length of the pattern m: " + m + "(" + pattern + ")");
			
			cpt = 0;
			t1 = System.currentTimeMillis();
			boyerMooreAlgo(text, pattern);
			t2 = System.currentTimeMillis();
			
			diffT = t2 - t1;
			System.out.println("Tps = " + diffT + " ms");
			
			cptOverN = (double)cpt / (n / m);
			System.out.println("cpt / (n / m)= " + cptOverN);
			System.out.println("-----------------------------------");
			n = n * 2;
		}
		
		System.out.println("\n=== Worst Case with Repetitive text ===");
		
		String worstCasePattern = "BAAAA";
		m = worstCasePattern.length();
		
		n = (int) Math.pow(10, 6);
		
		for (int i = 0; i < 6; i++) {
			text = generateRepetitiveText(n);
			System.out.println("Length of the text n: " + n);
			System.out.println("Length of the pattern m: " + m + "(" + worstCasePattern + ")");
			
			cpt = 0;
			t1 = System.currentTimeMillis();
			boyerMooreAlgo(text, worstCasePattern);
			t2 = System.currentTimeMillis();
			
			diffT = t2 - t1;
			System.out.println("Tps = " + diffT + " ms");
			
			cptOverN = (double)cpt / (n + m);
			System.out.println("cpt / (n + m) = " + cptOverN);
			System.out.println("-----------------------------------");
			n = n * 2; 	
		}
	}

	/**
	 * Generates a random text composed of uppercase letters (A-Z).
	 * @param size - The size of the text to generate
	 * @return The text as an ArrayList of Characters
	 */
	ArrayList<Character> generateRandomText(int size) {
		ArrayList<Character> text = null;
		if (size >= 0) {
			text = new ArrayList<>(size);
			String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			for (int i = 0; i < size; i++) {
				int index = (int)(Math.random() * alphabet.length());
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
	 * Tests a specific case of generateRandomText()
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
	 * Used to simulate the worst-case scenario.
	 * @param size - The size of the text to generate
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
	 * Tests a specific case of generateRepetitiveText()
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
