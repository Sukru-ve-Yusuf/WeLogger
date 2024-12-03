/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Interfaces;

/**
 * Eşsiz kimliğe ihtiyaç duyan nesneler için yapılmış bir arayüz
 * 
 * @author yusuf
 */
public interface IKimlikli {
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
