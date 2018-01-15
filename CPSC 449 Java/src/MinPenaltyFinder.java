import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MinPenaltyFinder {
	static int[] taskAssignments = new int[8];							//Order of task assignment from machine 1-8
	
	public static void main(String[] args) {	
		dataArray inputData = new dataArray();
		Node currentNode = null;
		
		String fileName = "/Users/USER/Desktop/CPSC449Input.txt";		//Input file location and name
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
			inputData.forcedPairs.remove(0);							//Remove pair to prevent duplication
			
			for(int j=0; j < inputData.forcedPairs.size(); j++){		//If there are more forced, add them, NEED TO CHECK FOR FORBIDDENS
				int[] assignedPair = inputData.forcedPairs.get(j);
				System.out.println("New Machine: " + assignedPair[0] + "    Task:" + assignedPair[1]);
				Node node = new Node(currentNode, assignedPair[0], assignedPair[1], penalty[assignedPair[0]][assignedPair[1]]);
				currentNode = node;
			}
		}
		
		//Else use 0 as default Master   IS NOT GARANTEED TO BE USABLE SO TO BE FIXED
		else{
			Node masterNode = new Node(0, 0, penalty[0][0]);
			currentNode = masterNode;
		}
		

		//Create remaining tree
		//Create branches of current machine
		while(!(currentNode.isMaster && !currentNode.open)){			//Until node is master and master has been closed
			while(currentNode.remainingMachines.length != 0){			//Check if all possible branches have been created from current node
				int newMachine = currentNode.remainingMachines[0];		//Set the next machine to assign to as the 0th index
				for(int newTaskIndex=0; newTaskIndex < currentNode.remainingTasks.length ;newTaskIndex++){	//For ever remaining unassigned task
					//System.out.println("Lenght of remaing tasks: " + currentNode.remainingTasks.length);
					//System.out.println("Lenght of remaing machines: " + currentNode.remainingMachines.length);
					System.out.println("New Machine: " + newMachine + "    Task:" + currentNode.remainingTasks[newTaskIndex]);
					Node node = new Node(currentNode, newMachine, currentNode.remainingTasks[newTaskIndex], penalty[newMachine][newTaskIndex]);	//Create a new node which links to this one
					//System.out.println("Machine: " + currentNode.machine + " has child machine: " + currentNode.children.get(0).machine);
				}
				
				//Dig down one layer and set current node to a child node if possible
				boolean skip = false;
				//System.out.println("num of children: " + currentNode.children.size());
				for(int createdNodeIndex = 0 ; createdNodeIndex < currentNode.children.size(); createdNodeIndex++){	//Check through each child node 
					if(currentNode.children.get(createdNodeIndex).open && !skip){  									//If child is open and the first found
						currentNode = currentNode.children.get(createdNodeIndex);									//Set child node as active
						skip = true;																				//Skip all remaining nodes
						System.out.println("Current Node changed to: " + currentNode.machine + "-" + currentNode.task);
						System.out.println("New node has: " + currentNode.remainingMachines.length + " remaining machines to test");
					}
				}
			}
			currentNode.open = false;								//Once you hit the bottom
			currentNode = currentNode.parent;						//Close branch and climb one level
			System.out.println("Current Node moved up to: " + currentNode.machine + "-" + currentNode.task);
			System.out.println("Remaining machines: " + currentNode.remainingMachines[0]);
		}
		
		
		
		
		//Result
		System.out.print("Solution" + " Quality:");
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
	


