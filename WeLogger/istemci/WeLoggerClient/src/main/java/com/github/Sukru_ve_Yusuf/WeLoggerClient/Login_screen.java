package istemci.WeLoggerClient.src.main.java.com.github.Sukru_ve_Yusuf.WeLoggerClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import okhttp3.*;

public class Login_screen {
    UserClient user;
    private OkHttpClient client;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login_screen().createAndShowGUI());
    }

    public Login_screen() {
        client = new OkHttpClient();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Giriş Ekranı");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));

        JLabel userLabel = new JLabel("Kullanıcı Adı:");
        JTextField userField = new JTextField();
        panel.add(userLabel);
        panel.add(userField);

        JLabel passLabel = new JLabel("Şifre:");
        JPasswordField passField = new JPasswordField();
        panel.add(passLabel);
        panel.add(passField);

        JLabel newUser = new JLabel("Hesabınız yok mu?");
        JLabel userLabelnew = new JLabel("Kullanıcı Adı:");
        JTextField userFieldnew = new JTextField();
        panel.add(newUser);
        panel.add(new JLabel("Kayıt olun"));
        panel.add(userLabelnew);
        panel.add(userFieldnew);

        JLabel passLabelnew = new JLabel("Şifre:");
        JPasswordField passFieldnew = new JPasswordField();
        panel.add(passLabelnew);
        panel.add(passFieldnew);

        JButton loginButton = new JButton("Giriş");
        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                isuserValid(username, password, frame, messageLabel);
            }
        });

        JButton registerButton = new JButton("Kayıt Ol");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userFieldnew.getText();
                String password = new String(passFieldnew.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    messageLabel.setText("Kullanıcı adı veya şifre boş olamaz.");
                } else {
                    newUserRequest(username, password, frame, messageLabel);
                }
            }
        });

        panel.add(loginButton);
        panel.add(messageLabel);
        panel.add(registerButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private void openrecord(UserClient user) {
        SwingUtilities.invokeLater(() -> new Record_screen(user));
    }

    private void isuserValid(String usernameInput, String password, JFrame frame, JLabel messageLabel) {
        try {
            String json = "{ \"KullaniciAdi\": \"" + usernameInput + "\", \"Parola\": \"" + password + "\" }";
            String urlString = "http://localhost:9000/Üyelik/OturumAç/" + usernameInput + "/" + password;

            RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(urlString)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            int responseCode = response.code();

            System.out.println("API Yanıtı: " + responseBody);

            if (responseCode == 200) {
                if (responseBody.contains("_id")) {
                    JOptionPane.showMessageDialog(frame, "Giriş Başarılı!");
                    user = new UserClient(usernameInput, usernameInput, password);
                    frame.dispose();
                    openrecord(user);
                } else {
                    messageLabel.setText("Hatalı kullanıcı adı veya şifre!");
                }
            } else if (responseCode == 403) {
                messageLabel.setText("Giriş başarısız, yetkisiz erişim!");
            } else {
                messageLabel.setText("Hata: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Bir hata oluştu. Lütfen tekrar deneyin.");
        }
    }

    private void newUserRequest(String usernameInput, String password, JFrame frame, JLabel messageLabel) {
        try {
            String urlString = "http://localhost:9000/Üyelik/ÜyeOl/" + usernameInput + "/" + usernameInput + "/" + password;
            String json = "{ \"Ad\": \"" + usernameInput + "\", \"KullaniciAdi\": \"" + usernameInput + "\", \"Parola\": \"" + password + "\" }";

            RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(urlString)
                    .put(body)
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            int responseCode = response.code();

            if (responseCode == 201) {
                JOptionPane.showMessageDialog(frame, "Kayıt Başarılı! Lütfen giriş yapın.");
            } else if (responseCode == 400) {
                messageLabel.setText("Kullanıcı adı zaten alınmış.");
            } else {
                messageLabel.setText("Kayıt sırasında bir hata oluştu.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Bir hata oluştu. Lütfen tekrar deneyin.");
        }
    }
}
