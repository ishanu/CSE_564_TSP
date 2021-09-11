import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {


    public File openFile() throws IOException {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = jFileChooser.showOpenDialog(null);
        if(result  == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            return file;
        } else {
            return null;
        }
    }

    public List<double[]> fileOperations(File file) throws IOException {
       String fileContent = readFile(file.getAbsolutePath());
       if(fileContent != null) {
           fileContent = fileContent.replace("EOF", "");
           if (fileContent.contains("TSP")) {
               int startIndex = fileContent.indexOf("NODE_COORD_SECTION");
               startIndex = fileContent.indexOf("$", startIndex) + 1;
               return extractTspData(fileContent.substring(startIndex));
           } else {
               return null;// return extractAtspData(fileContent);
           }
       }
       return null;
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

    /*
    private List<double[]> extractAtspData(String fileContent) {
        String text="";
        int lineNumber;
        FileReader readFile = new FileReader("C:\\Users\\depri\\OneDrive\\Documents\\Fall Sem 2021\\CSE 564\\Asignment2\\Test\\atsp_17.txt");
        BufferedReader readBuffer = new BufferedReader(readFile);

        for (lineNumber =1;lineNumber<8;lineNumber++) {
            if(lineNumber==4) {
                text=readBuffer.readLine();
            }
            else readBuffer.readLine();
        }
        String[] str= text.trim().split("\\s+");


        int numRows=Integer.parseInt(str[1]);
        System.out.println("Num of Rows and Columns"+ numRows);

        int numColumns = numRows;

        int[][] array2D= new int[numRows][numColumns];
        int rowCounter=0;
        int index=0;

        while(!(text=readBuffer.readLine()).equals("EOF")) {

            String[] temp = text.trim().split("\\s+");
            for (int i=0;i<temp.length;i++) {
                array2D[rowCounter][index]=Integer.parseInt(temp[i]);
                index++;
                if (index>numColumns-1) {
                    rowCounter++;
                    index=0;
                }

            }

        }

        for (int i=0;i<numRows;i++) {
            for (int j=0;j<numColumns;j++) {
                if (i==j)
                    System.out.println(array2D[i][j]);
            }
        }
    }*/
}
