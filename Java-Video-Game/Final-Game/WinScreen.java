import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * This world is used to indicate the player has won the game.
 * 
 * @author croweld@email.uscb.edu 
 * @version Dec 11, 2022
 */
public class WinScreen extends World
{
    /* FIELD(S) */
    
    /* CONSTRUCTOR(S) */
    /**
     * This constructor sets the world size.
     */
    public WinScreen()
    {    
        super(640, 480, 1); 
    } // end WinScreen constructor
    
    /* METHOD(S) */
    /**
     * This contains code for allowing the user to enter keyboard inputs in 
     * order to navigate to different screens.
     */
    public void act()
    {
        if ( Greenfoot.isKeyDown("escape") )
        {
            Greenfoot.setWorld(new StartScreen());
        } // end if
    } // end method act
} // end class WinScreen
