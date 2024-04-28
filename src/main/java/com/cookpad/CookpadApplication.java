package com.cookpad;

import com.cookpad.entities.*;
import com.cookpad.entities.enums.Gender;
import com.cookpad.entities.enums.RecipeType;
import com.cookpad.repositories.NutritionRepository;
import com.cookpad.repositories.RecipeRepository;
import com.cookpad.repositories.RoleRepository;
import com.cookpad.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Sets;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@SpringBootApplication
/*@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
									 DataSourceTransactionManagerAutoConfiguration.class,
									 HibernateJpaAutoConfiguration.class})*/
@ComponentScan(basePackages = {"com.cookpad"})
@EnableJpaRepositories("com.cookpad.repositories")
@MapperScan("com.cookpad.mapper")
public class CookpadApplication {

	public static void main(String[] args) {
		SpringApplication.run(CookpadApplication.class, args);
		System.out.println("Cookpad app is running...");
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return mapper;
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}



	@Bean
	CommandLineRunner commandLineRunner (UserRepository userRepository,
										 RoleRepository roleRepository,
										 RecipeRepository recipeRepository,
										 NutritionRepository nutritionRepository) {
		return args -> {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			Role user = new Role("USER");
			Role admin = new Role("ADMIN");
			roleRepository.saveAll(Arrays.asList(user, admin));

			User user1 = new User("john_doe", "john@gmail.com", Gender.MALE, encoder.encode("john123") , Collections.singleton(user), LocalDateTime.now());
			User user2 = new User("jane_smith", "jane@gmail.com", Gender.FEMALE, encoder.encode("jane123"), Sets.newHashSet(user, admin), LocalDateTime.now());
			userRepository.saveAll(Arrays.asList(user1, user2));

			Recipe recipe1 = new Recipe("Egg and tomato wraps", RecipeType.VEG, 20 , 10, 2, "https://example.com/spaghetti_carbonara.jpg", "2 wholemeal or multi-seed tortilla wraps | 4 tbsp reduced-fat soft cheese | 1-2 tbsp fresh chives or parsley (optional) | 2 hard-boiled eggs | 25g rocket leaves | 2 tomatoes | Black pepper", "Spread tortilla wraps with soft cheese and chives or parsley. | Slice eggs. Arrange egg slices and rocket on wraps. | Top with tomato slices, black pepper. | Wrap/fold or loosely roll up tortilla wraps to enclose filling. | Serve immediately.",  LocalDateTime.now(), LocalDateTime.now());
			Recipe recipe2 = new Recipe("Piri-piri chicken", RecipeType.NON_VEG, 20 , 11, 4, "https://example.com/chicken_stir_fry.jpg", "16-20oz/4 skinless, boneless chicken thigh fillets | 1 small fresh red chilli, finely chopped | 1 clove garlic, crushed (optional) | ½ tsp dried oregano | 1 tsp smoked paprika | 3 tsp olive oil | 1 tbsp freshly squeezed lemon juice | Black pepper | 1 red pepper, cut into small chunks | 1 red onion, sliced", "Slash chicken thighs | Mix chili, garlic, oregano, paprika, 2 tsp olive oil, lemon juice, and pepper | Marinate chicken for 30 mins | Preheat oven to 200ºC | Toss pepper, onion, and remaining oil | Add chicken and veggies to roasting tin | Roast for 30–40 mins, stirring halfway | Serve with bread and salad." , LocalDateTime.now(), LocalDateTime.now());
			Recipe recipe3 = new Recipe("Mutter paneer (made with tofu)", RecipeType.VEG,  5 , 20, 2, "https://example.com/chicken_stir_fry.jpg", "300g/10oz firm tofu | 2 tbsp rapeseed oil | ½ tbsp  mustard seeds | ½ tbsp cumin seeds | 1 small onion, chopped | 2 medium tomatoes, chopped | 1 tbsp  tomato purée | 1 tbsp cumin coriander powder | 1 tbsp coriander leaves, chopped | ½ tbsp red chilli powder | ½ tbsp turmeric powder | 100ml/4fl oz water | 450g/1lb frozen peas", "Cut the tofu into 2cm in cubes and set aside | In a warm pan, add the oil, mustard, and cumin seeds | When the seeds begin to pop, add the onions and cook until soft | Add the tomatoes, tomato purée, and all the spices, and cook for 5 minutes | Add the tofu pieces and the 100ml/4fl oz water | Bring to the boil and then add the peas | Reduce the heat and simmer for 10 minutes | Serve warm.",LocalDateTime.now(), LocalDateTime.now());
			recipeRepository.saveAll(Arrays.asList(recipe1, recipe2, recipe3));

			Nutrition nutrition1 = new Nutrition(900.0, 54.0, 40.0, 48.0, 4.0);
			Nutrition nutrition2 = new Nutrition(910.0, 156.0, 57.0, 7.5, 5.0);
			Nutrition nutrition3 = new Nutrition(319.0, 10.0,  20.0, 20.0, 8.0);

			nutrition1.setRecipe(recipe1);
			nutrition2.setRecipe(recipe2);
			nutrition3.setRecipe(recipe3);
			nutritionRepository.saveAll(Arrays.asList(nutrition1, nutrition2, nutrition3));

		};
	}
}
