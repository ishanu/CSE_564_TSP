import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Frame extends JFrame implements ActionListener {
    WhiteBoard whiteBoard;
    JLabel label = null;

    Frame(String name) {
        super(name);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        whiteBoard = new WhiteBoard();
        add(whiteBoard, BorderLayout.CENTER);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int)screenSize.getHeight()-((int)(0.1*screenSize.getHeight())), (int)screenSize.getHeight()-((int)(0.1*screenSize.getHeight())));
        int x = (int) ((screenSize.getWidth() - this.getWidth()) / 2);
        int y = (int) ((screenSize.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        configureButtonArea();
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void configureButtonArea() {
        JPanel buttonArea = new JPanel();
        buttonArea.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonArea.setBackground(Color.GRAY);
        add(buttonArea,BorderLayout.SOUTH);
        JButton openFileButton = new JButton("Open File");
        JButton drawGraphButton = new JButton("Draw Graph");
        this.label = new JLabel("Loading");
        this.label.setVisible(false);
        openFileButton.setPreferredSize(new Dimension(120, 50));
        drawGraphButton.setPreferredSize(new Dimension(120, 50));
        openFileButton.addActionListener(this);
        drawGraphButton.addActionListener(this);
        buttonArea.add(label);
        buttonArea.add(openFileButton);
        buttonArea.add(drawGraphButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            if(((JButton)e.getSource()).getText().equals("Open File")) {
                drawCities();
            } else {
                drawGraph();
            }
    }

    private void drawGraph() {
        TSPAlgorithm tspAlgorithm = new TSPAlgorithm(whiteBoard.cityCoordinates);
        List<City> routes = tspAlgorithm.findRoute();
        if(!routes.isEmpty()) {
            whiteBoard.routes = routes;
            whiteBoard.repaint();
        }
    }

    private void drawCities() {
        whiteBoard.cityCoordinates = null;
        whiteBoard.routes = null;
        whiteBoard.repaint();
        FileManager fileManager = new FileManager();
        try {
            File file = fileManager.openFile();
            if(file == null)
                return;
            this.label.setVisible(true);
            List<double[]> cityCoordinates = fileManager.fileOperations(file);
            if(!cityCoordinates.isEmpty()) {
                whiteBoard.cityCoordinates = cityCoordinates;
                whiteBoard.repaint();
                System.out.println("file parsed: "+ cityCoordinates.size());
                this.label.setVisible(false);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

