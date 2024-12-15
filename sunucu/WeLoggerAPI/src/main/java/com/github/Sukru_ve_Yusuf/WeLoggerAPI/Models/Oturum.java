/*
 * Oturum
 * 
 * Sürüm 0.1
 * 
 * Aralık 2024
 * 
 * Copyright (C) 2024 Yusuf Kozan, Şükrü Fırat Sarp
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Models;

import java.util.*;
import java.security.SecureRandom;
import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Interfaces.*;
import com.fasterxml.jackson.annotation.*;

/**
 * Uygulamada açılmış oturumların bilgilerini tutan bir sınıf.
 * 
 * @see IKimlikli
 */
@JsonPropertyOrder({"_id", "Kullanıcı", "Baş", "Son"})
public class Oturum implements IKimlikli
{
    /**
     * Bu oturumu ayırt eden eşsiz bir sayı.
     */
    private byte[] kimlik;
    /**
     * Bu oturumun iyesi olan kullanıcının kimliğii
     */
    private String kullanıcı;
    /**
     * Bu oturumun geçerliliğinin başladığı tarih.
     */
    private Calendar baş;
    /**
     * Bu oturumun geçerliliğinin bittiği tarih.
     */
    private Calendar son;
    
    /**
     * Yepyeni bir kimlikle oturum nesnesi oluşturur.
     * 
     * @param kullanıcı_kimliği Oturumun iyesi olan kullanıcının kimliği
     * @param baş               Oturumun başlangıç tarihi
     * @param son               Oturumun bitiş tarihi
     */
    public Oturum(String kullanıcı_kimliği, Calendar baş, Calendar son)
    {
        this.setKullanıcı(kullanıcı_kimliği);
        this.setBaş(baş);
        this.setSon(son);
        this.KimliğiYenile();
    }
    /**
     * Belirtilen kimlikle oturum nesnesi oluşturur.
     * 
     * @param kullanıcı_kimliği Oturumun iyesi olan kullanıcının kimliği
     * @param baş               Oturumun başlangıç tarihi
     * @param son               Oturumun bitiş tarihi
     * @param kimlik            Bayt dizisi olarak oturumun kimliği
     */
    public Oturum(String kullanıcı_kimliği, Calendar baş, Calendar son,
            byte[] kimlik)
    {
        this.setKullanıcı(kullanıcı_kimliği);
        this.setBaş(baş);
        this.setSon(son);
        this.kimlik = kimlik.clone();
    }
    /**
     * Belirtilen kimlikle oturum nesnesi oluşturur.
     * 
     * @param kullanıcı_kimliği Oturumun iyesi olan kullanıcının kimliği
     * @param baş               Oturumun başlangıç tarihi
     * @param son               Oturumun bitiş tarihi
     * @param kimlik_base64     Base64 metni olarak oturumun kimliği
     */
    @JsonCreator
    public Oturum(
            @JsonProperty("Kullanıcı") String kullanıcı_kimliği,
            @JsonProperty("Baş") Calendar baş,
            @JsonProperty("Son") Calendar son,
            @JsonProperty("_id") String kimlik_base64)
    {
        this.setKullanıcı(kullanıcı_kimliği);
        this.setBaş(baş);
        this.setSon(son);
        Base64.Decoder b64decoder = Base64.getDecoder();
        this.kimlik = b64decoder.decode(kimlik_base64);
    }
    
    /**
     * Oturumun iyesi olan kullanıcının kimliğini bildirir.
     * 
     * @return  Oturumun iyesinin kimliği
     */
    @JsonGetter("Kullanıcı")
    public String getKullanıcı()
    {
        return this.kullanıcı;
    }
    /**
     * Oturumun iyesi olan kullanıcıyı belirler.
     * 
     * @param kullanıcı_kimliği Oturumun iyesinin kimliği
     */
    public void setKullanıcı(String kullanıcı_kimliği)
    {
        this.kullanıcı = kullanıcı_kimliği;
    }
    /**
     * Oturumun başlangıç tarihine erişim sağlar.
     * 
     * @return  Oturumun başlangıç tarihi
     */
    @JsonGetter("Baş")
    public Calendar getBaş()
    {
        return (Calendar)this.baş.clone();
    }
    /**
     * Oturumun başlangıç tarihini belirler.
     * 
     * @param başlangıç Oturumun yeni başlangıç tarihi
     */
    public void setBaş(Calendar başlangıç)
    {
        this.baş = (Calendar)başlangıç.clone();
    }
    /**
     * Oturumun bitiş tarihine erişim sağlar.
     * 
     * @return  Oturumun bitiş tarihi
     */
    @JsonGetter("Son")
    public Calendar getSon()
    {
        return (Calendar)this.son.clone();
    }
    /**
     * Oturumun bitiş tarihini belirler.
     * 
     * @param son Oturumun yeni bitiş tarihi
     */
    public void setSon(Calendar son)
    {
        this.son = (Calendar)son.clone();
    }
    
    
    @Override @JsonIgnore
    public byte[] getKimlikBytes()
    {
        return this.kimlik.clone();
    }
    @Override @JsonGetter("_id")
    public String getKimlikBase64()
    {
        Base64.Encoder b64encoder = Base64.getEncoder();
        return b64encoder.encodeToString(this.kimlik);
    }
    
    /**
     * Bu oturum için yepyeni bir kimlik atar.
     */
    private void KimliğiYenile()
    {
        SecureRandom rast = new SecureRandom();
        this.kimlik = new byte[32];
        rast.nextBytes(this.kimlik);
    }
}
