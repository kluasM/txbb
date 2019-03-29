package com.qf.Algorithm;




public class MergeSort {
    public static void main(String[] args) throws ClassNotFoundException {
        int[] arr = {8,2,9,3,10,15,4,7,9,20,1,5,11,22,13,28,29,0,16,17,26,21,6,31,29,19,27,28,32,12};
        System.out.println("Origin arr : ");
        for (int var : arr) {
            System.out.print(var + " ");
        }
        System.out.println();
        System.out.println("Sorted arr : ");

    }
    private static void mergeSort(int[] arr, int[] temp, int left, int right){
        if(left<right){
            int mid = (left+right)/2;
            mergeSort(arr,temp,left,mid);
            mergeSort(arr,temp,mid+1,right);
            merge(arr,temp,left,mid,right);
        }

    }
    private static void merge(int[] arr, int[] temp, int left, int mid, int right){
        int i = left;
        int j = mid+1;
        int k = 0;
        while(i<=mid && j<=right){
            if(arr[i]<arr[j]){
                temp[k++] = arr[i++];
            }else{
                temp[k++] = arr[j++];
            }
        }

        while(i<=mid){
            temp[k++] = arr[i++];
        }

        while(j<=right){
            temp[k++] = arr[j++];
        }
        k=0;

        while (left<=right) {
            arr[left++] = temp[k++];
        }
    }

}
