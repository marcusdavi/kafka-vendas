package com.udemy.kafka.vendaingressos.service;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.udemy.kafka.vendaingressos.model.Venda;
import com.udemy.kafka.vendaingressos.serializer.VendaSerializer;

public class GeradorVendas {

    private static Random rand = new Random();
    private static Long operacao = 0L;
    private static BigDecimal VALOR_INGRESSO = BigDecimal.valueOf(500);

    public static void main(String[] args) throws InterruptedException {

	Properties properties = new Properties();
	properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
	properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
	properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VendaSerializer.class.getName());

	try (KafkaProducer<String, Venda> producer = new KafkaProducer<>(properties)) {

	    while (true) {
		Venda venda = geraVenda();
		ProducerRecord<String, Venda> record = new ProducerRecord<String, Venda>("venda-ingressos", venda);
		producer.send(record);
		Thread.sleep(200);
	    }
	}
    }

    private static Venda geraVenda() {
	long cliente = rand.nextLong();
	int qtdIngressos = rand.nextInt(9) + 1;

	return new Venda(operacao, cliente, qtdIngressos, VALOR_INGRESSO.multiply(BigDecimal.valueOf(qtdIngressos)));

    }
}