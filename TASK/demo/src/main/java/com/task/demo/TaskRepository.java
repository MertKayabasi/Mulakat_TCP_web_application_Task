package com.task.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Repository interfacemiz
//database işlemleri için hibernate (orm) kullanıldı
@Repository

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
