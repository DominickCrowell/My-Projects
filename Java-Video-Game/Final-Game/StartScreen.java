import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * This world is a simple start screen that accepts "enter" as a keyboard input.
 * 
 * @author croweld@email.uscb.edu 
 * @version Dec 11, 2022
 */
public class StartScreen extends World
{
    /* FIELD(S) */
    
    /* CONSTRUCTOR(S) */
    /**
     * This constructor sets the world size.
     */
    public StartScreen()
    {    
        super(640, 480, 1); 
    } // end StartScreen constructor
    
    /* METHOD(S) */
    /**
     * This contains code for entering keyboard inputs in order to navigate to different screens.
     */
    public void act()
    {
        if ( Greenfoot.isKeyDown("enter") )
        {
            Greenfoot.setWorld(new StorageRoom());
        } // end if
    } // end method act
} // end class StartScreen
