
import java.util.Properties

import org.apache.kafka.clients.producer._

object ProducerExample extends App {

   val  props = new Properties()
   val bootStrapServers = "localhost:9092"
   props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrapServers)
   props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer")
   props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer")

   //props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
   //props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

   val producer = new KafkaProducer[String, String](props)

   val TOPIC="test"

   for(i<- 1 to 30){
     val record = new ProducerRecord(TOPIC, "key", s"hello Sree and joe value  is : $i")
     producer.send(record)
     Thread.sleep(1000)
   }

   val record = new ProducerRecord(TOPIC, "key", "the end "+new java.util.Date)
   producer.send(record)

   producer.close()
 }

