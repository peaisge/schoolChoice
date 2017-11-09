import java.util.LinkedList;

public class schoolChoiceBoston {
	
	int n; //number of students
	int m; //number of schools
	int[] capacity; //array of length m that contains the capacities of the m schools
	int[][] priority; 
	//double-array of length mxn such that priority[i] is a list of the m students
	//sorted in a decreasing order of priorities of the school i
	int[][] preference; 
	//double-array of length nxm such that preference[j] is a list of the m schools
	//sorted in a decreasing order of preference of the student j
	
	boolean[] isFree; 
	//array of length n that shows whether a student has an assignment
	//isFree[j]=false is the student j has an assignment
	
	boolean existsFree(){
		for (boolean bool: isFree){
			if (bool) return true;
		}
		return false;
	}
	//tells whether there are still available students
	
	int[] seatsAssigned; 
	//array of length m such that seatsAssigned[i] is the number of seats of the school i 
	//that have already been assigned to a student

	boolean isFull(int i){
		return (seatsAssigned[i] >= capacity[i]);
	}
	//tells whether the school i has seats left

	LinkedList<Integer> availableSchools;
	//queue that contains all the schools that are not full;
	//used in order not to go through all the schools - included the full ones - at each step

	
	int[][] matching; 
	//double-array of length m that representing a matching between the schools and the students
	//For each school i, matching[i] is an array of length capacity[i] that contains the students 
	//who have a seat in the school i
	//matching[i][j]=-1 if the j-th seat of the school i is not occupied	
	
	schoolChoiceBoston(int n, int m, int[] capacity, int[][] priority, int[][] preference){
		this.n = n;
		this.m = m;
		this.capacity = capacity;
		this.priority = priority;
		this.preference = preference;
		
		isFree = new boolean[n];
		for (int j=0; j<n; j++) isFree[j] = true; 
		
		seatsAssigned = new int[m];
		for (int i=0; i<m; i++) seatsAssigned[i] = 0; 
		
		availableSchools = new LinkedList<Integer>();
		for (int i=0; i<m; i++) availableSchools.add(i);
		
		matching = new int[m][];
		for (int i=0; i<m; i++){
			matching[i] = new int[capacity[i]];
			for (int j=0 ; j<capacity[i]; j++) matching[i][j] = -1;
		}
	}
	
	//executes the k-th round of the Boston mechanism
	void bostonk(int k){
		LinkedList<Integer> stillAvailables = new LinkedList<Integer>();
		while (!availableSchools.isEmpty()){
			int school = availableSchools.remove();
			int j = 0;
			while (!isFull(school) && j<n){
				int student = priority[school][j];
				if (isFree[student] && preference[student][k] == school){
						matching[school][seatsAssigned[school]] = student;
						isFree[student] = false;
						seatsAssigned[school]++;
					}
				j++;				
			}
			if (!isFull(school)) stillAvailables.add(school);
		}
		availableSchools = stillAvailables;
	}
	
	int[][] boston(){
		int k = 0;
		while (!availableSchools.isEmpty() && existsFree() && k<m){
			bostonk(k);
			k++;
		}
		return matching;
	}
	
}
