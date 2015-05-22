
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.font.*;

import javax.swing.*;

import java.util.*;

public class Cluster extends JFrame 
{
    private static int NUM_CLUSTERS;    // Total clusters.
    private static int TOTAL_DATA;  // Total data points.
    private static final int PAD = 20;
    
    private static ArrayList<Data> dataSet = new ArrayList<Data>();
    private static ArrayList<Centroid> centroids = new ArrayList<Centroid>();

    private static int width, height, CanvasWidth, CanvasHeight;
    private static Graphics2D  g2d ;
    private static JFrame frame;
    private static Insets insets;
    private static DrawingCanvas canvas;
/*
 	 static double SAMPLES[][] = new double[][] { { 1.0, 1.0 },
							{ 1.5, 2.0 }, 
							{ 3.0, 4.0 }, 
							{ 5.0, 7.0 }, 
							{ 3.5, 5.0 },
							{ 4.5, 5.0 },
							{ 3.5, 4.5 } };
*/
 	 static double SAMPLES[][] = new double[][] { { 1.0, 1.0 },
							{ 1.5, 2.0 }, 
							{ 3.0, 4.0 }, 
							{ 5.0, 7.0 }, 
							{ 3.5, 5.0 },
							{ 4.5, 5.0 },
							{ 6.5, 2.0 }, 
							{ 3.0, 1.0 }, 
							{ 3.0, 5.0 }, 
							{ 3.5, 6.0 },
							{ 7.5, 7.0 },
							{ 8.5, 8.5 } };
	public static void initialize() {
		
		String input = JOptionPane.showInputDialog(frame, "Enter the value of K (NUMBER ONLY)");
		int k = Integer.parseInt(input);
		// Initializing Centroids depending on how many K is
		TOTAL_DATA = SAMPLES.length;
		NUM_CLUSTERS = k;

		System.out.println("Centroids initialised:");
	
		for(int i = 0; i < NUM_CLUSTERS; i++)
		{
		   centroids.add(new Centroid(Math.random()*10, Math.random()*10)); 
		   System.out.println(centroids.get(i).X() + ", " + centroids.get(i).Y());
		}
		
		System.out.print("\n");

	}

