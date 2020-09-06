package cnabookstore;

import cnabookstore.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    DeliveryRepository deliveryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrdered_CreateDelivery(@Payload Ordered ordered){

        if(ordered.isMe()){
            System.out.println("##### listener CreateDelivery : " + ordered.toJson());
            Delivery delivery = new Delivery();
            delivery.setOrderId(ordered.getOrderId());
            delivery.setDeliveryStatus("CreateDelivery");

            System.out.println("Ordered Info: " + ordered.toJson());
            deliveryRepository.save(delivery);
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDeliveryPrepared_Ship(@Payload DeliveryPrepared deliveryPrepared){

        if(deliveryPrepared.isMe()){
            System.out.println("##### listener Ship : " + deliveryPrepared.toJson());
            Delivery delivery = new Delivery();
            delivery.setOrderId(deliveryPrepared.getOrderId());
            delivery.setDeliveryStatus("Shipped");

            System.out.println("DeliveryPrepared Info: " + deliveryPrepared.toJson());
            deliveryRepository.save(delivery);
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderCanceled_CancelDelivery(@Payload OrderCanceled orderCanceled){

        if(orderCanceled.isMe()){
            System.out.println("##### listener CancelDelivery : " + orderCanceled.toJson());
            Delivery delivery = new Delivery();
            delivery.setOrderId(orderCanceled.getOrderId());
            delivery.setDeliveryStatus("CancelDeliver");

            System.out.println("OrderCanceled Info: " + orderCanceled.toJson());
            deliveryRepository.save(delivery);
        }
    }

}
