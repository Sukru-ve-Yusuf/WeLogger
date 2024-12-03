/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Models;

import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Interfaces.*;

import java.util.*;
import java.security.SecureRandom;
/**
 *
 * @author yusuf
 * 
 * @see IKimlikli
 */
public class Video implements IKimlikli
{
    private String dosya_yolu;
    private String açıklama;
    private Kullanıcı iye;
    private byte[] kimlik;
    private Date tarih;
    
    public Video(String dosya_yolu, String açıklama, Kullanıcı iye, Date tarih)
    {
        this.dosya_yolu = dosya_yolu;
        this.açıklama = açıklama;
        this.iye = iye;
        this.tarih = tarih;
        this.KimliğiYenile();
    }
    
    @Override
    public byte[] getKimlikBytes()
    {
        return this.kimlik;
    }
    
    @Override
    public String getKimlikBase64()
    {
        Base64.Encoder b64encoder = Base64.getEncoder();
        return b64encoder.encodeToString(this.kimlik);
    }
    
    private void KimliğiYenile()
    {
        SecureRandom rast = new SecureRandom();
        kimlik = new byte[32];
        rast.nextBytes(this.kimlik);
    }
}
