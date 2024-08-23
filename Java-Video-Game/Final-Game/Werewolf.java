import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Werewolf (also known as the "Wolf Man"), is the antagonistic character
 * that attacks CityGuard2. It can be damaged by knives.
 * 
 * @author croweld@email.uscb.edu 
 * @version Dec 11, 2022
 * 
 * Source for Werewolf's sprite & animations:
 * https://www.spriters-resource.com/snes/zombiesneighbours/sheet/31620/
 */
public class Werewolf extends Actor
{
    /* FIELD(S) */
    private int verticalDirection;
    private int horizontalDirection;
    private int cycleCount = 100;
    private int life = 140;
    private int spriteFrameCounter = 60;
    
    /* CONSTRUCTOR(S) */
    
    /* METHOD(S) */
    /**
     * Act - do whatever the Werewolf wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        moveRandomly();
        lookForCityGuard();
        isTouchingKnife();
        spriteFrameCounter--;
        cycleCount--;
    } // end method act
    
    /**
     * This method allows the Werewolf() to touch the player and then show a
     * fail screen.
     */
    private void lookForCityGuard()
    {
        if ( isTouching( CityGuard2.class ) )
        {
            Greenfoot.setWorld(new CourtyardLoseScreen2());
        } // end if
    } // end method lookForCityGuard
    
    /**
     * This method enables Werewolf() to move in random directions at a quick
     * speed.
     */
    private void moveRandomly()
    {
        if ( cycleCount == 0 )
        {
            verticalDirection = Greenfoot.getRandomNumber(2);
            horizontalDirection = Greenfoot.getRandomNumber(2);
            cycleCount = 100;
        } // end if
        if (horizontalDirection == 0 && cycleCount <= 25 && getX() <= 590)
        {
            setLocation(getX()+4, getY());
            setImage("werewolf1.png");
            if ( isTouching(Pillar.class) )
            {
                setLocation(getX()-4, getY());
            } // end inner if
            if ( spriteFrameCounter <= 40 )
            {
                setImage("werewolf2.png");
            } // end inner if
            if ( spriteFrameCounter <= 20 )
            {
                setImage("werewolf3.png");
            } // end inner if
            if ( spriteFrameCounter <= 0 )
            {
                spriteFrameCounter = 60;
            } // end inner if
        }
        else if (horizontalDirection == 1 && cycleCount > 25 && cycleCount <= 50 && getX() >= 50)
        {
            setLocation(getX()-4, getY());
            setImage("werewolf1.png");
            if ( isTouching(Pillar.class) )
            {
                setLocation(getX()+4, getY());
            } // end inner if
            if ( spriteFrameCounter <= 40 )
            {
                setImage("werewolf2.png");
            } // end inner if
            if ( spriteFrameCounter <= 20 )
            {
                setImage("werewolf3.png");
            } // end inner if
            if ( spriteFrameCounter <= 0 )
            {
                spriteFrameCounter = 60;
            } // end inner if
        }
        else if (verticalDirection == 0 && cycleCount > 50 && cycleCount <= 75 && getY() <= 416)
        {
            setLocation(getX(), getY()+4);
            setImage("werewolf1.png");
            if ( isTouching(Pillar.class) )
            {
                setLocation(getX(), getY()-4);
            } // end inner if
            if ( spriteFrameCounter <= 40 )
            {
                setImage("werewolf2.png");
            } // end inner if
            if ( spriteFrameCounter <= 20 )
            {
                setImage("werewolf3.png");
            } // end inner if
            if ( spriteFrameCounter <= 0 )
            {
                spriteFrameCounter = 60;
            } // end inner if
        }
        else if (verticalDirection == 1 && cycleCount > 75 && cycleCount < 100 && getY() >= 49)
        {
            setLocation(getX(), getY()-4);
            setImage("werewolf1.png");
            if ( isTouching(Pillar.class) )
            {
                setLocation(getX(), getY()+4);
            } // end inner if
            if ( spriteFrameCounter <= 40 )
            {
                setImage("werewolf2.png");
            } // end inner if
            if ( spriteFrameCounter <= 20 )
            {
                setImage("werewolf3.png");
            } // end inner if
            if ( spriteFrameCounter <= 0 )
            {
                spriteFrameCounter = 60;
            } // end inner if
        } // end outer if/else
    } // end method move
    
    /**
     * This method is used to detect whether or not a SilverKnife is touching
     * the Werewolf. If so, it's health (life) is decremented by 10. If it reaches
     * 0, the WinScreen() world will appear.
     * 
     * Source for "wolfhurt":
     * https://www.youtube.com/shorts/BuyfRCGtMZY
     */
    private void isTouchingKnife()
    {
        if ( isTouching(ThrowableSilverKnife.class))
        {
            life = life - 10;
            removeTouching(ThrowableSilverKnife.class);
            Greenfoot.playSound("wolfhurt.wav");
            if ( life <= 0 )
            {
                Greenfoot.setWorld(new WinScreen());
            } // end inner if
        } // end outer if
    } // end method isTouchingKnife
} // end class Werewolf
