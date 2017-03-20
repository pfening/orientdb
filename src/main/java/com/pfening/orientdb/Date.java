/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfening.orientdb;

import java.time.OffsetDateTime;

public class Date {

    public static void main(String[] args) {
        String now = OffsetDateTime.now().toString();
        System.out.println(now);
    }
    
}
