package resistorcolorcode;

/**
 *
 * @author rcbgalido
 */
public class Calculator {

    private static final int VALUE_OF_K = 1000;
    private static final int VALUE_OF_M = 1000000;

    public int[] getResistorColorCodes(String input) {

        if (isValidInput(input) == false) {
            return null;
        }
        
        int multiplier = 1;
        if (input.endsWith("k") || input.endsWith("K")) {
            multiplier = VALUE_OF_K;
            input = input.substring(0, input.length() - 1);
        }
        if (input.endsWith("m") || input.endsWith("M")) {
            multiplier = VALUE_OF_M;
            input = input.substring(0, input.length() - 1);
        }
        
        float resistorValueFloat; // float is used in order to support large resistor values
        try {
            resistorValueFloat = Float.parseFloat(input) * multiplier;
        } catch (NumberFormatException ex) {
            return null;
        }

        String resistorValue= Math.round(resistorValueFloat) + "";
        int trailingZerosCount = getTrailingZerosCount(Math.round(resistorValueFloat));
        
        String firstAndSecondBand = resistorValue.substring(0, resistorValue.length() - trailingZerosCount);
        int firstAndSecondBandLength = firstAndSecondBand.length();
        if (firstAndSecondBandLength == 1) {
            firstAndSecondBand += "0";
            trailingZerosCount--;
        } else if (firstAndSecondBandLength > 2) {
            return null;
        }

        int firstBand = Integer.parseInt("" + firstAndSecondBand.charAt(0));
        int secondBand = Integer.parseInt("" + firstAndSecondBand.charAt(1));
        int thirdBand = trailingZerosCount;

        // third band does not have "Gray" and "White"
        if (thirdBand > 7) {
            return null;
        }

        return new int[]{firstBand, secondBand, thirdBand};
    }
    
    public boolean isValidInput(String input) {
        
        // (1) check if input is not empty
        if (input.equals("")) {
            return false;
        }
        
        // (2) check if input does not start with "0"
        if (input.startsWith("0")) {
            return false;
        }
        
        // (3) check if input does not start with a negative sign (-)
        if (input.startsWith("-")) {
            return false;
        }
        
        int inputLength = input.length();
        
        // (4) check if input length is not less than two (2)
        // least valid resistor value is ten (10)
        if (inputLength < 2) {
            return false;
        }
        
        // (5) check if input does not end with a "f" or "F"
        // treated as float if not trapped
        if (input.endsWith("f") || input.endsWith("F")) {
            return false;
        }
        
        // (6) check if input does not end with a decimal point (.)
        if (input.endsWith(".")) {
            return false;
        }
        
        int nonZeroDigitsCount = 0; // non-zero digits: 1-9
        int decimalPointsCount = 0;
        for (int x = 0; x < inputLength; x++) {
            if (Character.isDigit(input.charAt(x)) && input.charAt(x) != '0') {
                // (7) check if input does not have more than two (2) non-zero digits
                if (++nonZeroDigitsCount > 2) {
                    return false;
                }
            }
            if (input.charAt(x) == '.') {
                // (8) check if input does not have more than one (1) decimal point
                if (++decimalPointsCount > 1){
                    return false;
                }
            }
        }

        // (9) check if input has at least one (1) non-zero digit
        if (nonZeroDigitsCount == 0) {
            return false;
        }
        
        if (decimalPointsCount == 1) { // input has a decimal point
            
            // (10) check if input ends with a correct suffix
            if (!input.endsWith("k") && !input.endsWith("K") && !input.endsWith("m") && !input.endsWith("M")) {
                return false;
            }
            
            int decimalPointIndex = input.indexOf('.');
            
            // (11) check if digit after decimal point is not zero (0)
            if (input.charAt(decimalPointIndex + 1) == '0') {
                return false;
            }
            
            String[] inputSplitByDecimalPoint = input.split("\\.");
            int beforeDecimalPointLength = inputSplitByDecimalPoint[0].length();
            int afterDecimalPointLength = inputSplitByDecimalPoint[1].length();
            
            // (12) check if there is exactly one (1) character before the decimal point
            if (beforeDecimalPointLength != 1) {
                return false;
            }
            
            // (13) check if there is exactly two (2) characters (including the suffix) after the decimal point
            if (afterDecimalPointLength != 2){
                return false;
            }
            
        }
        
        return true;
    }

    public String getResistorValue(int firstBand, int secondBand, int thirdBand) {
        firstBand++;    // first band does not have "Black"
        int multiplier = (int) Math.pow(10, thirdBand);
        String resistorValue = "" + ((firstBand * 10) + secondBand) * multiplier;
        return simplifyResistorValue(resistorValue);
    }

    public String simplifyResistorValue(String resistorValue) {
        if (resistorValue.endsWith("K")) {
            resistorValue = resistorValue.substring(0, resistorValue.length() - 1) + "k";
        }

        if (resistorValue.endsWith("m")) {
            resistorValue = resistorValue.substring(0, resistorValue.length() - 1) + "M";
        }

        if (resistorValue.endsWith("000")) {
            resistorValue = resistorValue.substring(0, resistorValue.length() - 3) + "k";
        }

        if (resistorValue.endsWith("000k")) {
            resistorValue = resistorValue.substring(0, resistorValue.length() - 4) + "M";
        }

        if (resistorValue.endsWith("00k") && resistorValue.charAt(1) != '0') {
            resistorValue = resistorValue.charAt(0) + "." + resistorValue.charAt(1) + "M";
        }

        if (resistorValue.endsWith("00") && resistorValue.charAt(1) != '0') {
            resistorValue = resistorValue.charAt(0) + "." + resistorValue.charAt(1) + "k";
        }

        return resistorValue;

    }
    
    public int getTrailingZerosCount(int num) {
        int count = 0;
        while (num % 10 == 0 && num != 0) {
            count++;
            num /= 10;
        }
        return count;
    }
}
