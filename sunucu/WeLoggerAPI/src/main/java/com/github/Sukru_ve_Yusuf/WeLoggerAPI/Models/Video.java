/*
 * Video
 *
 * Sürüm 0.1
 *
 * Aralık 2024
 *
 * Copyright (C) 2024 Yusuf Kozan, Şükrü Fırat Sarp
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Models;

import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Interfaces.*;

import java.util.*;
import java.security.SecureRandom;

/**
 * Kullanıcıların çektiği videoların her birini temsil eder.
 * VideoListesi sınıfı kullanılarak bağlı liste yapılabilir.
 * 
 * @see VideoListesi
 * @see IKimlikli
 */
public class Video implements IKimlikli
{
    /**
     * Videonun dosya sistemindeki adresi.
     */
    private String dosya_yolu;
    /**
     * Kullanıcının video hakkındaki açıklama yazısı.
     */
    private String açıklama;
    /**
     * Videonun sahibi olan kullanıcı.
     * @see Kullanıcı
     */
    private Kullanıcı iye;
    /**
     * Video nesnesini ayırt etmek için eşsiz bir sayı.
     */
    private byte[] kimlik;
    /**
     * Videonun çekildiği tarih ve saat.
     * @see java.util.Date
     */
    private Date tarih;
    
    /**
     * Verilen bilgilerle yeni bir video nesnesi oluşturur.
     * Oluşturulan nesneye yeni bir kimlik atanır.
     * 
     * @param dosya_yolu    Temsil edilen video dosyasının adresi
     * @param açıklama      Kullanıcının video hakkındaki açıklama metni
     * @param iye           Videonun sahibi olan kullanıcı
     * @param tarih         Videonun çekildiği tarih ve saat bilgisi
     */
    public Video(String dosya_yolu, String açıklama, Kullanıcı iye, Date tarih)
    {
        this.dosya_yolu = dosya_yolu;
        this.açıklama = açıklama;
        this.iye = iye;
        this.tarih = tarih;
        this.KimliğiYenile();
    }
    
    /**
     * Video dosyasının bulunduğu adresi bildirir.
     * 
     * @return  Videonun dosya sistemindeki adresi
     */
    public String getDosyaYolu()
    {
        return this.dosya_yolu;
    }
    /**
     * Video için farklı bir adres belirtilir.
     * Eski adresteki dosya taşınmaz. Video yeni adreste aranır.
     * 
     * @param yeni_yol  Videonun dosya sistemindeki yeni adresi
     */
    public void setDosyaYolu(String yeni_yol)
    {
        this.dosya_yolu = yeni_yol;
    }
    
    /**
     * Videonun açıklama metnine erişim sağlar.
     * 
     * @return  Kullanıcının video hakkındaki açıklama metni
     */
    public String getAçıklama()
    {
        return this.açıklama;
    }
    /**
     * Videonun açıklama metnini yeniler.
     * 
     * @param yeni_açıklama Mevcut açıklama metninin yerini alacak metin
     */
    public void setAçıklama(String yeni_açıklama)
    {
        this.açıklama = yeni_açıklama;
    }
    
    /**
     * Videonun sahibini temsil eden Kullanıcı nesnesine erişim sağlar.
     * 
     * @return  Videonun sahibi
     * @see Kullanıcı
     */
    public Kullanıcı getİye()
    {
        return this.iye;
    }
    /**
     * Videonun sahibini belirtilen yeni Kullanıcı nesnesi olarak değiştirir.
     * 
     * @param yeni_iye  Videonun yeni sahibi
     * @see Kullanıcı
     */
    public void setİye(Kullanıcı yeni_iye)
    {
        this.iye = yeni_iye;
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
    /**
     * Video için yepyeni bir kimlik atar.
     */
    private void KimliğiYenile()
    {
        SecureRandom rast = new SecureRandom();
        kimlik = new byte[32];
        rast.nextBytes(this.kimlik);
    }
    
    /**
     * Videonun çekildiği tarihi bildirir.
     * 
     * @return  Videonun çekildiği tarihi bildiren Date nesnesinin klonu
     */
    public Date getTarih()
    {
        return (Date)this.tarih.clone();
    }
    /**
     * Videonun çekildiği tarihi belirtilen yeni tarih olarak değiştirir.
     * 
     * @param yeni_tarih    Videonun yeni tarihi
     */
    public void setTarih(Date yeni_tarih)
    {
        this.tarih = (Date)yeni_tarih.clone();
    }
}
