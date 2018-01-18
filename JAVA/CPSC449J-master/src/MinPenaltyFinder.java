import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MinPenaltyFinder {
	static int[] taskAssignments = new int[8];							//Order of task assignment from machine 1-8
	
	public static void main(String[] args) {	
		dataArray inputData = new dataArray();
		Node currentNode = null;
		
		ArrayList<Integer> bestAssignment = new ArrayList<Integer>();	//Stores the current best machine task assignment
		int lowestPenalty = 1000;										//Stores the lowest penalty found in tree (1000 is test value to see if nothing is found)
		
		ArrayList<Integer> remainingMachines = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7));	//Records which machines have not been used in this branch
		ArrayList<Integer> remainingTasks = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7));						//Records which tasks have not been used in this branch
		
		ArrayList<Integer> currentPath = new ArrayList<Integer>(Arrays.asList(-1,-1,-1,-1,-1,-1,-1,-1));		//Records task assignments up to and including this node
		
		String fileName = "//C:/Users/USER/Desktop/CPSC449Input.txt";	//Input file location and name
		FileParser.ReadFile(fileName, inputData);
		testArray(inputData); 											//Loads file into variables
				
		//Create top level MasterNode (machine and task are -1)	
		currentNode = new Node();
		
		//Assign forced pairs
		for(int i=0; i<inputData.forcedPairs.size(); i++)
		{
			int[] assignedPair = inputData.forcedPairs.get(i);
			Node node = new Node(currentNode, assignedPair[0], assignedPair[1], inputData.machinePenalties[assignedPair[0]][assignedPair[1]]);	//Create node for forced pair
					
			currentNode.open = false;									//Close node so it won't make new nodes again
			currentNode = node;											//Move down to new node
			
			remainingMachines.remove(arrayListSearch(remainingMachines, currentNode.machine));				//Remove machine and task from available list and adds to path
			remainingTasks.remove(arrayListSearch(remainingTasks, currentNode.task));
			currentPath.set(currentNode.machine, currentNode.task);
		}
		
		
		//Create and search remaining tree
		do																
		{
			//Create branches of current machine and/or climbs down
			while(remainingMachines.size() != 0)								//Check if all possible branches have been created from current node
			{
				if(currentNode.open)											//If children have never been created
				{
					int nextMachine = remainingMachines.get(0);					//Set the next machine to assign to (implement algorithm to pick best next machine)
										
					for(int task=0; task < remainingTasks.size(); task++)		//For each unassigned task
					{
						Node node = new Node(currentNode, nextMachine, remainingTasks.get(task), inputData.machinePenalties[nextMachine][remainingTasks.get(task)]);	//Create a new node which links to this node
						System.out.println("Node machine: " + node.machine + "  Index: " + node.task + "   created");
					}
				
					currentNode.open = false;									//Close node so it won't make new nodes again
				}

				currentNode = currentNode.children.get(0);						//Pick a node to climb down to
				remainingMachines.remove(arrayListSearch(remainingMachines, currentNode.machine));				//Remove machine and task from available list and adds to path
				remainingTasks.remove(arrayListSearch(remainingTasks, currentNode.task));
								
				currentPath.set(currentNode.machine, currentNode.task);	
			}
			
			//Should always now be in a leaf
			
			//Check if best solution
			if(currentNode.accumulatedPenalty < lowestPenalty){
				bestAssignment.clear();
				for(Integer i : currentPath){bestAssignment.add(new Integer(i));}	//Record new best path
				lowestPenalty = currentNode.accumulatedPenalty;						//Remember lowest penalty
			}

			System.out.println("Children Size: " +currentNode.children.size()+ "  Parent status: " +(currentNode.parent != null));
			
			//Start climbing up
			while(currentNode.children.size() == 0 && currentNode.parent != null)	//Until there is a child to climb down to
			{	
				currentNode.parent.children.remove(currentNode);	//Remove child node from parents
				
				remainingMachines.add(currentNode.machine);			//Re-add machine and task to available list and removes from path
				remainingTasks.add(currentNode.task);
				currentPath.set(currentNode.machine, -1);
				
				currentNode = currentNode.parent;					//Climb up 
				System.out.println("Climbed up to machine: " +currentNode.machine+ "  task: " +currentNode.task);
			}

			
		}while(currentNode.parent != null && currentNode.children.size() != 0);							//Repeat until node is master again
		
		
		
		//Result
		System.out.print("Solution: ");
		for(int i=0 ; i < bestAssignment.size(); i++){System.out.print(" " + bestAssignment.get(i) + " ");}
		System.out.print(" Quality: " + lowestPenalty);
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
	
	
	
	
	
	
	
	
	
	
	static void testArray(dataArray inputData){
		//For testing
		System.out.println("Condensed Content:");
		for(int j=0 ; j <= 7; j++){
			for(int i=0 ; i <= 7; i++){
				if(0 <= inputData.machinePenalties[j][i] && inputData.machinePenalties[j][i] <= 9){
					System.out.print(" " + inputData.machinePenalties[j][i]+":");
				}
				else{
					System.out.print(inputData.machinePenalties[j][i]+":");
				}
			}
			System.out.println("");
		}
	}
}
	


