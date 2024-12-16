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
import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.VeriTabanı.*;
import java.util.*;
import java.security.SecureRandom;
import com.fasterxml.jackson.annotation.*;

/**
 * Aynı kişinin aynı günde çektiği videoları bir araya getiren sınıf.
 * 
 * @see Video
 * @see Kullanıcı
 * @see IKimlikli
 */
@JsonPropertyOrder({"_id", "Başkahraman", "Açıklama", "Videolar", "Tarih"})
public class Gün implements IKimlikli
{
    /**
     * Bu günü yaşamış kişi.
     * @see Kullanıcı#getKimlikBase64() 
     * @see Kullanıcı#kimlik
     */
    protected String başkahraman;
    /**
     * Başkahramanın bu gün hakkında yazdığı yorum ve açıklama metni.
     */
    protected String açıklama;
    /**
     * Başkahramanın bu gün çektiği videoların tarihsel sıralı bağlı listesi.
     */
    protected SıralıVideolar videolar;
    /**
     * Bu günün tarihi.
     * @see Calendar
     */
    protected Calendar tarih;
    /**
     * Bu günü diğer günlerden ayıran eşsiz bir sayı.
     */
    protected byte[] kimlik;
    
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
        this.setTarih(tarih);
        this.KimliğiYenile();
        this.başkahraman = başkahraman.getKimlikBase64();
        this.setAçıklama(açıklama);
        this.setVideolar(videolar);
    }
    /**
     * Başka bir Gün nesnesini kullanarak yeni bir Gün nesnesi oluşturur.
     * İsteğe bağlı olarak kimlik korunabilir ya da yenilenebilir.
     * 
     * @param başka_gün         İçeriği alınacak gün nesnesi
     * @param kimlik_yenilensin true ise yeni kimlik atanır,
     *                          false ise kaynak nesnenin kimliği kopyalanır.
     */
    public Gün(Gün başka_gün, boolean kimlik_yenilensin)
    {
        this.başkahraman = başka_gün.getBaşkahraman();
        this.setAçıklama(başka_gün.getAçıklama());
        this.setVideolar(başka_gün.getVideolar());
        this.setTarih(başka_gün.getTarih());
        if (kimlik_yenilensin)
            this.KimliğiYenile();
        else
            this.kimlik = başka_gün.getKimlikBytes().clone();
    }
    /**
     * Verilen bilgilerle videosu olmayan bir gün oluşturur.
     * 
     * @param başkahraman   Günü yaşayan kullanıcının kimliği
     * @param açıklama      Gün hakkında açıklama metni
     * @param tarih         Günün tarihi
     * @param kimlik        Base64 metni olarak günün kimliği
     */
    @JsonCreator
    public Gün(
            @JsonProperty("Başkahraman") String başkahraman,
            @JsonProperty("Açıklama") String açıklama,
            @JsonProperty("Tarih") Calendar tarih,
            @JsonProperty("_id") String kimlik)
    {
        this.başkahraman = başkahraman;
        this.setAçıklama(açıklama);
        this.setTarih(tarih);
        this.setVideolar(null);
        Base64.Decoder b64decoder = Base64.getDecoder();
        this.kimlik = b64decoder.decode(kimlik);
    }
    /**
     * Verilen bilgilerle eşsiz kimliği olan bir gün oluşturur.
     * 
     * @param başkahraman   Günü yaşayan kullanıcının kimliği
     * @param açıklama      Gün hakkında açıklama metni
     * @param tarih         Günün tarihi
     * @param gün_vt        Kimlik eşsizliği için gün veri tabanı hizmeti
     * 
     * @see GünVT
     */
    public Gün(String başkahraman, String açıklama, Calendar tarih,
            GünVT gün_vt)
    {
        this.başkahraman = başkahraman;
        this.setAçıklama(açıklama);
        this.setTarih(tarih);
        this.KimliğiYenile(gün_vt);
        this.setVideolar(null);
    }
    
    /**
     * Günün başkahramanınının kullanıcı kimliğine erişim sağlar.
     * 
     * @return  Günün başkahramanının kimliği
     * @see Kullanıcı#getKimlikBase64() 
     * @see Kullanıcı#kimlik
     * @see Kullanıcı
     */
    @JsonGetter("Başkahraman")
    public String getBaşkahraman()
    {
        return this.başkahraman;
    }
    
    /**
     * Başkahramanın bu gün hakkındaki açıklamasına erişim sağlar.
     * 
     * @return  Başkahramanın bu gün hakkındaki açıklaması
     */
    @JsonGetter("Açıklama")
    public String getAçıklama()
    {
        if (this.açıklama == null)
            return "";
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
    @JsonIgnore
    public SıralıVideolar getVideolar()
    {
        return this.videolar;
    }
    /**
     * Bu gün içinde çekilmiş videolardan birinin kimliğine erişim sağlar.
     * 
     * @return  Base64 metni olarak, gün içinde çekilmiş videolardan birinin
     *          kimliği. Video yoksa null.
     */
    @JsonGetter("Videolar")
    public String getVideolarınKimliği()
    {
        if (this.videolar == null)
            return null;
        return this.videolar.getKimlikBase64();
    }
    /**
     * Bu gün içinde çekilmiş videoların listesini tanımlar.
     * Verilen sıralı video nesnesinin tarihi gün nesnesiyle uyuşmuyorsa
     * veya aynı kişinin değilse işlem yapılmaz.
     * 
     * @param videolar  Bu gün içinde çekilmiş videoların sıralı listesi
     * @return  Video listesi yenilenirse true, yenilenmezse false
     */
    @JsonIgnore
    public boolean setVideolar(SıralıVideolar videolar)
    {
        if (videolar == null)
        {
            this.videolar = null;
            return true;
        }
        
        Calendar bu_gün = this.getTarih();
        Calendar video_günü = videolar.getTarih();
        boolean aynı_günde = (bu_gün.get(Calendar.YEAR) == video_günü.get(Calendar.YEAR))
                && (bu_gün.get(Calendar.DAY_OF_YEAR) == video_günü.get(Calendar.DAY_OF_YEAR));
        boolean aynı_iye = this.getBaşkahraman().equals(videolar.getİye());
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
    @JsonGetter("Tarih")
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
    
    @Override @JsonIgnore
    public byte[] getKimlikBytes()
    {
        return this.kimlik;
    }
    @Override @JsonGetter("_id")
    public String getKimlikBase64()
    {
        Base64.Encoder b64encoder = Base64.getEncoder();
        return b64encoder.encodeToString(this.kimlik);
    }
    /**
     * Bu gün için yepyeni bir kimlik atar.
     */
    protected void KimliğiYenile()
    {
        SecureRandom rast = new SecureRandom();
        this.kimlik = new byte[33];
        rast.nextBytes(this.kimlik);
    }
    /**
     * Gün için yepyeni bir eşsiz kimlik atar.
     * 
     * @param gün_vt  Eşsizlik denetimi için gün veri tabanı hizmeti
     */
    protected void KimliğiYenile(GünVT gün_vt)
    {
        SecureRandom rast = new SecureRandom();
        byte[] yeni = new byte[33];
        rast.nextBytes(yeni);
        
        Base64.Encoder b64encoder = Base64.getEncoder();
        String yeni_b64 = b64encoder.encodeToString(yeni);
        
        byte kullanımda = gün_vt.KimlikKullanımda(yeni_b64);
        if (kullanımda == 0)
        {
            this.kimlik = yeni;
        }
        else
        {
            this.KimliğiYenile(gün_vt);
        }
    }
}
