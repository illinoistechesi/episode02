
public class RelativeRiskReader {

    public static void main(String[] args) {
        
        int[] data = {0, 0, 0, 0};
        for (int i = 0; i < 4; i++) {
            data[i] = Integer.parseInt(args[i]);
        }
        
        RelativeRiskReader rr = new RelativeRiskReader();
        
        try {
            rr.displayRisk(data[0], data[1], data[2], data[3]);
        } catch (ProgramException e) {
            e.printStackTrace();
            System.out.println("Code: " + e.getCode());
            switch (e.getCode()) {
                case 600:
                    System.out.println("This is a 600 exception.");
                    break;
                default:
                    System.out.println("This is some other exception.");
            }
        }
        
    }
    
    public RelativeRiskReader() {
        
    }
    
    public void displayRisk(int a, int b, int c, int d) throws ProgramException {
        double risk = calculateRelativeRisk(a, b, c, d);
        double p = calculatePValue(a, b, c, d);
        System.out.println("Input: " + String.format("%s, %s, %s, %s", a, b, c, d));
        System.out.println("RR: " + risk);
        System.out.println("p: " + p);
    }
    
    public double calculateRelativeRisk(int an, int bn, int cn, int dn) throws ProgramException {
        double a = (double) an;
        double b = (double) bn;
        double c = (double) cn;
        double d = (double) dn;
        double numerator = a / (a + b);
        double denominator = c / (c + d);
        double ratio = numerator / denominator;
        if ((a + b) == 0 && (c + d) == 0) {
            throw new ProgramException(600, "Unknown Exception.");
        }
        else if ((a + b) == 0) {
            throw new ProgramException(601, "Unknown Exception.");
        }
        else if ((c + d) == 0) {
            throw new ProgramException(602, "Unknown Exception.");
        }
        else if (denominator == 0) {
            throw new ProgramException(603, "Unknown Exception.");
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
    
    public double fishersExactTest(double a, double b, double c, double d){
        double n = a + b + c + d;
        double num = factorial(a + b) * factorial(c + d) * factorial(a + c) * factorial(b + d);
        double den = factorial(a) * factorial(b) * factorial(c) * factorial(d) * factorial(n);
        double p = num / den;
        return p;
    }
    
    public static double factorial(double n){
        double res = 1;
        while (n > 0) {
            res *= n;
            n--;
        }
        return res;
    }
    
    public static void readFileData(String filename) throws ProgramException {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
        } catch (IOException e) {
            throw new ProgramException(700, "Unknown Exception.");
        }
        try {
            boolean endOfFile = false;
            String line = null;
            while (!endOfFile) {
                line = br.readLine();
                if (line == null) {
                    endOfFile = true;
                } else {
                    String comma = Pattern.quote(",");
                    String[] data = line.split(comma);
                    if (data.length !== 5) {
                        throw new ProgramException(702, "Unknown Exception.");
                    }
                }
            }
        } catch (IOException e) {
            throw new ProgramException(701, "Unknown Exception.");
        }
    }

    public static class ProgramException extends Exception {
        
        private int code;
        
        public ProgramException(int code, String message) {
            super(message);
            this.code = code;
        }
        
        public int getCode() {
            return this.code;
        }
        
    }

}