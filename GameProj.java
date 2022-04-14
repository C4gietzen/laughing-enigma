import javax.swing.*;
import javax.swing.JOptionPane;
import java.awt.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.awt.event.*;
import java.math.*;

public class GameProj extends JFrame {
   
   private Player wooer = new Player( 40, 40, Color.BLUE);
   private static ArrayList<ArrayList<GameObject>> gameObjs = new ArrayList<ArrayList<GameObject>>(); //this will hold all of the game objects that are on the map
   int N, jump;
   static boolean keyUp, keyLeft, keyRight, game=false;



   public GameProj(){ //Frame constructor where panel is added to contents
   
      super("woo");
      setVisible(true);
      setSize(832,666);
      setBackground(Color.BLACK);
      Container contents = getContentPane();
      contents.setLayout(new BorderLayout(5,5));
      contents.add(new Level(), BorderLayout.CENTER);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      addKeyListener(new KeyEventMethod());
      
      Timer t = new Timer (10, new TimeListener());
      t.start();
   }
   
   public class KeyEventMethod implements KeyListener{
      
      
      public void keyTyped (KeyEvent e) {}
      public void keyReleased (KeyEvent e) {
         if(e.getKeyCode() == KeyEvent.VK_W){
         
            keyUp = false;
         }
         if(e.getKeyCode() == KeyEvent.VK_A){
         
            keyLeft = false;
         }
         if(e.getKeyCode() == KeyEvent.VK_D){
         
            keyRight = false;
         }
         
         repaint();
      }
      public void keyPressed (KeyEvent e) {
         if(e.getKeyCode() == KeyEvent.VK_W){
            keyUp = true;
         }
         if(e.getKeyCode() == KeyEvent.VK_A){
            keyLeft = true;
         }
         if(e.getKeyCode() == KeyEvent.VK_D){
            keyRight = true;
         }
         
         repaint();
      }
   }
   
   
   
   
   public class TimeListener implements ActionListener{
      
      
      public void actionPerformed (ActionEvent e){
         
         
         if (!wooer.isOnGround(gameObjs)){
               wooer.move(0, 1, gameObjs);
               repaint();
               jump -= .1;
            
         }else{
            N = 1;
         }
                  
         
         wooer.setX(wooer.getX()+wooer.getXSpeed());
         if ( wooer.collides(gameObjs) ) {
            wooer.setX(wooer.getX()-wooer.getXSpeed());
         }
         wooer.setY(wooer.getY()+wooer.getYSpeed());
         if ( wooer.collides(gameObjs) ) {
            wooer.setY(wooer.getY()-wooer.getYSpeed());
         }

         
         if ( (keyLeft && keyRight) || ( !keyLeft && !keyRight ) ) {

            wooer.setXSpeed( (wooer.getXSpeed() * .9) ); //friction 
            
            
         }else if ( keyLeft && !keyRight ) {
         
            wooer.setXSpeed( wooer.getXSpeed() - .5 );
            
         }else if ( keyRight && !keyLeft ) {
         
            wooer.setXSpeed( wooer.getXSpeed() + .5 );
            
         }
         
         if ( (wooer.getXSpeed() > 0) && (wooer.getXSpeed() < .25) ) { //to prevent sliding
            wooer.setXSpeed(0);
         }
         
         if ( wooer.getXSpeed() > 3 ) { //set max speed right
            wooer.setXSpeed(3);
         }
         
         if ( wooer.getXSpeed() < -3 ) { //set max speed left 
            wooer.setXSpeed(-3);
         }

         
         if ( keyUp && wooer.isOnGround(gameObjs) ) {
               wooer.setYSpeed(-12);
         }
         
         wooer.setYSpeed(wooer.getYSpeed() + .3);
         
         if ( wooer.getYSpeed() > 5 ) {
            wooer.setYSpeed(5);
         
         }
         
         if ( game ) {
            JOptionPane.showMessageDialog(null, "Winner Winner Chicken Dinner", "You Win!", JOptionPane.INFORMATION_MESSAGE);
            System.exit(1);
         }
         
         
         
         repaint();
      }
   }
   
   
   
   
   
   
   
   
   public static void  main (String [] args){
      GameProj woo = new GameProj();
   }
   
   
   
   
   
   
   
   public class Level extends JPanel {
      
      int row, columns;
      int [][] levelData;
      
      public Level(){
      
      
         setVisible(true);
         setBackground(Color.CYAN);
         setSize(832,645);
         
         
         try{
         
         
            Scanner read = new Scanner(new File("BahBahData.txt"));
            
            wooer.setX ( ( 26*read.nextInt() ) + 14 );
            
            wooer.setY ( ( 26*read.nextInt() ) + 14 );
            
            row = read.nextInt();
            
            columns = read.nextInt();
            
            levelData = new int [row] [columns];
            for (int i=0; i < row; i++){
            
               gameObjs.add(new ArrayList<GameObject>());
               
               for (int j=0; j < columns; j++ ){
                  Color c = null;
                  levelData [i] [j] = read.nextInt();
                  if (levelData [i][j] == 0){
                     c = null;
                  }else if ( levelData [i][j] == 1 ){
                     c = Color.RED;
                  }else if ( levelData [i][j] == 2 ){
                     c = Color.GREEN;
                  }else if ( levelData [i][j] == 3 ){
                     c = Color.BLACK;
                  }
                  
                  if (c == null){
                     gameObjs.get(i).add(null);
                  }else{
                     gameObjs.get(i).add(new GameObject( (j*26) + 13 , (i*26) + 13 , c));
                  }
                  
               }
            }
         }catch (FileNotFoundException fnfe) {
            
         }
      }
      
