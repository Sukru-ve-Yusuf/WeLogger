/*
 * VideoVT
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
 * Videoların veri tabanı sorguları için bir hizmet sınıfı.
 * 
 * @see SıralıVideolar
 * @see Video
 */
public class VideoVT
{
    /**
     * Uygulamada yer alan veri tabanı hizmetlerine erişim sağlar.
     * 
     * @see VeriTabanıHizmetleri
     */
    private VeriTabanıHizmetleri VT;
    /**
     * Veri tabanında videoların tutulduğu koleksiyonun adı.
     */
    private final String KoleksiyonAdı = "video";
    
    /**
     * Yanlış oluşturmayı önlemek için gizli başlatıcı.
     */
    private VideoVT() {}
    
    /**
     * Belirtilen veri tabanı hizmetleriyle bağlı yeni video veri tabanı
     * hizmeti başlatır.
     * 
     * @param vt_hizmetleri Uygulamada kullanılan veri tabanı hizmetleri
     * @return  Yeni video veritabanı hizmeti
     * 
     * @see VeriTabanıHizmetleri
     */
    public static VideoVT Başlat(VeriTabanıHizmetleri vt_hizmetleri)
    {
        if (vt_hizmetleri == null)
            return null;
        
        VideoVT video_vt = new VideoVT();
        video_vt.VT = vt_hizmetleri;
        return video_vt;
    }
    
    /**
     * Verilen videoyu, geçmişini ve geleceğini pek umursamadan, tek başına
     * veri tabanına kaydeder.
     * 
     * @param eklenecek Veri tabanına kaydedilecek video
     * @return  Kayıt başarılı olursa true, başarısız olursa false
     */
    public boolean TekVideoEkle(SıralıVideolar eklenecek)
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
     * Kimliği belirtilen videoyu veri tabanından getirir.
     * Öncesi ve sonraki boş bırakılır.
     * 
     * @param kimlik    Base64 metni olarak, istenen videonun kimliği
     * @return  Geçmişi ve geleceği olmadan, kimliği verilen video.
     *          Video bulunamazsa null.
     */
    public SıralıVideolar TekVideoOku(String kimlik)
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
                SıralıVideolar bulunan = SıralıVideolar.BSONBelgesinden(sonuç);
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
     * Kimliği belirtilen videoyu geçmişi ve geleceğiyle birlikte
     * veri tabanından getirir.
     * 
     * @param kimlik    Base64 metni olarak, istenen videonun kimliği
     * @return  Geçmişi ve geleceğiyle birlikte, kimliği verilen video.
     *          Video bulunamazsa null.
     */
    public SıralıVideolar VideolarıOku(String kimlik)
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
            SıralıVideolar videolar = SıralıVideolar.BSONBelgesinden(sonuç);
            
            String önceki_kimlik = sonuç.getString("Önceki");
            if (önceki_kimlik != null && !önceki_kimlik.isBlank())
            {
                Document önceki = koleksiyon
                        .find(eq("_id", önceki_kimlik))
                        .first();
                if (önceki != null)
                {
                    SıralıVideolar övid = SıralıVideolar
                            .BSONBelgesinden(önceki);
                    videolar.DüğümEkle(övid);
                    önceki_kimlik = önceki.getString("Önceki");
                    while (önceki_kimlik != null && !önceki_kimlik.isBlank())
                    {
                        önceki = koleksiyon
                                .find(eq("_id", önceki_kimlik))
                                .first();
                        if (önceki == null)
                            break;
                        övid = SıralıVideolar.BSONBelgesinden(önceki);
                        videolar.DüğümEkle(övid);
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
                    SıralıVideolar svid = SıralıVideolar
                            .BSONBelgesinden(sonraki);
                    videolar.DüğümEkle(svid);
                    sonraki_kimlik = sonraki.getString("Sonraki");
                    while (sonraki_kimlik != null && !sonraki_kimlik.isBlank())
                    {
                        sonraki = koleksiyon
                                .find(eq("_id", sonraki_kimlik))
                                .first();
                        if (sonraki == null)
                            break;
                        svid = SıralıVideolar.BSONBelgesinden(sonraki);
                        videolar.DüğümEkle(svid);
                        sonraki_kimlik = sonraki.getString("Sonraki");
                    }
                }
            }
            return videolar;
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
