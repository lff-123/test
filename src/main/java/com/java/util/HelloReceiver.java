package com.java.util;

import com.java.config.RabbitConfig;
import com.java.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author：刘芳芳
 * @Date：2019/4/15
 * @Description：接收者
 */
@Component
public class HelloReceiver  {

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.QUEUE_A)
    public void processA(String content){
        logger.info("接收器one接收处理列队A当中的消息:"+content);
        if(content.equals("邮箱发送")){
            this.sendMail();
        }
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.QUEUE_A)
    public void processA1(String content){
        logger.info("接收器two接收处理列队A当中的消息:"+content);
        if(content.equals("邮箱发送")){
            this.sendMail();
        }
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.QUEUE_A)
    public void processA2(String content){
        logger.info("接收器three接收处理列队A当中的消息:"+content);
        if(content.equals("邮箱发送")){
            this.sendMail();
        }
    }


    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.QUEUE_B)
    public void processB(User user){
        logger.info("接收器one接收处理列队B当中的消息:"+user);
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.QUEUE_B)
    public void processB1(User user){
        logger.info("接收器two接收处理列队B当中的消息:"+user);
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.QUEUE_B)
    public void processB2(User user){
        logger.info("接收器three接收处理列队B当中的消息:"+user);
    }


    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.QUEUE_C)
    public void processC(String context){
        logger.info("接收器one接收处理列队C当中的消息:"+context);
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.QUEUE_C)
    public void processC1(String context){
        logger.info("接收器two接收处理列队C当中的消息:"+context);
    }

    //发送邮箱
    public void sendMail(){
        MailOperation operation = new MailOperation();
        String res = null;
        try {
            res = operation.sendMail("ljxh_123@163.com", "shljxhdsqm123", "smtp.163.com", "ljxh_123@163.com", "confident_liu@163.com",
                    "测试", "测试发送消息",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