      public void paintComponent (Graphics g){
         
         super.paintComponent(g);
         wooer.draw(g);
         for (int i=0; i < row; i++){
            for (int j=0; j < columns; j++ ){
               if( !(gameObjs.get(i).get(j) == null) ){
                  gameObjs.get(i).get(j).draw(g);
               }
            }
         }
      }
   }
   
   
   
   
   
   
   
   public class GameObject {
      
      private double xPos, yPos;
      Color color;
      
      public GameObject( int x_in, int y_in, Color c){
         xPos = x_in;
         yPos = y_in;
         color = c;
      }
      
      public boolean collides (GameObject o){
         
            
      
         if( this == o || this == null){
         
            return false;
            
         }else{
            
            
            if ( (Math.abs(o.yPos - this.yPos) < 26) && (Math.abs(o.xPos - this.xPos) < 26) ){
               return true;
            }else{
               return false;
            }
         
         }
      }
      
      public void draw (Graphics g){ //draw the 26x26 gameobject at xPos and Ypos
         try{
            if ( !(getColor() == null) ){
               g.setColor(getColor());
               g.fillRect((int)getX()-13 , (int)getY()-13, 26, 26);
            }
         }catch(NullPointerException npe){
            
         }
      }
      
      public double getX(){
         return xPos;
      }
      
      public double getY(){
         return yPos;
      }
      
      public void setX(double x_in){
         xPos = x_in;
      }
      
      public void setY(double y_in){
         yPos = y_in;
      }
      
      public Color getColor(){
         return color;
      }
   }
   
   
   
   
   
   
   
   
   public class Player extends GameObject{
      
      double xSpeed, ySpeed;
      
      
      
      public Player ( int x_in, int y_in, Color c )
      {
         super(x_in, y_in, c);
      }
      
      public boolean isOnGround ( ArrayList<ArrayList<GameObject>> a){
         
         setY(getY()+1);
         if (wooer.collides(a)){
            setY(getY()-1);
            return true;
         }else{
            return false;
         }
         
      }
      
      public boolean move (double x_in, double y_in, ArrayList<ArrayList<GameObject>> a){
      
         boolean success;
         wooer.setX(wooer.getX()+x_in);
         wooer.setY(wooer.getY()+y_in);
         success = !(wooer.collides(a));
         if (!success){
            wooer.setX(getX()-x_in);
            wooer.setY(getY()-y_in);
         }
         return success;
      }
      
               
      
      public boolean collides (ArrayList<ArrayList<GameObject>> a){
         boolean collide = false;
         int xTL = ((int)wooer.getX()-13)/26;
         int yTL = ((int)wooer.getY()-13)/26;
         
         try{
            if (a.get(yTL+1).get(xTL).collides(wooer) ) {
               collide = true;
               if (a.get(yTL+1).get(xTL).getColor() == Color.GREEN ) {
                  game = true;
               }
               if (a.get(yTL+1).get(xTL).getColor() == Color.BLACK ) {
                  wooer.setX(50);
                  wooer.setY(50);
               }

            }
         }catch( Exception e ){
         }
         try{
            if (a.get(yTL).get(xTL).collides(wooer) ) {
               //wooer.setYSpeed(1);
               collide = true;
               if (a.get(yTL).get(xTL).getColor() == Color.GREEN ) {
                  game = true;
               }
               if (a.get(yTL).get(xTL).getColor() == Color.BLACK ) {
                  wooer.setX(50);
                  wooer.setY(50);
               }


            }
         }catch( Exception e ){
         }
         try{
            if (a.get(yTL-1).get(xTL).collides(wooer) ) {
               collide = true;
               if (a.get(yTL-1).get(xTL).getColor() == Color.GREEN ) {
                  game = true;
               }
               if (a.get(yTL-1).get(xTL).getColor() == Color.BLACK ) {
                  wooer.setX(50);
                  wooer.setY(50);
               }


            }
         }catch( Exception e ){
            
         }
         try{
            if (a.get(yTL).get(xTL+1).collides(wooer) ) {
               //wooer.setYSpeed(1);
               collide = true;
               if (a.get(yTL).get(xTL+1).getColor() == Color.GREEN ) {
                  game = true;
               }
               if (a.get(yTL).get(xTL+1).getColor() == Color.BLACK ) {
                  wooer.setX(50);
                  wooer.setY(50);
               }


            }
         }catch( Exception e ){
            
         }
         try{
            if (a.get(yTL+1).get(xTL+1).collides(wooer) ) {
               collide = true;
               if (a.get(yTL+1).get(xTL+1).getColor() == Color.GREEN ) {
                  game = true;
               }
               if (a.get(yTL+1).get(xTL+1).getColor() == Color.BLACK ) {
                  wooer.setX(50);
                  wooer.setY(50);
               }


            }
         }catch( Exception e ){
            
         }



         return collide;
      }
      
      public double getXSpeed(){
         return xSpeed;
      }
      
      public double getYSpeed(){
         return ySpeed;
      }
      
      public void setXSpeed(double xSpeed_in){
         xSpeed = xSpeed_in;
      }
      
      public void setYSpeed(double ySpeed_in){
         ySpeed = ySpeed_in;
      }
   }
   
   
   
   
   
   
  
}
