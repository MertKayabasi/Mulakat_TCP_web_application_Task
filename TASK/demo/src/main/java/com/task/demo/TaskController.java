package com.task.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api/tcp")
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {

    private final TaskService taskService;
    private final TcpServer tcpServer;           //private değişkenlerimiz

    public TaskController(TaskService taskService, TcpServer tcpServer) {   //constructor
        this.taskService = taskService;
        this.tcpServer = tcpServer;
    }

    // TCP ye mesaj gönderir
    @PostMapping("/send")
    public String send(@RequestBody String msg) {
        return taskService.sendToTcp(msg);
    }

    // Tüm mesajları databaseden getirir
    @GetMapping("/messages")
    public List<TaskEntity> messages() {
        return taskService.getAll();
    }

    // TCP server ı durdur
    @PostMapping("/stop")
    public String stop() {
        tcpServer.stop();  //tcp server durduruluyor
        return "TCP Server durduruldu.";
    }


    @PostMapping("/files")
    public String uploadFile(@RequestParam("myFile") MultipartFile file) {    //dosya upload edilmesini sağlayan metod

        if (file.isEmpty()) {       //dosya var mı yok mu kontrolü
            return "Dosya boş!";
        }

        tcpServer.sendFileToTcpServer(file);  //dosya tcp server a yollanıyor

        return "Dosya gönderildi: " + file.getOriginalFilename();
    }

}
