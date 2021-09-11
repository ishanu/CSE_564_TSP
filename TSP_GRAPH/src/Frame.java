import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class Frame extends JFrame implements ActionListener {
    WhiteBoard whiteBoard;

    Frame(String name) {
        super(name);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        whiteBoard = new WhiteBoard();
        add(whiteBoard, BorderLayout.CENTER);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int)screenSize.getWidth()-((int)(0.1*screenSize.getWidth())), (int)screenSize.getHeight()-((int)(0.1*screenSize.getHeight())));
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
        openFileButton.setPreferredSize(new Dimension(120, 50));
        drawGraphButton.setPreferredSize(new Dimension(120, 50));
        openFileButton.addActionListener(this);
        drawGraphButton.addActionListener(this);
        buttonArea.add(openFileButton);
        buttonArea.add(drawGraphButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            if(((JButton)e.getSource()).getText().equals("Open File")) {
                FileOpener fileOpener = new FileOpener();
                try {
                   List<double[]> cityCoordinates = fileOpener.openFile();
                   if(!cityCoordinates.isEmpty()) {
                       whiteBoard.cityCoordinates = cityCoordinates;
                       whiteBoard.repaint();
                       System.out.println("file parsed: "+ cityCoordinates.size());
                   }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {

            }
    }
}
