//<--------------This double slashes are used to write what ever you want
//               in the code without affecting everything (only comments one line)


/* <--- alternative way to comment (mostly used) (comments multiple lines)
*/ 

import java.util.List;//            
import java.util.ArrayList;//           
import java.util.regex.Pattern;//     all of these import are like copyrights       
import java.io.FileReader;//          you need to use certain commands
import java.io.BufferedReader;//            
import java.io.IOException;//           

public class RelativeRiskReader { //This is the name of the ENTIRE file, which is :RelativeRiskReader
                                  //Note the class name and saved file name MUST be the SAME

    public static void main(String[] args) { //<----in every java code you gotta have a main, this is the exact way you want it for basics
        
        String filename = args[0]; // This allows your TERMINAL to send information into this program
        
        try { // Try, basically program TRIES to run the code below if not it will catch an exception. 
              //Similar to an if else but different
            RelativeRiskReader rr = new RelativeRiskReader();
            List<String> lines = readFileLines(filename);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                try {
                    rr.processLine(line);
                } catch(ProgramException e) {//Catches a problem in the code and sends a message
                                             //Try it out go to "data.txt" and type anything you will get
                    System.out.println("Problem with line " + i + ": " + line);//<----this message from the program
                    e.printStackTrace();//method caller, no need to understand yet
                    System.out.println(" ");
                }
            }
        } catch(Exception e) { 
            System.out.println("Problem with file: " + filename);
            e.printStackTrace();
            System.out.println(" ");
        }
        
    }
    
    public RelativeRiskReader() {//dont need to know
        // Empty Constructor
    }
    
    /*
     *
     */
    public void processLine(String line) throws ProgramException {//Method that takes in a text and manipulates it
                                                                  //throws exception if problem occurs
        String comma = Pattern.quote(",");//program scans YOUR INPUT in data.txt to make sure you have commas
        String[] data = line.split(comma);//The line of code that will SCAN through
        if (data.length < 5) {//if your input is less than 5 info give you exception (will not give correct output)
            throw new ProgramException(602, "Unknown Exception.");
        } else if (data.length > 5) {//if your input is more than 5 info give you exception (will not give correct output)
            throw new ProgramException(603, "Unknown Exception.");
        } else {
            String name = data[0];
            //Parsing changes datatypes,
            //In this example: words---[parsing]--->numbers
            int a = Integer.parseInt(data[1]);
            int b = Integer.parseInt(data[2]);
            int c = Integer.parseInt(data[3]);
            int d = Integer.parseInt(data[4]);
            this.displayRisk(name, a, b, c, d);
        }
    }
    
    public void displayRisk(String name, int a, int b, int c, int d) throws ProgramException {//shows your output from input(what will show you in the terminal)
        System.out.println(name + ": " + String.format("%s, %s, %s, %s", a, b, c, d));
        double risk = calculateRelativeRisk(a, b, c, d);
        double p = calculatePValue(a, b, c, d);
        System.out.println("RR: " + risk);
        System.out.println("p: " + p);
        System.out.println(" ");
    }
    
    public double calculateRelativeRisk(int an, int bn, int cn, int dn) throws ProgramException {//Calculates all the math
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
    
    public double fishersExactTest(double a, double b, double c, double d){//calculation of P-Value equation
        double n = a + b + c + d;
        double num = factorial(a + b) * factorial(c + d) * factorial(a + c) * factorial(b + d);
        double den = factorial(a) * factorial(b) * factorial(c) * factorial(d) * factorial(n);
        double p = num / den;
        return p;
    }   /*
                                                                                    ^
                                                                                    |
                                                                                    |
                                                                                    |    */
    public static double factorial(double n){//factorial method use for this method |
        double res = 1;
        while (n > 0) {
            res *= n;
            n--;
        }
        return res;
    }
    
    //Everything below is something you dont need to know right now
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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