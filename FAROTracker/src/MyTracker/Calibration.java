/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

/**
 *
 * @author szhc
 */
public class Calibration {
    static{
        try{
            System.loadLibrary("Calibration");
        }
        catch(UnsatisfiedLinkError e) {
            System.out.println(e);
        }
    }
    
    public native void DLL_CalibrationS6(final double[] points,double[] zero_cal,double[] ratio_cal,
            double[] tool_cal,double[] diff_post,double[] diff_prev,double[] param_cal,final int[] dir,
            boolean[] couple_en,double[] couple_cal,double comp,double[] base);
    
    public native boolean DLL_CheckPosePosError(final double[] points,double[] outputs,int number,
            double[] tool,double[] diff,double[] param,final int[] dir,boolean[] couple_en,double[] couple_cal,
            double[] qzero,final double[] base);
    
    public native boolean DLL_CheckLineError(final double[] points,int number,double[] error,int mode);
    
    public native boolean DLL_CalcRotationZY(double[] rotation,final double[] start,final double[] end);
    public native boolean DLL_CalcPointAfterRotationZY(double[] after,final double[] before,final double[] roation);
    public native void DLL_Joints2Base(double[] pos,final double[] Alpha,final double[] Dxa,final double[] Theta,
            final double[] Dzd,final double[] K,final double[] qzero,final double[] Tool);
    
    public native char[] DLL_Version();
    
    public native void DLL_CalibrationScara(final double[] points,double[] zero_cal,double[] ratio_cal,
            double[] tool_cal,double[] diff_post,double[] diff_prev,
            double[] param_cal,double[] couple_cal);
    public native boolean DLL_CheckPosePosErrorScara(final double[] points,double[] outputs,int number,double[] tool,double[] diff,
            double[] param,double[] couple_cal);
    
    public native void DLL_AES(char[] key);
    public native char[] DLL_Cipher(char[] input);
    public native char[] DLL_InvCipher(char[] input);
}
