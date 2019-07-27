/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
//import java.util.Arrays;

import java.util.prefs.Preferences;
//import java.util.prefs.BackingStoreException;


/**
 *
 * @author szhc
 */
public class Register {
    public Register(Calibration c) {
        cal = c;
        cal.DLL_AES(key);
    }
    
    public String GetWinHDSeries(String driver) {
        String result = "";
        try {
            File file = File.createTempFile("temp0", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);
            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    +"Set colDrives = objFSO.Drives\n"
                    +"Set objDrive = colDrives.item(\"" + driver + "\")\n"
                    +"Wscript.Echo objDrive.SerialNumber";
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        }
        catch(IOException e) {
            
        }
        return result.trim();
    }
    
    public void WriteValues(String[] values) {
        if(keys.length != values.length) {
            return;
        }
        char[] series = values[1].toCharArray();
        char[] seriesTemp;
        if(series.length < 8) {
            seriesTemp = new char[8];
            for(int i = 0;i < 8;++i) {
                if(i < series.length){
                    seriesTemp[i] = series[i];
                }
                else {
                    seriesTemp[i] = '0';
                }
            }
        }
        else {
            seriesTemp = new char[series.length];
            for(int i = 0;i < series.length;++i) {
                seriesTemp[i] = series[i];
            }
        }
        
        
        char[] cipherCode = values[2].toCharArray();
        cal.DLL_Cipher(seriesTemp);
//        System.out.println(char2HexString(cipherCode));
//        cal.DLL_InvCipher(seriesTemp);
//        System.out.println(Arrays.toString(seriesTemp));
        String cipherSeries = "";
        for(int i = 0;i < seriesTemp.length;++i) {
            cipherSeries += String.valueOf(seriesTemp[i]);
        }
        
        // HKEY_LOCAL_MACHINE\Software\JavaSoft\prefs下写入注册表值
        Preferences pre = Preferences.userRoot().node("/FaroTracker");
        for(int i = 0;i < keys.length;++i) {
            if(i != 1) pre.put(keys[i], values[i]);
            else pre.put(keys[i], cipherSeries);
        }
    }
    
    public boolean ReadValues() {
        boolean ret = false;
        String cipherCode,cipherSeries;
        Preferences pre = Preferences.userRoot().node("/FaroTracker");
        if(pre == null) {
            return false;
        }
        cipherCode = pre.get("password", "");
        cipherSeries = pre.get("series", "");
        if(cipherCode == null || cipherSeries == null) {
            return false;
        }
        
        char[] series = cipherSeries.toCharArray();
        if(series.length < 8) {
            return false;
        }
        cal.DLL_InvCipher(series);
        String seriesStr = String.valueOf(series);
        String regSeriesStr = GetWinHDSeries("C:");
        
        if((seriesStr == null ? regSeriesStr == null : seriesStr.equals(regSeriesStr)) && (IsRegCodeCorrect(cipherCode))) {
            ret = true;
        }
        
        return ret;
    }
    
    public String char2HexString(char[] b) {
        String r="";
        for(int i = 0;i < b.length;++i) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if(hex.length() == 1) {
                hex = '0' + hex;
            }
            r+=hex.toUpperCase();
        }
        return r;
    }
    
    public boolean IsRegCodeCorrect(String cipherCode) {
        String randomCode = cipherCode.substring(0,8);
        cipherCode = cipherCode.substring(8);
//        System.out.println(cipherCode);
        
        char[] cipherChar = new char[8];
        for(int i = 0;i < cipherCode.length();i+=4)
        {
            String strL = String.valueOf(cipherCode.charAt(i)) + String.valueOf(cipherCode.charAt(i+1));
            String strH = String.valueOf(cipherCode.charAt(i+2)) + String.valueOf(cipherCode.charAt(i+3));
            int low = Integer.parseInt(strL, 16);
            int hight = Integer.parseInt(strH,16);
            cipherChar[i/4] = (char)(low | (hight<<8));
//            System.out.println(cipherChar[i/4]);
        }
        
        cal.DLL_InvCipher(cipherChar);
//        for(int i = 0;i < cipherChar.length;++i) {
//            System.out.println("inv:" + String.valueOf(cipherChar[i]));
//        }
        String temp = "";
        for(int i = 0;i < cipherChar.length;++i) {
            temp += String.valueOf(cipherChar[i]);
        }
//        System.out.println(randomCode);
//        System.out.println(temp);
        return (randomCode == null ? temp == null : randomCode.equals(temp));
    }
    
    private final String[] keys = {"creator","series","password"};
    private static Calibration cal;
    static char[] key = {'s','z','h','c','t','r','a','c','k','e','r','1','8','7','1','7'};
}
