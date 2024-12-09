/*
 * IBağlıListe
 * 
 * Sürüm 0.1
 * 
 * Aralık 2024
 * 
 * Copyright (C) 2024 Yusuf Kozan, Şükrü Fırat Sarp
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Interfaces;

/**
 * Bağlı liste olarak tutulması gereken nesneler için bir arayüz.
 * 
 * @param <T>   Bağlı listede tutulacak verinin türü
 */
public interface IBağlıListe<T>
{
    /**
     * Bağlı listeye yeni eklenecek veri için düğüm yaratır ve listeye ekler.
     * 
     * @param yeni_veri Yeni düğümün tutacağı veri
     * @return  Oluşturulan yeni düğüm
     */
    public IBağlıListe<T> DüğümEkle(T yeni_veri);
    
    /**
     * Bağlı listenin son düğümünü döndürür.
     * 
     * @return Bağlı listenin en uç düğümü
     */
    public IBağlıListe<T> UçDüğüm();
}
