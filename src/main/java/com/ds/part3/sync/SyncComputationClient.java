package com.ds.part3.sync;
import computationServices.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;


public class SyncComputationClient {
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Starting process for Sychronous computation:");
        System.out.println("Choose between: \n1. Sort an array\n2. Add 2 numbers");
        int usrIp = sc.nextInt();
        switch (usrIp) {
            case 1:
                syncSort();
                break;
            case 2:
                syncAdd();
                break;
            default:
                System.out.println("please enter valid input!");
        }
    }
    private static void syncSort() {
        ManagedChannel ch ;
        ch = ManagedChannelBuilder.forAddress("localhost",2023).usePlaintext().build();
        syncSortGrpc.syncSortBlockingStub stub = syncSortGrpc.newBlockingStub(ch);
        System.out.println("enter the number of elements in the array:\n");
        List<Integer> inputList = new ArrayList<>();
        System.out.println("enter the elements:");
        while(sc.hasNextInt()){
            if(!sc.nextLine().equals("end")){
                inputList.add(sc.nextInt());
            }
        }
        syncSortIp srtIp = syncSortIp.newBuilder().addAllArr(inputList).build();
        syncSortOp srtOp = stub.syncSort(srtIp);
        System.out.println("sorted array:"+srtOp.getArrList());
        srtOp = stub.syncSort(srtIp);
        System.out.println("sorted array:"+srtOp.getArrList());
    }

    private static void syncAdd() {
        ManagedChannel ch ;
        ch = ManagedChannelBuilder.forAddress("localhost",2023).usePlaintext().build();
        //creating a new blocking stub as it will help in sync access of service.

        SyncAddGrpc.SyncAddBlockingStub stub = SyncAddGrpc.newBlockingStub(ch);
        //creating input object
        System.out.println("enter the two numbers to be added:\n");
        int a = sc.nextInt();
        int b = sc.nextInt();
        syncSumIp addIp = syncSumIp.newBuilder().setA(a).setB(b).setSumReqId(UUID.randomUUID().toString()).build();
        System.out.println("processing request 1 with ID: "+addIp.getSumReqId());
        syncSumOp sumOp = stub.syncAdd(addIp);
        System.out.println("Sum is: "+sumOp.getSum());
        syncSumOp sumOp1 = stub.syncAdd(addIp);
        System.out.println(sumOp1.getSum());
        System.out.println("Sum for request 1: "+sumOp1.getSum());

        //creating second input
        System.out.println("enter 2 numbers for request 2:");
        a = sc.nextInt();
        b = sc.nextInt();

        addIp = syncSumIp.newBuilder().setA(a).setB(b).setSumReqId(UUID.randomUUID().toString()).build();
        System.out.println("processing request 2 with ID: "+addIp.getSumReqId());
        sumOp1 = stub.syncAdd(addIp);
        System.out.println("Sum is: "+sumOp1.getSum());
    }

}