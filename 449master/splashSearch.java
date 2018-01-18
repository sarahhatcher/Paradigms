import java.util.ArrayList;
import java.util.Arrays;

public class splashSearch{
  /*set up vars.
  * create the master node
  * set forced assignment nodes
  * enter a do while loop for the entire search(end is master&no children)
  *   enter a loop that generates nodes if active then decends
  *   checks for best solution (or creates first best solution)
  *   climbs up until node with children is found \
  *    (closing nodes that are childless)
  *
  */

  int[][][] searchArray = new int[8][8][2];
  Node currentNode = new Node();

  //Stores the lowest penalty found in tree
  int lowestPenalty = -1;

  //Stores the current best machine task assignment
  ArrayList<Integer> bestAssignment = new ArrayList<Integer>();

	//Records which machines have not been used in this branch
  ArrayList<Integer> remainingMachines = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7));

  //Records which tasks have not been used in this branch
  ArrayList<Integer> remainingTasks = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7));

	//Records task assignments up to and including this node
  ArrayList<Integer> currentPath = new ArrayList<Integer>(Arrays.asList(-1,-1,-1,-1,-1,-1,-1,-1));

  //Records forced assignments
  ArrayList<int[]> forcedAssignments = new ArrayList<int[]>();
  /*constructor
  * pntArray = penalty too near array
  */
  public splashSearch(int[][][] pntArray){
    searchArray = pntArray;

    //Extract forced assignments
    Boolean firstPenFound = false;


    //For each machine
    for(int m=0 ; m <= 7 ; m++){
      //For each task
      int[] tempPair = new int[2];
      for(int j=0 ; j <= 7 ; j++){

        //If entry is valid, and is not the first valid, set value to be added to forcedAssignments
        if(searchArray[m][j][0] != -1){
          if(firstPenFound){
            firstPenFound = false;
            break;
          }
          firstPenFound = true;

          tempPair[0] = m;
          tempPair[1] = j;
        }
      }

      //After break, firstPenFound is true only if exactly one non -1 was found
      if(firstPenFound){
        forcedAssignments.add(tempPair);
      }
      firstPenFound = false;
    }
  }

  /*search bitches
  */
  public void treeConstruct(){
    forceAssignNode();

    do{
      while(remainingMachines.size() != 0){								//Check if all possible branches have been created from current node
        buildBranches();
        pickBestChild();
      }
      //Should always now be in a leaf
      updateBestSoln();
      backTrack();
    }while(currentNode.parent != null && currentNode.children.size() != 0);							//Repeat until node is master again

    //Result
		System.out.print("Solution: ");
		for(int i=0 ; i < bestAssignment.size(); i++){System.out.print(" " + bestAssignment.get(i) + " ");}
		System.out.print(" Quality: " + lowestPenalty);
  }


  public void forceAssignNode(){
    //Forced assignments
    for(int i=0 ; i < forcedAssignments.size() ; i++){
			int[] forcedPair = forcedAssignments.get(i);
      // new node takes parent node, mach, task, pen

      /*This is missing the implementation for TNT and TNP for forced decision making
      * and pen's that we are forced to take.
      */
			Node newNode = new Node(currentNode, forcedPair[0], forcedPair[1], searchArray[forcedPair[0]][forcedPair[1]][0]);

      //Close previous (parent of newNode) node so it won't make new nodes again
			currentNode.active = false;
			currentNode = newNode;									//Make the new node the current node

			remainingMachines.remove(arrayListSearch(remainingMachines, currentNode.machine));				//Remove machine and task from available list and adds to path
			remainingTasks.remove(arrayListSearch(remainingTasks, currentNode.task));
			currentPath.set(currentNode.machine, currentNode.task);
		}
  }


  public void buildBranches(){
    if(currentNode.active){											     //If children have never been created
        int nextMachine = remainingMachines.get(0);					//Set the next machine to assign to (implement algorithm to pick best next machine)

        for(int task=0; task < remainingTasks.size(); task++)		//For each unassigned task
        {
          int nextTask = remainingTasks.get(task);
          Node node = new Node(currentNode, nextMachine, remainingTasks.get(task), searchArray[nextMachine][nextTask][0]);	//Create a new node which links to this node
          //System.out.println("Node machine: " + node.machine + "  Index: " + node.task + "   created");
        }

        currentNode.active = false;									//Close node so it won't make new nodes again
    }
  }


  public void updateBestSoln(){
    if(currentNode.accumulatedPenalty < lowestPenalty || lowestPenalty == -1){
			bestAssignment.clear();
				for(Integer i : currentPath){bestAssignment.add(new Integer(i));}	//Record new best path
				lowestPenalty = currentNode.accumulatedPenalty;						//Remember lowest penalty
			}
    }


  public void pickBestChild(){
    currentNode = currentNode.children.get(0);						//Pick a node to climb down to
    remainingMachines.remove(arrayListSearch(remainingMachines, currentNode.machine));				//Remove machine and task from available list and adds to path
    remainingTasks.remove(arrayListSearch(remainingTasks, currentNode.task));

    currentPath.set(currentNode.machine, currentNode.task);
  }


  public void backTrack(){
    while(currentNode.children.size() == 0 && currentNode.parent != null){	//Until there is a child to climb down to
      currentNode.parent.children.remove(currentNode);	//Remove child node from parents

      remainingMachines.add(currentNode.machine);			//Re-add machine and task to available list and removes from path
      remainingTasks.add(currentNode.task);
      currentPath.set(currentNode.machine, -1);

      currentNode = currentNode.parent;					//Climb up
      //System.out.println("Climbed up to machine: " +currentNode.machine+ "  task: " +currentNode.task);
    }
  }











  //Finds index of an element in an array list
  static int arrayListSearch(ArrayList<Integer> list, int element)
  {
    int index = -1;

    for(int i=0; i<list.size(); i++)
    {
      if(list.get(i) == element)
      {
        index = i;
        break;
      }
    }

    return index;
  }
}
