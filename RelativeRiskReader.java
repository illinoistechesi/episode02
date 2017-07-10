/*
 * Imports other programs to this program to be used
 */
import java.util.List;         
import java.util.ArrayList;        
import java.util.regex.Pattern;         
import java.io.FileReader;         
import java.io.BufferedReader;          
import java.io.IOException;       

/*
 * This is the name of the ENTIRE file, which is: RelativeRiskReader
 * Note the class name and saved file name MUST be the SAME
 */
public class RelativeRiskReader { 
    
/*
 * This is the main method, this is what runs when you run:
 * java RelativeRiskReader filename
 */
    public static void main(String[] args) {
        
        String filename = args[0];
        
        try { 
            RelativeRiskReader rr = new RelativeRiskReader();
            List<String> lines = readFileLines(filename);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                try {
                    rr.processLine(line);
                } catch(ProgramException e) {
                    System.out.println("Problem with line " + i + ": " + line);
                    e.printStackTrace();
                    System.out.println(" ");
                }
            }
        } catch(Exception e) { 
            System.out.println("Problem with file: " + filename);
            e.printStackTrace();
            System.out.println(" ");
        }
        
    }
    
    public RelativeRiskReader() {
        // Empty Constructor
    }
    
    /*
     * Method that takes in a text and manipulates it
     * Throws exception if problem occurs
     */
    public void processLine(String line) throws ProgramException {
        String comma = Pattern.quote(",");
        String[] data = line.split(comma);
        if (data.length < 5) {
            throw new ProgramException(602, "Unknown Exception.");
        } else if (data.length > 5) {
            throw new ProgramException(603, "Unknown Exception.");
        } else {
            String name = data[0];
            int a = Integer.parseInt(data[1]);
            int b = Integer.parseInt(data[2]);
            int c = Integer.parseInt(data[3]);
            int d = Integer.parseInt(data[4]);
            this.displayRisk(name, a, b, c, d);
        }
    }
    
    /*
     * Shows your output from input (what will be displayed in the terminal)
     */
    public void displayRisk(String name, int a, int b, int c, int d) throws ProgramException {
        System.out.println(name + ": " + String.format("%s, %s, %s, %s", a, b, c, d));
        double risk = calculateRelativeRisk(a, b, c, d);
        double p = calculatePValue(a, b, c, d);
        System.out.println("RR: " + risk);
        System.out.println("p: " + p);
        System.out.println(" ");
    }
    /*
     * Calculates all the math for relative risk
     */
    public double calculateRelativeRisk(int an, int bn, int cn, int dn) throws ProgramException {
        double a = (double) an;
        double b = (double) bn;
        double c = (double) cn;
        double d = (double) dn;
        double numerator = a / (a + b);
        double denominator = c / (c + d);
        double ratio = numerator / denominator;
        if ((a + b) == 0 && (c + d) == 0) {
            throw new ProgramException(604, "Unknown Exception.");
        }
        else if ((a + b) == 0) {
            throw new ProgramException(605, "Unknown Exception.");
        }
        else if ((c + d) == 0) {
            throw new ProgramException(606, "Unknown Exception.");
        }
        else if (denominator == 0) {
            throw new ProgramException(607, "Unknown Exception.");
        }
        return ratio;
    }
    
    public double calculatePValue(int an, int bn, int cn, int dn) {
        double a = (double) an;
        double b = (double) bn;
        double c = (double) cn;
        double d = (double) dn;
        return fishersExactTest(a, b, c, d);
    }
    
    /*
     * Calculation of P-Value equation
     */
    public double fishersExactTest(double a, double b, double c, double d){
        double n = a + b + c + d;
        double num = factorial(a + b) * factorial(c + d) * factorial(a + c) * factorial(b + d);
        double den = factorial(a) * factorial(b) * factorial(c) * factorial(d) * factorial(n);
        double p = num / den;
        return p;
    } 
    
    /*
     * Factorial method use for this method 
     */
    public static double factorial(double n){
        double res = 1;
        while (n > 0) {
            res *= n;
            n--;
        }
        return res;
    }
    
    /*
     * Reads in data from a text file
     * This looks complicated, but you don't need to worry about its specifics
     */
    public static List<String> readFileLines(String filename) throws ProgramException {
        List<String> lines = new ArrayList<String>();
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
        } catch (IOException e) {
            throw new ProgramException(600, "Unknown Exception.");
        }
        try {
            boolean endOfFile = false;
            String line = null;
            while (!endOfFile) {
                line = br.readLine();
                if (line == null) {
                    endOfFile = true;
                } else {
                    lines.add(line);
                }
            }
            br.close();
        } catch (IOException e) {
            throw new ProgramException(601, "Unknown Exception.");
        }
        return lines;
    }

    public static class ProgramException extends Exception {
        
        private int code;
        private String message;
        
        public ProgramException(int code, String message) {
            super(message);
            this.code = code;
            this.message = message;
        }
        
        public int getCode() {
            return this.code;
        }
        
        public String getMessage() {
            return this.message;
        }
        
        @Override
        public String toString() {
            return this.getCode() + ": " + this.getMessage();
        }
        
    }

}