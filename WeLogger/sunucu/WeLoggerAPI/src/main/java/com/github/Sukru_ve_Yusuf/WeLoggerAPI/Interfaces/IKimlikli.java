/*
 * IKimlikli
 * 
 * Sürüm 0.1
 * 
 * Aralık 2024
 * 
 * Copyright (C) 2024 Yusuf Kozan, Şükrü Fırat Sarp
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Interfaces;

/**
 * Eşsiz kimliğe ihtiyaç duyan nesneler için yapılmış bir arayüz.
 */
public interface IKimlikli
{
    /**
     * Kimliği ikili sayı biçiminde döndürür.
     * 
     * @return Byte dizisi olarak kimlik
     */
    public byte[] getKimlikBytes();
    
    /**
     * Kimliği Base64 metni olarak döndürür.
     * 
     * @return Base64 String olarak kimlik
     */
    public String getKimlikBase64();
}
