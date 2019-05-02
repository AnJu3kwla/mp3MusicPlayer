package mp3Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MainClass {
    
    FileInputStream FIS;        //from the package import java.io.FileInputStream;
    BufferedInputStream BIS;    //from the package import java.io.FileInputStream;
    
    
    //create global variables using the package import javazoom.jl.player.Player;
        public Player player;
        public long pauseLocation;
        public long songTotalLength;
        public String fileLocation;
        
        //create method for STOP
        public void stop(){
            
            if(player !=null){
                player.close();
                pauseLocation =0;
                songTotalLength =0;
                
                 mp3PlayerGUI.jLDisplay.setText("");
            }
        }
        
        //create method for play
        public void play(String path){
        
            try 
            {
                FIS= new FileInputStream(path);
                BIS= new BufferedInputStream(FIS);
                
                player=new Player(BIS);
                
                songTotalLength=FIS.available();
                fileLocation=path + " ";
            } 
            catch (FileNotFoundException | JavaLayerException ex) 
            {

            }
            catch (IOException ex) 
            {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //make a new thread for play method
            new Thread(){
            
                @Override
                public void run(){
                    
                    try 
                    {
                        player.play();
                        
                        if(player.isComplete() && mp3PlayerGUI.count == 1)
                        {
                            play(fileLocation);
                            mp3PlayerGUI.jLDisplay.setText(path);
                        }
                        if(player.isComplete())
                        {
                             mp3PlayerGUI.jLDisplay.setText("");
                        }
                        
                    } 
                    catch (JavaLayerException ex) 
                    {
                       
                    }
                }
            }.start();
        }
        
        //create method for pause & resume--------------------------------------------------------------------------------------
        //create method for pause
        public void pause(){
            
            if(player !=null){
                try 
                {
                    pauseLocation=FIS.available();
                    player.close();
                } 
                catch (IOException ex) 
                {
                    
                }
            }
        }
        
        //create method for resume
        public void resume( ){
        
            try 
            {
                FIS= new FileInputStream(fileLocation);
                BIS= new BufferedInputStream(FIS);
                
                player=new Player(BIS);
                
                FIS.skip(songTotalLength-pauseLocation);
            } 
            catch (FileNotFoundException | JavaLayerException ex) 
            {

            } 
            catch (IOException ex) 
            {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            new Thread(){
            
                @Override
                public void run(){
                    
                    try 
                    {
                        player.play();
                    } 
                    catch (JavaLayerException ex) 
                    {
                       
                    }
                }
            }.start();
        }
}
