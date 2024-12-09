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
import java.util.*;
import java.security.SecureRandom;

/**
 * Üye olan her bir kullanıcıyı temsil eden sınıf.
 */
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
     * Kullanıcının gerçek adına erişim sağlar.
     * 
     * @return  Kullanıcının gerçek adı
     */
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
     * Bu kullanıcının başkahraman olduğu günlere erişim sağlar.
     * 
     * @return  Kullanıcının başkahraman olduğu günlerin sıralı bağlı listesi
     */
    public SıralıGünler getÖmür()
    {
        return this.ömür;
    }
    /**
     * Bu kullanıcının başkahraman olduğu günlerin listesini tanımlar.
     * Verilen günlerin başkahramanı bu kişi değilse işlem yapılmaz.
     * 
     * @param ömür  Bu kullanıcının başkahraman olduğu günlerin sıralı listesi
     * @return  Ömür yenilenirse true, yenilenmezse false
     */
    public boolean setÖmür(SıralıGünler ömür)
    {
        Kullanıcı bu_kişi = this;
        Kullanıcı kahraman = ömür.getBaşkahraman();
        
        if (bu_kişi.getKimlikBase64() == kahraman.getKimlikBase64())
        {
            this.ömür = ömür;
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
