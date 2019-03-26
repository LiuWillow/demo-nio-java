package com.lwl.grpc;

import com.lwl.proto.*;
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
        responseObserver.onNext(MyResponse.newBuilder().setRealname("lwl").build());
        responseObserver.onCompleted();
    }

    /**
     * 所谓的流其实就是迭代器
     * @param request
     * @param responseObserver
     */
    @Override
    public void getStudentsByAge(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        System.out.println("接收到客户端信息： " + request.getAge());
        responseObserver.onNext(StudentResponse.newBuilder().setName("王八").setAge(34).setCity("杭州").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("张三").setAge(24).setCity("北京").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("里斯").setAge(64).setCity("成都").build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<StudentRequest> getStudentsWrapperByAges(StreamObserver<StudentResponseList> responseObserver) {
        return new StreamObserver<StudentRequest>() {
            @Override
            public void onNext(StudentRequest studentRequest) {
                System.out.println("onNext: " + studentRequest.getAge());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                StudentResponse studentResponse1 = StudentResponse.newBuilder().setName("张三").setAge(12)
                        .setCity("杭州").build();
                StudentResponse studentResponse2 = StudentResponse.newBuilder().setName("阿道夫").setAge(12)
                        .setCity("北京").build();
                StudentResponse studentResponse3 = StudentResponse.newBuilder().setName("士大夫").setAge(12)
                        .setCity("天津").build();
                StudentResponseList list = StudentResponseList.newBuilder().addStudentResponse(studentResponse1)
                        .addStudentResponse(studentResponse2)
                        .addStudentResponse(studentResponse3).build();
                responseObserver.onNext(list);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<StreamRequest> biTalk(StreamObserver<StreamResponse> responseObserver) {
        return new StreamObserver<StreamRequest>() {
            @Override
            public void onNext(StreamRequest streamRequest) {
                System.out.println(streamRequest.getRequestInfo());
                responseObserver.onNext(StreamResponse.newBuilder().setResponseInfo("sddfsdf").build());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}