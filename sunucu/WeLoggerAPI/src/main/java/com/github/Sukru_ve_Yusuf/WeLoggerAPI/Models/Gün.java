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
 * 
 * @see Video
 * @see IKimlikli
 */
public class Gün implements IKimlikli
{
    private Kullanıcı iye;
    private String açıklama;
    private Video[] videolar;
    private Date tarih;
    private byte[] kimlik;
    
    
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
