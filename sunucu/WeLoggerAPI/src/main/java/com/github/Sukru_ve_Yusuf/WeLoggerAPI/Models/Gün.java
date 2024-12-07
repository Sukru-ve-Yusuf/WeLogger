/*
 * Gün
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
 * Aynı kişinin aynı günde çektiği videoları bir araya getiren sınıf.
 * 
 * @see Video
 * @see IKimlikli
 */
public class Gün implements IKimlikli
{
    /**
     * Bu günü yaşamış kişi.
     * @see Kullanıcı
     */
    private Kullanıcı başkahraman;
    /**
     * Başkahramanın bu gün hakkında yazdığı yorum ve açıklama metni.
     */
    private String açıklama;
    /**
     * Başkahramanın bu gün çektiği videoların tarihsel sıralı bağlı listesi.
     */
    private SıralıVideolar videolar;
    /**
     * Bu günün tarihi.
     * @see Calendar
     */
    private Calendar tarih;
    /**
     * Bu günü diğer günlerden ayıran eşsiz bir sayı.
     */
    private byte[] kimlik;
    
    /**
     * Verilen bilgilerle yeni bir Gün nesnesi oluşturur.
     * Oluşturulan nesneye yeni bir kimlik atanır.
     * 
     * @param başkahraman   Oluşturulan günü yaşamış kişi
     * @param açıklama      Bu gün hakkında açıklama metni
     * @param videolar      Başkahramanın bu günde çektiği videolar
     * @param tarih         Bu günün tarihi
     * 
     * @see Kullanıcı
     * @see SıralıVideolar
     * @see Calendar
     */
    public Gün(Kullanıcı başkahraman, String açıklama, SıralıVideolar videolar,
            Calendar tarih)
    {
        this.başkahraman = başkahraman;
        this.setAçıklama(açıklama);
        this.setVideolar(videolar);
        this.setTarih(tarih);
        this.KimliğiYenile();
    }
    
    /**
     * Günün başkahramanını temsil eden Kullanıcı nesnesine erişim sağlar.
     * 
     * @return  Günün başkahramanı
     * @see Kullanıcı
     */
    public Kullanıcı getBaşkahraman()
    {
        return this.başkahraman;
    }
    
    /**
     * Başkahramanın bu gün hakkındaki açıklamasına erişim sağlar.
     * 
     * @return  Başkahramanın bu gün hakkındaki açıklaması
     */
    public String getAçıklama()
    {
        return this.açıklama;
    }
    /**
     * Gün hakkındaki açıklamayı yeni açıklamayla değiştirir.
     * 
     * @param yeni_açıklama Gün hakkındaki yeni açıklama
     */
    public void setAçıklama(String yeni_açıklama)
    {
        this.açıklama = yeni_açıklama;
    }
    
    /**
     * Bu gün içinde çekilmiş videolara erişim sağlar.
     * 
     * @return  Gün içinde çekilmiş videoların sıralı bağlı listesi
     */
    public SıralıVideolar getVideolar()
    {
        return this.videolar;
    }
    /**
     * Bu gün içinde çekilmiş videoların listesini tanımlar.
     * Verilen sıralı video nesnesinin tarihi gün nesnesiyle uyuşmuyorsa
     * veya aynı kişinin değilse işlem yapılmaz.
     * 
     * @param videolar  Bu gün içinde çekilmiş videoların sıralı listesi
     * @return  Video listesi yenilenirse true, yenilenmezse false
     */
    public boolean setVideolar(SıralıVideolar videolar)
    {
        Calendar bu_gün = this.getTarih();
        Calendar video_günü = videolar.getTarih();
        boolean aynı_günde = (bu_gün.get(Calendar.YEAR) == video_günü.get(Calendar.YEAR))
                && (bu_gün.get(Calendar.DAY_OF_YEAR) == video_günü.get(Calendar.DAY_OF_YEAR));
        boolean aynı_iye = this.getBaşkahraman().getKimlikBase64() == videolar.getİye().getKimlikBase64();
        if (aynı_günde && aynı_iye)
        {
            this.videolar = videolar;
            return true;
        }
        return false;
    }
    
    /**
     * Günün tarihini belirtir.
     * 
     * @return  Günün tarihini belirten Calendar nesnesinin klonu
     */
    public Calendar getTarih()
    {
        return (Calendar)this.tarih.clone();
    }
    /**
     * Günün tarihini belirtilen yeni tarih olarak değiştirir.
     * Verilen yeni tarih null ise işlem yapmaz.
     * 
     * @param yeni_tarih    Günün yeni tarihi
     * @return  Günün tarihi değişirse true, değişmezse false
     */
    public boolean setTarih(Calendar yeni_tarih)
    {
        if (yeni_tarih != null)
        {
            this.tarih = yeni_tarih;
            return true;
        }
        return false;
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
     * Bu gün için yepyeni bir kimlik atar.
     */
    private void KimliğiYenile()
    {
        SecureRandom rast = new SecureRandom();
        this.kimlik = new byte[32];
        rast.nextBytes(this.kimlik);
    }
}