	public static void kMeans()
 {
        final double bigNumber = Math.pow(10, 10);    // some big number that's sure to be larger than our data range.
        double minimum = bigNumber;                   // The minimum value to beat. 
        double distance = 0.0;                        // The current minimum value.
        int sampleNumber = 0;
        int cluster = 0;
        boolean isStillMoving = true;
        Data newData = null;

		Euclidean euc = new Euclidean();

		// Add in new data, one at a time, recalculating centroids with each new
		// one.
		while (dataSet.size() < TOTAL_DATA) {
			newData = new Data(SAMPLES[sampleNumber][0],SAMPLES[sampleNumber][1]);
			dataSet.add(newData);
			minimum = bigNumber;
			for (int i = 0; i < NUM_CLUSTERS; i++) {
				distance = euc.calculate(newData, centroids.get(i));
				if (distance < minimum) {
					minimum = distance;
					cluster = i;
				}
			}
			newData.cluster(cluster);

			// Calculate the new Centroids
			for (int i = 0; i < NUM_CLUSTERS; i++) {
				int totalX = 0;
				int totalY = 0;
				int totalInCluster = 0;
				for (int j = 0; j < dataSet.size(); j++) {
					if (dataSet.get(j).cluster() == i) {
						totalX += dataSet.get(j).X();
						totalY += dataSet.get(j).Y();
						totalInCluster++;
					}
				}
				if (totalInCluster > 0) {
					centroids.get(i).X(totalX / totalInCluster);
					centroids.get(i).Y(totalY / totalInCluster);
				}
			}
			sampleNumber++;

			// Now, keep shifting centroids until equilibrium occurs.
			while (isStillMoving) {
				// calculate new centroids.
				for (int i = 0; i < NUM_CLUSTERS; i++) {
					int totalX = 0;
					int totalY = 0;
					int totalInCluster = 0;
					for (int j = 0; j < dataSet.size(); j++) {
						if (dataSet.get(j).cluster() == i) {
							totalX += dataSet.get(j).X();
							totalY += dataSet.get(j).Y();
							totalInCluster++;
						}
					}
					if (totalInCluster > 0) {
						centroids.get(i).X(totalX / totalInCluster);
						centroids.get(i).Y(totalY / totalInCluster);
					}
				}

				// Assign all data to the new centroids
				isStillMoving = false;

				for (int i = 0; i < dataSet.size(); i++) {
					Data tempData = dataSet.get(i);
					minimum = bigNumber;
					for (int j = 0; j < NUM_CLUSTERS; j++) {
						distance = euc.calculate(tempData, centroids.get(j));
						if (distance < minimum) {
							minimum = distance;
							cluster = j;
						}
					}
					tempData.cluster(cluster);
					if (tempData.cluster() != cluster) {
						tempData.cluster(cluster);
						isStillMoving = true;
					}
				}

			}
						
		}
	}
	
	
	 public static void plot()
	    {
	        width=600;
	        height=600;
		// Create and set up the window.
		frame = new JFrame("KMeans");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(width,height);
	        frame.setLocation(200,200);
		frame.setLayout(null);

		// Size and display the window.

		insets = frame.getInsets();
		canvas = new DrawingCanvas();
		Dimension size0 = canvas.getPreferredSize();
			
		canvas.setBounds( 25 + insets.left, 25 + insets.top, size0.width , size0.height );
		canvas.setBackground(Color.lightGray);

		JViewport viewport = new JViewport();
		viewport.setView(canvas);
			
	        frame.add("Center", canvas );

		Insets insets = frame.getInsets();
		frame.setSize(width + insets.left + insets.right, height + insets.top + insets.bottom);

		frame.setVisible(true);

	    }
	
	
	public static class DrawingCanvas extends Canvas
	    {
		public DrawingCanvas()
		{
		   CanvasWidth = width - 100;
		   CanvasHeight = height - 100;

		   setSize(CanvasWidth, CanvasHeight); // width and height of canvas
		}

