package com.udemy.kafka.processamentovendas.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.udemy.kafka.processamentovendas.deserializer.VendaDeserializer;
import com.udemy.kafka.processsamentovendas.model.Venda;

public class ProcessadorVendas {

    public static void main(String[] args) throws InterruptedException {

	Properties properties = new Properties();
	properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
	properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, VendaDeserializer.class.getName());
	properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
	properties.put(ConsumerConfig.GROUP_ID_CONFIG, "grupo-processamento");
	properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
	properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10");

	try (KafkaConsumer<String, Venda> consumer = new KafkaConsumer<>(properties)) {
	    consumer.subscribe(Arrays.asList("venda-ingressos"));

	    while (true) {

		ConsumerRecords<String, Venda> vendas = consumer.poll(Duration.ofMillis(200));

		vendas.forEach(v -> {
		    Venda venda = v.value();
		    venda.setStatus(new Random().nextBoolean() ? "APROVADA" : "REPROVADA");
		    try {
			Thread.sleep(500);
			System.out.println(venda);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }

		});
	    }
	}
    }
}