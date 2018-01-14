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
    if(table1.noSolution == 1){
      System.out.print("No valid solution possible!\n");
      System.exit(0);
    }
    table1.print(); System.out.print("\n"); //two lines on one for ease of test

    //create a data structure that can handel TNT and TNP, indexed by task
    //preform a depth first search, create bound after getting 8th penalty
    //return no solution if none is found
    //create an array of 8 tasks with current solution
    //create an array of 8 tasks with current itteration
    //procede down each of the eight tasks in machine one while keeping a 8x8
    //   matrix as the state function to avoid doing the loop more then once
    //if the alg reaches machine eight and with lower pen, make it current
    //itterate through the 8 starting tasks until fully explored
    //return the current solution and it's penalty
  }

  public class solMatrix{
    public solMatrix(){
      for(int m = 0; m<8;m++){
        for(int t = 0; t<8; t++){

        }
      }
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
    public int[][][] penArray = new int[8][8][4];
    /* arrays contain [m][t][p,PA flag, TNT flag, TNP flag]
    *  p = penality
    *  PA = Forced Partial Assignemnt (it will flag the entire row and column)
    *  TNT = too near task flag. Check an outside array for the destination
    *        and set its flag to -1 for that search instance only
    *  TNP = too near penality flag. Check an outside array for the destination
    *        and set its penality to current + TNP for that search instant only
    *  could also add a TNP penality as a fifth flag
    *  And for the love of god, do not confuse TNT and TNP
    */
    public int noSolution = 0;
    // constructor for testing only. Should find a way to actualy use input
    public table(){
      for(int m = 0; m < 8;m++){
        for(int t=0; t<8; t++){
          penArray[m][t][0]= 1;
        }
      }
    }

    //method for applying forbidden machine as -1 on the array

    public void applyForbidden(int m, int t){
      this.penArray[m-1][t-1][0] = -1;
    }

    //method for forced partial assignement.
    //turns forced partial assignments into -2.
    public void applyPartial(int m, int t){
      //
      if (this.penArray[m-1][t-1][0] == -1){
        this.noSolution = 1; //flags no solution for later
      }else if(this.penArray[m-1][t-1][1] >0){
        //if this mach or task has a partial assignment anywhere on it throw.
        System.out.println("partial assignment error");
        System.exit(0);
      } else {
        /* need to work out the logic for this.
        */
        this.penArray[m-1][t-1][1] = 2;
        for(int i = 0; i<8;i++){
          if(i!=m-1){
            this.penArray[i][t-1][1] = 1;
          }
          if(i!=t-1){
            this.penArray[m-1][i][1] = 1;
          }

        }
      }
    }

    // display the array for error checking. Do not use in final assignment
    public void print(){
      for(int m = 0;m<8;m++){
        System.out.print("\n");
        for(int t = 0; t<8;t++){
          System.out.print(Arrays.toString(this.penArray[m][t]));
        }
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
