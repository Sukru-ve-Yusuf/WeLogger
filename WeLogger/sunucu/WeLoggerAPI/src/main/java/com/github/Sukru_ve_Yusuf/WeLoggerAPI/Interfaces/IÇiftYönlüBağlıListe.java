/*
 * IÇiftYönlüBağlıListe
 * 
 * Sürüm 0.1
 * 
 * Aralık 2024
 * 
 * Copyright (C) 2024 Yusuf Kozan, Şükrü Fırat Sarp
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Interfaces;

/**
 * Çift yönlü bağlı liste olarak tutulması gereken nesneler için bir arayüz.
 * IBağlıListe arayüzünün genişletilmiş bir biçimidir.
 * 
 * @param <T>   Bağlı listede tutulacak verinin türü
 * @see IBağlıListe
 */
public interface IÇiftYönlüBağlıListe<T> extends IBağlıListe<T>
{
    @Override
    public IÇiftYönlüBağlıListe<T> DüğümEkle(T yeni_veri);
    
    /**
     * Bağlı listenin ilk düğümünü döndürür.
     * 
     * @return  Bağlı listenin kök düğümü
     */
    public IÇiftYönlüBağlıListe<T> Kök();
    
    @Override
    public IÇiftYönlüBağlıListe<T> UçDüğüm();
    
    /**
     * Düğümün bağlarını keser ve listenin kalanlarını birbirine bağlar.
     * 
     * @return  Kalan listeden bir düğüm
     */
    public IÇiftYönlüBağlıListe<T> Kopar();
}
