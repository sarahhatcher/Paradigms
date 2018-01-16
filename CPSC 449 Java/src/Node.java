import java.util.ArrayList;

public class Node {
	boolean open = true;
	boolean isMaster = false;
	
	int nodePenalty = 0;										//Records this nodes penalty (may not be needed)
	int accumulatedPenalty = 0;									//Records total penalty including this node
	int[] currentPath = new int[8];								//Records task assignments up to and including this node
	
	Node parent = null;
	ArrayList<Node> children = new ArrayList<Node>();
	
	int[] remainingMachines = new int[8];
	int[] remainingTasks = new int[8];
	int[] remainingUnassignedTasks = new int[8];
	
	int machine;
	int task;

	Node(int assignedMachine, int assignedTask, int penalty){	//Overload for Master Node
		isMaster = true;										//Set master
		
		machine = assignedMachine;
		task = assignedTask;
		
		int[] options = {0,1,2,3,4,5,6,7};
		remainingMachines = removeElement(options, assignedMachine);
		remainingTasks = removeElement(options, assignedTask);		
		remainingUnassignedTasks = remainingTasks;
		
		nodePenalty = penalty;
		currentPath[assignedMachine] = assignedTask;			//Adds task to task path
	}
	
	
	Node(Node above, int assignedMachine, int assignedTask, int penalty){							//New branch or leaf node
		machine = assignedMachine;
		task = assignedTask;
		
		parent = above;				//Link child to parent
		parent.addChildNode(this);	//Link parent to child
		
		remainingMachines = removeElement(parent.remainingMachines, assignedMachine);	//Remove this machine from available machines
		remainingTasks = removeElement(parent.remainingTasks, assignedTask);			//Remove this task from available machines
		remainingUnassignedTasks = remainingTasks;
		
		//System.out.print("remainingTasks: ");
		//for(int i=0 ; i < remainingTasks.length; i++){System.out.print(remainingTasks[i]);}
		//System.out.println();
		
		System.out.println("new penalty: " + penalty);
		nodePenalty = penalty;
		accumulatedPenalty = parent.accumulatedPenalty + nodePenalty;
		System.out.println("accumulatedPenalty: " + accumulatedPenalty);
		
		currentPath = parent.currentPath;												//Inherit parent task path
		currentPath[assignedMachine] = assignedTask;									//Add this node to task path
	}
	
	//Executed in the parent when a child is created
	public void addChildNode(Node child){
		children.add(child);					//Add child parent relationship
		
		//System.out.println("child creation is the problem");
		//if(parent!=null){System.out.println("Trigrered on parent: " + parent.machine + "    Child: " + this.machine);}
		
		this.remainingUnassignedTasks = removeElement(this.remainingUnassignedTasks, child.task);	//Remove this machine from parent search			
	}
	
	//Removes one element
	public int[] removeElement(int[] oldArray, int toRemove){
		//System.out.print("Array: ");
		//for(int i=0 ; i < oldArray.length; i++){System.out.print(oldArray[i]);}
		//System.out.println("      Removeing: " + toRemove);
				
		boolean elementRemoved = false;
		int[] newArray = new int[oldArray.length-1];

		for(int index=0; index < newArray.length; index++){
			if(oldArray[index] == toRemove){elementRemoved=true;}	//Wait to find the element to be removed
			
			if(!elementRemoved){newArray[index] = oldArray[index];}	//If element has not been removed, map 1 to 1
			else{newArray[index] = oldArray[index+1];}				//If element has removed, shift all other elements back
		}
		
		//System.out.print("New Array: ");
		//for(int i=0 ; i < newArray.length; i++){System.out.print(newArray[i]);}
		//System.out.println();
		
		return(newArray);
	}
}
