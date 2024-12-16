/*
 * VeriTabanıAyar
 * 
 * Sürüm 0.1
 * 
 * Aralık 2024
 * 
 * Copyright (C) 2024 Yusuf Kozan, Şükrü Fırat Sarp
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services;

import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.StringBuilder;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

/**
 * Veri tabanına erişim için gerekli bilgileri tutan sınıf.
 */
public class VeriTabanıAyar
{
    /**
     * Veri tabanı sunucusunun adı. (en. host)
     */
    private String sunucu;
    /**
     * Veri tabanının sunulduğu liman. (en. port)
     */
    private String liman;
    /**
     * Veri tabanının adı.
     */
    private String veri_tabanı;
    /**
     * Veri tabanına erişim sağlayacak kullanıcı adı.
     */
    private String kullanıcı;
    /**
     * Veri tabanına erişim sağlayacak parola.
     */
    private String parola;
    
    @JsonCreator
    private VeriTabanıAyar(
            @JsonProperty("host") String sunucu,
            @JsonProperty("port") String liman,
            @JsonProperty("database") String veri_tabanı,
            @JsonProperty("user") String kullanıcı,
            @JsonProperty("password") String parola)
    {
        this.setSunucu(sunucu);
        this.setLiman(liman);
        this.setVeriTabanı(veri_tabanı);
        this.setKullanıcı(kullanıcı);
        this.setParola(parola);
    }
    
    /**
     * Belirtilen ayar belgesini okuyup yeni bir ayar nesnesi oluşturur.
     * 
     * @param dosya_yolu    Ayar belgesinin dosya sistemindeki konumu
     * @return  Belge içeriğinden yeni ayar nesnesi
     */
    public static VeriTabanıAyar AyarBelgesinden(String dosya_yolu)
    {
        File ayar_belgesi = new File(dosya_yolu);
        if (ayar_belgesi.exists() && !ayar_belgesi.isDirectory())
        {
            try
            {
                ObjectMapper haritacı = new ObjectMapper();
                VeriTabanıAyar ayarlar = haritacı.readValue(
                        ayar_belgesi,
                        VeriTabanıAyar.class
                );
                return ayarlar;
            }
            catch (Exception e)
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Ayar belgesindeki bilgileri kullanarak MongoDB'ye bağlanmak için url.
     * 
     * @return  MongoDB bağlantı dizesi
     */
    public String MongoBağlantıDizesi()
    {
        try
        {
            StringBuilder dize = new StringBuilder("mongodb://");
            dize.append(URLEncoder.encode(this.getKullanıcı(), "UTF-8"));
            dize.append(':');
            dize.append(URLEncoder.encode(this.getParola(), "UTF-8"));
            dize.append('@');
            dize.append(this.getSunucu());
            dize.append(':');
            dize.append(this.getLiman());
            dize.append('/');
            dize.append(this.getVeriTabanı());
            return dize.toString();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    /**
     * Veri tabanı sunucusunun adına erişim sağlar. (en. host)
     * @return  Sunucunun adı (host name)
     */
    @JsonGetter("host")
    public String getSunucu()
    {
        return sunucu;
    }
    /**
     * Veri tabanı sunucusunun adını belirler. (en. host)
     * @param sunucu    Sunucunun adı (host name)
     */
    @JsonSetter("host")
    public void setSunucu(String sunucu) {
        this.sunucu = sunucu.trim();
    }
    /**
     * Veri tabanının sunulduğu limana erişim sağlar. (en. port)
     * @return  Veri tabanının sunulduğu liman (port)
     */
    @JsonGetter("port")
    public String getLiman() {
        return liman;
    }
    /**
     * Veri tabanının sunulduğu limanı belirler. (en. port)
     * @param liman Veri tabanının sunulduğu liman (port)
     */
    @JsonSetter("port")
    public void setLiman(String liman) {
        this.liman = liman.trim();
    }
    /**
     * Veri tabanının adına erişim sağlar.
     * @return  Veri tabanının adı
     */
    @JsonGetter("database")
    public String getVeriTabanı() {
        return veri_tabanı;
    }
    /**
     * Veri tabanının adını belirler.
     * @param veri_tabanı   Veri tabanının adı
     */
    @JsonSetter("database")
    public void setVeriTabanı(String veri_tabanı) {
        this.veri_tabanı = veri_tabanı.trim();
    }
    /**
     * Veri tabanına erişim sağlayacak kullanıcı adına erişim sağlar.
     * @return  Veri tabanı için kullanıcı adı
     */
    @JsonGetter("user")
    public String getKullanıcı() {
        return kullanıcı;
    }
    /**
     * Veri tabanına erişim sağlayacak kullanıcı adını belirler.
     * @param kullanıcı Veri tabanı için kullanıcı adı
     */
    @JsonSetter("user")
    public void setKullanıcı(String kullanıcı) {
        this.kullanıcı = kullanıcı.trim();
    }
    /**
     * Veri tabanına erişim sağlayacak parolaya erişim sağlar.
     * @return  Veri tabanının parolası
     */
    @JsonGetter("password")
    public String getParola() {
        return parola;
    }

    /**
     * Veri tabanına erişim sağlayacak parolayı belirler.
     * @param parola    Veri tabanının parolası
     */
    @JsonSetter("password")
    public void setParola(String parola) {
        this.parola = parola;
    }
    
    
}
