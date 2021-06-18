package pacman;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class Mapa {
    private Circle[][] hraciPole;
    private ObservableList<Shape> list;
    
    public Mapa(ObservableList list){
        this.list=list;
    }
    
    private void vodorovne(int radek,int a,int b){
       for (int i = a; i <= b; i++) {
    	   if(hraciPole[i][radek] == null) {
	        hraciPole[i][radek]=new Circle(i*20,radek*20,5); 
	        hraciPole[i][radek].setFill(Color.YELLOW);
	        list.add(hraciPole[i][radek]);
    	   }
        }  
    }
    private void svisle(int sloupec,int a,int b){
       for (int i = a; i <= b; i++) {
    	   if(hraciPole[sloupec][i]==null) {
	        hraciPole[sloupec][i]=new Circle(sloupec*20,i*20,5); 
	        hraciPole[sloupec][i].setFill(Color.YELLOW);
	        list.add(hraciPole[sloupec][i]);
    	   }
        }  
    }
    private void obdelnik(double x,double y,double w,double h){
      Rectangle o=new Rectangle(x,y,w,h);
      o.setFill(Color.BLACK);
      o.setStroke(Color.BLUE);
      o.setArcHeight(30);
      o.setArcWidth(30);
      list.add(o);
     }
     
        
    public void mapa1(){
        hraciPole=new Circle[32][32];
       
         vodorovne(1,2,30);
         vodorovne(30,2,30);
         vodorovne(10,7,10);
         vodorovne(10,12,15);
         vodorovne(10,17,24);
         vodorovne(10,26,29);
         vodorovne(20,2,15);
         vodorovne(25,2,29);
         vodorovne(6,12,4);
         vodorovne(15,17,20);
         vodorovne(5,12,24);
         
         svisle(6,2,19);
         svisle(11,2,19);        
         svisle(16,6,24);
         svisle(25,2,10);
         svisle(21,11,24);
         svisle(1,1,30);
         svisle(30,1,30);
         svisle(12,26,30);
         
         
         obdelnik(30,30,75,358);
         obdelnik(30,410,275,80); 
         obdelnik(30,510,200,80);
         
         obdelnik(135,30,70,160); 
         obdelnik(135,210,70,175); 
         
         obdelnik(232.5,32,255,55);
         obdelnik(232.5,110,75,75);
         obdelnik(235,210,70,175);
         
         obdelnik(250,512.5,340,75);
               
         obdelnik(335,112.5,150,75);
         obdelnik(335,212.5,70,72.5);
         obdelnik(335,312.5,70,172.5);    
         
         obdelnik(434,212,150,275); 
         obdelnik(510,35,75,155);
         
         
         
        
        
    }


    public Circle[][] getHraciPole(){
        return hraciPole;
    }
    
    public void reload() {
    	vodorovne(1,2,30);
        vodorovne(30,2,30);
        vodorovne(10,7,10);
        vodorovne(10,12,15);
        vodorovne(10,17,24);
        vodorovne(10,26,29);
        vodorovne(20,2,15);
        vodorovne(25,2,29);
        vodorovne(6,12,4);
        vodorovne(15,17,20);
        vodorovne(5,12,24);
        
        svisle(6,2,19);
        svisle(11,2,19);        
        svisle(16,6,24);
        svisle(25,2,10);
        svisle(21,11,24);
        svisle(1,1,30);
        svisle(30,1,30);
        svisle(12,26,30);
    }



}


