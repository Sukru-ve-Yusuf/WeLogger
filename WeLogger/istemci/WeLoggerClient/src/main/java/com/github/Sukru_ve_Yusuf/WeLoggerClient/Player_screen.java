package istemci.WeLoggerClient.src.main.java.com.github.Sukru_ve_Yusuf.WeLoggerClient;


import java.io.IOException;

public class Player_screen {
    private String videoPath;

    public Player_screen(String videoPath) {
        this.videoPath = videoPath;
    }

    // Video oynatma metodunu tanımlıyoruz
    public void play() {
        try {
            String command = "cmd /c start \"\" \"" + videoPath + "\"";
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
