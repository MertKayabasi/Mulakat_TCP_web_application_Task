package com.task.demo;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class TcpServer implements Runnable {

    private final TaskService taskService;        //private final değişkenimiz
    private ServerSocket serverSocket; //private değişkenlerimiz
    private volatile boolean running = true;

    public TcpServer(TaskService taskService) throws IOException {
        this.taskService = taskService;
        this.serverSocket = new ServerSocket(9090);
        new Thread(this, "TCP-SERVER").start();
    }

    @Override
    public void run() {
        System.out.println("TCP Server 9090 portunda dinliyor...");

        while (running) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("Client bağlandı -> " + client.getRemoteSocketAddress());

                new Thread(() -> handleClient(client)).start();

            } catch (IOException e) {
                if (running) e.printStackTrace();
                break;
            }
        }

        System.out.println("TCP Server durdu.");
    }


    public void handleClient(Socket clientSocket) {
        String clientAddr = clientSocket.getRemoteSocketAddress().toString(); //client ın ip port bilgisi
        taskService.saveEvent(TaskEventType.CONNECT, clientAddr, "Client bağlandı");  //client bağlandı olarak loglama

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //client ın gönderdiği mesajların okunması
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)  //client a mesaj gönderir
        ) {
            String line;

            while ((line = in.readLine()) != null) {       //client mesaj gönderdikçe döngü çalışır
                System.out.println("TCP Mesaj alındı: " + line);       //mesaj alındı olarak yazma
                taskService.saveEvent(TaskEventType.MESSAGE, clientAddr, line);
                out.println("ALINDI: " + line);              //alındı mesajını bastırma
            }

        } catch (Exception e) {
            System.out.println("Client bağlantısı kapandı: " + e.getMessage());  //exception yakalarsa bağlantı kapandı mesajı
        } finally {
            try { clientSocket.close(); } catch (IOException ignored) {}        //client bağlantısının kapanma olayı
            taskService.saveEvent(TaskEventType.DISCONNECT, clientAddr, "Client ayrıldı"); //client disconnect
        }
    }

    public void stop() {            //server ın durdurulma metodu
        running = false;
        try { serverSocket.close(); } catch (IOException ignored) {}
    }

    // dosyayı tcp üzerinden gönderme metodu
    public void sendFileToTcpServer(MultipartFile file) {
        try (Socket socket = new Socket("localhost", 9090)) {   //socket ile port açılıyor

            DataOutputStream data = new DataOutputStream(socket.getOutputStream());

            String fileName = file.getOriginalFilename();      //dosya ismi alınıyor
            byte[] bytes = file.getBytes();        //dosya byte byte kaydediliyor
            int length = bytes.length;            //byte uzunluğu

            // Dosya adını gönder
            data.writeUTF(fileName);

            //Dosya boyutunu gönder
            data.writeInt(length);

            //Dosya içeriğini gönder
            data.write(bytes);


            data.flush(); //buffer daki veri hemen gönderilir

            System.out.println("Dosya TCP üzerinden gönderildi: " + fileName);  //konsola basılıyor
           String from = serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort();
            // database log kaydı
            taskService.saveEvent(TaskEventType.FILE,from , "File sent: " + fileName);

        } catch (Exception e) {     //exception
            e.printStackTrace();
        }
    }

}
