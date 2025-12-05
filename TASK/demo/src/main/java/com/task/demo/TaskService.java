package com.task.demo;

import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class TaskService {

    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {     //constructor
        this.repo = repo;
    }


    public List<TaskEntity> getAll() {    //tüm verilerin databaseden çekilerek listelenmesi
        return repo.findAll();
    }

    // TCP'ye mesaj gönderen client fonksiyonu
    public String sendToTcp(String message) {
        try (Socket socket = new Socket("localhost", 9090)) {

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);

            //spring TCP client bilgileri
            String localInfo = socket.getLocalAddress().getHostAddress() + ":" + socket.getLocalPort();
            String serverInfo = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();

            TaskEntity e = new TaskEntity();
            e.setType(TaskEventType.MESSAGE);
            e.setClientFrom(localInfo);     // spring in client portu
            e.setClientTo(serverInfo);      // TCP server portu
            e.setContent(message);          //mesaj
            e.setDirection("OUTBOUND");
            e.setStatus("SUCCESS");          //statüsü
            e.setTimestamp(LocalDateTime.now());

            return "Mesaj TCP server'a gönderildi.";     //frontend e gönderilecek yanıt olarak bu string dönüyor

        } catch (Exception e) {
            return "Gönderim hatası: " + e.getMessage();         //exception olursa
                                                                  //frontend e gönderilecek yanıt olarak bu string dönüyor
        }
    }
    //TCP üzerinden gerçekleşen her event ı veritabanına kaydeden log metodu
    public void saveEvent(TaskEventType type, String from, String content) {
        TaskEntity e = new TaskEntity();         //database e kaydedilecek entity
        e.setType(type);        //olay türü
        e.setClientFrom(from);   //kimden geldiği
        e.setClientTo("server");   //kime gittiği
        e.setContent(content);    //içerik
        e.setTimestamp(LocalDateTime.now());    //zaman damgası eklenmesi
        e.setDirection("TCP");   //trafik yönü belirtiliyor
        e.setStatus("OK");    //başarılı olunduğu durumu belirtir

        repo.save(e);    //database e kayıt edilme işlemi
    }


}
