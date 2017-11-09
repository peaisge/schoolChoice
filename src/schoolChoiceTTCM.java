import java.util.LinkedList;

public class schoolChoiceTTCM {
	
	int n; //number of students
	int m; //number of schools
	int[] capacity; //array of length m that contains the capacities of the m schools
	int[][] priority; 
	//double-array of length mxn such that priority[i] is a list of the m students
	//sorted in a decreasing order of priorities of the school i
	int[][] preference; 
	//double-array of length nxm such that preference[j] is a list of the m schools
	//sorted in a decreasing order of preference of the student j
	
	int[] seatsAvailable;
	//array of length m that contains counters which keep track of how many seats are available
	//at each step in a school
	
	boolean[] isFree; 
	//array of length n that shows whether a student has an assignment
	//isFree[j]=false is the student j has an assignment
	
	LinkedList<Integer> pointingList;
	//the list being built at each step
	//In order to differentiate the students from the schools, students are added in pointingList
	//as j=0,...,n-1, while schools are represented by the integers i=n,...,n+m-1
	
	int freeStudent;
	//counter that represents a student not assigned yet which will be the first element
	//of the list pointingList
	
	boolean[] inTheList;
	//array of length n+m that contains booleans that indicate whether a school/a student is
	//in the list being built
	//inTheList[i], i=0,...,n-1, equals true if the student belongs to the list pointingList
	//inTheList[n+j], j=0,...,m-1, equals true if the school j belongs to the list pointingList
	
	int[] schoolToAsk;
	//array of length n such that schoolToAsk[j] is a counter which represents the rank of the school
	//that the student j must point at in the list pointingList
	
	int[] studentToAsk;
	//array of length m such that studentToAsk[i] is a counter which represents the rank of the student
	//that the school i must point at in the list pointingList
	
	int[][] matching; 
	//double-array of length m that representing a matching between the schools and the students
	//For each school i, matching[i] is an array of length capacity[i] that contains the students 
	//who have a seat in the school i
	//matching[i][j]=-1 if the j-th seat of the school i is not occupied
	
	schoolChoiceTTCM(int n, int m, int[] capacity, int[][] priority, int[][] preference){
		this.n = n;
		this.m = m;
		this.capacity = capacity;
		this.priority = priority;
		this.preference = preference;
		
		seatsAvailable = new int[m];
		for (int i=0; i<m; i++) seatsAvailable[i] = capacity[i];
		
		isFree = new boolean[n];
		for (int j=0; j<n; j++) isFree[j] = true; 
		
		pointingList = new LinkedList<Integer>();
		
		freeStudent = 0;
		
		inTheList = new boolean[n+m];
		for (int k=0; k<n+m; k++) inTheList[k] = false;
		
		schoolToAsk = new int[n];
		for (int j=0; j<n; j++) schoolToAsk[j] = 0;
		
		studentToAsk = new int[m];
		for (int i=0; i<m; i++) studentToAsk[i] = 0;
				
		matching = new int[m][];
		for (int i=0; i<m; i++){
			matching[i] = new int[capacity[i]];
			for (int j=0 ; j<capacity[i]; j++) matching[i][j] = -1;
		}
	}
	
	int[][] ttcm(){
		
		while (freeStudent < n){
			if (!isFree[freeStudent]) freeStudent++;
			else{
				int element = freeStudent;
				int l = 0;
				while (!((l%2 == 0) && (inTheList[element]))
						&& !((l%2 == 1) && (inTheList[element + n]))){
					//a cycle is detected as soon as element points at a student/a school that
					//is already in pointingList
					if (l%2 == 0){
						//element is a student if l is even
						int school = preference[element][schoolToAsk[element]];
						if (seatsAvailable[school] > 0){
							//if the school is not full, it will be added to pointingList
							pointingList.add(element);
							inTheList[element] = true;
							element = school;
							l++;							
						}
						else schoolToAsk[element]++;
						//else the student element has to move on to a school of lower preference
					}
					else{
						//element is a school if l is odd
						int student = priority[element][studentToAsk[element]];
						if (isFree[student]){
							//if the student is free, it will be added to the pointing list
							pointingList.add(n + element);
							//in pointingList, schools have indexes that are strictly greater than n
							inTheList[n + element] = true;
							element = student;
							l++;
						}
						else studentToAsk[element]++;
					}
				}
				//the while-loop is built so as not to add twice the first element of the cycle
				//in pointingList
				
				int first = freeStudent;
				int delta = l%2 == 0 ? 0 : n; 
				while (first != element + delta){
					//elements out of the cycle are kept for the next step
					first = pointingList.remove();
					inTheList[first] = false;
					//the array inTheList is re-initialized
				}
				while (!pointingList.isEmpty()){
					first = pointingList.remove();
					if (first < n){
						//if first<n, it is a student and then it is assigned in the school
						//which it points at in pointingList
						int school = preference[first][schoolToAsk[first]];
						//school is the school that first points at in pointingList
						matching[school][capacity[school]-seatsAvailable[school]] = first;
						seatsAvailable[school]--;
						isFree[first] = false;
					}
					inTheList[first] = false;
					//the array inTheList is re-initialized
				}
			}
		}	
		
		return matching;
	}

}
