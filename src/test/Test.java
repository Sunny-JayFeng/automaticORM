package test;

import service.JX3Service;

import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        JX3Service service = new JX3Service();
        ArrayList list = service.select();
        System.out.println(list);

        System.out.println(service.select("WHERE user_name = \"111\""));
    }
}
