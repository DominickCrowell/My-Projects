import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class CourtyardLoseScreen here.
 * 
 * @author croweld@email.uscb.edu
 * @version Dec 11, 2022
 */
public class CourtyardLoseScreen1 extends World
{
    /* FIELD(S) */
    private boolean soundPlayed = false;
    
    /* CONSTRUCTOR(S) */
    /**
     * This constructor sets the world size.
     */
    public CourtyardLoseScreen1()
    {    
        super(640, 480, 1); 
    } // end CourtyardLoseScreen1 constructor
    
    /* METHOD(S) */
    /**
     * This contains code for playing a sound when the screen appears and allows
     * the user to enter keyboard inputs in order to navigate to different screens.
     */
    public void act()
    {
        if ( soundPlayed == false )
        {
            Greenfoot.playSound("fail.wav");
            soundPlayed = true;
        } // end if
        if ( Greenfoot.isKeyDown("enter") )
        {
            Greenfoot.setWorld(new Courtyard());
        } // end if
        if ( Greenfoot.isKeyDown("escape") )
        {
            Greenfoot.setWorld(new StartScreen());
        } // end if
    } // end method act
} // end class CourtyardLoseScreen1
