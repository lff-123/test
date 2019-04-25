package com.java.util;

import com.java.config.RabbitConfig;
import com.java.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author：刘芳芳
 * @Date：2019/4/15
 * @Description：发送者 RabbitTemplate.ConfirmCallback,消息确认监听器
 */
@Component
public class HelloSender implements RabbitTemplate.ConfirmCallback {

    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    //由于rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，所以不能自动注入
    private AmqpTemplate rabbitTemplate;

    /**
     * 构造方法注入rabbitTemplate
     */
    @Autowired
    public HelloSender(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate=rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);//rabbitTemplate如果为单例的话，那回调就是最后设置的内容
    }

    //发送字符串 DirectExchange 按照routingkey分发到指定队列
    public void sendDirectMsg(String context){
        CorrelationData correlationId=new CorrelationData(UUID.randomUUID().toString());
        //把消息放入ROUTINGKEY_A对应的队列当中去，对应的是队列A
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A, RabbitConfig.ROUTINGKEY_A, context);
    }

    //发送对象 DirectExchange 按照routingkey分发到指定队列
    public void sendDirectObject(User user){
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A, RabbitConfig.ROUTINGKEY_B, user);
    }

    //发送字符串  FanoutExchange: 将消息分发到所有的绑定队列
    public void sendFanoutMsg(String context){
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_B,"",context);
    }

    //发送字符串  TopicExchange:多关键字匹配1
    public void sendTopicMsg(String context){
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_C,"topic.message",context);
    }
    //发送字符串  TopicExchange:多关键字匹配2
    public void sendTopicMsg1(String context){
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_C,"topic.messages",context);
    }

    //限时抢购实现
    public void sendMsg(List<String> stringList){
        List<String> stringList1=new ArrayList<>();
        //只有十个名额
        for(int i=0;i<10;i++){
            stringList1.add(stringList.get(i));
        }
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A, RabbitConfig.ROUTINGKEY_A, stringList1);
    }

    @Override
    public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String s) {
        /*logger.info(" 回调id:" + correlationData);*/
        if (ack) {
            logger.info("消息成功消费");
        } else {
            logger.info("消息消费失败:" + s);
        }
    }

}

