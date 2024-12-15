/*
 * Kullanıcı
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
import org.bson.Document;
import com.fasterxml.jackson.annotation.*;

/**
 * Üye olan her bir kullanıcıyı temsil eden sınıf.
 */
@JsonPropertyOrder({"_id", "Ad", "KullanıcıAdı", "Parola", "Ömür"})
public class Kullanıcı implements IKimlikli
{
    /**
     * Kullanıcının gerçek adı.
     */
    private String ad;
    /**
     * Kullanıcının uygulama içinde kullandığı eşsiz rumuzu.
     */
    private String kullanıcı_adı;
    /**
     * Kullanıcının parolasının karılmış hâli.
     */
    private String parola;
    /**
     * Kullanıcının yaşadığı günlerin tarihsel sıralı bağlı listesi.
     */
    private SıralıGünler ömür;
    /**
     * Bu kullanıcıyı diğer kullanıcılardan ayırt etmek için eşsiz bir sayı.
     */
    private byte[] kimlik;
    
    /**
     * Yeni bir kimlikle boş bir kullanıcı nesnesi oluşturur.
     * Diğer alanların ayrıca doldurulması gerekir.
     */
    public Kullanıcı()
    {
        this.KimliğiYenile();
    }
    /**
     * Önceden belirlenmiş bir kimlikle boş bir kullanıcı nesnesi oluşturur.
     * Diğer alanların ayrıca doldurulması gerekir.
     * 
     * @param kimlik    Önceden belirlenmiş kimlik
     */
    public Kullanıcı(byte[] kimlik)
    {
        this.kimlik = kimlik.clone();
    }
    /**
     * Verilen bilgilerle ömrü olmayan bir kullanıcı oluşturur.
     * 
     * @param kimlik        Base64 metni olarak kullanıcının kimliği
     * @param ad            Kullanıcının gerçek adı,
     * @param kullanıcı_adı Kullanıcının rumuzu
     * @param parola        Kullanıcının karılmış parolası
     */
    @JsonCreator
    public Kullanıcı(
            @JsonProperty("_id") String kimlik,
            @JsonProperty("Ad") String ad,
            @JsonProperty("KullanıcıAdı") String kullanıcı_adı,
            @JsonProperty("Parola") String parola)
    {
        Base64.Decoder b64decoder = Base64.getDecoder();
        this.kimlik = b64decoder.decode(kimlik);
        this.setAd(ad);
        this.setKullanıcıAdı(kullanıcı_adı);
        this.parola = parola;
        this.ömür = null;
    }
    /**
     * Eşsiz bir kimlik ile yeni bir kullanıcı oluşturur.
     * 
     * @param ad            Kullanıcının gerçek adı
     * @param kullanıcı_adı Kullanıcının rumuzu
     * @param parola        Kullanıcının karılmış parolası
     * @param kullanıcı_vt  Kimlik eşsizliği için kullanıcı veri tabanı hizmeti
     */
    public Kullanıcı(String ad, String kullanıcı_adı, String parola,
            KullanıcıVT kullanıcı_vt)
    {
        this.setAd(ad);
        this.setKullanıcıAdı(kullanıcı_adı);
        this.parola = parola;
        this.ömür = null;
        this.KimliğiYenile(kullanıcı_vt);
    }
    /**
     * Veri tabanı sorgusuyla elde edilmiş Document türündeki BSON belgesinin
     * içeriğini kullanarak yeni bir kullanıcı nesnesi oluşturur.
     * Ömür boş bırakılır.
     * 
     * @param belge Veri tabanından okunmuş Document türünde BSON belgesi
     * @return  Belgede doğru alanlar varsa yeni kullanıcı nesnesi,
     *          yoksa null
     * 
     * @see Document
     * @see com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.VeriTabanıHizmetleri
     * @see com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.VeriTabanı
     */
    public static Kullanıcı BSONBelgesinden(Document belge)
    {
        String kimlik = belge.getString("_id");
        if (kimlik == null)
            return null;
        if (belge.containsKey("KullanıcıAdı") && belge.containsKey("Parola")
                && belge.containsKey("Ömür") && !kimlik.isBlank())
        {
            Kullanıcı kullanıcı = new Kullanıcı(
                    belge.getString("_id"),
                    belge.getString("Ad"),
                    belge.getString("KullanıcıAdı"),
                    belge.getString("Parola"));
            return kullanıcı;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Kullanıcının gerçek adına erişim sağlar.
     * 
     * @return  Kullanıcının gerçek adı
     */
    @JsonGetter("Ad")
    public String getAd()
    {
        return this.ad;
    }
    /**
     * Kullanıcının gerçek adını belirtilen yeni ad olarak değiştirir.
     * 
     * @param ad    Kullanıcının yeni gerçek adı.
     */
    public void setAd(String ad)
    {
        this.ad = ad;
    }
    
    /**
     * Kullanıcının rumuzuna erişim sağlar.
     * 
     * @return  Kullanıcının rumuzu
     */
    @JsonGetter("KullanıcıAdı")
    public String getKullanıcıAdı()
    {
        return this.kullanıcı_adı;
    }
    /**
     * Kullanıcıya yeni rumuz tanımlar.
     * 
     * @param kullanıcı_adı Yeni rumuz
     * @return  Yeni rumuz tanımlama başarılıysa true, başarısızsa false
     */
    public boolean setKullanıcıAdı(String kullanıcı_adı)
    {
        // Buraya ileride eşsizlik denetimi gelecek
        this.kullanıcı_adı = kullanıcı_adı;
        return true;
    }
    
    /**
     * Kullanıcının parolasının karılmış biçimine erişim sağlar.
     * 
     * @return  Kullanıcının karılmış parolası
     */
    @JsonGetter("Parola")
    public String getParola()
    {
        return this.parola;
    }
    
    /**
     * Bu kullanıcının başkahraman olduğu günlere erişim sağlar.
     * 
     * @return  Kullanıcının başkahraman olduğu günlerin sıralı bağlı listesi
     */
    @JsonIgnore
    public SıralıGünler getÖmür()
    {
        return this.ömür;
    }
    /**
     * Bu kullanıcının başkahraman olduğu günlerden birinin kimliğine
     * erişim sağlar.
     * 
     * @return  Kullanıcının başkahraman olduğu günlerden birinin kimliği
     */
    @JsonGetter("Ömür")
    public String getÖmrünKimliği()
    {
        if (this.ömür == null)
            return null;
        return this.ömür.getKimlikBase64();
    }
    /**
     * Bu kullanıcının başkahraman olduğu günlerin listesini tanımlar.
     * Verilen günlerin başkahramanı bu kişi değilse işlem yapılmaz.
     * 
     * @param ömür  Bu kullanıcının başkahraman olduğu günler
     * @return  Ömür yenilenirse true, yenilenmezse false
     */
    @JsonIgnore
    public boolean setÖmür(SıralıGünler ömür)
    {
        if (ömür == null)
        {
            return false;
        }
        
        if (this.getKimlikBase64().equals(ömür.getBaşkahraman()))
        {
            this.ömür = ömür;
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
     * Bu kullanıcı için yepyeni bir kimlik atar.
     */
    private void KimliğiYenile()
    {
        SecureRandom rast = new SecureRandom();
        this.kimlik = new byte[33];
        rast.nextBytes(this.kimlik);
    }
    /**
     * Bu kullanıcı için yepyeni bir eşsiz kimlik atar.
     * 
     * @param üye_vt    Eşsizlik denetimi için kullanıcı veri tabanı hizmeti
     */
    private void KimliğiYenile(KullanıcıVT üye_vt)
    {
        SecureRandom rast = new SecureRandom();
        byte[] yeni = new byte[33];
        rast.nextBytes(yeni);
        Base64.Encoder b64encoder = Base64.getEncoder();
        String yeni_b64 = b64encoder.encodeToString(yeni);
        
        byte kullanımda = üye_vt.KimlikKullanımda(yeni_b64);
        if (kullanımda == 0)
        {
            this.kimlik = yeni;
        }
        else
        {
            KimliğiYenile(üye_vt);
        }
    }
}
