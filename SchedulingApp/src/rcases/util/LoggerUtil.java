/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcases.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author rober
 */
public class LoggerUtil {
    
 private final static Logger LOGGER = Logger.getLogger(LoggerUtil.class.getName());
 private static FileHandler handler = null;
 
 public static void init(){
    try {
    handler = new FileHandler("SchedulingApp-Userlog.%u.%g.txt", 1024 * 1024, 10, true);
    } catch (SecurityException | IOException e) {
        e.printStackTrace();
        }
    Logger logger = Logger.getLogger("");
    handler.setFormatter(new SimpleFormatter());
    logger.addHandler(handler);
    logger.setLevel(Level.INFO);
 }
    
}
