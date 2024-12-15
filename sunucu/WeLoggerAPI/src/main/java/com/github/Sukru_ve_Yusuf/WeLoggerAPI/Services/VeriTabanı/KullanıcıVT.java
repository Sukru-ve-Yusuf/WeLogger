/*
 * KullanıcıVT
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
 * Kullanıcı nesnelerinin veri tabanı işlemleri için bir hizmet sınıfı.
 * 
 * @see Kullanıcı
 */
public class KullanıcıVT
{
    /**
     * Uygulamada yer alan veri tabanı hizmetlerine erişim sağlar.
     * 
     * @see VeriTabanıHizmetleri
     */
    private VeriTabanıHizmetleri VT;
    /**
     * Veri tabanında üyelerin tutulduğu koleksiyonun adı.
     */
    private final String KoleksiyonAdı = "kullanıcı";
    
    /**
     * Yanlış oluşturmayı önlemek için gizli başlatıcı.
     */
    private KullanıcıVT() {}
    
    /**
     * Belirtilen veri tabanı hizmetlerine bağlı yeni kullanıcı veri tabanı
     * hizmeti başlatır.
     * 
     * @param vt_hizmetleri Uygulamada kullanılan veri tabanı hizmetleri
     * @return  Yeni kullanıcı veri tabanı hizmeti
     * 
     * @see VeriTabanıHizmetleri
     */
    public static KullanıcıVT Başlat(VeriTabanıHizmetleri vt_hizmetleri)
    {
        if (vt_hizmetleri == null)
            return null;
        
        KullanıcıVT üye_vt = new KullanıcıVT();
        üye_vt.VT = vt_hizmetleri;
        return üye_vt;
    }
    
    /**
     * Verilen kullanıcıyı veri tabanına kaydeder.
     * 
     * @param eklenecek Veri tabanına kaydedilecek kullanıcı
     * @return  Kayıt başarılı olursa true, başarısız olursa false
     */
    public boolean KullanıcıEkle(Kullanıcı eklenecek)
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
     * Kimliği belirtilen kullanıcıyı ömrüyle birlikte veri tabanından getirir.
     * 
     * @param kimlik    Base64 metni olarak, istenen kullanıcının kimliği
     * @return  Ömrüyle birlikte, kimliği verilen kullanıcı.
     *          Kullanıcı bulunamazsa null.
     */
    public Kullanıcı KullanıcıOku(String kimlik)
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
            
            Kullanıcı bulunan = Kullanıcı.BSONBelgesinden(sonuç);
            bulunan.setÖmür(VT.getGünVT().GünleriOku(sonuç.getString("Ömür")));
            return bulunan;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
}
