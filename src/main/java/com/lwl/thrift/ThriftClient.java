package com.lwl.thrift;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import thrift.generated.Person;
import thrift.generated.PersonService;

/**
 * @author lwl
 * @date 2019/3/26 10:59
 * @description
 */
public class ThriftClient {
    public static void main(String[] args) {
        //transport类型与服务端一致
        TTransport transport = new TFastFramedTransport(new TSocket("localhost", ThriftServer.PORT), 600);
        //protocol类型与服务端一致
        TProtocol protocol = new TCompactProtocol(transport);
        PersonService.Client client = new PersonService.Client(protocol);

        try{
            transport.open();
            Person person = client.getPersonByUsername("张三");
            System.out.println(person.getUsername());
            System.out.println(person.getAge());
            System.out.println(person.isMarried());
            System.out.println("--------------");

            Person person2 = new Person().setUsername("啦啦啦").setAge(12).setMarried(false);
            client.savePerson(person2);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            transport.close();
        }
    }
}