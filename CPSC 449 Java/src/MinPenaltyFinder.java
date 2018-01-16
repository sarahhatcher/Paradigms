import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MinPenaltyFinder {
	static int[] taskAssignments = new int[8];							//Order of task assignment from machine 1-8
	
	public static void main(String[] args) {	
		dataArray inputData = new dataArray();
		Node currentNode = null;
		
		int[] bestAssignment = new int[8];								//Stores the current best machine task assignment
		int lowestPenalty = 1000;												//Stores the lowest penalty found in tree (1000 is test value to see if nothing is found)
		
		
		//String fileName = "//C:/Users/USER/Desktop/CPSC449Input.txt";		//Input file location and name
		//String fileName = "//C:/Users/smithcg/Desktop/CPSC449Input.txt";	//Input file location at school
		String fileName = "/home/uga/smithcg/Desktop/CPSC449Input.txt";
		
		FileParser.ReadFile(fileName, inputData);
		testArray(inputData); 										//Loads file into variables
		
		//NEED CHECK TO CONFIRM THERE IS AT LEAST ONE MACHINE ASSIGNMENT
		
		//Create top level MasterNode
		//Try to use forced assignments first
		int[][] penalty = inputData.machinePenalties;					//Alternate tag for simple calling
		
		if(inputData.forcedPairs.size() != 0){							//If at least one, make it the master
			int firstForcedMach = inputData.forcedPairs.get(0)[0];
			int firstForcedTask = inputData.forcedPairs.get(0)[1];
			
			System.out.println("Master Machine: " + firstForcedMach + "    Task:" + firstForcedTask);
			Node masterNode = new Node(firstForcedMach, firstForcedTask, penalty[firstForcedMach][firstForcedTask]);
			
			currentNode = masterNode;
			currentNode.remainingUnassignedTasks = new int[1];			//Prevent other children from being created
			
			inputData.forcedPairs.remove(0);							//Remove pair to prevent duplication
			
			for(int j=0; j < inputData.forcedPairs.size(); j++){		//If there are more forced, add them, NEED TO CHECK FOR FORBIDDENS
				int[] assignedPair = inputData.forcedPairs.get(j);
				System.out.println("New Machine: " + assignedPair[0] + "    Task:" + assignedPair[1]);
				Node node = new Node(currentNode, assignedPair[0], assignedPair[1], penalty[assignedPair[0]][assignedPair[1]]);
				currentNode = node;										//Dig down to new node
			}
		}
		
		//Else use 0 as default Master   IS NOT GARANTEED TO BE USABLE SO TO BE FIXED
		else{
			Node masterNode = new Node(0, 0, penalty[0][0]);
			currentNode = masterNode;
			currentNode.remainingUnassignedTasks = new int[0];		//Prevent other children from being created
		}
		

		//Create remaining tree
		//Create branches of current machine
		while(!(currentNode.isMaster && !currentNode.open)){			//Until node is master and master has been closed
			while(currentNode.remainingMachines.length != 0 && currentNode.remainingUnassignedTasks.length!=0){	//Check if all possible branches have been created from current node (no machines remain)
				int newMachine = currentNode.remainingMachines[0];		//Set the next machine to assign to as the 0th index
				
				while(currentNode.remainingUnassignedTasks.length!=0){			//For every remaining unassigned task
					int newTask = currentNode.remainingUnassignedTasks[0];
					//System.out.println("Lenght of remaing tasks: " + currentNode.remainingTasks.length);
					//System.out.println("Lenght of remaing machines: " + currentNode.remainingMachines.length);
					System.out.println("New Machine: " + newMachine + "    Task:" + currentNode.remainingUnassignedTasks[0]);
					Node node = new Node(currentNode, newMachine, newTask, penalty[newMachine][newTask]);	//Create a new node which links to this one
					//System.out.println("Machine: " + currentNode.machine + " has child machine: " + currentNode.children.get(0).machine);
				}
				
				//Dig down one layer and set current node to a child node if possible
				boolean skip = false;
				//System.out.println("num of children: " + currentNode.children.size());
				for(int createdNodeIndex = 0 ; createdNodeIndex < currentNode.children.size(); createdNodeIndex++){	//Check through each child node 
					if(currentNode.children.get(createdNodeIndex).open && !skip){  									//If child is open and the first found
						currentNode = currentNode.children.get(createdNodeIndex);									//Set child node as active
						skip = true;																				//Skip all remaining nodes
						System.out.println("Current Node dug down to: " + currentNode.machine + "-" + currentNode.task);
						//System.out.println("Current Node changed to: " + currentNode.machine + "-" + currentNode.task);
						//System.out.println("New node has: " + currentNode.remainingMachines.length + " remaining machines to test");
					}
				}
				
				//Check if best solution
				if(currentNode.remainingMachines.length==0){			//If no machines are left, it must be a leaf
					if(currentNode.accumulatedPenalty < lowestPenalty){
						bestAssignment = currentNode.currentPath;		//Assign best path
						lowestPenalty = currentNode.accumulatedPenalty;	//Assign lowest penalty
					}
				}
			}
		
			
			//Climb one layer
			if(currentNode.parent!=null){								//Guard against null pointer of master node
				//currentNode.open = false;								//Once you hit the bottom
				currentNode.parent.children.remove(currentNode);		//Remove child node from checks
				currentNode = currentNode.parent;						//Close branch and climb one level
				System.out.println("Current Node moved up to: " + currentNode.machine + "-" + currentNode.task);
				//System.out.println("Remaining machines to test: " + currentNode.remainingMachines.length);
			
				if(currentNode.children.size()!=0){						//If there are any remaining children
					currentNode = currentNode.children.get(0);			//Dig down to first child
					System.out.println("Current Node dug down to: " + currentNode.machine + "-" + currentNode.task);
				}
			}
			
			//Close node if children are closed
			if(currentNode.children.size()==0){
				currentNode.open = false;
			}
		}
		
		
		
		
		//Result
		System.out.print("Solution: ");
		for(int i=0 ; i < bestAssignment.length; i++){System.out.print(" " + bestAssignment[i] + " ");}
		System.out.print(" Quality: " + lowestPenalty);
	}
	
	
	//2+2+9+0+2+3+6+2
	
	
	
	
	
	
	
	
	
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
	


