import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class is specifically designed to read in the csv files
 * that contain the "findings" data on different vulnerabilities.
 */
public class CSVReaderTool {

    // The specific csv file being processed for GAM
    private File csv_file;

    // An array which is essentially just a list of the column names in the csv file.
    private String[] col_names;

    // An array which stores the row names.
    private String[] row_names;

    // Stores the values that are in the csv so that it can be quickly accessed (and potentially mapped).
    // This is basically an "arrayed" version of the csv file minus the first row and first column.
    private int[][] values;

    /**
     * Constructor sets the reader's csv-file-to-be-processed to a specific file.
     *
     * @param file_name File path of csv file
     */
    public CSVReaderTool(String file_name) {
        this.csv_file = new File(file_name);
    }

    /**
     * Get the column names for the csv file.
     *
     * @return String array of column names in the csv.
     */
    public String[] getColNames() {
        return this.col_names;
    }

    /**
     * Sets up the instance arrays that we will be indexing from to create the graphs.
     *
     * @throws FileNotFoundException
     */
    public void setArrays() throws FileNotFoundException {

        // Create column name array
        this.createColNameArray();
        // Create row name array
        this.createRowNameArray();
        // Create values array
        this.createValuesArray();

    }

    /**
     * Creates and populates the column name array for the csv file.
     *
     * @throws FileNotFoundException
     */
    private void createColNameArray() throws FileNotFoundException {
        Scanner scan = new Scanner(this.csv_file);

        String header_line = scan.nextLine();
        String[] header_elements = header_line.split(",");

        // Remove one for the initial empty cell in the csv file.
        int num_of_cols = header_elements.length - 1;

        // Initialize column name array
        this.col_names = new String[num_of_cols];

        // Populate column name array
        for (int i = 1; i < num_of_cols + 1; i++) {
            this.col_names[i-1] = header_elements[i];
        }
    }

    /**
     * Creates and populates the row name array for the csv file.
     *
     * @throws FileNotFoundException
     */
    private void createRowNameArray() throws FileNotFoundException {
        Scanner scan = new Scanner(this.csv_file);

        // Skip the first line which is the header
        scan.nextLine();

        // Find the number of rows in csv file
        int rows = 0;
        while (scan.hasNextLine()) {
            rows++;
            scan.nextLine();
        }

        // Initialize row name array
        this.row_names = new String[rows];

        // Reset scanner so it points to beginning of csv file
        scan = new Scanner(this.csv_file);
        scan.nextLine();

        // Populate row name array
        for (int i = 0; i < rows; i++){
            this.row_names[i] = scan.nextLine().split(",")[0];
        }
    }

    /**
     * Creates and populates the values array for the csv file.
     *
     * @throws FileNotFoundException
     */
    private void createValuesArray() throws FileNotFoundException {
        // Initialize values array
        this.values = new int[this.row_names.length][this.col_names.length];

        Scanner scan = new Scanner(this.csv_file);
        // Skip header line
        scan.nextLine();

        // Populate the values array
        String[] parsed_line;
        for (int i = 0; i < this.row_names.length; i++) {
            parsed_line = scan.nextLine().split(",");
            for (int j = 1; j < this.col_names.length + 1; j++) {
                this.values[i][j-1] = Integer.parseInt(parsed_line[j]);
            }
        }

    }

    /**
     * Extracts all the values in a column and returns them in an array.
     *
     * @param target_col_name The name of the column you want to extract values from.
     *
     * @return Int array of extracted column values for the specified column.
     */
    public int[] extractOneColumnValues(String target_col_name) {
        int[] column_values = new int[this.row_names.length];

        for (int i = 0; i < this.col_names.length; i++) {
            if (this.col_names[i].equals(target_col_name)) {
                for (int j = 0; j < this.row_names.length; j++) {
                    column_values[j] = this.values[j][i];
                }
            }
        }

        return column_values;
    }
}
