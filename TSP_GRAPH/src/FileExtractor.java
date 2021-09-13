import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class deals with file management. It opens and reads the TSP files.
 * It provides methods to extract the TSP and ATSP data for further operations.
 * @author : Ishanu Dhar (ID: 1222326326, idhar@asu.edu)
 * @author : Pritam De (ID: 1219491988, pritamde@asu.edu)
 */
public class FileExtractor {

    /**
     * This method opens a file from the system and returns it.
     * @return a file from the system
     * @throws IOException
     */
    public File openFile() throws IOException {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = jFileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            return file;
        } else {
            return null;
        }
    }

    /**
     * This method extracts the file data which has been entered from the system.
     * @param file: file from the system
     * @return a list of locations along with the coordinates.
     * @throws IOException
     */
    public List<double[]> fileOperations(File file) throws IOException {
        String fileContent = readFile(file.getAbsolutePath());
        if (!fileContent.contains("ATSP")) {
            fileContent = fileContent.replace("EOF", "");
            int startIndex = fileContent.indexOf("NODE_COORD_SECTION");
            startIndex = fileContent.indexOf("$", startIndex) + 1;
            return extractTspData(fileContent.substring(startIndex));
        } else {
            return extractAtspData(file);
        }
    }

    /**
     * This method extracts TSP data from the file string.
     * @param fileContent: a stream of data read from the file
     * @return a list of locations along with the coordinates.
     */
    public List<double[]> extractTspData(String fileContent) {
        List<double[]> cityCoordinates = new ArrayList<>();
        double[] cityLocation = new double[3];
        int columnTracker = 0;
        String data = "";
        fileContent = fileContent.replaceAll("\\$", " ");
        for (int i = 0; i < fileContent.length(); i++) {
            char character = fileContent.charAt(i);
            if (character != ' ') {
                while (fileContent.charAt(i) != ' ') {
                    data += fileContent.charAt(i);
                    i++;
                }
                cityLocation[columnTracker] = Double.parseDouble(data);
                data = "";
                columnTracker++;
                if (columnTracker == 3) {
                    cityCoordinates.add(cityLocation);
                    cityLocation = new double[3];
                    columnTracker = 0;
                }
            }
        }
        return cityCoordinates;
    }


    /**
     * This method extracts ATSP data from the file string.
     * @param file: file from the system
     * @return a list of locations along with the coordinates.
     */
    public List<double[]> extractAtspData(File file) throws IOException {
        String text = "";
        int lineNumber;
        BufferedReader readBuffer = new BufferedReader(new FileReader(file.getAbsolutePath()));
        for (lineNumber = 1; lineNumber < 8; lineNumber++) {
            if (lineNumber == 4) {
                text = readBuffer.readLine();
            } else readBuffer.readLine();
        }
        String[] str = text.trim().split("\\s+");
        int numRows = Integer.parseInt(str[1]);
        int numColumns = numRows;
        int[][] distanceMatrix = new int[numRows][numColumns];
        int rowCounter = 0;
        int index = 0;
        int maxValue = 0;
        while (!(text = readBuffer.readLine()).equals("EOF")) {
            String[] temp = text.trim().split("\\s+");
            for (int i = 0; i < temp.length; i++) {
                    distanceMatrix[rowCounter][index] = Integer.parseInt(temp[i]);
                    if (Integer.parseInt(temp[i]) > maxValue) {
                        maxValue = Integer.parseInt(temp[i]);
                    }
                    index++;
                    if (index > numColumns - 1) {
                        rowCounter++;
                        index = 0;
                    }
            }
        }
        return getAtspCoordinates(distanceMatrix, numColumns, maxValue);
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

    private List<double[]> getAtspCoordinates(int[][] distanceMatrix, int numColumns,int maxValue) {
        List<double[]> cityCoordinates = new ArrayList<>();
        double[] cityLocation = new double[3];
        Random rand = new Random();
        int upperBound=numColumns-1;
        int k=rand.nextInt(upperBound);
        cityLocation[0] = k+1;
        cityLocation[1] = 1;
        cityLocation[2] = 1;
        cityCoordinates.add(cityLocation);
        for (int i=0;i<=numColumns-1;i++) {
            cityLocation = new double[3];
            if (i!=k) {
                cityLocation[0] = i+1;
                cityLocation[1] = distanceMatrix[k][i];
                cityLocation[2]=  rand.nextInt(maxValue-1);
                cityCoordinates.add(cityLocation);
            }
        }
        return cityCoordinates;
    }

}
