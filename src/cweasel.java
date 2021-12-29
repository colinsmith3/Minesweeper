import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//LOOK AT DRIVER COMMENTS

public class cweasel extends JFrame {

    ////All Grid Sizes
    final int SMALL_GRID_SIZE = 16; // 4 x 4 grid size
    final int MEDIUM_GRID_SIZE = 64; // 8 x 8 grid size
    final int LARGE_GRID_SIZE = 225; // 15 x 15 grid size

    ////All numbers of traps for each difficulty
    final int NUM_TRAPS_BEGINNER = 5; // number of traps for beginner difficulty
    final int NUM_TRAPS_INTERMEDIATE = 14; // number of traps for intermediate difficulty
    final int NUM_TRAPS_EXPERT = 60; // number of traps for expert difficulty

    Container c = getContentPane(); //container for content pane

    boolean gameInProgress = false; //tracks if the game is currently in progress

    JMenuBar menuBar; //holds menu bar

    //Components on JMenuBar
    JMenuItem start; //holds start button
    JMenuItem quit; //holds quit button
    JMenu settingsMenu; //holds settings menu
    JMenu helpMenu; //holds help menu

    //Submenus
    JMenu Difficulty;

    //Preset difficulty levels for settings JMenu
    JMenuItem Beginner;
    JMenuItem Intermediate;
    JMenuItem Expert;

    //Help JTextArea
    JTextArea help;

    JPanel topPanel; //holds Panel to show timer and current number of traps
    JLabel timerLabel; //displays timer in topPanel
    JLabel numTrapsLabel; //displays number of traps in topPanel

    Grid grid; //holds grid JPanel

    Timer gameTimer; //holds game timer and checks gameState on each refresh

    //Time variables:
    long startTime;
    long elapsedTime;
    long elapsedSecs;
    long secondsDisplay;
    int elapsedMins;

    //Handlers
    settingsHandler sHandler;
    startAndQuitHandler sqHandler;

    public cweasel(int gridSize, int numOfTraps){ //cweasel constructor, needs gridSize and numOfTraps

        double trapCheck = gridSize * 0.33333;
        double gridCheck = Math.sqrt(gridSize);

        if (numOfTraps < 5 || numOfTraps > trapCheck){ //trap amount error check
            System.out.println("Invalid number of traps. Try again...");
            System.exit(0);
        }
        if (gridCheck % 1 != 0 || gridSize < 16 || gridSize > 225){ //gridSize error check
            System.out.println("Invalid number grid size. Try again...");
            System.exit(0);
        }

        setTopPanel(numOfTraps); //set up top panel
        grid = new Grid(gridSize, numOfTraps); //construct grid

        setContentPane(c); //set up container
        setThisFrame(); //set up this frame
    }

    public void setContentPane(Container x){ //sets up content pane
        x.add(topPanel, BorderLayout.PAGE_START); //add topPanel to content pane
        x.add(grid, BorderLayout.CENTER); //add grid to content pane
    }

    public void setStartAndQuitMenu(JMenuItem s, JMenuItem q){ //set up start and quit JMenus
        s.setText("Start");
        q.setText("Quit");

        sqHandler = new startAndQuitHandler();
        s.addActionListener(sqHandler);
        q.addActionListener(sqHandler);
    }

    public void setSettingsMenu(JMenu s){ //set up settings JMenu
        Difficulty = new JMenu();

        Beginner = new JMenuItem();
        Intermediate = new JMenuItem();
        Expert = new JMenuItem();

        s.setText("Settings");
        Difficulty.setText("Select Difficulty");
        Beginner.setText("Beginner");
        Intermediate.setText("Intermediate");
        Expert.setText("Expert");

        settingsMenu.add(Difficulty);
        Difficulty.add(Beginner);
        Difficulty.add(Intermediate);
        Difficulty.add(Expert);

        sHandler = new settingsHandler();
        s.addActionListener(sHandler);
    }

    public void setHelpMenu(JMenu h){
        h.setText("Help");

        help = new JTextArea();

        help.setPreferredSize(new Dimension(450,125));
        help.setText(" Welcome to The Yellowstone Caldera Weasel Game! \n Press start to start the timer and begin your race through the Caldera \n Press Quit to reset the game \n Use the settings menu to select a different difficulty \n -Beginner: 4x4 grid with 5 traps \n -Intermediate: 8x8 grid with 14 traps \n -Expert: 15x15 grid with 60 traps");
        help.setEditable(false);

        h.add(help);
    }

    public void setMenuBar(){ //set up menuBar
        menuBar = new JMenuBar();

        start = new JMenuItem();
        quit = new JMenuItem();
        settingsMenu = new JMenu();
        helpMenu = new JMenu();

        //Call Menu set functions:
        setStartAndQuitMenu(start, quit);
        setSettingsMenu(settingsMenu);
        setHelpMenu(helpMenu);

        //Add menus to menuBar:
        menuBar.add(start);
        menuBar.add(quit);
        menuBar.add(settingsMenu);
        menuBar.add(helpMenu);

        menuBar.setAlignmentX(0);
    }

    public void setTimerLabel(){ //set up timer label
        timerLabel = new JLabel();
        timerLabel.setText("       Timer:  ");
        timerLabel.setAlignmentX(menuBar.getAlignmentX() + 30);
    }

