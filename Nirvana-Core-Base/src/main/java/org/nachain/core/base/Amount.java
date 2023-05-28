package org.nachain.core.base;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static java.math.RoundingMode.FLOOR;


public final class Amount implements Comparable<Amount> {

    public static final Amount ZERO = new Amount(new BigInteger("0"));
    public static final Amount ONE = new Amount(new BigInteger("1"));
    public static final Amount TEN = new Amount(new BigInteger("10"));
    public static final Amount HUNDRED = new Amount(new BigInteger("100"));
    public static final Amount THOUSAND = new Amount(new BigInteger("1000"));
    public static final Amount TEN_THOUSAND = new Amount(new BigInteger("10000"));
    public static final Amount HUNDRED_THOUSAND = new Amount(new BigInteger("100000"));
    public static final Amount MILLION = new Amount(new BigInteger("1000000"));


    public static final int SCALE = 9;


    public static final RoundingMode ROUNDING_MODE = RoundingMode.DOWN;

    private final BigInteger nano;

    private Amount(BigInteger nano) {
        this.nano = nano;
    }


    public static Amount of(BigInteger n) {
        return new Amount(n);
    }


    public static Amount of(String n) {
        return new Amount(new BigInteger(n));
    }


    public static Amount of(String n, Unit unit) {
        return new Amount(new BigInteger(n).multiply(BigInteger.valueOf(unit.factor)));
    }


    public static Amount of(BigInteger n, Unit unit) throws ArithmeticException {
        return new Amount(n.multiply(BigInteger.valueOf(unit.factor)));
    }


    public static Amount of(long n, Unit unit) throws ArithmeticException {
        return of(BigInteger.valueOf(n), unit);
    }


    public static BigInteger toBigInteger(long n, Unit unit) throws ArithmeticException {
        return of(BigInteger.valueOf(n), unit).toBigInteger();
    }


    public static BigInteger toToken(long n) throws ArithmeticException {
        return of(BigInteger.valueOf(n), Unit.NAC).toBigInteger();
    }


    public static BigInteger toToken(double n) throws ArithmeticException {
        return of(BigDecimal.valueOf(n), Unit.NAC).toBigInteger();
    }


    public static double toTokenDouble(BigInteger n) throws ArithmeticException {
        return of(n).toDecimal(Unit.NAC).doubleValue();
    }


    public static long toTokenLong(BigInteger n) throws ArithmeticException {
        return of(n).toDecimal(Unit.NAC).longValue();
    }


    public static Amount of(BigDecimal d, Unit unit) {
        return new Amount(d.movePointRight(unit.exp).setScale(0, FLOOR).toBigInteger());
    }

    public static Amount sum(Amount a, Amount b) throws ArithmeticException {
        return new Amount(a.nano.add(b.nano));
    }


    public static String stripTrailingZeros(BigDecimal amount) {
        return amount.stripTrailingZeros().toPlainString();
    }


    public static String stripTrailingZeros(BigInteger amount) {
        return stripTrailingZeros(Amount.of(amount).toDecimal(Unit.NAC));
    }


    public BigDecimal toDecimal(int scale, Unit unit) {
        BigDecimal nano = new BigDecimal(this.nano);
        return nano.movePointLeft(unit.exp).setScale(scale, FLOOR);
    }


    public BigDecimal toDecimal(Unit unit) {
        return toDecimal(unit.exp, unit);
    }

    public BigDecimal toDecimal() {
        return new BigDecimal(nano);
    }


    public BigInteger toBigInteger() {
        return nano;
    }

    @Override
    public int compareTo(Amount other) {
        return this.lessThan(other) ? -1 : (this.greaterThan(other) ? 1 : 0);
    }

    @Override
    public int hashCode() {
        return nano.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Amount && ((Amount) other).nano == nano;
    }

    @Override
    public String toString() {
        return nano.toString();
    }


    public boolean greaterThan(Amount other) {

        return nano.compareTo(other.nano) == 1;
    }


    public boolean greaterThanOrEqual(Amount other) {
        return nano.compareTo(other.nano) == 1 || nano.compareTo(other.nano) == 0;
    }


    public boolean isPositive() {
        return greaterThan(ZERO);
    }


    public boolean isNotNegative() {
        return greaterThanOrEqual(ZERO);
    }


    public boolean lessThan(Amount other) {

        return nano.compareTo(other.nano) == -1;
    }


    public boolean lessThanOrEqual(Amount other) {
        return nano.compareTo(other.nano) == -1 || nano.compareTo(other.nano) == 0;
    }


    public boolean isNegative() {
        return lessThan(ZERO);
    }


    public boolean isNotPositive() {
        return lessThanOrEqual(ZERO);
    }


    public Amount negate() throws ArithmeticException {
        return new Amount(this.nano.negate());
    }


    public Amount add(Amount a) throws ArithmeticException {
        return new Amount(this.nano.add(a.nano));
    }


    public Amount subtract(Amount a) throws ArithmeticException {
        return new Amount(this.nano.subtract(a.nano));
    }


    public Amount multiply(BigInteger a) throws ArithmeticException {
        return new Amount(this.nano.multiply(a));
    }


    public Amount divide(BigInteger a) throws ArithmeticException {
        return new Amount(this.nano.divide(a));
    }

}