		public void paint(Graphics g)
		{
	           super.paint(g);
	           Graphics2D g2 = (Graphics2D)g;
	           g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	           g2.setFont(new Font("Century Schoolbook", Font.PLAIN, 10));
	   
	           int w = getWidth();
	           int h = getHeight();

	           // get min max for axis
	           float xminval = 0.0f;
	           float yminval = 0.0f;
	           float xmaxval = Float.MIN_VALUE;
	           float ymaxval = Float.MIN_VALUE;

	           // compute extreme maximum values
	           for(int i = 0; i < NUM_CLUSTERS; i++)
	           {
	              for(int j = 0; j < TOTAL_DATA; j++)
	              {
	                 if(dataSet.get(j).cluster() == i)
	                 {
	                    if( (float) dataSet.get(j).X() > xmaxval ) xmaxval = (float) dataSet.get(j).X();
	                    if( (float) dataSet.get(j).Y() > ymaxval ) ymaxval = (float) dataSet.get(j).Y(); 
	                 }
	              } 
	           }
	           // create axis twixce as big as max values
	           xmaxval=2.0f*xmaxval;
	           ymaxval=2.0f*ymaxval;

	 
	           //System.out.println("xmin="+xminval+" xmax="+xmaxval+" ymin="+yminval+" ymax="+ymaxval );	
	           // Draw Y axis.
	           g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
	           // draw tickmark at min/max
	           drawCenteredString(g2, String.valueOf(ymaxval), PAD-10, PAD, (float) 0);
	           drawCenteredString(g2, String.valueOf(yminval), PAD-10, h-PAD, (float) 0);
	           g2.draw(new Line2D.Float(PAD-5, PAD, PAD, PAD ));

	           // Draw X-axis.
	           g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
	           // draw tickmark at min/max
	           drawCenteredString(g2, String.valueOf(xminval), PAD, h-PAD+10, (float) 0);
	           drawCenteredString(g2, String.valueOf(xmaxval), w-PAD, h-PAD+10, (float) 0);
	           g2.draw(new Line2D.Float(w-PAD, h-PAD, w-PAD, h-PAD+5 ));

	           // compute scaling factors 
	           double xScale = (double)(w - 2*PAD)/(xmaxval);
	           double yScale = (double)(h - 2*PAD)/(ymaxval);
	           double scale = xScale;
	           if( yScale < xScale ) scale = yScale;

	           // Mark cluster points.
	           for(int i = 0; i < NUM_CLUSTERS; i++)
	           {
	              switch (i)
	              {
	                case 0: g2.setPaint(Color.red);
	                        break;
	                case 1: g2.setPaint(Color.blue);
	                        break;
	                case 2: g2.setPaint(Color.cyan);
	                        break;
	                case 3: g2.setPaint(Color.orange);
	                        break;
	                case 4: g2.setPaint(Color.green);
	                        break;
	                case 5: g2.setPaint(Color.yellow);
	                        break;
	                case 6: g2.setPaint(Color.white);
	                        break;
			default: g2.setPaint(Color.black);
	              }

	              for(int j = 0; j < TOTAL_DATA; j++)
	              {
	                 if(dataSet.get(j).cluster() == i)
	                 {
	                    float x = PAD + (float) ( dataSet.get(j).X() * scale ); 
	                    float y = h - PAD - (float) ( dataSet.get(j).Y() * scale );
	                    g2.fill(new Ellipse2D.Float(x-2, y-2, 4, 4));
	                 }
	              }
	           }
	          // Mark centroids.
	           for(int i = 0; i < NUM_CLUSTERS; i++)
	           {
	              switch (i)
	              {
	                case 0: g2.setPaint(Color.red);
	                        break;
	                case 1: g2.setPaint(Color.blue);
	                        break;
	                case 2: g2.setPaint(Color.cyan);
	                        break;
	                case 3: g2.setPaint(Color.orange);
	                        break;
	                case 4: g2.setPaint(Color.green);
	                        break;
	                case 5: g2.setPaint(Color.yellow);
	                        break;
	                case 6: g2.setPaint(Color.white);
	                        break;
	                default: g2.setPaint(Color.black);
	              }
	 
	              float x = PAD + (float) ( centroids.get(i).X() * scale ); 
	              float y = h - PAD - (float) ( centroids.get(i).Y() * scale );
	              g2.draw(new Line2D.Float(x-4, y, x+4, y));
	              g2.draw(new Line2D.Float(x, y-4, x, y+4));
	           }

	        }
	    }

	   static void drawCenteredString(Graphics2D g2d, String string,
	                            int x0, int y0, float angle) {
	        FontRenderContext frc = g2d.getFontRenderContext();
	        Rectangle2D bounds = g2d.getFont().getStringBounds(string, frc);
	        LineMetrics metrics = g2d.getFont().getLineMetrics(string, frc);
	        if (angle == 0) {
	            g2d.drawString(string, x0 - (float) bounds.getWidth() / 2,
	                    y0 + metrics.getHeight() / 2);
	        } else {
	            g2d.rotate(angle, x0, y0);
	            g2d.drawString(string, x0 - (float) bounds.getWidth() / 2,
	                    y0 + metrics.getHeight() / 2);
	            g2d.rotate(-angle, x0, y0);
	        }
	    }

	   
	   public static void main(String[] args)
	    {
	        initialize();     
	        kMeans();
	        plot();

        // Print out clustering results.
        for(int i = 0; i < NUM_CLUSTERS; i++)
        {
            System.out.println("Cluster " + i + " includes:");
            for(int j = 0; j < TOTAL_DATA; j++)
            {
                if(dataSet.get(j).cluster() == i){
                    System.out.println("     (" + dataSet.get(j).X() + ", " + dataSet.get(j).Y() + ")");
                }
            } // j
            System.out.println();
        } // i
        
        // Print out centroid results.
        System.out.println("Centroids finalized at:");
        for(int i = 0; i < NUM_CLUSTERS; i++)
        {
            System.out.println("     (" + (float) centroids.get(i).X() + ", " + (float) centroids.get(i).Y()+ ")");
        }
        System.out.print("\n");



	 }
	   
}
