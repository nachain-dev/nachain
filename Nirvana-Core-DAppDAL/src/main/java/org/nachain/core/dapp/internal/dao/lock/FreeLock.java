package org.nachain.core.dapp.internal.dao.lock;

import org.nachain.core.crypto.Hash;
import org.nachain.core.util.Hex;

import java.math.BigInteger;


public class FreeLock {


    String address;


    double lockNacPrice;


    long lockDay;


    long unlockNacBlockHeight;


    String lockTx;


    BigInteger amount;


    String hash;


    String unlockTx;


    String withdrawTx;

    public String getAddress() {
        return address;
    }

    public FreeLock setAddress(String address) {
        this.address = address;
        return this;
    }

    public double getLockNacPrice() {
        return lockNacPrice;
    }

    public FreeLock setLockNacPrice(double lockNacPrice) {
        this.lockNacPrice = lockNacPrice;
        return this;
    }

    public long getLockDay() {
        return lockDay;
    }

    public FreeLock setLockDay(long lockDay) {
        this.lockDay = lockDay;
        return this;
    }

    public long getUnlockNacBlockHeight() {
        return unlockNacBlockHeight;
    }

    public FreeLock setUnlockNacBlockHeight(long unlockNacBlockHeight) {
        this.unlockNacBlockHeight = unlockNacBlockHeight;
        return this;
    }

    public String getLockTx() {
        return lockTx;
    }

    public FreeLock setLockTx(String lockTx) {
        this.lockTx = lockTx;
        return this;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public FreeLock setAmount(BigInteger amount) {
        this.amount = amount;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public FreeLock setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String getUnlockTx() {
        return unlockTx;
    }

    public FreeLock setUnlockTx(String unlockTx) {
        this.unlockTx = unlockTx;
        return this;
    }

    public String getWithdrawTx() {
        return withdrawTx;
    }

    public FreeLock setWithdrawTx(String withdrawTx) {
        this.withdrawTx = withdrawTx;
        return this;
    }

    @Override
    public String toString() {
        return "FreeLock{" +
                "address='" + address + '\'' +
                ", lockNacPrice=" + lockNacPrice +
                ", lockDay=" + lockDay +
                ", unlockNacBlockHeight=" + unlockNacBlockHeight +
                ", lockTx='" + lockTx + '\'' +
                ", amount=" + amount +
                ", hash='" + hash + '\'' +
                ", unlockTx='" + unlockTx + '\'' +
                ", withdrawTx='" + withdrawTx + '\'' +
                '}';
    }

    public String toHashString() {
        return "FreeLock{" +
                "address='" + address + '\'' +
                ", lockNacPrice=" + lockNacPrice +
                ", lockDay=" + lockDay +
                ", unlockNacBlockHeight=" + unlockNacBlockHeight +
                ", lockTx='" + lockTx + '\'' +
                ", amount=" + amount +
                '}';
    }


    public byte[] encodeHash() {

        return Hash.h256(this.toHashString().getBytes());
    }


    public String encodeHashString() {
        return Hex.encode0x(encodeHash());
    }

}
