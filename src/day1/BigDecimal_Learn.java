package day1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimal_Learn {

    /*
        It provides operations on double for arithmetic, scale handling, rounding, etc.
        It is little slower, it provides accurate precision for any operations.
        It consists of arbitrary precision integer unscaled value and 32-bit integer scale.
        If Zero or +ve -> scale is the number of digits after the decimal point
        If Negative -> value of unscaled is (unscaled value * 10^-scale)

        MathContext Class:
            -> used to set precision(number of digits used) and RoundingMode
            -> MathContext.Decimal128 -> 34 digits precision and HALF_EVEN rounding mode by default.
            -> MathContext.Decimal64 -> 16 digits precision and HALF_EVEN rounding mode by default.
            -> MathContext.Decimal32 -> 7 digits precision and HALF_EVEN rounding mode by default.
            -> MathContext.UNLIMITED -> no limit on digits' precision.
            Constructors:
                -> MathContext(int precision) -> sets the precision with HALF_UP rounding mode.
                -> MathContext(int precision, RoundingMode) -> sets precision with RoundingMode specified.
                -> MathContext(String val) -> constructs MathContext from String.

        RoundingMode:
            -> RoundingMode.ROUND_CEILING -> round towards positive Infinity.
            -> RoundingMode.ROUND_FLOOR -> round towards negative infinity.
            -> RoundingMode.ROUND_UP -> round away from zero.
            -> RoundingMode.ROUND_DOWN -> round towards zero.
            -> RoundingMode.ROUND_HALF_UP -> round towards nearest neighbour and if both are same round up.
            -> RoundingMode.ROUND_HALF_DOWN -> round towards nearest neighbour and if both are same round down.
            -> RoundingMode.ROUND_HALF_EVEN -> round towards nearest neighbour and if both are same round towards even.
            -> RoundingMode.ROUND_UNNECESSARY -> no rounding necessary.
     */

    public static void main(String[] args) {
        checkBigDecimalOtherMethods();
        checkBigDecimalCompareMethods();
        checkBigDecimalConverters();
        checkBigDecimalArithmeticOperations();
        checkBigDecimalConstructorsWithRoundingModeAndPrecision();
    }

    private static void checkBigDecimalOtherMethods() {

        // 1. movePointLeft(n) -> returns BigDecimal with n points moves left
        BigDecimal val1 = new BigDecimal("3865.678");
        System.out.println("movePointLeft(): " + val1.movePointLeft(2));

        // 2. movePointRight(n) -> returns BigDecimal with n points moves right
        System.out.println("movePointRight(): " + val1.movePointRight(2));

        // 3. hashcode() -> returns hashcode
        System.out.println("hashcode(): " + val1.hashCode());
    }

    private static void checkBigDecimalCompareMethods() {

        // 1. compareTo -> compare two objects, returns 1: val1>val2, 0: val1=val2, val1<val2
        BigDecimal val1 = new BigDecimal("3865.00");
        BigDecimal val2 = new BigDecimal("3865.000");
        System.out.println("compareTo(): " + val1.compareTo(val2));

        // 2.equals -> check equality between two objects (6.0 != 6.00)
        System.out.println("equals(): " + val1.equals(val2));
    }

    private static void checkBigDecimalConverters() {

        BigDecimal val1 = new BigDecimal("123");
        BigDecimal val2 = new BigDecimal("89878.908");

        /* 1.byteValueExact() -> converts BigDecimal to byte,
            return Rounding necessary ArithmeticException if fractional part is non-zero,
            and if it overflows.
         */
        System.out.println("byteValueExact(): " + val1.byteValueExact());

        // 2. doubleValue() -> converts BigDecimal to double.
        System.out.println("doubleValue(): " + val2.doubleValue());

        // 3. floatValue() -> converts BigDecimal to float.
        System.out.println("floatValue(): " + val2.floatValue());

        // 4. intValue() -> converts BigDecimal to int.
        System.out.println("intValue(): " + val2.intValue());

        // 5. intValueExact() -> converts BigDecimal to int, returns ArithmeticException if overflows.
        System.out.println("intValueExact(): " + val1.intValueExact());

        // 6. longValue() -> converts BigDecimal to long.
        System.out.println("longValue(): " + val2.longValue());

        // 7. longValueExact() -> converts BigDecimal to long, returns ArithmeticException if overflows.
        System.out.println("longValueExact(): " + val1.longValueExact());

        // 8. toBigInteger() -> converts BigDecimal to BigInteger.
        System.out.println("toBigInteger(): " + val2.toBigInteger());

        // 9. toBigIntegerExact() -> converts BigDecimal to BigInteger, returns ArithmeticException if overflows.
        System.out.println("toBigIntegerExact(): " + val1.toBigIntegerExact());

        // 10. toEngineeringString() -> converts to string using engineering notation if exponent is needed.
        System.out.println("toEngineeringString(): " + val2.toEngineeringString());

        // 11. toPlainString() -> converts to string and no exponent is given.
        System.out.println("toPlainString(): " + val2.toPlainString());

        // 12. toString() -> converts to string using scientific notation if exponent is needed.
        System.out.println("toString(): " + val2.toString());

        /* 13. valueOf(double) -> converts double to BigDecimal
               valueOf(long unscaledVal) -> converts long to BigDecimal with scale as 0
               valueOf(long unscaledVal, int scale) -> converts long to BigDecimal with scale as specified.
         */
        BigDecimal obj = BigDecimal.valueOf(4.678);
        System.out.println("valueOf(double): " + obj);
        obj = BigDecimal.valueOf(7894345);
        System.out.println("valueOf(long): " + obj);
        obj = BigDecimal.valueOf(7894345, 2);
        System.out.println("valueOf(long, scale): " + obj);

    }

    private static void checkBigDecimalArithmeticOperations() {

        BigDecimal val1 = new BigDecimal("-59878.67098");
        BigDecimal val2 = new BigDecimal("89878.908");
        MathContext mc = new MathContext(3, RoundingMode.HALF_UP);

        /* 1. abs() -> returns absolute value of this BigDecimal and scale as this.scale.
            abs(MathContext) -> returns absolute value of this BigDecimal and scale/rounding as per MathContext.
         */
        System.out.println("abs(): " + val1.abs());
        System.out.println("abs() with MathContext: " + val2.abs(mc));

        /* 2. add(val) -> returns value of this + val and scale as max(this.scale, val.scale).
            add(val, MathContext) -> returns value of this + val and scale/rounding as per MathContext.
         */
        System.out.println("add(BigDecimal): " + val1.add(val2));
        System.out.println("add(BigDecimal) with MathContext: " + val1.add(val2, mc));

        /* 3. divide(val) -> return this/val, scale as this.scale - val.scale
            -> returns ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result.
            divide(val, int roundingMode/RoundingMode) -> return this/val, scale is this.scale
            divide(val, scale, int roundingMode/RoundingMode) -> return this/val, scale as mentioned
            divide(val, MathContext) -> return this/val, rounding according to MathContext
         */
        //System.out.println("divide(BigDecimal): " + val2.divide(val1));
        System.out.println("divide(BigDecimal) with roundingMode: " + val1.divide(val2, RoundingMode.HALF_EVEN));
        System.out.println("divide(BigDecimal) with scale and roundingMode: " + val1.divide(val2, 2, RoundingMode.HALF_EVEN));
        System.out.println("divide(BigDecimal) with MathContext: " + val1.divide(val2, mc));

        /* 4.divideAndRemainder(val) -> returns BigDecimal[] result of divideToIntegralValue followed by the result of remainder on the two operands.
            divideAndRemainder(val, MathContext) -> returns BigDecimal[] result of divideToIntegralValue followed by the result of remainder on the two operands.
         */
        BigDecimal[] arr = val1.divideAndRemainder(val2);
        System.out.println("divideAndRemainder(BigDecimal) - quotient: " + arr[0] + " rem: " + arr[1]);
        arr = val2.divideAndRemainder(val1, mc);
        System.out.println("divideAndRemainder(BigDecimal) with MathContext - quotient: " + arr[0] + " rem: " + arr[1]);

        /* 5.divideToIntegerValue(val) -> returns BigDecimal whose value is the Integer part of the quotient rounded down.
            divideToIntegerValue(val, MathContext) -> returns BigDecimal whose value is the Integer part of the quotient & rounded acc to MC.
         */
        System.out.println("divideToIntegerValue(BigDecimal): " + val2.divideToIntegralValue(val1));
        System.out.println("divideToIntegerValue(BigDecimal) with MathContext: " + val2.divideToIntegralValue(val1, mc));

        // 6. max(val) -> return max(this, val)
        System.out.println("max(BigDecimal): " + val1.max(val2));

        // 7. min(val) -> return min(this, val)
        System.out.println("min(BigDecimal): " + val2.min(val1));

        /* 8. multiply(val) -> returns this * val, scale as this.scale + val.scale
            multiply(val, MathContext) -> returns this * val, rounding acc to MathContext
         */
        System.out.println("multiply(BigDecimal): " + val2.multiply(val1));
        System.out.println("multiply(BigDecimal) with MathContext: " + val2.multiply(val1, mc));

        /* 9. negate() -> returns (-this) and scale as this.scale
            negate() -> returns (-this) and rounding acc to MathContext
         */
        System.out.println("negate(): " + val2.negate());
        System.out.println("negate() with MathContext: " + val1.negate(mc));

        /* 10. plus() -> returns (+this) and scale as this.scale
            plus(MathContext) -> returns (+this) and rounding acc to MathContext
         */
        System.out.println("plus(): " + val2.plus());
        System.out.println("plus() with MathContext: " + val1.plus(mc));

        /* 11. pow() -> returns (this^pow) to unlimited precision.
            pow(MathContext) -> returns (this^pow)  and rounding acc to MathContext
         */
        System.out.println("pow(): " + val2.pow(3));
        System.out.println("pow() with MathContext: " + val1.pow(3, mc));

        // 12. precision -> returns precision of this BigDecimal
        System.out.println("precision(): " + val1.precision());

        /* 13. remainder(val) -> return this % val
               remainder(val, MathContext) -> returns this % val, rounding acc to MathContext
         */
        System.out.println("remainder(BigDecimal): " + val2.remainder(val1));
        System.out.println("remainder(BigDecimal) with MathContext: " + val1.remainder(val2, mc));

        // 14. round(MathContext) -> round acc to MathContext
        System.out.println("round(): " + val2.round(mc));

        // 15. scale -> returns scale of this BigDecimal
        System.out.println("scale(): " + val1.scale());

        // 16. scaleByPowerOfTen(n) -> return this * 10^n
        System.out.println("scaleByPowerOfTen(n): " + val1.scaleByPowerOfTen(2));

        /* 17.setScale(scale) -> sets new scale, Throws Rounding Necessary ArithmeticException
            setScale(scale, int roundingMode/RoundingMode) -> sets new scale and
            unscaled value is multiplication/division of unscaled value with appropriate pow of 10.
         */
        //System.out.println("setScale(scale): " + val1.setScale(3));
        System.out.println("setScale(scale) with RoundingMode " + val1.setScale(6, RoundingMode.HALF_UP));

        /* 18. subtract(val) -> returns this - val, scale as max(this.scale , val.scale)
            subtract(val, MathContext) -> returns this - val, rounding acc to MathContext
         */
        System.out.println("subtract(BigDecimal): " + val2.subtract(val1));
        System.out.println("subtract(BigDecimal) with MathContext: " + val2.subtract(val1, mc));

        // 19. unscaledValue() -> returns unscaled value of this
        System.out.println("unscaledValue: " + val2.unscaledValue());

    }

    private static void checkBigDecimalConstructorsWithRoundingModeAndPrecision() {

        /* 1. BigDecimal using new keyword and passing double value
            It has same effect as double/Double, it is error-prone to precision/scale.
            Literals can also be passed in the constructor.
         */
        double doubleVal = 3.45;
        MathContext mc = new MathContext(2, RoundingMode.CEILING);
        BigDecimal objDouble = new BigDecimal(doubleVal);
        System.out.println("new BigDecimal(double): " + objDouble);
        objDouble = new BigDecimal(doubleVal, mc);
        System.out.println("new BigDecimal(double) with MathContext: " + objDouble);

        /* 2. BigDecimal using new keyword and passing String
            It provides accurate precision/scale.
         */
        String strVal = "6.9876509";
        BigDecimal objStr = new BigDecimal(strVal);
        System.out.println("new BigDecimal(String): " + objStr);
        objStr = new BigDecimal(strVal, mc);
        System.out.println("new BigDecimal(String) with MathContext " + objStr);

        /* 3. BigDecimal using new keyword and passing int
         */
        int intVal = 6789765;
        BigDecimal objInt = new BigDecimal(intVal);
        System.out.println("new BigDecimal(int): " + objInt);
        objInt = new BigDecimal(intVal, mc);
        System.out.println("new BigDecimal(int) with MathContext: " + objInt);

        /* 4. BigDecimal using new keyword and passing long
         */
        long longVal = 1234999999999999L;
        BigDecimal objLong = new BigDecimal(longVal);
        System.out.println("new BigDecimal(long): " + objLong);
        objLong = new BigDecimal(longVal, mc);
        System.out.println("new BigDecimal(long) with MathContext: " + objLong);

        /* 5. BigDecimal using new keyword and passing BigInteger
         */
        BigInteger bigIntegerVal = new BigInteger("1346277");
        BigDecimal objBigInteger = new BigDecimal(bigIntegerVal);
        System.out.println("new BigDecimal(BigInteger): " + objBigInteger);
        objBigInteger = new BigDecimal(bigIntegerVal, mc);
        System.out.println("new BigDecimal(BigInteger) with MathContext: " + objBigInteger);
        objBigInteger = new BigDecimal(bigIntegerVal, 3);
        System.out.println("new BigDecimal(BigInteger) with Scale: " + objBigInteger);
        objBigInteger = new BigDecimal(bigIntegerVal, 4, mc);
        System.out.println("new BigDecimal(BigInteger) with Scale and MathContext: " + objBigInteger);

        /* 6. It also accepts char[] with scale/MathContext and length and offset can also be given.
            It is same as BigDecimal(String)
            BigDecimal(char[], offset, len)
            BigDecimal(char[], offset, len, mc)
            BigDecimal(char[], mc)
         */
        char[] charArrVal = {'1', '4', '7', '8'};
        BigDecimal objCharArray = new BigDecimal(charArrVal);
        System.out.println("new BigDecimal(CharArray): " + objCharArray);
    }
}
