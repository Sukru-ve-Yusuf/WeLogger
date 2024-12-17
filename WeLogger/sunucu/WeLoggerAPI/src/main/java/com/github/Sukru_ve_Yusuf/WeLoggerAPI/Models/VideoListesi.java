/*
 * VideoListesi
 *
 * Sürüm 0.1
 * 
 * Aralık 2024
 * 
 * Copyright (C) 2024 Yusuf Kozan, Şükrü Fırat Sarp
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Models;

import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Interfaces.*;

/**
 * Video türündeki nesneleri tutan bir bağlı liste.
 * 
 * @see Video
 * @see IBağlıListe
 */
public class VideoListesi implements IBağlıListe<Video>
{
    /**
     * Düğümde tutulan Video nesnesi.
     */
    private Video veri;
    /**
     * Listenin bir sonraki düğümü.
     */
    private VideoListesi sonraki;
    
    /**
     * Video nesnesinden yeni liste oluşturur.
     * 
     * @param video Düğümde bulunacak video nesnesi
     * @see Video
     */
    public VideoListesi(Video video)
    {
        this.setVeri(video);
        this.setSonraki(null);
    }
    /**
     * Video nesnesinden, devamı bilinen bir liste oluşturur.
     * 
     * @param video     Düğümde bulunacak video nesnesi
     * @param sonraki   VideoListesi türünde bir sonraki düğüm
     * @see Video
     */
    public VideoListesi(Video video, VideoListesi sonraki)
    {
        this.setVeri(video);
        this.setSonraki(sonraki);
    }
    
    /**
     * Düğümdeki video nesnesine erişim sağlar.
     * 
     * @return Düğümde tutulan video nesnesi
     * @see #veri
     * @see Video
     */
    public Video getVeri()
    {
        return this.veri;
    }
    /**
     * Düğümdeki video nesnesini başka bir nesneyle değiştirir.
     * 
     * @param yeni_veri Düğümde bulunacak yeni video nesnesi
     * @see #veri
     * @see Video
     */
    public void setVeri(Video yeni_veri)
    {
        veri = yeni_veri;
    }
    
    /**
     * Listenin bir sonraki düğümüne erişim sağlar.
     * 
     * @return Listedeki bir sonraki düğüm
     * @see #sonraki
     */
    public VideoListesi getSonraki()
    {
        return this.sonraki;
    }
    /**
     * Listenin devamıyla bağı koparıp, belirtilen yeni düğümden devam ettirir.
     * 
     * @param yeni_sonraki  Listenin devamının yerine geçecek yeni düğüm
     * @see #sonraki
     */
    public void setSonraki(VideoListesi yeni_sonraki)
    {
        this.sonraki = yeni_sonraki;
    }
    
    /**
     * Belirtilen video nesnesini tutan bir düğümü listenin sonuna ekler.
     * 
     * @param yeni_veri Listeye eklenecek Video nesnesi
     * @return  Listenin son düğümü
     * @see Video
     * @see IBağlıListe#DüğümEkle(java.lang.Object) 
     */
    @Override
    public VideoListesi DüğümEkle(Video yeni_veri)
    {
        VideoListesi kafa = this;
        while (kafa.getSonraki() != null)
        {
            kafa = kafa.getSonraki();
        }
        VideoListesi yeni_düğüm = new VideoListesi(yeni_veri);
        kafa.setSonraki(yeni_düğüm);
        return kafa.getSonraki();
    }
    
    /**
     * Listenin sonundaki düğümü döndürür.
     * 
     * @return  Listenin son düğümü
     * @see IBağlıListe#UçDüğüm() 
     */
    @Override
    public VideoListesi UçDüğüm()
    {
        VideoListesi kafa = this;
        while (kafa.getSonraki() != null)
        {
            kafa = kafa.getSonraki();
        }
        return kafa;
    }
}
