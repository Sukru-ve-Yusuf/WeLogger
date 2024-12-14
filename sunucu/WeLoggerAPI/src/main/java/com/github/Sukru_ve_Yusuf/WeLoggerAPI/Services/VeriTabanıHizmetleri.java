/*
 * VeriTabanıHizmetleri
 * 
 * Sürüm 0.1
 * 
 * Aralık 2024
 * 
 * Copyright (C) 2024 Yusuf Kozan, Şükrü Fırat Sarp
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services;

import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.VeriTabanı.*;
import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Models.*;

/**
 *  Uygulamada kullanılacak tüm veri tabanı hizmetlerini başlatıp
 *  bir arada tutan bir sınıf.
 */
public class VeriTabanıHizmetleri
{
    /**
     * Veri tabanı bağlantılarında kullanılacak bağlantı dizesi.
     * 
     * @see VeriTabanıAyar#MongoBağlantıDizesi()
     * @see VeriTabanıAyar
     */
    private String bağlantı_dizesi;
    /**
     * Kullanılacak veri tabanının adı.
     * 
     * @see VeriTabanıAyar#veri_tabanı
     * @see VeriTabanıAyar
     */
    private String veri_tabanı_adı;
    /**
     * Veri tabanındaki videolara erişim sağlayan hizmet
     * 
     * @see VideoVT
     * @see SıralıVideolar
     * @see Video
     */
    private VideoVT video_vt;
    
    /**
     * Veri tabanı hizmetlerini başlatıp bir araya getirir.
     * 
     * @param ayar_belgesinin_yolu  Bağlantı ayarlarının olduğu JSON belgesi
     * @return  Yeni bir VeriTabanıHizmetleri nesnesi
     */
    public static VeriTabanıHizmetleri HizmetleriBaşlat(
            String ayar_belgesinin_yolu)
    {
        VeriTabanıAyar ayarlar = VeriTabanıAyar.AyarBelgesinden(
                ayar_belgesinin_yolu);
        if (ayarlar == null)
        {
            return null;
        }
        String bağlantı_dizesi = ayarlar.MongoBağlantıDizesi();
        if (bağlantı_dizesi.isBlank())
        {
            return null;
        }
        
        VeriTabanıHizmetleri hizmetler = new VeriTabanıHizmetleri();
        hizmetler.bağlantı_dizesi = bağlantı_dizesi;
        hizmetler.veri_tabanı_adı = ayarlar.getVeriTabanı();
        
        VideoVT video_vthizmeti = VideoVT.Başlat(hizmetler);
        if (video_vthizmeti == null)
            return null;
        hizmetler.video_vt = video_vthizmeti;
        
        return hizmetler;
    }
    
    /**
     * Veri tabanı bağlantı dizesine erişim sağlar.
     * 
     * @return  Veri tabanı bağlantı dizesi
     */
    public String getBağlantıDizesi()
    {
        return this.bağlantı_dizesi;
    }
    /**
     * Veri tabanının adına erişim sağlar.
     * 
     * @return  Veri tabanının adı
     */
    public String getVeriTabanıAdı()
    {
        return this.veri_tabanı_adı;
    }
    
    /**
     * Veri tabanındaki videolara erişim sağlayan hizmete erişim sağlar.
     * 
     * @return  Video veri tabanı hizmeti
     * @see VideoVT
     */
    public VideoVT getVideoVT()
    {
        return this.video_vt;
    }
}
