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
import java.lang.*;
import org.bson.*;
import org.bson.types.*;
import org.bson.conversions.Bson;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.*;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.model.*;

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
     * @return  Oturum açıksa 1, bitmişse ya da yoksa 0, henüz başlamamışsa 2,
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

                Long baş = oturum.getLong("Baş");
                if (baş == null)
                    return 0;
                Calendar başlangıç = new Calendar.Builder()
                        .setInstant(baş).build();
                
                Long son = oturum.getLong("Son");
                if (son == null)
                    return 0;
                Calendar bitiş = new Calendar.Builder()
                        .setInstant(son).build();

                Calendar şimdi = Calendar.getInstance();
                
                if (baş > son)
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
    /**
     * Belirtilen oturumu veri tabanına kaydeder.
     * 
     * @param yeni_oturum   Kaydedilecek oturum
     * @return  Kayıt başarılı olursa true, olmazsa false
     */
    public boolean OturumKaydet(Oturum yeni_oturum)
    {
        if (yeni_oturum == null)
            return false;
        
        try (MongoClient istemci = MongoClients.create(VT.getBağlantıDizesi()))
        {
            MongoDatabase veri_tabanı = istemci.getDatabase(
                    VT.getVeriTabanıAdı());
            MongoCollection<Document> koleksiyon = veri_tabanı.getCollection(
                    this.KoleksiyonAdı);
            
            try
            {
                ObjectMapper haritacı = new ObjectMapper();
                String eklenecek_json = haritacı
                        .writeValueAsString(yeni_oturum);
                Document eklenecek_doc = Document.parse(eklenecek_json);
                if (eklenecek_doc == null)
                {
                    return false;
                }
                InsertOneResult ekleme = koleksiyon.insertOne(eklenecek_doc);
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }
    /**
     * Kimliği ve kullanıcısı belirtilen oturumu kapatır.
     * 
     * @param oturum_kimliği    Kapanacak oturumun kimliği
     * @param kullanıcı_kimliği Kapanacak oturumun kullanıcısının kimliği
     * @return  Kapatma başarılı olursa true, başarısız olursa false
     */
    public boolean OturumKapat(String oturum_kimliği, String kullanıcı_kimliği)
    {
        if (oturum_kimliği == null || oturum_kimliği.isBlank())
            return false;
        if (kullanıcı_kimliği == null || kullanıcı_kimliği.isBlank())
            return false;
        
        try (MongoClient istemci = MongoClients.create(VT.getBağlantıDizesi()))
        {
            MongoDatabase veri_tabanı = istemci.getDatabase(
                    VT.getVeriTabanıAdı());
            MongoCollection<Document> koleksiyon = veri_tabanı
                    .getCollection(this.KoleksiyonAdı);
            
            Bson sorgu = and(eq("_id", oturum_kimliği),
                    eq("Kullanıcı", kullanıcı_kimliği));
            Calendar şimdi = Calendar.getInstance();
            Bson güncelleme = Updates.set("Son", şimdi.getTimeInMillis());
            
            try
            {
                UpdateResult sonuç = koleksiyon.updateOne(sorgu, güncelleme);
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
