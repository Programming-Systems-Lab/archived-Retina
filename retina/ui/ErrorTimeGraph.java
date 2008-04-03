package retina.ui;

import java.awt.*;
import javax.swing.*;
import java.applet.*;
import graph.*;

public class ErrorTimeGraph 
{

	Graph2D graph;
    DataSet data;
    Axis    xaxis;
    Axis    yaxis_left;
    Axis    yaxis_right;
    double d1[];
    int np = 100000;

    public ErrorTimeGraph(int[] x, int[] y, int points){
    	plotGraph(x, y, points);
    }
    
    public Graph2D getGraph()
    {
    	return graph;
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
      
      data = graph.loadDataSet(d1,MAXPOINTS);
      data.linestyle = 1;
      data.linecolor = Color.black;
      data.marker = 1;
      data.markerscale = 1;
      data.markercolor = new Color(0,0,255);
     // data.legend(200,100,"Y=X, linear");
     // data.legendColor(Color.black);
      
      xaxis = graph.createAxis(Axis.BOTTOM);
      xaxis.attachDataSet(data);
      xaxis.setTitleText("Time");
      xaxis.setTitleFont(new Font("Arial",Font.PLAIN,20));
      xaxis.setLabelFont(new Font("Arial",Font.PLAIN,15));
/*
**      Attach the first data set to the Left Axis
*/
      yaxis_left = graph.createAxis(Axis.LEFT);
      yaxis_left.attachDataSet(data);
      yaxis_left.setTitleText("Number of Errors per CompilationEvent");
      yaxis_left.setTitleFont(new Font("Arial",Font.PLAIN,20));
      yaxis_left.setLabelFont(new Font("Arial",Font.PLAIN,15));
      yaxis_left.setTitleColor(Color.black);
   
    }
    
}

