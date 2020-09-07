package cnabookstore;

import cnabookstore.config.kafka.KafkaProcessor;

import java.util.List;
import java.util.Optional;

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

            Optional<List<Delivery>> deliveryOptional = deliveryRepository.findByOrderByOrderIdAsc(deliveryPrepared.getOrderId());
            if (deliveryOptional.isPresent()) {
                List<Delivery> deliveryList = deliveryOptional.get();
                for (Delivery delivery : deliveryList) {
                    delivery.setDeliveryStatus("Shipped");
                    deliveryRepository.save(delivery);
                }
            }
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderCanceled_CancelDelivery(@Payload OrderCanceled orderCanceled){

        if(orderCanceled.isMe()){
            System.out.println("##### listener CancelDelivery : " + orderCanceled.toJson());

//            Optional<Delivery> deliveryOptional = deliveryRepository.findById(orderCanceled.getId());
//            deliveryOptional.ifPresent((delivery) ->{
//                delivery.setDeliveryStatus("CancelDeliver");
//                deliveryRepository.save(delivery);
//            });

            Optional<List<Delivery>> deliveryOptional = deliveryRepository.findByOrderByOrderIdAsc(orderCanceled.getOrderId());
            if (deliveryOptional.isPresent()) {
                List<Delivery> deliveryList = deliveryOptional.get();
                for (Delivery delivery : deliveryList) {
                    delivery.setDeliveryStatus("CancelDeliver");
                    deliveryRepository.save(delivery);
                }
            }
        }
    }

}
