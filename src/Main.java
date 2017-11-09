
public class Main {
		
	public static void printMatching(int[][] matching){
		for (int i=0; i<matching.length; i++){
			System.out.println("The students assigned to the school " + i + " are");
			for (int j=0 ; j<matching[i].length; j++){
				if (matching[i][j] != -1) System.out.println(matching[i][j]);
				//only the occupied seats are printed
			}
			System.out.println("\n");
		}
	}
	public static void testBoston(int n, int m, int[] capacity, int[][] priority, int[][] preference){
		schoolChoiceBoston scp = new schoolChoiceBoston(n, m, capacity, priority, preference);
		int[][] matching = scp.boston();
		printMatching(matching);
	}
	
	public static void testSOSM(int n, int m, int[] capacity, int[][] priority, int[][] preference){
		schoolChoiceSOSM sosm = new schoolChoiceSOSM(n, m, capacity, priority, preference);
		int[][] matching = sosm.galeShapley();
		printMatching(matching);
	}
	
	public static void testTTCM(int n, int m, int[] capacity, int[][] priority, int[][] preference){
		schoolChoiceTTCM ttcp = new schoolChoiceTTCM(n, m, capacity, priority, preference);
		int[][] matching = ttcp.ttcm();
		printMatching(matching);
	}
	
	public static void example1(){
		int n = 8;
		int m = 4;
		int[] capacity = {2, 2, 3, 3};
		int[][] priority = {
				{0, 1, 2, 3, 4, 5, 6, 7},
				{2, 4, 3, 7, 6, 1, 0, 5},
				{4, 2, 0, 6, 1, 7, 5, 3},
				{5, 7, 6, 5, 1, 2, 4, 0}
		};
		int[][] preference = {
				{1, 0, 2, 3},
				{0, 1, 2, 3},
				{2, 1, 0, 3},
				{2, 3, 0, 1},
				{0, 2, 3, 1},
				{3, 0, 1, 2},
				{0, 1, 2, 3},
				{0, 1, 3, 2}
		};
		//testBoston(n,m,capacity,priority,preference);
		//testSOSM(n,m,capacity,priority,preference);
		testTTCM(n,m,capacity,priority,preference);
	}
	
	public static void example2(){
		int n = 3;
		int m = 3;
		int[] capacity = {1, 1, 1};
		int[][] priority = {
				{0, 2, 1},
				{1, 0, 2},
				{1, 0, 2}
		};
		int[][] preference = {
				{1, 0, 2},
				{0, 1, 2},
				{0, 1, 2}
		};
		//testBoston(n,m,capacity,priority,preference);
		//testSOSM(n,m,capacity,priority,preference);	
		testTTCM(n,m,capacity,priority,preference);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		example1();
	}

}
