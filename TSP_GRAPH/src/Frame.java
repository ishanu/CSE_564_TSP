import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Frame extends JFrame implements ActionListener {
    WhiteBoard whiteBoard;
    JButton openFileButton;
    JButton drawGraphButton;

    Frame(String name) {
        super(name);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        whiteBoard = new WhiteBoard();
        add(whiteBoard, BorderLayout.CENTER);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) screenSize.getHeight() - ((int) (0.1 * screenSize.getHeight())), (int) screenSize.getHeight() - ((int) (0.1 * screenSize.getHeight())));
        int x = (int) ((screenSize.getWidth() - this.getWidth()) / 2);
        int y = (int) ((screenSize.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        configureButtonArea();
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void configureButtonArea() {
        JPanel buttonArea = new JPanel();
        buttonArea.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonArea.setBackground(Color.GRAY);
        add(buttonArea, BorderLayout.SOUTH);
        this.openFileButton = new JButton("Open File");
        this.drawGraphButton = new JButton("Draw Graph");
        this.openFileButton.setPreferredSize(new Dimension(120, 50));
        this.drawGraphButton.setPreferredSize(new Dimension(120, 50));
        this.openFileButton.addActionListener(this);
        this.drawGraphButton.addActionListener(this);
        buttonArea.add(this.openFileButton);
        buttonArea.add(this.drawGraphButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (((JButton) e.getSource()).getText().equals("Open File")) {
            drawCities(((JButton) e.getSource()));
        } else {
            drawGraph();
        }
    }

    private void drawGraph() {
        TSPAlgorithm tspAlgorithm = new TSPAlgorithm(whiteBoard.cityCoordinates);
        List<City> routes = tspAlgorithm.findRoute();
        if (!routes.isEmpty()) {
            whiteBoard.routes = routes;
            whiteBoard.repaint();
        }
    }

    private void drawCities(JButton button) {
        button.setEnabled(false);
        button.setText("Loading ..");
        whiteBoard.cityCoordinates = null;
        whiteBoard.routes = null;
        whiteBoard.repaint();
        FileExtractor fileExtractor = new FileExtractor();
        try {
            File file = fileExtractor.openFile();
            if (file != null) {
                List<double[]> cityCoordinates = fileExtractor.fileOperations(file);
                if (!cityCoordinates.isEmpty()) {
                    whiteBoard.cityCoordinates = cityCoordinates;
                    whiteBoard.repaint();
                    System.out.println("file parsed: " + cityCoordinates.size());
                }
            }
            button.setEnabled(true);
            button.setText("Open File");
            this.drawGraphButton.setEnabled(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

