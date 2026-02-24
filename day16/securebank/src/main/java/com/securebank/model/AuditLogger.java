package com.securebank.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {
    private String logFile;
    private boolean detailedLogging;

    public AuditLogger(){
        System.out.println("AuditLogger created");
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public void setDetailedLogging(boolean detailedLogging) {
        this.detailedLogging = detailedLogging;
    }

    public void log(String event, String details){
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println(" [AUDIT: "+logFile+"] "+timeStamp+" | "+event);
        if (detailedLogging) System.out.println(" DETAILS: "+details);
    }
}
