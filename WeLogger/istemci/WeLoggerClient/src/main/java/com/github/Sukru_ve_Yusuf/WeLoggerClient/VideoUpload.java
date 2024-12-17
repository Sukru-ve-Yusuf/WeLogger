import okhttp3.*;
import java.io.*;

public class VideoUpload {
    private static final String BASE_URL = "http://localhost:9000/Video/VideoYükle";

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        File videoFile = new File("C:\\Users\\FTRT2\\Videos\\Ekran Kayıtları\\Ekran Kaydı 2024-12-16 001302.mp4");

        InputStream videoInputStream = new FileInputStream(videoFile);

        MediaType mediaType = MediaType.parse("video/mp4");
        RequestBody videoBody = RequestBody.create(videoFile, mediaType);

        Headers headers = new Headers.Builder()
                .add("Oturum", "your-session-id")
                .add("Kullanici", "your-user-id")
                .add("AcIklama", "TESTTEST")
                .add("VideoTarihi", String.valueOf(System.currentTimeMillis()))
                .build();

        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", videoFile.getName(), videoBody)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL)
                .headers(headers)
                .put(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Video başarıyla yüklendi.");
            } else {
                System.out.println("Video yükleme başarısız. Hata kodu: " + response.code());
            }
        }
    }
}
