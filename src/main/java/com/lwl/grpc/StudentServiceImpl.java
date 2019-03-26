package com.lwl.grpc;

import com.lwl.proto.MyRequest;
import com.lwl.proto.MyResponse;
import io.grpc.stub.StreamObserver;

/**
 * @author lwl
 * @date 2019/3/26 15:39
 * @description
 */
public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase{
    @Override
    public void getRealNameByUsername(MyRequest request, StreamObserver<MyResponse> responseObserver) {
        System.out.println("接收到客户端信息： " + request.getUsername());
        //把对象返回客户端
        responseObserver.onNext(MyResponse.newBuilder().setRealname("lwl").build());
        responseObserver.onCompleted();
    }
}