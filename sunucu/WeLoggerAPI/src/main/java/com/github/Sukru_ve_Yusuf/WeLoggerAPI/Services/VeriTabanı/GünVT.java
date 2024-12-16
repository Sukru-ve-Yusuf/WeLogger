/*
 * GünVT
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
 * Günlerin veri tabanı işlemleri için bir hizmet sınıfı.
 * 
 * @see SıralıGünler
 * @see Gün
 */
public class GünVT
{
    /**
     * Uygulamada yer alan veri tabanı hizmetlerine erişim sağlar.
     * 
     * @see VeriTabanıHizmetleri
     */
    private VeriTabanıHizmetleri VT;
    /**
     * Veri tabanında günlerin tutulduğu koleksiyonun adı.
     */
    private final String KoleksiyonAdı = "gün";
    
    /**
     * Yanlış oluşturmayı önlemek için gizli başlatıcı.
     */
    private GünVT() {}
    
    /**
     * Belirtilen veri tabanı hizmetlerine bağlı yeni gün veri tabanı
     * hizmeti başlatır.
     * 
     * @param vt_hizmetleri Uygulamada kullanılan veri tabanı hizmetleri
     * @return  Yeni gün veri tabanı hizmeti
     * 
     * @see VeriTabanıHizmetleri
     */
    public static GünVT Başlat(VeriTabanıHizmetleri vt_hizmetleri)
    {
        if (vt_hizmetleri == null)
            return null;
        
        GünVT gün_vt = new GünVT();
        gün_vt.VT = vt_hizmetleri;
        return gün_vt;
    }
    
    /**
     * Belirtilen kimlikte bir gün olup olmadığını denetler.
     * 
     * @param kimlik    Aranan gün kimliği
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
            MongoCollection<Document> koleksiyon = veri_tabanı
                    .getCollection(this.KoleksiyonAdı);
            
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
     * Verilen günü, geçmişini ve geleceğini pek umursamadan, tek başına
     * veri tabanına kaydeder.
     * 
     * @param eklenecek Veri tabanına kaydedilecek gün
     * @return  Kayıt başarılı olursa true, başarısız olursa false
     */
    public boolean TekGünEkle(SıralıGünler eklenecek)
    {
        if (eklenecek == null)
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
                String eklenecek_json = haritacı.writeValueAsString(eklenecek);
                Document eklenecek_doc = Document.parse(eklenecek_json);
                if (eklenecek_doc == null)
                {
                    return false;
                }
                InsertOneResult ekleme = koleksiyon.insertOne(eklenecek_doc);
                return true;
            }
            catch (JsonProcessingException je)
            {
                je.printStackTrace();
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Kimliği belirtilen günü veri tabanından getirir.
     * Öncesi ve sonrası boş bırakılır.
     * 
     * @param kimlik    Base64 metni olarak, istenen günün kimliği
     * @return  Geçmişi ve geleceği olmadan, kimliği verilen gün.
     *          Gün bulunamazsa null.
     */
    public SıralıGünler TekGünOku(String kimlik)
    {
        if (kimlik == null)
            return null;
        try (MongoClient istemci = MongoClients.create(VT.getBağlantıDizesi()))
        {
            MongoDatabase veri_tabanı = istemci.getDatabase(
                    VT.getVeriTabanıAdı());
            MongoCollection<Document> koleksiyon = veri_tabanı.getCollection(
                    this.KoleksiyonAdı);
            
            Document sonuç = koleksiyon.find(eq("_id", kimlik)).first();
            if (sonuç == null)
            {
                return null;
            }
            else
            {
                SıralıGünler bulunan = SıralıGünler.BSONBelgesinden(sonuç);
                if (sonuç.getString("Videolar") != null)
                {
                    bulunan.setVideolar(VT.getVideoVT()
                            .VideolarıOku(sonuç.getString("Videolar")));
                }
                return bulunan;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Kimliği belirtilen günü geçmişi ve geleceğiyle birlikte
     * veri tabanınından getirir.
     * 
     * @param kimlik    Base64 metni olarak, istenen günün kimliği
     * @return  Geçmişi ve geleceğiyle birlikte, kimliği verilen gün.
     *          Gün bulunamazsa null.
     */
    public SıralıGünler GünleriOku(String kimlik)
    {
        if (kimlik == null)
            return null;
        try(MongoClient istemci = MongoClients.create(VT.getBağlantıDizesi()))
        {
            MongoDatabase veri_tabanı = istemci.getDatabase(
                    VT.getVeriTabanıAdı());
            MongoCollection<Document> koleksiyon = veri_tabanı.getCollection(
                    this.KoleksiyonAdı);
            
            Document sonuç = koleksiyon.find(eq("_id", kimlik)).first();
            if (sonuç == null)
            {
                return null;
            }
            SıralıGünler günler = SıralıGünler.BSONBelgesinden(sonuç);
            günler.setVideolar(VT.getVideoVT()
                    .VideolarıOku(sonuç.getString("Videolar")));
            
            String önceki_kimlik = sonuç.getString("Önceki");
            if (önceki_kimlik != null && !önceki_kimlik.isBlank())
            {
                Document önceki = koleksiyon
                        .find(eq("_id", önceki_kimlik))
                        .first();
                if (önceki != null)
                {
                    SıralıGünler ögün = SıralıGünler.BSONBelgesinden(önceki);
                    ögün.setVideolar(VT.getVideoVT()
                            .VideolarıOku(önceki.getString("Videolar")));
                    günler.DüğümEkle(ögün);
                    önceki_kimlik = önceki.getString("Önceki");
                    while (önceki_kimlik != null && !önceki_kimlik.isBlank())
                    {
                        önceki = koleksiyon
                                .find(eq("_id", önceki_kimlik))
                                .first();
                        if (önceki == null)
                            break;
                        ögün = SıralıGünler.BSONBelgesinden(önceki);
                        ögün.setVideolar(VT.getVideoVT()
                                .VideolarıOku(önceki.getString("Videolar")));
                        günler.DüğümEkle(ögün);
                        önceki_kimlik = önceki.getString("Önceki");
                    }
                }
            }
            
            String sonraki_kimlik = sonuç.getString("Sonraki");
            if (sonraki_kimlik != null && !sonraki_kimlik.isBlank())
            {
                Document sonraki = koleksiyon
                        .find(eq("_id", sonraki_kimlik))
                        .first();
                if (sonraki != null)
                {
                    SıralıGünler sgün = SıralıGünler.BSONBelgesinden(sonraki);
                    sgün.setVideolar(VT.getVideoVT()
                            .VideolarıOku(sonraki.getString("Videolar")));
                    günler.DüğümEkle(sgün);
                    sonraki_kimlik = sonraki.getString("Sonraki");
                    while (sonraki_kimlik != null && !sonraki_kimlik.isBlank())
                    {
                        sonraki = koleksiyon
                                .find(eq("_id", sonraki_kimlik))
                                .first();
                        if (sonraki == null)
                            break;
                        sgün = SıralıGünler.BSONBelgesinden(sonraki);
                        sgün.setVideolar(VT.getVideoVT()
                                .VideolarıOku(sonraki.getString("Videolar")));
                        günler.DüğümEkle(sgün);
                        sonraki_kimlik = sonraki.getString("Sonraki");
                    }
                }
            }
            return günler;
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
