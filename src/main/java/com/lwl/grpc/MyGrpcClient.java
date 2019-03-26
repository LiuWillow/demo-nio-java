package com.lwl.grpc;

import com.lwl.proto.MyRequest;
import com.lwl.proto.MyResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * @author lwl
 * @date 2019/3/26 15:52
 * @description
 */
public class MyGrpcClient {
    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8899)
                .usePlaintext(true).build();
        StudentServiceGrpc.StudentServiceBlockingStub blockingStub = StudentServiceGrpc.newBlockingStub(managedChannel);
        MyResponse myResponse = blockingStub.getRealNameByUsername(MyRequest.newBuilder().setUsername("金额哈哈").build());
        System.out.println(myResponse.getRealname());
    }
}