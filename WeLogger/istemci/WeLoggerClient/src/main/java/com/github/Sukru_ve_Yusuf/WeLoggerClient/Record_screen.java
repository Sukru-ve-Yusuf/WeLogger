package istemci.WeLoggerClient.src.main.java.com.github.Sukru_ve_Yusuf.WeLoggerClient;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;


import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import java.util.Date;
import java.text.SimpleDateFormat;
import javax.sound.sampled.*;
import java.io.IOException;
import java.io.*;
import javax.swing.JOptionPane;
// opencv  videosu https://www.youtube.com/watch?v=TsUhEuySano
public class Record_screen {
    Boolean record = false;
    VideoWriter writer;
    String username = "markwhatney";
    String userID = "5";
    AudioRecorder audioRecorder;
    Thread audioThread;
    String time_record_start;
    String audioFilename;
    String filename;
    JTextArea textArea;
    String videonote;
    String filegeneralname;
    String filenametxt;
    private UserClient userClient;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public Record_screen(UserClient user) {
        this.userClient =user;
        this.username=user.getAd();
        //this.filegeneralname=user.getAd() + this.userID+//username + "_" + userID + "_" + time_record_start + "_.mp4";
        JFrame frame = new JFrame("weLogger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        JLabel imageLabel = new JLabel();
        frame.add(imageLabel, BorderLayout.CENTER);

        textArea = new JTextArea(new StringBuilder().append("Notlarını buraya yazabilirsin ").append(this.username).toString(),5, 20);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true); textArea.setWrapStyleWord(true);
        frame.add(scrollPane, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton startButton = new JButton("Kayda Başla");
        JButton stopButton = new JButton("Kaydı Bitir");
        JButton myRecordsButton = new JButton("Kayıtlarım");
        JButton myRecordsButtonDays = new JButton("Günler");



        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time_record_start = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                audioFilename = username + "_" + userID + "_" + time_record_start + "_Out.wav";
                filename = username + "_" + userID + "_" + time_record_start + "_Out.avi";
                int fourcc = VideoWriter.fourcc('M', 'J', 'P', 'G');
                double fps = 30.0;
                VideoCapture capture = new VideoCapture(0);
                if (!capture.isOpened()) {
                    JOptionPane.showMessageDialog(frame, "Kamera açılamadı");
                    return;
                }

                int frameWidth = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
                int frameHeight = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
                writer = new VideoWriter(filename, fourcc, fps, new org.opencv.core.Size(frameWidth, frameHeight));

                record = true;
                JOptionPane.showMessageDialog(frame, "Kayıt başlatılıyor");

                audioRecorder = new AudioRecorder(audioFilename);
                audioThread = new Thread(() -> audioRecorder.start());
                audioThread.start();

                new Thread(() -> captureVideo(capture, imageLabel)).start();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                record = false;
                videonote = textArea.getText();
                filenametxt = username + "_" + userID + "_" + time_record_start + "_Out.txt";
                try {
                    FileWriter writer = new FileWriter(filenametxt);
                    writer.write(videonote);
                    writer.close();
                    System.out.println("Notunuz başarıyla kaydedildi.");
                } catch (IOException f) {
                    f.printStackTrace();
                }

                JOptionPane.showMessageDialog(frame, "Kayıt sonlandırıldı");
                writer.release();
                if (audioRecorder != null) {
                    audioRecorder.stop();
                    try {
                        audioThread.join();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                String outputPath = username + "_" + userID + "_" + time_record_start + "_.mp4";
                merge(filename, audioFilename, outputPath);
                textArea.setText("Yeni bir kayıt başlatabilirsiniz");
            }
        });

        myRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //to do kayıtlarımı ayrı bir sayfa halinde yapıp  serverdan güncelleme  ve  tarih  saate  göre filitreleme ekliycem video oynatıldığındada txt yi ekrana yazdırıcam

                JFrame recordsFrame = new JFrame("Kayıtlarım");
                recordsFrame.setSize(600, 400);
                recordsFrame.setLocationRelativeTo(null);
                recordsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel panel = new JPanel(new BorderLayout());
                DefaultListModel<String> listModel = new DefaultListModel<>();

                File dir = new File(".");
                File[] files = dir.listFiles((d, name) -> name.endsWith(".mp4")&& name.contains(username));

                if (files != null) {
                    for (File file : files) {
                        listModel.addElement(file.getName());
                    }
                } else {
                    JOptionPane.showMessageDialog(recordsFrame, "Kayıt Bulunamadı");
                }

                JList<String> recordsList = new JList<>(listModel);
                panel.add(new JScrollPane(recordsList), BorderLayout.CENTER);

                JButton mp4selectButon = new JButton("Seçilen Kaydı Oynat");
                mp4selectButon.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String selectedFile = recordsList.getSelectedValue();
                        if (selectedFile != null) {
                            JOptionPane.showMessageDialog(recordsFrame, "Seçilen kayıt: " + selectedFile);
                            File selectedFilePath = new File(selectedFile);
                            String videoPath = selectedFilePath.getAbsolutePath();
                            Player_screen videoPlayer = new Player_screen(videoPath);
                            videoPlayer.play();
                        } else {
                            JOptionPane.showMessageDialog(recordsFrame, "Lütfen bir kayıt seçin.");
                        }
                    }
                });
                panel.add(mp4selectButon, BorderLayout.SOUTH);

                recordsFrame.add(panel);
                recordsFrame.setVisible(true);
            }
        });
        myRecordsButtonDays.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //to do kayıtlarımı ayrı bir sayfa halinde yapıp  serverdan güncelleme  ve  tarih  saate  göre filitreleme ekliycem video oynatıldığındada txt yi ekrana yazdırıcam

                JFrame recordsFrame = new JFrame("Kayıtlarım");
                recordsFrame.setSize(600, 400);
                recordsFrame.setLocationRelativeTo(null);
                recordsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel panel = new JPanel(new BorderLayout());
                DefaultListModel<String> listModel = new DefaultListModel<>();

                File dir = new File(".");
                File[] files = dir.listFiles((d, name) -> name.endsWith(".mp4")&& name.contains(username));

                if (files != null) {
                    for (File file : files) {
                        listModel.addElement(file.getName());
                    }
                } else {
                    JOptionPane.showMessageDialog(recordsFrame, "Kayıt Bulunamadı");
                }

                JList<String> recordsList = new JList<>(listModel);
                panel.add(new JScrollPane(recordsList), BorderLayout.CENTER);

                JButton mp4selectButon = new JButton("Seçilen Kaydı Oynat");
                mp4selectButon.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String selectedFile = recordsList.getSelectedValue();
                        if (selectedFile != null) {
                            JOptionPane.showMessageDialog(recordsFrame, "Seçilen kayıt: " + selectedFile);
                            File selectedFilePath = new File(selectedFile);
                            String videoPath = selectedFilePath.getAbsolutePath();
                            Player_screen videoPlayer = new Player_screen(videoPath);
                            videoPlayer.play();
                        } else {
                            JOptionPane.showMessageDialog(recordsFrame, "Lütfen bir kayıt seçin.");
                        }
                    }
                });
                panel.add(mp4selectButon, BorderLayout.SOUTH);

                recordsFrame.add(panel);
                recordsFrame.setVisible(true);
            }
        });

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(myRecordsButton);
        buttonPanel.add(myRecordsButtonDays);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void captureVideo(VideoCapture capture, JLabel imageLabel) {
        Mat matFrame = new Mat();
        while (record) {
            if (capture.read(matFrame)) {
                String time = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                Imgproc.putText(matFrame, time, new org.opencv.core.Point(10, 30), Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(255, 255, 255), 2);

                Imgproc.cvtColor(matFrame, matFrame, Imgproc.COLOR_BGR2RGB);
                if (record) {
                    writer.write(matFrame);
                }
                ImageIcon imageIcon = new ImageIcon(matToBufferedImage(matFrame));
                SwingUtilities.invokeLater(() -> imageLabel.setIcon(imageIcon));
            }
        }
        capture.release();
    }

    private static BufferedImage matToBufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_3BYTE_BGR;
        if (mat.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        }
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        mat.get(0, 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData());
        return image;
    }

    public static void merge(String videoPath, String audioPath, String outputPath) {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg", "-i", videoPath, "-i", audioPath,
                "-c:v", "libx264", "-preset", "veryfast", "-crf", "23",
                "-c:a", "aac", "-b:a", "128k", outputPath
        );

        processBuilder.inheritIO();
        try {
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println("Video ve ses başarıyla birleştirildi: " + outputPath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    class AudioRecorder {
        private TargetDataLine line;
        private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
        private File audioFile;

        public AudioRecorder(String filename) {
            audioFile = new File(filename);
        }

        public void start() {
            try {
                AudioFormat format = getAudioFormat();
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                if (!AudioSystem.isLineSupported(info)) {
                    System.out.println("ses fromatı bozuk");
                    return;
                }
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();

                System.out.println("kaydediliyor");
                AudioInputStream ais = new AudioInputStream(line);
                AudioSystem.write(ais, fileType, audioFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void stop() {
            System.out.println("kayıt durduruluyor");
            if (line != null) {
                line.stop();
                line.close();
            }
        }

        private AudioFormat getAudioFormat() {
            float sampleRate = 44100;
            int sampleSizeInBits = 16;
            int channels = 2;
            boolean signed = true;
            boolean bigEndian = false;
            return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        }
    }

    public static void main( UserClient user ) {
        System.out.println(user.getAd());
        new Record_screen(user);
    }
}
