import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * This world contains a CityGuard1, many Container() and EmptyContainer() objects,
 * CollectibleSilverKnife() objects, and a Door().
 * 
 * @author croweld@email.uscb.edu 
 * @version Dec 11, 2022
 */
public class StorageRoom extends World
{
    /* FIELD(S) */
    private int cycleCount = 70;
    private int secondsCount = 30;
    
    /* CONSTRUCTOR(S) */
    /**
     * This constructor sets the world size and calls the prepare() method.
     */
    public StorageRoom()
    {    
        // Create a new StorageRoom with 640x480 cells with a cell size of 1x1 pixels.
        super(640, 480, 1);
        prepare();
    } // end StorageRoom constructor
    
    /* METHOD(S) */
    /**
     * This act method decrements the timeCounter variable and displays it
     * on the screen and contains other methods.
     */
    public void act()
    {
        cycleCount--;
        showText("Time left: " + secondsCount, 540, 44);
        showGameOver();
        showSecondsPassed();
    } // end method act
    
    /**
     * This method shows a game over screen if the player's time runs out.
     */
    private void showGameOver()
    {
        if (secondsCount <= 0)
        {
            Greenfoot.setWorld(new StorageRoomLoseScreen());
        } // end if
    } // end method showGameOver
    
    /**
     * This method turns the cycleCount into a readable timer measured in
     * seconds.
     */
    private void showSecondsPassed()
    {
        if ( cycleCount == 0 )
        {
            secondsCount--;
            cycleCount = 70;
        }
    } // end method showSecondsPassed
    
    /**
     * Adds walls, containers, a city guard, silver knives, and a door to the
     * world.
     */
    private void prepare()
    {
        addObject(new Container(), 389, 35);
        addObject(new Container(), 547, 177);
        addObject(new Container(), 253, 342);
        addObject(new Container(), 46, 240);
        addObject(new Container(), 434, 35);
        addObject(new EmptyContainer(), 594, 177);
        addObject(new EmptyContainer(), 594, 299);
        addObject(new EmptyContainer(), 343, 157);
        addObject(new EmptyContainer(), 162, 35);
        addObject(new EmptyContainer(), 434, 375);
        addObject(new CollectibleSilverKnife(), 457, 258);
        addObject(new CollectibleSilverKnife(), 592, 92);
        addObject(new CollectibleSilverKnife(), 570, 405);
        addObject(new CollectibleSilverKnife(), 46, 161);
        addObject(new CollectibleSilverKnife(), 251, 291);
        addObject(new Door(), 320, 461);
        addObject(new CityGuard1(), 320, 423);
        addObject(new HorizontalWall(), 533, 240);
        addObject(new HorizontalWall(), 495, 240);
        addObject(new HorizontalWall(), 191, 240);
        addObject(new VerticalWall(), 287, 372);
        addObject(new VerticalWall(), 355, 344);
        addObject(new VerticalWall(), 287, 344);
        addObject(new VerticalWall(), 355, 372);
        addObject(new VerticalWall(), 287, 372);
        addObject(new VerticalWall(), 355, 336);
        addObject(new VerticalWall(), 287, 336);
    } // end method prepare
} // end class StorageRoom
