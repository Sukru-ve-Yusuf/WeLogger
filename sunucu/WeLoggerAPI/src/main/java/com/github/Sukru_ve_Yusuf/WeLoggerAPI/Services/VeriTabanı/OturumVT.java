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
    
    /**
     * Belirtilen oturum kimliğinin kullanımda olup olmadığını denetler.
     * 
     * @param kimlik    Aranan oturum kimliği
     * @return  Kimlik kullanımdaysa 1, değilse 0, hata olursa -1
     */
    public byte KimlikKullanımda(String kimlik)
    {
        if (kimlik == null || kimlik.isBlank())
            return -1;
        
        try (MongoClient istemci = MongoClients.create(VT.getBağlantıDizesi()))
        {
            MongoDatabase veri_tabanı = istemci.getDatabase(
                    VT.getVeriTabanıAdı());
            MongoCollection<Document> koleksiyon = veri_tabanı.getCollection(
                    this.KoleksiyonAdı);
            
            try
            {
                long nicelik = koleksiyon.countDocuments(eq("_id", kimlik));
                if (nicelik == 0)
                    return 0;
                return 1;
            }
            catch (Exception e)
            {
                return -1;
            }
        }
        catch (Exception e)
        {
            return -1;
        }
    }
    
    /**
     * Belirtilen oturumun açık olup olmadığını denetler.
     * 
     * @param kimlik    Denetlenen oturumun kimliği
     * @param kullanıcı Denetlenen oturumun kullanıcısı
     * @return  Oturum açıksa 1, bitmişse 0, henüz başlamamışsa 2,
     *          hata olursa -1
     */
    public byte OturumAçık(String kimlik, String kullanıcı)
    {
        if (kimlik == null || kimlik.isBlank())
            return -1;
        if (kullanıcı == null || kullanıcı.isBlank())
            return -1;
        
        try (MongoClient istemci = MongoClients.create(VT.getBağlantıDizesi()))
        {
            MongoDatabase veri_tabanı = istemci.getDatabase(
                    VT.getVeriTabanıAdı());
            MongoCollection<Document> koleksiyon = veri_tabanı.getCollection(
                    this.KoleksiyonAdı);
            
            try
            {
                Document oturum = koleksiyon.find(and(
                        eq("_id", kimlik),
                        eq("Kullanıcı", kullanıcı))).first();
                if (oturum == null)
                    return 0;

                long baş = oturum.get("Baş", long.class);
                Calendar başlangıç = new Calendar.Builder()
                        .setInstant(baş).build();
                
                long son = oturum.get("Son", long.class);
                Calendar bitiş = new Calendar.Builder()
                        .setInstant(son).build();

                Calendar şimdi = Calendar.getInstance();
                
                if (başlangıç.after(bitiş))
                    return -1;
                
                if (şimdi.before(başlangıç))
                    return 2; // Başlamamış
                
                if (şimdi.before(bitiş))
                    return 1; // Açık
                
                return 0; // Bitmiş
                
            }
            catch (Exception e)
            {
                return -1;
            }
        }
        catch (Exception e)
        {
            return -1;
        }
    }
}
