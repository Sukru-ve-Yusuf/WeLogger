/*
 * SıralıGünler
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
import org.bson.Document;
import com.fasterxml.jackson.annotation.*;

/**
 * Günleri tarihsel olarak geçmişten geleceğe doğru
 * sıralı tutan bir çift yönlü bağlı liste.
 * 
 * @see Gün
 * @see IÇiftYönlüBağlıListe
 */
@JsonPropertyOrder({"_id", "Başkahraman", "Açıklama", "Videolar", "Tarih", 
    "Önceki", "Sonraki"})
public class SıralıGünler extends Gün
        implements IÇiftYönlüBağlıListe<SıralıGünler>
{
    /**
     * Tarihsel sıralamada bir geçmişteki gün.
     */
    private SıralıGünler önceki;
    /**
     * Tarihsel sıralamada bir gelecekteki gün.
     */
    private SıralıGünler sonraki;
    
    /**
     * Verilen bilgilerle yeni bir SıralıGünler nesnesi oluşturur.
     * Oluşturulan nesneye yeni bir kimlik atanır.
     * 
     * @param başkahraman   Oluşturulan günü yaşamış kişi
     * @param açıklama      Bu gün hakkında açıklama metni
     * @param videolar      Başkahramanın bu günde çektiği videolar
     * @param tarih         Bu günün tarihi
     * 
     * @see Gün
     * @see Kullanıcı
     * @see SıralıVideolar
     * @see Calendar
     */
    public SıralıGünler(Kullanıcı başkahraman, String açıklama,
            SıralıVideolar videolar, Calendar tarih)
    {
        super(başkahraman, açıklama, videolar, tarih);
        this.setÖnceki(null);
        this.setSonraki(null);
    }
    /**
     * Bir gün nesnesini kullanarak yeni bir sıralı gün nesnesi oluşturur.
     * İsteğe bağlı olarak kimlik korunabilir ya da yenilenebilir.
     * 
     * @param kaynak_gün        İçeriği alınacak gün nesnesi
     * @param kimlik_yenilensin true ise yeni kimlik atanır,
     *                          false ise kaynak günün kimliği kopyalanır.
     * 
     * @see Gün
     */
    public SıralıGünler(Gün kaynak_gün, boolean kimlik_yenilensin)
    {
        super(kaynak_gün, kimlik_yenilensin);
        this.setÖnceki(null);
        this.setSonraki(null);
    }
    /**
     * Verilen bilgilerle videosu olmayan bir gün oluşturur.
     * Geçmiş ve gelecek boş bırakılır.
     * 
     * @param başkahraman   Günü yaşayan kullanıcının kimliği
     * @param açıklama      Gün hakkında açıklama metni
     * @param tarih         Günün tarihi
     * @param kimlik        Base64 metni olarak günün kimliği
     */
    @JsonCreator
    public SıralıGünler(
            @JsonProperty("Başkahraman") String başkahraman,
            @JsonProperty("Açıklama") String açıklama,
            @JsonProperty("Tarih") Calendar tarih,
            @JsonProperty("_id") String kimlik)
    {
        super(başkahraman, açıklama, tarih, kimlik);
        this.setÖnceki(null);
        this.setSonraki(null);
    }
    /**
     * Verilen bilgilerle eşsiz kimliği olan bir gün oluşturur.
     * 
     * @param başkahraman   Günü yaşayan kullanıcının kimliği
     * @param açıklama      Gün hakkında açıklama metni
     * @param tarih         Günün tarihi
     * @param gün_vt        Kimlik eşsizliği için gün veri tabanı hizmeti
     * 
     * @seee GünVT
     */
    public SıralıGünler(String başkahraman, String açıklama, Calendar tarih,
            GünVT gün_vt)
    {
        super(başkahraman, açıklama, tarih, gün_vt);
        this.setÖnceki(null);
        this.setSonraki(null);
    }
    
    /**
     * Veri tabanı sorgusuyla elde edilmiş Document türündeki BSON belgesinin
     * içeriğini kullanarak yeni bir sıralı gün nesnesi oluşturur.
     * Günün videoları ile geçmiş ve gelecek günler boş bırakılır.
     * 
     * @param belge Veri tabanından okunmuş Document türünde BSON belgesi
     * @return  Belgede doğru alanlar varsa yeni sıralı gün nesnesi,
     *          yoksa null
     * 
     * @see Document
     * @see com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.VeriTabanıHizmetleri
     * @see com.github.Sukru_ve_Yusuf.WeLoggerAPI.Services.VeriTabanı
     */
    public static SıralıGünler BSONBelgesinden(Document belge)
    {
        String kimlik = belge.getString("_id");
        if (kimlik == null)
            return null;
        if (belge.containsKey("Başkahraman") && belge.containsKey("Tarih")
                && belge.containsKey("Videolar")  && !kimlik.isBlank())
        {
            SıralıGünler günler = new SıralıGünler(
                    belge.getString("Başkahraman"),
                    belge.getString("Açıklama"),
                    new Calendar.Builder()
                        .setInstant(belge.get("Tarih", Long.class))
                        .build(),
                    belge.getString("_id"));
            return günler;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Sıralı gün listesinin tarihsel sıraya uygun bir noktasına
     * yeni video ekler.
     * 
     * @param yeni_düğüm    Listeye eklenecek yeni sıralı gün nesnesi
     * @return  Eklenen yeni düğüm. Sıralama konusunda sorun yaşanırsa null.
     */
    @Override
    public SıralıGünler DüğümEkle(SıralıGünler yeni_düğüm)
    {
        if (yeni_düğüm.getTarih() == null)
        {
            return null;
        }
        SıralıGünler kök = this.Kök();
        SıralıGünler kafa = kök;
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
     * Günlerin tarihlerini ve açıklamalarını dizi olarak döndürür.
     * 
     * @return  Günler tarihleri ile açıklamaları. İlk boyut sırayı;
     *          ikinci boyut 0'da tarihi, 1'de açıklamayı tutar.
     */
    public String[][] TarihlerİleAçıklamalar()
    {
        SıralıGünler kafa1 = this.Kök();
        SıralıGünler kafa2 = kafa1;
        int nicelik = 1;
        while (kafa1.getSonraki() != null)
        {
            nicelik++;
            kafa1 = kafa1.getSonraki();
        }
        
        String[][] tarihler = new String[nicelik][2];
        int sıra = 0;
        while (kafa2.getSonraki() != null)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(kafa2.tarih.get(Calendar.YEAR));
            sb.append(" ");
            sb.append((this.tarih.get(Calendar.MONTH) + 1));
            sb.append(" ");
            sb.append(this.tarih.get(Calendar.DAY_OF_MONTH));
            tarihler[sıra][0] = sb.toString();
            tarihler[sıra][1] = kafa2.getAçıklama();
            sıra++;
            kafa2 = kafa2.getSonraki();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(kafa2.tarih.get(Calendar.YEAR));
        sb.append(" ");
        sb.append((this.tarih.get(Calendar.MONTH) + 1));
        sb.append(" ");
        sb.append(this.tarih.get(Calendar.DAY_OF_MONTH));
        tarihler[sıra][0] = sb.toString();
        tarihler[sıra][1] = kafa2.getAçıklama();
        
        return tarihler;
    }
    /**
     * Yıl, ay, gün değerleri verilen günü arar.
     * 
     * @param yıl   Aranan günün yılı
     * @param ay    Aranan günün ayı (1-12)
     * @param gün   Aranan günün ayın kaçıncı günü olduğu
     * @return  Bulunan gün nesnesi, bulunamazsa null
     */
    public SıralıGünler GünüBul(int yıl, int ay, int gün)
    {
        try
        {
            Calendar aranan = new Calendar.Builder().setDate(yıl, ay-1, gün)
                    .build();
            
            SıralıGünler kafa = this;
            if (AynıGün(kafa.tarih, aranan))
            {
                return kafa;
            }
            if (aranan.after(kafa.tarih))
            {
                while (kafa.getSonraki() != null)
                {
                    kafa = kafa.getSonraki();
                    if (AynıGün(aranan, kafa.tarih))
                    {
                        return kafa;
                    }
                    if (aranan.after(kafa))
                    {
                        continue;
                    }
                    return null;
                }
            }
            if (aranan.before(kafa.tarih))
            {
                while (kafa.getÖnceki() != null)
                {
                    kafa = kafa.getÖnceki();
                    if (AynıGün(aranan, kafa.tarih))
                    {
                        return kafa;
                    }
                    if (aranan.after(kafa))
                    {
                        continue;
                    }
                    return null;
                }
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return null;
    }
    
    /**
     * Verilen iki tarihin aynı günde olup olmadığını denetler.
     * 
     * @param a İlk tarih
     * @param b İkinci tarih
     * @return  Aynı gündelerse true, değilse false
     */
    private static boolean AynıGün(Calendar a, Calendar b)
    {
        return (a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
                a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_MONTH));
    }
    
    /**
     * En geçmişteki güne erişim sağlar.
     * 
     * @return  Listede en geçmişteki gün
     */
    public SıralıGünler Kök()
    {
        SıralıGünler kafa = this;
        while (kafa.getÖnceki() != null)
        {
            kafa = kafa.getÖnceki();
        }
        return kafa;
    }
    /**
     * En gelecekteki güne erişim sağlar.
     * 
     * @return  Listede en gelecekteki gün
     */
    @Override
    public SıralıGünler UçDüğüm()
    {
        SıralıGünler kafa = this;
        while (kafa.getSonraki() != null)
        {
            kafa = kafa.getSonraki();
        }
        return kafa;
    }
    
    @Override
    public SıralıGünler Kopar()
    {
        SıralıGünler önce = this.getÖnceki();
        SıralıGünler sonra = this.getSonraki();
        
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
     * Bir geçmişteki güne erişim sağlar.
     * 
     * @return  Listede bir geçmişteki günün sıralı gün nesnesi
     */
    @JsonIgnore
    public SıralıGünler getÖnceki()
    {
        return this.önceki;
    }
    /**
     * Bir geçmişteki günün kimliğine Base64 metni olarak erişim sağlar.
     * 
     * @return  Base64 metni olarak bir geçmiteki günün kimliği
     */
    @JsonGetter("Önceki")
    public String getÖncekininKimliği()
    {
        if (this.önceki == null)
            return null;
        return this.önceki.getKimlikBase64();
    }
    /**
     * Belirtilen günü bir gelecekteki gün olarak tanımlar.
     * Belirtilen günün tarihine erişilemiyorsa işlem yapılmaz.
     * Belirtilen günün tarihi daha sonraysa işlem yapılmaz.
     * 
     * @param yeni_önceki   Bir geleceğe yerleştirilecek gün
     * @return  İşlem başarılıysa true, başarısızsa false
     */
    @JsonIgnore
    public boolean setÖnceki(SıralıGünler yeni_önceki)
    {
        if (yeni_önceki == null)
        {
            this.önceki = null;
            return true;
        }
        // İki nesneden birinin tarihine erişilemiyorsa
        if (this.getTarih() == null || yeni_önceki.getTarih() == null)
        {
            return false;
        }
        // Yeni önceki gerçekten de sonra gelmiyorsa
        if (!yeni_önceki.getTarih().after(this.getTarih()))
        {
            this.önceki = yeni_önceki;
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Bir gelecekteki güne erişim sağlar.
     * 
     * @return  Listede bir gelecekteki günün sıralı gün nesnesi
     */
    @JsonIgnore
    public SıralıGünler getSonraki()
    {
        return this.sonraki;
    }
    /**
     * Bir gelecekteki günün kimliğine Base64 metni olarak erişim sağlar.
     * 
     * @return  Base64 metni olarak bir gelecekteki günün kimliği
     */
    @JsonGetter("Sonraki")
    public String getSonrakininKimliği()
    {
        if (this.sonraki == null)
            return null;
        return this.sonraki.getKimlikBase64();
    }
    /**
     * Belirtilen günü bir gelecekteki gün olarak tanımlar.
     * Belirtilen günün tarihine erişilemiyorsa işlem yapılmaz.
     * Belirtilen günün tarihi daha önceyse işlem yapılmaz.
     * 
     * @param yeni_sonraki  Bir geleceğe yerleştirilecek gün
     * @return  İşlem başarılıysa true, başarısızsa false
     */
    @JsonIgnore
    public boolean setSonraki(SıralıGünler yeni_sonraki)
    {
        if (yeni_sonraki == null)
        {
            this.sonraki = null;
            return true;
        }
        // İki nesneden birinin tarihine erişilemiyorsa
        if (this.getTarih() == null || yeni_sonraki.getTarih() == null)
        {
            return false;
        }
        // Yeni sonraki gerçekten de önce gelmiyorsa
        if (!yeni_sonraki.getTarih().before(this.getTarih()))
        {
            this.sonraki = yeni_sonraki;
            return true;
        }
        else
        {
            return false;
        }
    }
}
