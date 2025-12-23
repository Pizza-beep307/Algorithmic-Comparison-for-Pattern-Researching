import java.util.ArrayList;
import java.util.Arrays;

class BooyerMooreAlgo {
	
	long cpt = 0;
	
	void principal() {
		//testBoyerMooreAlgo();
		
	}
	ArrayList<Integer> boyerMooreAlgo ( ArrayList<Character> text, String pattern ) {
		
		
		
		
		
	}
	
	/**
     * Tests the Naive algorithm .
     */ 
	void testBoyerMooreAlgo() {
		
 
        System.out.println("\n--- Test of BoyerMooreAlgo() ---");

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
        

        System.out.println("Test : Limit Case : Pattern as long as the text");
        text = new ArrayList<>(Arrays.asList('T', 'E', 'X', 'T'));
        pattern = "TEXT";
        testCasNaiveAlgo(text, pattern, new ArrayList<>(Arrays.asList(1)));
            
        
        System.out.println("Test : Error Case : Pattern does not exist");
        text = new ArrayList<>(Arrays.asList('T', 'E', 'X', 'T'));
        pattern = null;
        testCasNaiveAlgo(text, pattern, null);
        
        
        System.out.println("Test : Error Case : Pattern is empty");
        text = new ArrayList<>(Arrays.asList('T', 'E', 'X', 'T'));
        pattern = "";
        testCasNaiveAlgo(text, pattern, null);
        
        
        System.out.println("Test : Error Case : Pattern longer than the text");
        text = new ArrayList<>(Arrays.asList('T', 'E', 'X', 'T'));
        pattern = "TEXTURE";
        testCasNaiveAlgo(text, pattern, null);
        
        
        System.out.println("Test : Error Case : Text does not exist");
        text = null;
        pattern = "srchNull";
        testCasNaiveAlgo(text, pattern, null);
        
        
        System.out.println("Test : Error Case : Text is empty");
        text = new ArrayList<>();
        pattern = "srchNull";
        testCasNaiveAlgo(text, pattern, null);  
        
    }
	
		
	void testCasBoyerMooreAlgo(ArrayList<Character> text, String pattern, ArrayList<Integer> expected) {
		
		
	}
	void testBoyerMooreAlgoEfficiency() {
	
	}
		
}

	
