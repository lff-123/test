package com.java.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


/**
 * @Author：刘芳芳
 * @Date：2019/4/15
 * @Description：
 */
@Configuration
public class RabbitConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    //交换机
    public static final String EXCHANGE_A = "my-mq-exchange_A";
    public static final String EXCHANGE_B = "my-mq-exchange_B";
    public static final String EXCHANGE_C = "my-mq-exchange_C";

    //队列
    public static final String QUEUE_A = "QUEUE_A";
    public static final String QUEUE_B = "QUEUE_B";
    public static final String QUEUE_C = "QUEUE_C";

    //路由键
    public static final String ROUTINGKEY_A = "spring-boot-routingKey_A";
    public static final String ROUTINGKEY_B = "spring-boot-routingKey_B";
    public static final String ROUTINGKEY_C = "spring-boot-routingKey_C";


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host,port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/vhost_m");
        connectionFactory.setPublisherConfirms(true);//发布确认
        return connectionFactory;
    }

    //不同的生产者需要对应不同的ConfirmCallback，如果rabbitTemplate设置为单例bean，则所有的rabbitTemplate实际的ConfirmCallback为最后一次申明的ConfirmCallback
    //必须是prototype类型  每次注入的时候回自动创建一个新的bean实例，默认的是单例模式，在整个应用中只能创建一个实例
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }

    /**
     * 获取队列A
     * @return
     */
    @Bean
    public Queue queueA() {
        return new Queue(QUEUE_A, true); //队列持久
    }
    @Bean
    public Queue queueB() {
        return new Queue(QUEUE_B, true); //队列持久
    }
    @Bean
    public Queue queueC() {
        return new Queue(QUEUE_C, true); //队列持久
    }


    //把交换机，队列，通过路由关键字进行绑定  DirectExchange:按照routingkey分发到指定队列
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_A);
    }

    //一个交换机可以绑定多个消息队列，也就是消息通过一个交换机，可以分发到不同的队列当中去
    @Bean
    public Binding bindingDirectA() {
        return BindingBuilder.bind(queueA()).to(directExchange()).with(RabbitConfig.ROUTINGKEY_A);
    }
    @Bean
    public Binding bindingDirectB(){
        return BindingBuilder.bind(queueB()).to(directExchange()).with(RabbitConfig.ROUTINGKEY_B);
    }


    //FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(EXCHANGE_B);
    }

    //把所有的队列都绑定到这个交换机上去
    @Bean
    Binding bindingFanoutA() {
        return BindingBuilder.bind(queueA()).to(fanoutExchange());
    }
    @Bean
    Binding bindingFanoutC() {
        return BindingBuilder.bind(queueC()).to(fanoutExchange());
    }


    //TopicExchange:多关键字匹配
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE_C);
    }

    //把所有的队列都绑定到这个交换机上去
    @Bean
    Binding bindingTopicA() {
        return BindingBuilder.bind(queueA()).to(topicExchange()).with("topic.message");
    }
    @Bean
    Binding bindingTopicC() {
        return BindingBuilder.bind(queueC()).to(topicExchange()).with("topic.#");
    }
    /**
     Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,
     Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
     Queue:消息的载体,每个消息都会被投到一个或多个队列。
     Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来.
     Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
     vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。
     Producer:消息生产者,就是投递消息的程序.
     Consumer:消息消费者,就是接受消息的程序.
     Channel:消息通道,在客户端的每个连接里,可建立多个channel.
     */
    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     HeadersExchange ：通过添加属性key-value匹配
     DirectExchange:按照routingkey分发到指定队列
     TopicExchange:多关键字匹配
     */
}
