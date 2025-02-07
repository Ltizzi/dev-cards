package com.ltizzi.dev_cards;


import com.ltizzi.dev_cards.security.utils.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@ComponentScan(basePackages = {"com.ltizzi.dev_cards"})
public class DevCardsApplication {


	public static void main(String[] args) {

		SpringApplication.run(DevCardsApplication.class, args);

	}

//	@Bean
//	CommandLineRunner run (UserRepository userRepo){
//		return args -> {
//			UserEntity user = userRepo.findById(1L).orElse(null);
//			System.out.println(user.toString());
//			if(user!= null){
//				List<TaskEntity> tasks = user.getDesignated_tasks();
//				List<TaskEntity> uniqueTasks = new ArrayList<>();
//				List<Long> ids = new ArrayList<>();
//				System.out.println("Empezando a checkear tasks");
//				for (TaskEntity task: tasks){
//					boolean alreadySaved = false;
//					for(Long id: ids){
//						if(task.getTask_id() == id ){
//							alreadySaved = true;
//						}
//						if(!alreadySaved){
//							uniqueTasks.add(task);
//							ids.add(task.getTask_id());
//							System.out.println("Añadida tasks" + task.toString());
//						}
//
//					}
//				}
//				System.out.println(uniqueTasks.toString());
//
//			}
//		};
//	}

}
