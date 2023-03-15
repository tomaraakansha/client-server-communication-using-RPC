package com.ds.part3.sync;

import computationServices.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.*;

public class SyncComputationServer {
    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Starting server for Synchronous: ");
        System.out.println("please choose between:");
        System.out.println("1. starting the server for Addition of 2 numbers\n2. Starting the server for Sorting an array");
        int choice = sc.nextInt();

        switch (choice){
            case 1:
                Server svAdd = ServerBuilder.forPort(2023).addService(new SyncSumComputation()).build();
                svAdd.start();
                System.out.println("Server Started successfully!");
                svAdd.awaitTermination();
                System.out.println("Server Terminated!");
                break;
            case 2:
                Server svSort = ServerBuilder.forPort(2023).addService(new SyncSortComputation()).build();
                svSort.start();
                System.out.println("Server Started successfully!");
                svSort.awaitTermination();
                System.out.println("Server Terminated!");
        }

    }
}
class SyncSumComputation extends SyncAddGrpc.SyncAddImplBase {
    @Override
    public void syncAdd(syncSumIp input, StreamObserver<syncSumOp> responseObserver) {
            int sum = input.getA()+input.getB();
            syncSumOp.Builder op = syncSumOp.newBuilder().setSum(sum);
            responseObserver.onNext(op.build());
            responseObserver.onCompleted();
    }
}
class SyncSortComputation extends syncSortGrpc.syncSortImplBase {
    @Override
    public void syncSort(syncSortIp request, StreamObserver<syncSortOp> responseObserver) {
        List<Integer> list1 = request.getArrList();
        List<Integer> list2 = new ArrayList<>();
        list2.addAll(list1);
        Collections.sort(list2);
        syncSortOp.Builder op = syncSortOp.newBuilder().addAllArr(list2);
        responseObserver.onNext(op.build());
        responseObserver.onCompleted();
    }
}

