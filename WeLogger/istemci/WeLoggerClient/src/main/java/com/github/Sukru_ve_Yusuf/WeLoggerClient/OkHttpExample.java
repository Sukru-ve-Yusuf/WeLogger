package com.github.Sukru_ve_Yusuf.WeLoggerClient;

import okhttp3.*;

import java.io.IOException;

public class OkHttpExample {
    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String url = "http://localhost:9000/Üyelik/ÜyeOl/sarp66/sarp66/sarpParola/";

        // JSON verisi
        String json = "{ \"Ad\": \"sarp66\", \"KullaniciAdi\": \"sarp66\", \"Parola\": \"sarpParola\" }";

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            // Durum kodunu kontrol et
            System.out.println("HTTP Durum Kodu: " + response.code());

            // Yanıt içeriği
            if (response.body() != null) {
                System.out.println(response.body().string());
            } else {
                System.out.println("Yanıt içeriği boş.");
            }
        }
    }
}
