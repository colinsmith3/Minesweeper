public class cweaselDriver {

    /* The constructor for the game takes arguments for the total grid size ( 4x4 = 16 ) and the number of traps. It has error checks to
    * prevent the user from entering a number of traps less than 5 or greater than 1/3 of the grid size and entering a
    * grid size that is not a perfect square. The only major problem I've noticed is that I was unable to fix the issue case where the
    * first tile clicked is a trap. At first, I was under the impression that completely resetting the game would be okay, then in class
    * on friday, I learned that it was not. Because of how my implementation was designed, I found it too difficult to solve without
    * changing some major components of my game, and I felt like I didn't have enough time to do this. So although it still works fine,
    * the way I've done this is incorrect. */



    public static void main(String[] args) {

        //Call game constructor with any grid size and number of traps (has necessary error checks)

        cweasel cw = new cweasel(64, 14);

    }

}
