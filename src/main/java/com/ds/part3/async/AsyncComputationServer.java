package com.ds.part3.async;

import computationServices.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class AsyncComputationServer {

    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Starting server for Aynchronous calls: ");
        System.out.println("please choose between:");
        System.out.println("1. starting the server for Addition of 2 numbers\n2. Starting the server for Sorting an array");
        int choice = sc.nextInt();

        switch (choice){
            case 1:
                Server svAdd = ServerBuilder.forPort(2024).addService(new AsyncSumComputation()).build();
                svAdd.start();
                System.out.println("Server Started successfully!");
                svAdd.awaitTermination();
                System.out.println("Server Terminated!");
                break;
            case 2:
                Server svSort = ServerBuilder.forPort(2025).addService(new AsyncSortComputation()).build();
                svSort.start();
                System.out.println("Server Started successfully!");
                svSort.awaitTermination();
                System.out.println("Server Terminated!");
        }

    }

}
class AsyncSumComputation extends addGrpc.addImplBase{
    @Override
    public StreamObserver<AsyncSumIp> asyncAdd(final StreamObserver<AsyncSumOp> responseObserver) {
        return new StreamObserver<AsyncSumIp>() {
            @Override
            public void onNext(AsyncSumIp value) {
                //req-response : providing an async reactive module
                // grpc by default is non-blocking. so nothing much required.
                System.out.println("Received a new request ID: "+value.getSumReqId());
                int sum = value.getA()+value.getB();
                responseObserver.onNext(AsyncSumOp.newBuilder().setMsg("Server sending ack!"+value.getSumReqId()).build());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                responseObserver.onNext(AsyncSumOp.newBuilder().setSum(sum).build());
            }
            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
//                 responseObserver.onNext(sumOp.getDefaultInstance());
                responseObserver.onCompleted();
                System.out.println("Request completed!");
            }
        };
    }
}

class AsyncSortComputation extends sortGrpc.sortImplBase{
    @Override
    public StreamObserver<AsyncSortIp> asyncSort(final StreamObserver<AsyncSortOp> responseObserver) {
        return new StreamObserver<AsyncSortIp>() {
            @Override
            public void onNext(AsyncSortIp value) {
                System.out.println("Received a new request ID: "+value.getSortReqId());
                List<Integer> list2 = new ArrayList<>();
                list2.addAll(value.getArrList());
                Collections.sort(list2);
                responseObserver.onNext(AsyncSortOp.newBuilder().setMsg("Server sending ack!"+value.getSortReqId()).build());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                responseObserver.onNext(AsyncSortOp.newBuilder().addAllArr(list2).build());
            }
            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
//                responseObserver.onNext(sortOp.getDefaultInstance());
                responseObserver.onCompleted();
                System.out.println("Request completed!");
            }
        };
    }
}



