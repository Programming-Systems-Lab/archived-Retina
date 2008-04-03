package retina.ui;

import java.awt.*;
import javax.swing.*;
import java.applet.*;
import graph.*;

public class StatGraph 
{

	Graph2D graph;
    DataSet data;
    Axis    xaxis;
    Axis    yaxis_left;
    Axis    yaxis_right;
    double d1[];
    int np = 100000; 
    
    public StatGraph(){
    }
    

    public StatGraph(int[] x, int[] y, int points){
    	plotGraph(x, y, points);
    }
    
    public Graph2D getGraph()
    {
    	return graph;
    }
    
    public void setXTitle(String title){
    	 xaxis.setTitleText(title);
    	
    }
    
    public void setYTitle(String title){
    	yaxis_left.setTitleText(title);
   	
   }
    
    public void setXLabels(String[] label){
    	xaxis.label_string = label;
    }
    
    public void setYLabels(String[] label){
    	yaxis_left.label_string = label; 
    }
    
    	
    public void plotGraph(int[] x, int[] y, int points) {
      int i;
      final int MAXPOINTS=points;
      double d1[] = new double[MAXPOINTS*2];
      
      graph = new Graph2D();
      graph.drawzero = false;
      graph.drawgrid = false;
      graph.borderTop = 50;
      graph.borderRight=100;
      
    /*  setLayout( new BorderLayout() );
      add("Center", graph); */
      
      for(i=0; i<MAXPOINTS; i++)
      {
    	  d1[i*2]=i; //x values 
    	  d1[i*2+1]=y[i]; //y values
      }
      
      xaxis = graph.createAxis(Axis.BOTTOM);
      yaxis_left = graph.createAxis(Axis.LEFT);
      
      data = graph.loadDataSet(d1,MAXPOINTS);
      data.linestyle = 1;
      data.linecolor = Color.black;
      data.marker = 1;
      data.markerscale = 1;
      data.markercolor = new Color(0,0,255);
     // data.legend(200,100,"Y=X, linear");
     // data.legendColor(Color.black);
      
      xaxis.attachDataSet(data);
      xaxis.setTitleFont(new Font("Arial",Font.PLAIN,20));
      xaxis.setLabelFont(new Font("Arial",Font.PLAIN,15));
/*
**      Attach the first data set to the Left Axis
*/
      yaxis_left.attachDataSet(data);
      yaxis_left.setTitleFont(new Font("Arial",Font.PLAIN,20));
      yaxis_left.setLabelFont(new Font("Arial",Font.PLAIN,15));
      yaxis_left.setTitleColor(Color.black);
   
    }
    
}

