/*
 * SıralıVideolar
 * 
 * Sürüm 0.1
 * 
 * Aralık 2024
 * 
 * Copyright (C) 2024 Yusuf Kozan, Şükrü Fırat Sarp
 */
package com.github.Sukru_ve_Yusuf.WeLoggerAPI.Models;

import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Interfaces.*;
import com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.VeriTabanı.*;
import java.util.*;
import com.fasterxml.jackson.annotation.*;
import org.bson.Document;

/**
 * Videoları geçmişten geleceğe doğru sıralanmış
 * çift yönlü bağlı liste olarak tutan sınıf.
 * 
 * @see Video
 * @see IÇiftYönlüBağlıListe
 */
@JsonPropertyOrder({"_id", "DosyaYolu", "Açıklama", "İye", "Tarih",
    "Önceki", "Sonraki"})
public class SıralıVideolar extends Video 
        implements IÇiftYönlüBağlıListe<SıralıVideolar>
{
    /**
     * Tarihsel sıralamada bir geçmişteki video.
     */
    private SıralıVideolar önceki;
    /**
     * Tarihsel sıralamada bir gelecekteki video.
     */
    private SıralıVideolar sonraki;
    
    /**
     * Verilen kimlikle boş bir sıralı video nesnesi oluşturur.
     * 
     * @param kimlik_base64 Base64 metni olarak videonun kimliği
     */
    protected SıralıVideolar(String kimlik_base64)
    {
        super(kimlik_base64);
    }
    /**
     * Verilen bilgilerle hiçbir yere bağlı olmayan yepyeni bir video oluşturur.
     * Oluşturulan nesneye yeni bir kimlik atanır.
     * 
     * @param dosya_yolu    Temsil edilen video dosyasının adresi
     * @param açıklama      Kullanıcının video hakkındaki açıklama metni
     * @param iye           Videonun sahibi olan kullanıcı
     * @param tarih         Videonun çekildiği tarih ve saat bilgisi
     */
    public SıralıVideolar(String dosya_yolu, String açıklama,
            Kullanıcı iye, Calendar tarih)
    {
        super(dosya_yolu, açıklama, iye, tarih);
        this.önceki = null;
        this.sonraki = null;
    }
    /**
     * Verilen bilgilerle yeni bir sıralı video nesnesi oluşturur.
     * Oluşturulan nesneye yeni bir eşsiz kimlik atanır.
     * 
     * @param dosya_yolu        Videonun dosya sistemindeki konumu
     * @param açıklama          Kullanıcının video hakkında açıklama metni
     * @param kullanıcı_kimliği Videonun sahibinin kullanıcı kimliği
     * @param tarih             Videonun çekildiği tarih ve saat bilgisi
     * @param video_vt          Kimlik eşsizliği video veri tabanı hizmeti
     */
    public SıralıVideolar(String dosya_yolu, String açıklama,
            String kullanıcı_kimliği, Calendar tarih, VideoVT video_vt)
    {
        super(dosya_yolu, açıklama, kullanıcı_kimliği, tarih, video_vt);
        this.önceki = null;
        this.sonraki = null;
    }
    /**
     * Verilen bilgilerle yeni bir sıralı video nesnesi oluşturur.
     * 
     * @param dosya_yolu    Temsil edilen video dosyasının adresi
     * @param açıklama      Kullanıcının video hakkındaki açıklama metni
     * @param iye_base64    Base64 metni olarak videonun sahibinin kimliği
     * @param tarih         Videonun çekildiği tarih ve saat bilgisi
     * @param kimlik_base64 Base64 metni olarak videonun kimliği
     */
    @JsonCreator
    public SıralıVideolar(
            @JsonProperty("DosyaYolu") String dosya_yolu,
            @JsonProperty("Açıklama") String açıklama,
            @JsonProperty("İye") String iye_base64,
            @JsonProperty("Tarih") Calendar tarih,
            @JsonProperty("_id") String kimlik_base64)
    {
        super(dosya_yolu, açıklama, iye_base64, tarih, kimlik_base64);
        this.önceki = null;
        this.sonraki = null;
    }
    /**
     * Bir video nesnesinin içeriğinden yeni bir sıralı video nesnesi oluşturur.
     * İsteğe bağlı olarak kimlik korunabilir ya da yenilenebilir.
     * 
     * @param kaynak_video      İçeriği alınacak video nesnesi
     * @param kimlik_yenilensin true ise yeni bir kimlik atanır,
     *                          false ise kaynak nesnenin kimliği kopyalanır.
     */
    public SıralıVideolar(Video kaynak_video, boolean kimlik_yenilensin)
    {
        super(kaynak_video, kimlik_yenilensin);
        this.önceki = null;
        this.sonraki = null;
    }
    
    /**
     * Veri tabanı sorgusuyla elde edilmiş Document türündeki BSON belgesinin
     * içeriğini kullanarak yeni bir sıralı video nesnesi oluşturur.
     * Geçmiş ve gelecek boş bırakılır.
     * 
     * @param belge Veri tabanından okunmuş Document türünde BSON belgesi
     * @return  Belgede doğru alanlar varsa yeni sıralı video nesnesi,
     *          yoksa null
     * 
     * @see Document
     * @see com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.VeriTabanıHizmetleri
     * @see com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.VeriTabanı
     */
    public static SıralıVideolar BSONBelgesinden(Document belge)
    {
        String kimlik = belge.getString("_id");
        if (kimlik == null)
            return null;
        if (belge.containsKey("DosyaYolu") && belge.containsKey("İye")
                && belge.containsKey("Tarih") && !kimlik.isBlank())
        {
            SıralıVideolar videolar = new SıralıVideolar(
                    belge.getString("DosyaYolu"),
                    belge.getString("Açıklama"),
                    belge.getString("İye"),
                    new Calendar.Builder()
                            .setInstant(belge.get("Tarih", Long.class))
                            .build(),
                    belge.getString("_id"));
            return videolar;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Sıralı video listesinin tarihsel sıraya uygun bir noktasına
     * yeni video ekler.
     * 
     * @param yeni_düğüm    Listeye eklenecek yeni sıralı video nesnesi
     * @return  Eklenen yeni düğüm. Sıralama konusunda sorun yaşanırsa null.
     */
    @Override
    public SıralıVideolar DüğümEkle(SıralıVideolar yeni_düğüm)
    {
        if (yeni_düğüm.getTarih() == null)
        {
            return null;
        }
        SıralıVideolar kök = this.Kök();
        SıralıVideolar kafa = kök;
        yeni_düğüm.Kopar();
        
        if (yeni_düğüm.getTarih().before(kafa.getTarih()))
        {
            yeni_düğüm.setSonraki(kafa);
            kafa.setÖnceki(yeni_düğüm);
            return yeni_düğüm;
        }
        
        boolean yerleşti = false;
        while (kafa.getSonraki() != null)
        {
            if (kafa.getSonraki().getTarih().after(yeni_düğüm.getTarih()))
            {
                yeni_düğüm.setSonraki(kafa.getSonraki());
                kafa.getSonraki().setÖnceki(yeni_düğüm);
                yeni_düğüm.setÖnceki(kafa);
                kafa.setSonraki(yeni_düğüm);
                yerleşti = true;
                break;
            }
            kafa = kafa.getSonraki();
        }
        if (!yerleşti && !yeni_düğüm.getTarih().before(kafa.getTarih()))
        {
            yeni_düğüm.setÖnceki(kafa);
            kafa.setSonraki(yeni_düğüm);
            yerleşti = true;
        }
        
        if (yerleşti)
        {
            return yeni_düğüm;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * En geçmişteki sıralı video nesnesini döndürür.
     * 
     * @return  Listenin en geçmişteki videosu
     */
    @Override
    public SıralıVideolar Kök()
    {
        SıralıVideolar kafa = this;
        while (kafa.getÖnceki() != null)
        {
            kafa = kafa.getÖnceki();
        }
        return kafa;
    }
    
    /**
     * En gelecekteki sıralı video nesnesini döndürür.
     * 
     * @return  Listenin en gelecekteki videosu
     */
    @Override
    public SıralıVideolar UçDüğüm()
    {
        SıralıVideolar kafa = this;
        while (kafa.getSonraki() != null)
        {
            kafa = kafa.getSonraki();
        }
        return kafa;
    }
    
    @Override
    public SıralıVideolar Kopar()
    {
        SıralıVideolar önce = this.getÖnceki();
        SıralıVideolar sonra = this.getSonraki();
        
        this.setÖnceki(null);
        this.setSonraki(null);
        
        if (sonra != null)
        {
            sonra.setÖnceki(önce);
        }
        if (önce != null)
        {
            önce.setSonraki(sonra);
            // Dönüş önceliği köke yakın olsun diye 
            return önce;
        }
        return sonra;
    }
    
    /**
     * Bir geçmişteki videoya erişim sağlar.
     * 
     * @return  Bir geçmişteki videonun sıralı video nesnesi
     */
    @JsonIgnore
    public SıralıVideolar getÖnceki()
    {
        return this.önceki;
    }
    /**
     * Bir geçmişteki videonun kimliğini bildirir.
     * 
     * @return  Bir geçmişte video varsa Base64 metni olarak kimliği,
     *          geçmişte video yoksa null
     */
    @JsonGetter("Önceki")
    public String getÖncekininKimliği()
    {
        if (this.önceki != null)
            return this.önceki.getKimlikBase64();
        else
            return null;
    }
    /**
     * Belirtilen videoyu bir geçmişteki video olarak tanımlar.
     * Belirtilen videonun tarihine erişilemiyorsa işlem yapılmaz.
     * Belirtilen videonun tarihi daha sonraysa işlem yapılmaz.
     * 
     * @param önceki    Bir geçmişe yerleştirilecek video
     * @return  İşlem başarılıysa true, başarısızsa false
     */
    @JsonIgnore
    public boolean setÖnceki(SıralıVideolar önceki)
    {
        if (önceki == null)
        {
            this.önceki = null;
            return true;
        }
        // İki nesneden birinin tarihine erişilemiyorsa
        if (this.getTarih() == null || önceki.getTarih() == null)
        {
            return false;
        }
        // Yeni önceki gerçekten de sonra gelmiyorsa
        if (!önceki.getTarih().after(this.getTarih()))
        {
            this.önceki = önceki;
            return true;
        }
        else
        {
            return false;
        }
    }
    /**
     * Bir gelecekteki videoya erişim sağlar.
     * 
     * @return  Bir gelecekteki videonun sıralı video nesnesi
     */
    @JsonIgnore
    public SıralıVideolar getSonraki()
    {
        return this.sonraki;
    }
    /**
     * Bir gelecekteki videonun kimliğini bildirir.
     * 
     * @return  Bir gelecekte video varsa Base64 metni olarak kimliği,
     *          gelecekte video yoksa null
     */
    @JsonGetter("Sonraki")
    public String getSonrakininKimliği()
    {
        if (this.sonraki != null)
            return this.sonraki.getKimlikBase64();
        else
            return null;
    }
    /**
     * Belirtilen videoyu bir gelecekteki video olarak tanımlar.
     * Belirtilen videonun tarihine erişilemiyorsa işlem yapılmaz.
     * Belirtilen videonun tarihi daha önceyse işlem yapılmaz.
     * 
     * @param sonraki    Bir geleceğe yerleştirilecek video
     * @return  İşlem başarılıysa true, başarısızsa false
     */
    @JsonIgnore
    public boolean setSonraki(SıralıVideolar sonraki)
    {
        if (sonraki == null)
        {
            this.sonraki = null;
            return true;
        }
        // İki nesneden birinin tarihine erişilemiyorsa
        if (this.getTarih() == null || sonraki.getTarih() == null)
        {
            return false;
        }
        // Yeni sonraki gerçekten de önce gelmiyorsa
        if (!sonraki.getTarih().before(this.getTarih()))
        {
            this.sonraki = sonraki;
            return true;
        }
        else
        {
            return false;
        }
    }
}
