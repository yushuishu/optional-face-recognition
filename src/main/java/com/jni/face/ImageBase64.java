package com.jni.face;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * 
 * @ 对图片进行base64编码解码类
 *
 */
public class ImageBase64 {
    static String  base64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    public static boolean isBase64(byte c) {
        return (Character.isDigit(c) || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '+') || (c == '/'));
    }
    /* 编码
     * DataByte
     * [in]输入的数据长度,以字节为单位
     */
    public static String encode(byte[] data, int dataByte) {
        // 编码表  
        char[] encodeTable = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
                'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
                'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
                'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
        // 返回值  
        String strEncode = "";
        byte[] tmp = new byte[4];
        int lineLength = 0;
        int index = 0;
        for (int i = 0; i < (int) (dataByte / 3); i++) {
            tmp[1] = data[index++];
            tmp[2] = data[index++];
            tmp[3] = data[index++];
            strEncode += encodeTable[tmp[1] >> 2];
            strEncode += encodeTable[((tmp[1] << 4) | (tmp[2] >> 4)) & 0x3F];
            strEncode += encodeTable[((tmp[2] << 2) | (tmp[3] >> 6)) & 0x3F];
            strEncode += encodeTable[tmp[3] & 0x3F];
            lineLength += 4;
            if ( lineLength == 76) {
                strEncode += "\r\n"; 
                lineLength = 0;
            }
        }
        // 对剩余数据进行编码  
        int mod = dataByte % 3;
        if (mod == 1) {
            tmp[1] = data[index++];
            strEncode += encodeTable[(tmp[1] & 0xFC) >> 2];
            strEncode += encodeTable[((tmp[1] & 0x03) << 4)];
            strEncode += "==";
        }
        else if (mod == 2) {
            tmp[1] = data[index++];
            tmp[2] = data[index++];
            strEncode += encodeTable[(tmp[1] & 0xFC) >> 2];
            strEncode += encodeTable[((tmp[1] & 0x03) << 4) | ((tmp[2] & 0xF0) >> 4)];
            strEncode += encodeTable[((tmp[2] & 0x0F) << 2)];
            strEncode += "=";
        }

        return strEncode;            
    }
    /* 解码
     * DataByte
     * [in]输入的数据长度,以字节为单位
     * OutByte
     * [out]输出的数据长度,以字节为单位,请不要通过返回值计算
     * 输出数据的长度
     */
    public static String decode(char[] data, int dataByte, int outByte) {
        // 解码表  
        char[] decodeTable = {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                62, // '+'  
                0, 0, 0,
                63, // '/'  
                52, 53, 54, 55, 56, 57, 58, 59, 60, 61, // '0'-'9'  
                0, 0, 0, 0, 0, 0, 0,
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
                13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, // 'A'-'Z'  
                0, 0, 0, 0, 0, 0,
                26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
                39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, // 'a'-'z'  
        };
        // 返回值  
        String strDecode = "";
        int nValue = 0;
        int i = 0;
        int index = 0;
        while (i < dataByte) {
            if (data[index] != '\r' && data[index] != '\n') {
                nValue = decodeTable[data[index++]] << 18;
                nValue += decodeTable[data[index++]] << 12;
                strDecode += (nValue & 0x00FF0000) >> 16;
                outByte++;
                if (data[index] != '=') {
                    nValue += decodeTable[data[index++]] << 6;
                    strDecode += (nValue & 0x0000FF00) >> 8;
                    outByte++;
                    if (data[index] != '=') {
                        nValue += decodeTable[data[index++]];
                        strDecode += nValue & 0x000000FF;
                        outByte++;
                    }
                }
                i += 4;
            }
            else { // 回车换行,跳过  
                index++;
                i++;
            }
        }
        return strDecode;            
    }
    /*
    * 传入图片地址进行base64编码示例
    */
    public static String file2base64(String filePath) {
        Mat mat = Imgcodecs.imread("d:/2.jpg");
        long matAddr = mat.getNativeObjAddr();
        if (mat.empty()) {
            return "";
        }
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, buffer);
        String imgBase64 = encode(mat.toString().getBytes(), mat.toString().getBytes().length);
        return imgBase64;
    }
}



