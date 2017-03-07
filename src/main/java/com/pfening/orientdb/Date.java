/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfening.orientdb;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 *
 * @author PFENIGA1
 */
public class Date {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        OffsetDateTime now = OffsetDateTime.now();
        System.out.println(now);
    }
    
}
