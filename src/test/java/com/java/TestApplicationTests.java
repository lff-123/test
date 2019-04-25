package com.java;

import com.java.entity.User;
import com.java.util.HelloSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApplicationTests {

	@Autowired
	private HelloSender helloSender;

	@Test
	public void contextLoad(){
		for (int i=0;i<5;i++){
			User user=new User("admin"+i,"123456");
			//helloSender.sendDirectMsg("这是我发送的第"+i+"条Direct测试消息");
			//helloSender.sendDirectObject(user);
			//helloSender.sendFanoutMsg("这是我发送的第"+i+"条Fanout测试消息");
			//helloSender.sendTopicMsg("这是我发送的第"+i+"条Direct测试消息");
			//helloSender.sendTopicMsg1("这是我发送的第"+i+"条Direct#测试消息");
		}


		List<String> stringList=new ArrayList<>();
		for(int i=0;i<100;i++){
			stringList.add("这是第"+i+"个幸运客户");
		}
		//helloSender.sendMsg(stringList);

		//helloSender.sendDirectMsg("邮箱发送");
	}

}