    public void setNumTrapsLabel(int numberOfTraps){ //set up number of traps label
        numTrapsLabel = new JLabel();
        numTrapsLabel.setText("       Number of Traps:  "+numberOfTraps);
    }

    public void setTopPanel(int numOfTraps){ //set up top panel
        setMenuBar();
        setTimerLabel();
        setNumTrapsLabel(numOfTraps);

        topPanel = new JPanel();

        //Add menubar and labels to topPanel
        topPanel.add(menuBar);
        topPanel.add(timerLabel);
        topPanel.add(numTrapsLabel);
    }

    ///gameInProgress methods
    public void setGameInProgress(boolean g){
        gameInProgress = g;
    }

    public boolean getGameInProgress(){
        return gameInProgress;
    }

    public void enableGridTiles(Grid g){
        g.enableTileArray();
    }

    public void setThisFrame(){
        super.setTitle("The Yellowstone Caldera Weasel Game"); //set title of JFrame
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1000, 1000)); //set preferred size of frame
        this.pack();
        this.setVisible(true); //set visible
    }

    public void GameWon(){ //method for displaying that the game has been won ( called from gameTimer )
        //set new labels
        timerLabel.setText("         Congrats Weasel! You won!    ");
        numTrapsLabel.setText("  Your final time: "+ elapsedMins + ":" + secondsDisplay);
    }

    public void GameLost(){ //method for displaying that the game has been lost ( called from gameTimer )
        //set new Labels
        timerLabel.setText("         Sorry Weasel...you lost    ");
        numTrapsLabel.setText("  Your final time: "+ elapsedMins + ":" + secondsDisplay);
    }

    ///Start and Quit Methods
    public void startPress(){
        enableGridTiles(this.grid);
        setGameInProgress(true);
    }
    public void quitPress(){
        int currentGridSize = grid.getNumTiles();
        int currentGridNumTraps = grid.getNumTraps();

        c.remove(grid);
        c.remove(topPanel);

        //Reset topPanel and grid components
        setTopPanel(currentGridNumTraps);
        grid = new Grid(currentGridSize, currentGridNumTraps);

        setContentPane(c);
        c.validate();

        setGameInProgress(false);
    }

    /// Settings methods
    public void beginnerReset(){
        setGameInProgress(false);
        c.remove(grid);
        c.remove(topPanel);

        //Reset topPanel and grid components
        setTopPanel(NUM_TRAPS_BEGINNER);
        grid = new Grid(SMALL_GRID_SIZE, NUM_TRAPS_BEGINNER);

        setContentPane(c);
        c.validate();
    }

    public void intermediateReset(){
        setGameInProgress(false);
        //Remove current components from container
        c.remove(grid);
        c.remove(topPanel);

        //Reset topPanel and grid components
        setTopPanel(NUM_TRAPS_INTERMEDIATE);
        grid = new Grid(MEDIUM_GRID_SIZE, NUM_TRAPS_INTERMEDIATE);

        setContentPane(c);
        c.validate();
    }

    public void expertReset(){
        setGameInProgress(false);
        c.remove(grid);
        c.remove(topPanel);

        //Reset topPanel and grid components
        setTopPanel(NUM_TRAPS_EXPERT);
        grid = new Grid(LARGE_GRID_SIZE, NUM_TRAPS_EXPERT);

        setContentPane(c);
        c.validate();
    }

    /////ActionListeners
    private class startAndQuitHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem whichItem = (JMenuItem) e.getSource();

            if (whichItem == start && !getGameInProgress()){
                startPress();
                runGameTimer();
            }
            else if (whichItem == quit && getGameInProgress()){
                quitPress();
                gameTimer = null;
            }
        }
    }
    private class settingsHandler implements ActionListener{

        public settingsHandler(){
            Beginner.addActionListener(this);
            Intermediate.addActionListener(this);
            Expert.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            JMenuItem whichItem = (JMenuItem) e.getSource();

            if (whichItem == Beginner){
                beginnerReset();
            }
            else if (whichItem == Intermediate){
                intermediateReset();
            }
            else if (whichItem == Expert){
                expertReset();
            }
        }
    }

    //Timer function and Action Listener
    public void runGameTimer(){ //method to construct and run gameTimer, checks gameStates every second and is called after start button press

        startTime = System.currentTimeMillis();

        ActionListener gt = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (getGameInProgress()) { //if game is in progress
                    if (grid.isGameLost || grid.isGameWon) { //check if game was won or lost, stop timer
                        if (grid.isGameWon) {
                            GameWon();
                            gameTimer.setRepeats(false);
                            gameTimer.stop();
                        } else {
                            GameLost();
                            gameTimer.setRepeats(false);
                            gameTimer.stop();
                        }
                    } else { //else update timer label every second
                        elapsedTime = System.currentTimeMillis() - startTime;
                        elapsedSecs = elapsedTime / 1000;
                        secondsDisplay = elapsedSecs % 60;
                        elapsedMins = (int) elapsedSecs / 60;

                        timerLabel.setText("Timer: " + elapsedMins + ":" + secondsDisplay);
                    }
                }
            }
        };
        gameTimer = new Timer(1000, gt); //construct gameTimer on function call
        gameTimer.start(); //start timer
    }

} //end cweasel class definition
