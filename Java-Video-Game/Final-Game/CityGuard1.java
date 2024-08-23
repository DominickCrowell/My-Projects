import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * CityGuard1 is the player-character that walks around collecting knives in
 * the stage I ( StorageRoom() ).
 * 
 * @author croweld@email.uscb.edu 
 * @version Dec 11, 2022
 * 
 * Source for CityGuard's sprite & animations:
 * https://opengameart.org/content/lpc-medieval-fantasy-character-sprites
 */
public class CityGuard1 extends Actor
{
    /* FIELD(S) */
    private int knivesCollected = 0;
    private int spriteFrameCounter = 60;
    private boolean doorUnlocked = false;
    
    /* CONSTRUCTOR(S) */
    
    /* METHOD(S) */
    /**
     * Act - do whatever the CityGuard wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        checkKeyPress();
        lookForSilverKnife();
        lookForDoor();
        spriteFrameCounter--;
    } // end method act
    
    /**
     * This method is used to assign keyboard inputs by the user to certain
     * actions of CityGuard such as walking and throwing knives.
     */
    private void checkKeyPress()
    {
        if ( Greenfoot.isKeyDown("w") && getY() >= 33 && spriteFrameCounter <= 60 )
        {
            setLocation(getX(), getY()-2);
            setImage("cityguard_back1.png");
            if ( isTouching(HorizontalWall.class) || isTouching(VerticalWall.class) )
            {
                setLocation(getX(), getY()+2);
            } // end inner if
            if ( spriteFrameCounter <= 40 )
            {
                setImage("cityguard_back2.png");
            } // end inner if
            if ( spriteFrameCounter <= 20 )
            {
                setImage("cityguard_back3.png");
            } // end inner if
            if ( spriteFrameCounter <= 0 )
            {
                spriteFrameCounter = 60;
            } // end inner if
        } // end outer if
        if ( Greenfoot.isKeyDown("s") && getY() <= 429 )
        {
            setLocation(getX(), getY()+2);
            setImage("cityguard_front1.png");
            if ( isTouching(HorizontalWall.class) || isTouching(VerticalWall.class) )
            {
                setLocation(getX(), getY()-2);
            } // end inner if
            if ( spriteFrameCounter <= 40 )
            {
                setImage("cityguard_front2.png");
            } // end inner if
            if ( spriteFrameCounter <= 20 )
            {
                setImage("cityguard_front3.png");
            } // end inner if
            if ( spriteFrameCounter <= 0 )
            {
                spriteFrameCounter = 60;
            } // end inner if
        } // end if
        if ( Greenfoot.isKeyDown("a") && getX() >= 40 )
        {
            setLocation(getX()-2, getY());
            setImage("cityguard_left1.png");
            if ( isTouching(HorizontalWall.class) || isTouching(VerticalWall.class) )
            {
                setLocation(getX()+2, getY());
            } // end inner if
            if ( spriteFrameCounter <= 40 )
            {
                setImage("cityguard_left2.png");
            } // end inner if
            if ( spriteFrameCounter <= 20 )
            {
                setImage("cityguard_left3.png");
            } // end inner if
            if ( spriteFrameCounter <= 0 )
            {
                spriteFrameCounter = 60;
            } // end inner if
        } // end if
        if ( Greenfoot.isKeyDown("d") && getX() <= 600 )
        {
            setLocation(getX()+2, getY());
            setImage("cityguard_right1.png");
            if ( isTouching(HorizontalWall.class) || isTouching(VerticalWall.class) )
            {
                setLocation(getX()-2, getY());
            } // end inner if
            if ( spriteFrameCounter <= 40 )
            {
                setImage("cityguard_right2.png");
            } // end inner if
            if ( spriteFrameCounter <= 20 )
            {
                setImage("cityguard_right3.png");
            } // end inner if
            if ( spriteFrameCounter <= 0 )
            {
                spriteFrameCounter = 60;
            } // end inner if
        } // end if
    } // end method checkKeyPress
    
    /**
     * This method is used to check if CityGuard is colliding with a Container,
     * if so, knivesCollected is incremented by 1 (until knivesCollected == 10).
     * 
     * Source for "knifecollected.wav":
     * https://www.youtube.com/watch?v=zqujcCBgwkU
     */
    private void lookForSilverKnife()
    {
        if ( isTouching(Container.class) || isTouching(CollectibleSilverKnife.class) )
        {
            knivesCollected++;
            Greenfoot.playSound("knifecollected.wav");
            removeTouching(Container.class);
            removeTouching(CollectibleSilverKnife.class);
        } // end if
    } // end method lookForKnife
    
    /**
     * This method allows the player to use the door to progress to the next
     * stage once they have collected all 10 knives.
     * 
     * Source for "doorunlocking.wav":
     * https://www.youtube.com/watch?v=92YfzPu3Ukc
     */
    private void lookForDoor()
    {
        if ( knivesCollected == 10 && doorUnlocked == false )
        {
            Greenfoot.playSound("doorunlocking.wav");
            doorUnlocked = true;
        } // end if
        if ( knivesCollected == 10 && isTouching(Door.class) )
        {
               Greenfoot.setWorld(new Courtyard());
        } // end if
    } // end method lookForDoor
} // end class CityGuard1
