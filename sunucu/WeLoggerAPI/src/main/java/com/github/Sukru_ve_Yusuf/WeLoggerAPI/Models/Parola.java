/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Models;

import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Interfaces.*;
import java.util.*;
/**
 * Kullanıcıların parolalarının basit bir veri yapısında tutulması için sınıf.
 * İşlemlerin önemli kısmı ParolaHizmeti sınıfında yapılır.
 * @see com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.ParolaHizmeti
 * 
 * @author yusuf
 */
public class Parola {
    private String karma;
    private byte[] tuz;
    
    /**
     * Yeni bir Parola nesnesi oluşturur.
     * 
     * @param karma Parola karma algoritmasıyla işlenmiş parola
     * @param tuz   Parolanın karımında kullanılmış tuz değeri
     */
    public Parola(String karma, byte[] tuz)
    {
        this.setKarma(karma);
        this.setTuz(tuz);
    }
    /**
     * Yeni bir Parola nesnesi oluşturur.
     * 
     * @param karma         Parola karma algoritmasıyla işlenmiş parola
     * @param base64_tuz    Parolanın karımında kullanılmış tuzun Base64 String gösterimi
     */
    public Parola(String karma, String base64_tuz)
    {
        this.setKarma(karma);
        this.setTuz(base64_tuz);
    }
    
    /**
     * Bu sınıfın karma alanına değer atar.
     * 
     * @param karılmış_parola   Parola karma algoritmasıyla işlenmiş parola
     *
     */
    public void setKarma(String karılmış_parola)
    {
        this.karma = karılmış_parola;
    }
    /**
     * Bu sınıfın karma alanının değerini döndürür.
     * 
     * @return Bu sınıfın karma alanının değeri
     */
    public String getKarma()
    {
        return this.karma;
    }
    
    /**
     * Bu sınıfın tuz alanına değer atar.
     * 
     * @param tuz   Parolanın karımında kullanılmış tuz değeri
     */
    public void setTuz(byte[] tuz)
    {
        this.tuz = tuz;
    }
    /**
     * Bu sınıfın tuz alanına değer atar.
     * 
     * @param base64_tuz    Parolanın karımında kullanılmış tuzun Base64 String gösterimi
     */
    public void setTuz(String base64_tuz)
    {
        Base64.Decoder b64decoder = Base64.getDecoder();
        this.tuz = b64decoder.decode(base64_tuz);
    }
    /**
     * Bu sınıfı tuz alanının değerini döndürür.
     * 
     * @return Bu sınıfın tuz alanının değeri
     */
    public byte[] getTuzBytes()
    {
        return this.tuz;
    }
    /**
     * Bu sınıfın tuz alanının değerini döndürür.
     * 
     * @return Bu sınıfın tuz alanının Base64 String gösterimi
     */
    public String getTuzBase64()
    {
        Base64.Encoder b64encoder = Base64.getEncoder();
        return b64encoder.encodeToString(this.tuz);
    }
}
