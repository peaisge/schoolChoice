import java.util.LinkedList;

public class schoolChoiceSOSM {
	
	int n; //number of students
	int m; //number of schools
	int[] capacity; //array of length m that contains the capacities of the m schools
	int[][] priority; 
	//double-array of length mxn such that priority[i] is a list of the m students
	//sorted in a decreasing order of priorities of the school i
	int[][] preference; 
	//double-array of length nxm such that preference[j] is a list of the m schools
	//sorted in a decreasing order of preference of the student j
	
	int[] rankToAsk;
	//array of length n such that rankToAsk[j] is a counter which represents the rank of the school
	//that the student j must ask at the start of the next loop (the "next choice" of the algorithm)
	
	boolean acceptance;
	//boolean equal to true if all the proposals of the students have been accepted
	
	LinkedList<Integer> freeStudents;
	//queue that contains all the students who are not assigned yet
	
	int[] seatsAssigned; 
	//array of length m such that seatsAssigned[i] is the number of seats of the school i 
	//that have already been assigned to a student
	
	int[][] matching; 
	//double-array of length m that representing a matching between the schools and the students
	//For each school i, matching[i] is an array of length capacity[i] that contains the students 
	//who have a seat in the school i
	//matching[i][j]=-1 if the j-th seat of the school i is not occupied
	
	schoolChoiceSOSM(int n, int m, int[] capacity, int[][] priority, int[][] preference){
		this.n = n;
		this.m = m;
		this.capacity = capacity;
		this.priority = priority;
		this.preference = preference;
		
		rankToAsk = new int[n];
		for (int j=0; j<n; j++) rankToAsk[j] = 0;
				
		acceptance = false;
		
		freeStudents = new LinkedList<Integer>();
		for (int j=0; j<n; j++) freeStudents.add(j);
		
		seatsAssigned = new int[m];
		for (int i=0; i<m; i++) seatsAssigned[i] = 0;
				
		matching = new int[m][];
		for (int i=0; i<m; i++){
			matching[i] = new int[capacity[i]];
			for (int j=0 ; j<capacity[i]; j++) matching[i][j] = -1;
		}
	}
	
	int[][] galeShapleyNaive(){
		
		while (!acceptance){
			acceptance = true;
			for (int i=0; i<m; i++){
				int seats = 0;
				for (int j=0; j<n; j++){
					int student = priority[i][j];
					int school = preference[student][rankToAsk[student]];
					if (school == i){
						if (seats < capacity[i]){
							matching[i][seats] = student;
							seats++;
						}
						else {
							rankToAsk[student]++;
							acceptance = false;
						}
					}
				}
			}
		}
		
		return matching;
		
		//peut-être pourrait-on plutôt raisonner sur les students (file)
		// --> cf. galeShapley
	}
	
	int[][] galeShapley(){
		//closer to the way a human who would run the algorithm on a sheet of paper
		
		int[][] rankStudent = new int[m][n];
		for (int i=0; i<m; i++){
			for (int j=0; j<n; j++) rankStudent[i][priority[i][j]] = j;
		}
		//rankStudent is the inverse of the array priority
		//rankStudent[i][j] is the rank of priority of the student j for the school i
		
		while (!freeStudents.isEmpty()){
			int student = freeStudents.remove();
			int school = preference[student][rankToAsk[student]];
			if (seatsAssigned[school] < capacity[school]){
				//if the schools has seats left, any proposer is accepted
				matching[school][seatsAssigned[school]] = student;
				seatsAssigned[school]++;
			}
			else {
				//if the school is full, either student does not have a rank good enough
				//to enter school, ...
				//... or there is a student studentRejected of lower priority worstRank, who
				//leaves the school and whose place student takes
				int worstRank = rankStudent[school][student];
				int studentRejected = student;
				for (int j=0; j<capacity[school]; j++){
					int rank = rankStudent[school][matching[school][j]];
					if (rank > worstRank){
						int var = matching[school][j];
						matching[school][j] = studentRejected;
						worstRank = rank;
						studentRejected = var;
					}
				}
				rankToAsk[studentRejected]++;
				freeStudents.add(studentRejected);
				//studentRejected is added to the queue freeStudents and will ask a school of lower
				//preference at the next loop
			}
			
		}
		return matching;
	}

}
