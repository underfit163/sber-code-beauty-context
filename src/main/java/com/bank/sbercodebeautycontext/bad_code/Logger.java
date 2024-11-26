
package com.bank.sbercodebeautycontext.bad_code;

import org.springframework.stereotype.Component;

@Component
public class Logger {

    public void log(String message) {
        System.out.println("LOG: " + message);
    }
}
