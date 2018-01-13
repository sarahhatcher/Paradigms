import java.util.Arrays;
class a1{

  public static void main(String[] args){
    //create a penality array with hard constraints forbidden mach. ,
    //and forced assignment

    table table1 = new table();
    table1.testBasic();
    //table1.testDiagFM();
    //table1.testFM_PA();
    //table1.testDupPA();
    //table1.testSameMach();
    //table1.testSameTask();
    table1.print();

    //create a tree
    //preform a depth first search
    //itterate through the tree until fully explored
    //return the assignment with the lowest pen.
  }

  //the nodes of the binary tree
  public class node {
    public node children[];
    public int mach;
    public int task;
    public int flag;
    public int pen;

    public void node(int m,int t,int f,int p){
      this.mach = m;
      this.task = t;
      this.flag = f;
      this.pen = p;
    }

  }


/*
* table with penalties.
* Can handel "partial assignment error" errors.
*
* Does not deal with "invalid machine/task" errors.
* Does not deal with "machine penalty error" errors.
*/
  public static class table{
    public int[][] penArray = new int[8][8];
    // constructor for testing only. Should find a way to actualy use input
    public table(){
      for(int i = 0; i < 8;i++){
        Arrays.fill(penArray[i], 1);
      }
    }

    //method for applying forbidden machine as -1 on the array

    public void applyForbidden(int m, int t){
      this.penArray[m-1][t-1] = -1;
    }

    //method for forced partial assignement.
    //turns forced partial assignments into -2.
    public void applyPartial(int m, int t){
      //
      if (this.penArray[m-1][t-1] == -1){
        //flag "no valid solution upon hard constrain error checking"
      }else if(this.penArray[m-1][t-1] <0){
        System.out.println("partial assignment error");
        System.exit(0);
      } else {
        /* need to work out the logic for this. Currently new
        */
        this.penArray[m-1][t-1] = -2;
        for(int i = 0; i<8;i++){
          if(i!=m-1){
            this.penArray[i][t-1] = -3;
          }
          if(i!=t-1){
            this.penArray[m-1][i] = -3;
          }

        }
      }
    }

    // display the array for error checking. Do not use in final assignment
    public void print(){
      for(int i = 0;i<8;i++){
        System.out.println(Arrays.toString(this.penArray[i]));
      }
    }
    /* Standard test case for some partial assignments and diag forbidden mach
    */
    public void testBasic(){
      for(int i=1;i<9;i++){
        this.applyForbidden(i,i);
      }
      this.applyPartial(2,3);
      this.applyPartial(5,7);
    }

    /* tests the table by assigning forbidden states
    */
    public void testDiagFM(){
      for(int i=1;i<9;i++){
      this.applyForbidden(i,i);
      }
    }
    /* should force apply partial to return a flag
    *  for "No valid solution possible!"
    *  FLAG NOT IMPLEMENTED YET
    */
    public void testFM_PA(){
      for(int i=1;i<9;i++){
        this.applyForbidden(i,i);
      }
      this.applyPartial(1,1);
    }
    /* should apply a program exiting Partial Assignment error
    *  partial assignment error test same task same mach input
    */
    public void testDupPA(){
      this.applyPartial(1,1);
      this.applyPartial(1,1);
    }
    /* should apply a program exiting Partial Assignment error
    *  partial assignment error test same mach diffirent task input
    */
    public void testSameMach(){
      this.applyPartial(1,1);
      this.applyPartial(1,2);
    }

    /* should apply a program exiting Partial Assignment error
    *  partial assignment error test diffirent mach same task input
    */
    public void testSameTask(){
      this.applyPartial(1,5);
      this.applyPartial(2,5);
    }
  }

}
