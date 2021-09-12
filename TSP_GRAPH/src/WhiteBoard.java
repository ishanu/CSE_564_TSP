import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WhiteBoard extends JPanel {

    public List<double[]> cityCoordinates;
    public List<City> routes;
    private double maxXCoordinate, maxYCoordinate, minXCoordinate, minYCoordinate;
    private static final int BUFFER_SPACE = 10;

    WhiteBoard() {
        super();
    }

    @Override
    public void paintComponent(Graphics g) {
       /* Graphics2D g2d = (Graphics2D) g;
        int w2 = getWidth() / 2;
        int h2 = getHeight() / 2;
        g2d.rotate(-Math.PI / 2, w2, h2);
*/
        super.paintComponent(g);
        if (cityCoordinates != null) {
            scaleCoordinates();
            cityCoordinates.forEach(n -> {
                g.drawOval((int) n[1],(int) n[2], 3, 3);
            });
        }
        if (routes != null && !routes.isEmpty()) {
            drawRoutes(g);
        }
    }

    private void drawRoutes(Graphics graphics) {
        int cityA = (int) Double.parseDouble(routes.get(0).name);
        int tempCity = cityA;
        int cityB = 0;
        for (int i = 1; i < routes.size(); i++) {
            graphics.setColor(i == 1? Color.red: Color.green);
            cityB = (int) Double.parseDouble(routes.get(i).name);
            drawLine(graphics, cityA,cityB);
            cityA = cityB;
        }
        graphics.setColor(Color.red);
        drawLine(graphics, tempCity, cityA);
    }

    private void drawLine(Graphics g, int cityA, int cityB) {
        double[] a = cityCoordinates.get(cityA - 1);
        double[] b = cityCoordinates.get(cityB - 1);
        g.drawLine((int) a[1], (int) a[2], (int) b[1], (int) b[2]);
    }

    private void scaleCoordinates() {
        final double scaledPanelHeight = this.getHeight() - this.getHeight() * 0.02;
        final double scaledPanelWidth = this.getWidth() - this.getWidth() * 0.006;
        findRange();
        cityCoordinates.forEach(n -> {
            n[1] = ((n[1]-(minXCoordinate-BUFFER_SPACE))/(maxXCoordinate+BUFFER_SPACE-minXCoordinate))*scaledPanelWidth;
            n[2] = ((n[2]-(minYCoordinate-BUFFER_SPACE))/(maxYCoordinate+BUFFER_SPACE-minYCoordinate))*scaledPanelHeight;
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
