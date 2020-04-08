package com.bfxy.springboot.conusmer;

import java.util.Map;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;

/**
 * @Description:消费者
 * @author: 耿程程 
 * @date: 2020-01-21 11:12:39
 */
@Component
public class RabbitReceiver {

	/**
	 * @Description:消息接收
	 * @author: 耿程程 
	 * @date: 2020-01-21 11:10:56
	 * @param message 消息实体
	 * @param channel 通信管道
	 * @throws Exception 异常
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "queue-1",durable="true"),
			exchange = @Exchange(value = "exchange-1", 
				durable="true", 
				type= "topic", 
				ignoreDeclarationExceptions = "true"),
				key = "springboot.*"
			)
	)
	@RabbitHandler
	public void onMessage(@SuppressWarnings("rawtypes") Message message, Channel channel) throws Exception {
		// 打印信息
		System.err.println("--------------------------------------");
		System.err.println("消费端Payload: " + message.getPayload());
		//手工ACK
		Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
		//                   传输标识            是否批量
		channel.basicAck(deliveryTag, false);
	}
	
	
	/**
	 * 	spring.rabbitmq.listener.order.queue.name=queue-2
		spring.rabbitmq.listener.order.queue.durable=true
		spring.rabbitmq.listener.order.exchange.name=exchange-1
		spring.rabbitmq.listener.order.exchange.durable=true
		spring.rabbitmq.listener.order.exchange.type=topic
		spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions=true
		spring.rabbitmq.listener.order.key=springboot.*
	 * @param order
	 * @param channel
	 * @param headers
	 * @throws Exception
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}", 
			durable="${spring.rabbitmq.listener.order.queue.durable}"),
			exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}", 
			durable="${spring.rabbitmq.listener.order.exchange.durable}", 
			type= "${spring.rabbitmq.listener.order.exchange.type}", 
			ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),
			key = "${spring.rabbitmq.listener.order.key}"
			)
	)
	@RabbitHandler  // @Payload 指定消息实体,这里指定了一个实体类对象,相当于Message的Body
	public void onOrderMessage(@Payload com.bfxy.springboot.entity.Order order, 
			Channel channel, 
			// 下面要使用到headers,@Headers可以进行拆分Message中的headers
			@Headers Map<String, Object> headers) throws Exception {
		System.err.println("--------------------------------------");
		System.err.println("消费端order: " + order.getId());
		Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
		//手工ACK
		channel.basicAck(deliveryTag, false);
	}
	
	
}
