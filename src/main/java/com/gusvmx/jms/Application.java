package com.gusvmx.jms;

import java.util.Random;

import javax.jms.ConnectionFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

import com.gus.protobuf.model.MyModelProtos.Person;

@SpringBootApplication
@EnableJms
public class Application {

    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public MessageConverter protobufJmsMessageConverter() {
    	// Serializa el contenido del mensaje a un BytesMessage.
        return new ProtobufJmsMessageConverter();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        // Envia un mensaje de un POJO - La plantilla reutiliza el convertidor de mensajes.
        Random random = new Random();
        while (true) {
	        System.out.println("Enviando una persona de datos aleatorios.");
	        Person person = Person.newBuilder()
	        		.setId(1)
	        		.setName("Gus")
	        		.setEmail("gusvmx@" + random.nextInt() + ".com")
	        		.setLastName("Vargas")
	        		.build();
	        jmsTemplate.convertAndSend("mailbox", person);
	        System.out.println("Nombre:" + person.getName() + " " + person.getLastName() + " " + person.getEmail());
	        
	        try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

}


