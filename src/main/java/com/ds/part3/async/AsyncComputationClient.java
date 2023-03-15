package com.ds.part3.async;

import computationServices.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class AsyncComputationClient {
   private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("Part3 for Distributed system project 1:");
        //bockingstub -> for sync calls betwee`n server and client. The client will wait for response.
        //newstub -> for creating an async stub.

        System.out.println("Starting process for Asychronous computation:");
        System.out.println("Choose between: \n1. Sort an array\n2. Add 2 numbers");
        int usrIp = sc.nextInt();

        switch (usrIp) {
            case 1:
                asyncSort();
                break;
            case 2:
                asyncAdd();
                break;
            default:
                System.out.println("please enter valid input!");
        }

    }
    private static void asyncSort(){
        ManagedChannel ch ;
        ch = ManagedChannelBuilder.forAddress("localhost",2025).usePlaintext().build();
        sortGrpc.sortStub stb = sortGrpc.newStub(ch);
        final StreamObserver<AsyncSortOp> obv = new StreamObserver<AsyncSortOp>() {
            @Override
            public void onNext(AsyncSortOp value) {
                System.out.println(value.getMsg());
                System.out.println("Enter unsorted array: ");
                if(!value.getArrList().isEmpty())
                System.out.println("sorted array received from server: "+value.getArrList());
            }
            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }
            @Override
            public void onCompleted() {
                System.out.println("Request completed!");
            }
        };

        StreamObserver<AsyncSortIp> IpObv = stb.asyncSort(obv);

        System.out.println("Enter unsorted array: ");
        for(int i = 0;i<5;i++){
            List<Integer> inputList = new ArrayList<>();
            Scanner sc = new Scanner(System.in);
            while(sc.hasNextInt()){
                if(!sc.nextLine().equals("end")){
                    inputList.add(sc.nextInt());
                }
            }
            AsyncSortIp sortIn = AsyncSortIp.newBuilder().addAllArr(inputList).setSortReqId(UUID.randomUUID().toString()).build();
            IpObv.onNext(sortIn);
        }
        IpObv.onCompleted();
    }
    private static void asyncAdd() {

        ManagedChannel ch;
        ch = ManagedChannelBuilder.forAddress("localhost",2024).usePlaintext().build();
        //creating a new future stub as it helps in non-blocking comm.
        addGrpc.addStub stb = addGrpc.newStub(ch);

        final StreamObserver<AsyncSumOp> obv = new StreamObserver<AsyncSumOp>() {
            @Override
            public void onNext(AsyncSumOp value) {
                System.out.println(value.getMsg());
                System.out.println("Enter 2 elements:");
                if(value.getSum()!=0)
                System.out.println("Sum received from server: "+value.getSum());

            }
            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("Request completed!");
            }
        };

        StreamObserver<AsyncSumIp> IpObv = stb.asyncAdd(obv);

        int a,b;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 2 elements:");
        for(int i = 0;i<5;i++){
            a = sc.nextInt();
            b = sc.nextInt();
            AsyncSumIp addIp = AsyncSumIp.newBuilder().setA(a).setB(b).setSumReqId(UUID.randomUUID().toString()).build();
            IpObv.onNext(addIp);
        }
        IpObv.onCompleted();

    }

}
