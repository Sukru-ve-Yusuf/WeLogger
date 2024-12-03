/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Models;

import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Interfaces.*;
import java.util.*;

/**
 *
 * @author yusuf
 */
public class Kullanıcı implements IKimlikli
{
    private String ad;
    private String kullanıcı_adı;
    private String parola;
    private byte[] kimlik;
    
    public String getAd()
    {
        return this.ad;
    }
    public void setAd(String ad)
    {
        this.ad = ad;
    }
    
    public String getKullanıcıAdı()
    {
        return this.kullanıcı_adı;
    }
    public boolean setKullanıcıAdı(String kullanıcı_adı)
    {
        // Buraya ileride eşsizlik denetimi gelecek
        this.kullanıcı_adı = kullanıcı_adı;
        return true;
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
}
