package com.lwl.thrift;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import thrift.generated.PersonService;

/**
 * @author lwl
 * @date 2019/3/26 10:49
 * @description
 */
public class ThriftServer {
    public static final int PORT = 8899;
    public static void main(String[] args) throws Exception {
        TNonblockingServerSocket serverSocket = new TNonblockingServerSocket(PORT);
        THsHaServer.Args arg = new THsHaServer.Args(serverSocket).minWorkerThreads(2).maxWorkerThreads(4);
        //定义处理器
        PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());
        //协议层，二进制压缩协议，定义消息的格式
        //TBinaryProtocol 二进制格式
        //TCompactProtocol 二进制基础上压缩
        //TJSONProtocol JSON格式
        //TSimpleJSONProtocol JSON只写，很少使用
        arg.protocolFactory(new TCompactProtocol.Factory());
        //传输层，数据传输方式
        //TSocket 阻塞式socket，效率很低
        //TFramedTransport，以frame为单位，将数据切分为一个个fram，非阻塞
        //TFileTransport 文件形式
        //TMemoryTransport
        arg.transportFactory(new TFramedTransport.Factory());
        arg.processorFactory(new TProcessorFactory(processor));

        //TSimpleServer 简单的单线程服务模型，用于测试
        //TThreadPoolServer 多线程阻塞式IO
        //TNoblockingServer 多线程非阻塞  搭配TFramedTransport
        //THsHaServer 半同步半异步
        TServer server = new THsHaServer(arg);
        System.out.println("Thrift Server Started!");
        //死循环
        server.serve();
    }
}