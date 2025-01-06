package day1;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Assignment {

    public static void main(String[] args) {

        double doubleVal = 12345.1234567;
        String strVal = "12345.1234567";
        BigDecimal obj1 = new BigDecimal("12345.12345");
        BigDecimal obj2 = new BigDecimal("12345.12346");

        // RoundingMode.DOWN, scale = 5
        BigDecimal obj = BigDecimal.valueOf(doubleVal);
        obj = obj.setScale(5, RoundingMode.DOWN);
        System.out.println("RoundingMode.DOWN, scale = 5: " + obj);
        System.out.println("compareTo: " + obj.compareTo(obj1));

        // RoundingMode.UP, precision = 10
        MathContext mc = new MathContext(10, RoundingMode.UP);
        obj = new BigDecimal(strVal, mc);
        System.out.println("RoundingMode.UP, precision = 10: " + obj);
        System.out.println("equals: " + obj.equals(obj2));
    }
}
