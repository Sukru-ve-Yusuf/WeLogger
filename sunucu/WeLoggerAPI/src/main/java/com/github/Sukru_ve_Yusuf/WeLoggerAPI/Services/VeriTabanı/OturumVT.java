/*
 * OturumVT
 * 
 * Sürüm 0.1
 * 
 * Aralık 2024
 * 
 * Copyright (C) 2024 Yusuf Kozan, Şükrü Fırat Sarp
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.VeriTabanı;

import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.*;
import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Models.*;

import java.util.*;
import org.bson.*;
import org.bson.types.*;
import org.bson.conversions.Bson;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.*;
import static com.mongodb.client.model.Filters.*;

/**
 * Oturumların veri tabanı sorguları için bir hizmet sınıfı.
 * 
 * @see Oturum
 */
public class OturumVT
{
    /**
     * Uygulamada yer alan veri tabanı hizmetlerine erişim sağlar.
     * 
     * @see VeriTabanıHizmetleri
     */
    private VeriTabanıHizmetleri VT;
    /**
     * Veri tabanında oturumların tutulduğu koleksiyonun adı.
     */
    private final String KoleksiyonAdı = "oturum";
    
    /**
     * Yanlış oluşturmayı önlemek için gizli başlatıcı.
     */
    private OturumVT() {}
    
    /**
     * Belirtilen veri tabanı hizmetlerine bağlı yeni oturum veri tabanı
     * hizmeti başlatır.
     * 
     * @param vt_hizmetleri Uygulamada kullanılan veri tabanı hizmetleri
     * @return  Yeni oturum veri tabanı hizmeti
     * 
     * @see VeriTabanıHizmetleri
     */
    public static OturumVT Başlat(VeriTabanıHizmetleri vt_hizmetleri)
    {
        if (vt_hizmetleri == null)
            return null;
        
        OturumVT oturum_vt = new OturumVT();
        oturum_vt.VT = vt_hizmetleri;
        return oturum_vt;
    }
}
