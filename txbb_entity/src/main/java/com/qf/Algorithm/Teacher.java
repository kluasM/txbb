package com.qf.Algorithm;

class Person{
    public Person(){
        System.out.println("this is a Person");
    }
}

public class Teacher extends Person{
    private String name="tom";
    public Teacher(){
        System.out.println("this is a teacher");

    }

    public static void main(String[] args) {
        Teacher teacher=new Teacher();
        //System.out.println(this.name);
    }
}
