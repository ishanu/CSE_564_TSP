import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WhiteBoard extends JPanel {

    public List<double[]> cityCoordinates;
    private double maxXCoordinate, maxYCoordinate, minXCoordinate, minYCoordinate;
    private final int BUFFER_SPACE = 10;

    WhiteBoard() {
        super();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(cityCoordinates != null) {
            scaleCoordinates();
            cityCoordinates.forEach(n-> {
                g.drawOval((int)n[1],(int)n[2], 3, 3);

            });
           // cityCoordinates = null;
        }
    }

    private void scaleCoordinates() {
        final double scaledPanelHeight = this.getHeight() - this.getHeight() * 0.02;
        final double scaledPanelWidth = this.getWidth() - this.getWidth() * 0.006;
        findRange();
        cityCoordinates.forEach(n -> {
            n[1] = ((n[1]  - (minXCoordinate-BUFFER_SPACE))/(maxXCoordinate + BUFFER_SPACE - minXCoordinate)) * scaledPanelWidth;
            n[2] = ((n[2]  - (minYCoordinate-BUFFER_SPACE))/(maxYCoordinate + BUFFER_SPACE - minYCoordinate)) * scaledPanelHeight;
        });
    }




    private void findRange() {
        maxXCoordinate = minXCoordinate = cityCoordinates.get(0)[1];
        maxYCoordinate = minYCoordinate = cityCoordinates.get(0)[2];
        for (double[] point : cityCoordinates) {
            if (point[1] < minXCoordinate)
                minXCoordinate = point[1];
            if (point[1] > maxXCoordinate)
                maxXCoordinate = point[1];
            if (point[2] < minYCoordinate)
                minYCoordinate = point[2];
            if (point[2] > maxYCoordinate)
                maxYCoordinate = point[2];
        }
    }
}
