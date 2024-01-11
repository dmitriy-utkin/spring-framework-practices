package ru.example.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

////	TODO: пользователь, у которого есть только ROLE_USER, может получить только информацию о себе -> UserController.findById
////	TODO: пользователь, у которого есть только ROLE_USER, имеет право на обновление только информации о себе -> UserController.update
////	TODO: пользователь, у которого есть только ROLE_USER, не может удалять профили других пользователей -> UserController.delete

	//TODO: продумать механизм создания новых категорий, чтобы закрыть эту возможность от USER

	//TODO: Обновлять новость может только пользователь, который её создал -> NewsController.update
	//TODO: пользователь, у которого есть только ROLE_USER, имеет право на удаление только той новости, которую создал сам -> NewsController.delete

	//TODO: Обновлять комментарий может только пользователь, который его создал -> CommentsController.update
	//TODO: Пользователь USER может удалить только своей комментарий -> CommentsController.update
}
