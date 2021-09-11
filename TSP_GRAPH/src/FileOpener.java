import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileOpener {


    public List<double[]> openFile() throws IOException {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = jFileChooser.showOpenDialog(null);
        if(result  == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            return fileOperations(file);
        } else {
            return Collections.emptyList();
        }
    }

    private String readFile(String path) throws IOException {
        File file = new File(path);
        BufferedReader br = null;
        String fileContent = "";
        try {
            br = new BufferedReader(new FileReader(file));
            String str;
            while ((str = br.readLine()) != null) {
                fileContent = fileContent + str + "$";
            }
        } finally {
            br.close();
            return fileContent;
        }
    }

    private List<double[]> fileOperations(File file) throws IOException {
       String fileContent = readFile(file.getAbsolutePath());
        fileContent = fileContent.replace("EOF", "");
       if(fileContent.contains("TSP")) {
           int startIndex = fileContent.indexOf("NODE_COORD_SECTION");
           startIndex = fileContent.indexOf("$", startIndex) + 1;
          return extractTspData(fileContent.substring(startIndex));
       } else {
          return extractAtspData(fileContent);
       }
    }

    private List<double[]> extractTspData(String fileContent) {
        List<double[]> cityCoordinates = new ArrayList<>();
        double[] cityLocation = new double[3];
        int columnTracker = 0;
        String data ="";
        fileContent = fileContent.replaceAll("\\$", " ");
        for (int i = 0; i < fileContent.length(); i++) {
            char character = fileContent.charAt(i);
            if (character != ' ') {
                while (fileContent.charAt(i) != ' ') {
                    data += fileContent.charAt(i);
                    i++;
                }
                cityLocation[columnTracker] =  Double.parseDouble(data);
                data = "";
                columnTracker ++;
                if(columnTracker == 3) {
                    cityCoordinates.add(cityLocation);
                    cityLocation = new double[3];
                    columnTracker = 0;
                }
            }
        }
        return cityCoordinates;
    }

    private List<double[]> extractAtspData(String fileContent) {
    return null;
    }
}
