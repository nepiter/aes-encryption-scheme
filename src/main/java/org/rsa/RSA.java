package org.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA {
    private BigInteger Mod_Value, Val2, PublicK;

    private int BitL = 1024;

    /** Create an instance that can encrypt using someone elses public key. */
    public RSA(BigInteger N, BigInteger E) {
        Mod_Value = N;
        PublicK = E;
    }

    /** Create an instance that can both encrypt and decrypt. */
    public RSA(int bits) {
        BitL = bits;
        SecureRandom r_secure = new SecureRandom();
        BigInteger Obj_p = new BigInteger(BitL / 2, 100, r_secure);
        BigInteger Obj_q = new BigInteger(BitL / 2, 100, r_secure);
        Mod_Value = Obj_p.multiply(Obj_q);
        BigInteger Obj_m = (Obj_p.subtract(BigInteger.ONE)).multiply(Obj_q.subtract(BigInteger.ONE));
        PublicK = new BigInteger("3");
        while (Obj_m.gcd(PublicK).intValue() > 1) {
            PublicK = PublicK.add(new BigInteger("2"));
        }
        Val2 = PublicK.modInverse(Obj_m);
    }

    /** Encrypt the given plaintext message. */
    public synchronized String encrypt(String message) {
        return (new BigInteger(message.getBytes())).modPow(PublicK, Mod_Value).toString();
    }

    /** Encrypt the given plaintext message. */
    public synchronized BigInteger encrypt(BigInteger message) {
        return message.modPow(PublicK, Mod_Value);
    }

    /** Decrypt the given ciphertext message. */
    public synchronized String decrypt(String message) {
        return new String((new BigInteger(message)).modPow(Val2, Mod_Value).toByteArray());
    }

    /** Decrypt the given ciphertext message. */
    public synchronized BigInteger decrypt(BigInteger message) {
        return message.modPow(Val2, Mod_Value);
    }

    /** Generate a new public and private key set. */
    public synchronized void generateKeys() {
        SecureRandom r_secure = new SecureRandom();
        BigInteger Obj_p = new BigInteger(BitL / 2, 100, r_secure);
        BigInteger Obj_q = new BigInteger(BitL / 2, 100, r_secure);
        Mod_Value = Obj_p.multiply(Obj_q);
        BigInteger Obj_m = (Obj_p.subtract(BigInteger.ONE)).multiply(Obj_q.subtract(BigInteger.ONE));
        PublicK = new BigInteger("3");
        while (Obj_m.gcd(PublicK).intValue() > 1) {
            PublicK = PublicK.add(new BigInteger("2"));
        }
        Val2 = PublicK.modInverse(Obj_m);
    }

    /** Return the modulus. */
    public synchronized BigInteger getMod() {
        return Mod_Value;
    }

    /** Return the public key. */
    public synchronized BigInteger getE() {
        return PublicK;
    }
}