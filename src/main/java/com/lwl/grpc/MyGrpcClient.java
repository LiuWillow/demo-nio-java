package com.lwl.grpc;

import com.lwl.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * @author lwl
 * @date 2019/3/26 15:52
 * @description
 */
public class MyGrpcClient {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8899)
                .usePlaintext(true).build();
        StudentServiceGrpc.StudentServiceBlockingStub blockingStub = StudentServiceGrpc.newBlockingStub(managedChannel);
        StudentServiceGrpc.StudentServiceStub nonBlockingStub = StudentServiceGrpc.newStub(managedChannel);

        //普通请求，返回普通内容
        MyResponse myResponse = blockingStub.getRealNameByUsername(MyRequest.newBuilder().setUsername("金额哈哈").build());
        System.out.println(myResponse.getRealname());

        System.out.println("-----------------------");

        //普通请求，返回流
        Iterator<StudentResponse> iterator = blockingStub.getStudentsByAge(StudentRequest.newBuilder().setAge(22).build());
        while (iterator.hasNext()){
            StudentResponse studentResponse = iterator.next();
            System.out.println(studentResponse.getName());
            System.out.println(studentResponse.getAge());
            System.out.println(studentResponse.getCity());
            System.out.println("****************");
        }

        System.out.println("--------------------------");
        //流请求，返回普通对象
        StreamObserver<StudentResponseList> studentResponseListStreamObserver = new StreamObserver<StudentResponseList>() {
            @Override
            public void onNext(StudentResponseList studentResponseList) {
                studentResponseList.getStudentResponseList().forEach(studentResponse -> {
                    System.out.println(studentResponse.getName());
                    System.out.println(studentResponse.getAge());
                    System.out.println(studentResponse.getCity());
                    System.out.println("******");
                });
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("completed!");
            }
        };
        //流式请求一定是异步的，因此stub也要改成异步的
        StreamObserver<StudentRequest> studentRequestStreamObserver = nonBlockingStub.getStudentsWrapperByAges(studentResponseListStreamObserver);
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(20).build());
        studentRequestStreamObserver.onCompleted();


        //流请求 返回流
        StreamObserver<StreamRequest> streamObserver = nonBlockingStub.biTalk(new StreamObserver<StreamResponse>() {
            @Override
            public void onNext(StreamResponse streamResponse) {
                System.out.println(streamResponse.getResponseInfo());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("completed");
            }
        });

        for (int i = 0; i < 10; i++) {
            streamObserver.onNext(StreamRequest.newBuilder().setRequestInfo(LocalDateTime.now().toString()).build());
            Thread.sleep(1000);
        }

        Thread.sleep(50000);
    }
}