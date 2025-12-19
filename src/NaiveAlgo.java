import java.util.ArrayList;
import java.util.Arrays;
class NaiveAlgo {
	
	long cpt = 0;
	
	void principal() {
		testNaiveAlgo();
		
	}
	/**
     * Searches for the 'pattern' in the 'text' using the Naive Algorithm.
     * @param text - The text (ArrayList<Character>)
     * @param pattern - The search pattern (String)
     * @return ArrayList<Integer> - The starting indices of the matches.
     */
	ArrayList<Integer> naiveAlgo ( ArrayList<Character> text, String pattern ) {
		cpt = 0; 
		ArrayList<Integer> indices = new ArrayList<Integer>(); //2 op
		int range = text.size() - pattern.length() + 1;
		// 1 op		//1 op 		// 1 op				//1op
		boolean findPattern;//1 op
		int j; // 1 op
		
		for (int i = 0; i < range; i++) {
			//2 op      // 1 op		// 2 op 
			findPattern = true; // 1op
			j = 0; // 1 op
			while (j < pattern.length() && findPattern) {
				   // j op					j op
				cpt ++;
				if (text.get(i + j) != pattern.charAt(j)) {
					//
					findPattern= false; // 1 op
				}
				j ++; // 1 op
			}
			
			if (findPattern) { // 1 op
				indices.add(i + 1); // 1 op //respect de la convention
			}
		}
		
		return indices;
	}
	/**
     * Tests the Naive algorithm .
     */ 
    void testNaiveAlgo() {
        System.out.println("\n--- Test of naiveAlgo() ---");

		ArrayList<Character> text;
		String pattern;

        System.out.println("Test : Normal Case : single match");
        text = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd', 'e'));
        pattern = "c";
        testCasNaiveAlgo(text, pattern, new ArrayList<>(Arrays.asList(3))); 
        

        System.out.println("Test : Normal Case : Multiple matches");
        text = new ArrayList<>(Arrays.asList('a', 'b', 'a', 'b', 'a', 'b'));
        pattern = "ab";
        testCasNaiveAlgo(text, pattern, new ArrayList<>(Arrays.asList(1, 3, 5))); 
        
        
        System.out.println("Test : Normal Case : No match");
        text = new ArrayList<>(Arrays.asList('x', 'y', 'z'));
        pattern = "a";
        testCasNaiveAlgo(text, pattern, new ArrayList<>()); 
        

        System.out.println("Test : Limit Case : Pattern at the very end");
        text = new ArrayList<>(Arrays.asList('h', 'e', 'l', 'l', 'o'));
        pattern = "lo";
        testCasNaiveAlgo(text, pattern, new ArrayList<>(Arrays.asList(4)));
        

        System.out.println("Test : Limit Case : Pattern at the beginning");
        text = new ArrayList<>(Arrays.asList('P', 'A', 'T', 'T', 'E', 'R', 'N'));
        pattern = "PA";
        testCasNaiveAlgo(text, pattern, new ArrayList<>(Arrays.asList(1)));
        

        System.out.println("Test : Limit Case : Pattern as long as the text");
        text = new ArrayList<>(Arrays.asList('T', 'E', 'X', 'T'));
        pattern = "TEXT";
        testCasNaiveAlgo(text, pattern, new ArrayList<>(Arrays.asList(1)));
    }
	
    /**
     * Test un cas particulier de naiveAlgo() en comparant le résultat obtenu avec un résultat attendu. 
     * Tests a specific case of naiveAlgo() by comparing the obtained result with an expected result. (Corresponds to testCasAlgo)
     * @param text - The text (ArrayList<Character>)
     * @param pattern - The pattern (String)
     * @param expected - The list of expected indices, now strictly an ArrayList<Integer>	
     */
    void testCasNaiveAlgo(ArrayList<Character> text, String pattern, ArrayList<Integer> expected) { 
        
        ArrayList<Integer> resObtenu = naiveAlgo(text, pattern);
        boolean succes = resObtenu.equals(expected);
        
        if (succes) {
			System.out.println(resObtenu);
            System.out.println("Test successful for pattern \"" + pattern + "\" in " + text.toString());
        } else {
       
            System.err.println("Test failed for pattern \"" + pattern + "\" in " + text.toString());
        }
        System.out.println("");
    }
	
	void testNaiveAlgoEfficiency() {
		
		
		
	}
}
