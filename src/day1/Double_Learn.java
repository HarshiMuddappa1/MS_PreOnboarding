package day1;

public class Double_Learn {

    /*
        Double class is a wrapper class for primitive type double.
        It contains several methods to deals with double values.
        It is faster but little error-prone and doesn't provide accurate precision.
    */
    public static void main(String[] args) {
        checkDoubleArithmaticAndOtherMethods();
        checkDoubleWithBits();
        checkDoubleCompareAndEquals();
        checkDoubleToAnotherTypeConverters();
        checkDoubleConverters();
        checkDoubleConstructors();
    }

    private static void checkDoubleArithmaticAndOtherMethods() {

        double d1 = 9.00, d2 = 9.001;

        // 1. sum() -> sum up two double values
        System.out.println("sum: " + Double.sum(d1, d2));

        // 2. min() -> returns min of two double values
        System.out.println("min: " + Double.min(d1, d2));

        // 3. max() -> returns max of two double values
        System.out.println("max: " + Double.max(d1, d2));

        // 4. isNan() -> checks if it's not a number
        System.out.println("isNan: " + Double.isNaN(d1));

        // 5. isInfinite() -> checks if it's infinite
        System.out.println("isInfinite: " + Double.isInfinite(d1));

        // 5. isFinite() -> checks if it's finite
        System.out.println("isFinite: " + Double.isFinite(d1));
    }

    private static void checkDoubleWithBits() {

        double val = 5.9;
        double positiveInfinity = Double.POSITIVE_INFINITY;
        double negativeInfinity = Double.NEGATIVE_INFINITY;
        double nan = Double.NaN;

        // 1. Double.doubleToLongBits() -> converts double to double format bit, returns long.
        System.out.println("Double.doubleToLongBits(): " + Double.doubleToLongBits(val));
        System.out.println("Double.doubleToLongBits(): positiveInfinity:" + Double.doubleToLongBits(positiveInfinity));
        System.out.println("Double.doubleToLongBits(): negativeInfinity:" + Double.doubleToLongBits(negativeInfinity));
        System.out.println("Double.doubleToLongBits(): NaN:" + Double.doubleToLongBits(nan));


        /* 2. Double.doubleToRawLongBits() -> converts double to double format bit, returns long.
            It preserves NaN
         */
        System.out.println("Double.doubleToRawLongBits(): " + Double.doubleToRawLongBits(val));
        System.out.println("Double.doubleToRawLongBits(): positiveInfinity:" + Double.doubleToRawLongBits(positiveInfinity));
        System.out.println("Double.doubleToRawLongBits(): negativeInfinity:" + Double.doubleToRawLongBits(negativeInfinity));
        System.out.println("Double.doubleToRawLongBits(): NaN:" + Double.doubleToRawLongBits(nan));

        // 3. Double.longBitsToDouble() -> converts from long bits to double value
        long longBitsVal = Double.doubleToRawLongBits(val);
        System.out.println("Double.longBitsToDouble(): " + Double.longBitsToDouble(longBitsVal));
    }

    private static void checkDoubleCompareAndEquals() {

        double d1 = 9.00, d2 = 9.001;
        Double c1 = d1, c2 = d2;

        /* 1. Static compare() -> Compares two primitive double values
           returns 1 -> d1>d2, -1 -> d1<d2, 0 -> d1==d2
         */
        System.out.println("Double.compare(): val1: " + d1 + " val2: " + d2 + " res:" + Double.compare(d1, d2));

        /* 2. compareTo() -> Compares two Double object values
           returns 1 -> c1>c2, -1 -> c1<c2, 0 -> c1==c2
           It throws NullPointerException if either c1 or c2 is null
         */
        System.out.println("compareTo(): " + c1.compareTo(c2));

        /* 3. equals() -> checks the equality of the Double objects based on values
            It throws NullPointerException if c1 is null, if c2 is null it will return false
         */
        System.out.println("equals(): " + c1.equals(c2));


    }

    private static void checkDoubleToAnotherTypeConverters() {

        Double val = 127894.6778889987;

        // 1. byteValue() -> convert double to byte
        System.out.println("byteValue(): " + val.byteValue());

        // 2. shortValue() -> convert double to short
        System.out.println("shortValue(): " + val.shortValue());

        // 3. intValue() -> convert double to int
        System.out.println("intValue(): " + val.intValue());

        // 4. longValue() -> convert double to long
        System.out.println("longValue(): " + val.longValue());

        // 5. doubleValue() -> convert double to double (unnecessary unboxing)
        System.out.println("doubleValue(): " + val.doubleValue());

    }

    private static void checkDoubleConverters(){
        double val = 7.1;
        String num = "67";

        // 1. Boxing (from primitive type to object class)
        Double valDouble = val;
        // This is unnecessary boxing -> valDouble = Double.valueOf(val);
        System.out.println("double: " + val + " Double: " + valDouble);

        // 2. Double.valueOf() -> convert String to Double object
        Double numDouble = Double.valueOf(num);
        System.out.println("String num: " + num + " Double.valueOf(): " + numDouble);

        // 3. Double.parseDouble() -> convert String to double primitive
        double numDoublePrim = Double.parseDouble(num);
        System.out.println("String num: " + num + " Double.parseDouble(): " + numDoublePrim);

        // 4. toString() -> returns String representation of an obj val, Override in Double class.
        System.out.println("ToString: " + numDouble.toString());
        System.out.println("ToString: " + Double.toString(numDoublePrim));

        // 5. toHexString() -> returns Hexadecimal rep of the double value
        System.out.println("ToHexString: " + Double.toHexString(numDoublePrim));
    }

    private static void checkDoubleConstructors(){
        // 1.Constructor accepting double value, it's deprecated since java 9
        // Double num = new Double(4.5);

        // 2.Constructor accepting String value, deprecated since java 9, it throws NumberFormatException
        // Double val = new Double("4.5");

        /* use parseDouble to convert a string to double primitive and
            use valueOf to convert string to double object
         */
    }




}
