package com.zzb.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Float.parseFloat;
public class GsonUtil {
    public static final String urlT = "http://iot-leyview.com:8011";
    private static final String TAG = "GsonUtil";
    /*
     * 封装的GSON解析工具类，提供泛型参数
     */
    // 将Json数据解析成相应的映射对象
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }
    //将Json数组解析成相应的映射对象列表
    public static <T> List<T> parseJsonArrayWithGson(String jsonData,Class<T> type) {
        Gson gson = new Gson();
        List<T> result = gson.fromJson(jsonData, new TypeToken<List<T>>() {
        }.getType());
        return result;
    }
    //生成随机数字和字母+时间戳
    public static String getStringRandom() {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < 20; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        Date date = new Date();
        long time = date.getTime();
        //时间戳只有10位 要做处理
        String dateline = time + "";
        dateline = dateline.substring(0, 10);
        return val + dateline;
    }
    /* 对象转换成字符串
         public static String objectToString(Info info){
        	 JSONObject obj = (JSONObject) JSONObject.toJSON(info);
        	 String str = obj.toJSONString();
        	 return str;
         }*/
    // 数据处理 获取分组类容
    public static String getGroup(String group) {
        if(group.charAt(group.length()-1)== ','){
            // 去除最后一位的逗号
            group = group.substring(0, group.length() - 1);
        }
        // 将group转换成int数组
        String[] stringArr = group.split(",");
        int[] arry = new int[16];
        for (int i = 0; i < arry.length; i++) {
            arry[i] = i + 1;
            if (arry[i] == 1) {
                arry[i] = -1;
            }
            for (int j = 0; j < stringArr.length; j++) {
                String ids = stringArr[j];
                String str2 = ids.replaceAll(" ", "");
                int str3 = Integer.parseInt(str2);
                if (str3 == 1) {
                    str3 = -1;
                }
                // 设置选中的如果和16其中的数相等就把他替换成1
                if (arry[i] == str3) {
                    arry[i] = 1;
                }
            }
        }
        int[] a = getSortD_X(arry);
        for (int i = 0; i < a.length; i++) {
            if (a[i] > 1 || a[i] == -1) {
                a[i] = 0;
            }
        }
        String a1 = Arrays.toString(a);
        String a2 = a1.replace("[", "").replace("]", "").replace(" ", "").replace(",", "");
        System.out.println("二进制分组:" + a2);
        return binaryString2hexString(a2);
    }

    // 数据处理 获取回路类容
    public static String getLamp(String lamp) {
        if(lamp.charAt(lamp.length()-1)== ','){
            // 去除最后一位的逗号
            lamp = lamp.substring(0, lamp.length() - 1);
        }
        // 将group转换成int数组
        String[] stringArr = lamp.split(",");
        int[] arry = new int[8];
        for (int i = 0; i < arry.length; i++) {
            arry[i] = i + 1;
            if (arry[i] == 1) {
                arry[i] = -1;
            }
            for (int j = 0; j < stringArr.length; j++) {
                String ids = stringArr[j];
                String str2 = ids;
                int str3 = Integer.parseInt(str2);
                if (str3 == 1) {
                    str3 = -1;
                }
                // 设置选中的如果和16其中的数相等就把他替换成1
                if (arry[i] == str3) {
                    arry[i] = 1;
                }
            }
        }
        int[] a = getSortD_X(arry);
        for (int i = 0; i < a.length; i++) {
            if (a[i] > 1 || a[i] == -1) {
                a[i] = 0;
            }
        }
        String a1 = Arrays.toString(a);
        String a2 = a1.replace("[", "").replace("]", "").replace(" ", "").replace(",", "");
        System.out.println("二进制的回路:" + a2);
        return binaryString2hexString(a2);
    }

    //分割日期(起始)(结束)
    public static String[] getdates(String dates) {
        String[] stringArr = dates.split("-");
        for (int j = 0; j < stringArr.length; j++) {
            String ids = stringArr[j];
            String str2 = ids.replaceAll(" ", "");
            System.out.println("分割日期：" + str2);
        }
        int m = Integer.parseInt(stringArr[1]);
        if (m < 10) {
            stringArr[1] = "0" + stringArr[1];
        }

        int d = Integer.parseInt(stringArr[2]);
        if (d < 10) {
            stringArr[2] = "0" + stringArr[2];
        }

        System.out.println("日期：" + stringArr);
        return stringArr;
    }

    //分割小时分钟
    public static String[] getTime(String times) {
        String[] stringArr = times.split(":");

        for (int j = 0; j < stringArr.length; j++) {
            String ids = stringArr[j];
            String str2 = ids.replaceAll(" ", "");
            System.out.println(str2);
        }
        int m = Integer.parseInt(stringArr[0]);
        if (m < 10) {
            stringArr[0] = "0" + stringArr[0];
        }
        if (m == 0) {
            stringArr[1] = "00";
        }

        int d = Integer.parseInt(stringArr[1]);
        if (d < 10) {
            stringArr[1] = "0" + stringArr[1];
        }
        if (d == 0) {
            stringArr[1] = "00";
        }
        return stringArr;
    }


    // 数据对比(分组)
    public static String setStringNumb(int sNumb) {
        String str = binaryString2hexString(sNumb);// 转化为字符串
        if (str.length() < 16) {
            int conut = 16 - str.length();
            String numb = "";
            for (int i = 0; i < conut; i++) {
                numb += "0";
            }
            str = numb + str;
        }

        // 转换int数组
        int[] intArray1 = parse(str);
        // 高低位
        int[] intArray2 = getSortD_X(intArray1);
        String str1 = Arrays.toString(intArray2);
        String str2 = str1.replace("[", "").replace("]", "").replace(" ", "").replace(",", "");
        System.out.println("分组字符串相加以后：" + str2);
        // 新建一个数组用来保存arr每一位的数字
        int[] intArray = new int[str2.length()];
        for (int i1 = 0; i1 < str2.length(); i1++) {
            // 遍历str将每一位数字添加如intArray
            Character ch = str2.charAt(i1);
            intArray[i1] = Integer.parseInt(ch.toString());
        }
        int[] arry = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        for (int i = 0; i < intArray.length; i++) {
            if (intArray[i] != 1) {
                arry[i] = 0;
            }
        }

        int[] a = cleanZero(arry);
        String s = Arrays.toString(a);
        // 将数组整合
        String str3 = s.replace("[", "").replace("]", "").replace(" ", "");

        System.out.println("数据处理" + str3);
        return str3;
    }

    // 数据对比(回路)
    public static String setStringLamp(int sNumb) {
        String str = binaryString2hexString(sNumb);// 转化为字符串
        if (str.length() < 8) {
            int conut = 8 - str.length();
            String numb = "";
            for (int i = 0; i < conut; i++) {
                numb += "0";
            }
            str = numb + str;
        }

        // 转换int数组
        int[] intArray1 = parse(str);
        // 高低位
        int[] intArray2 = getSortD_X(intArray1);
        String str1 = Arrays.toString(intArray2);
        String str2 = str1.replace("[", "").replace("]", "").replace(" ", "").replace(",", "");
        System.out.println("回路字符串相加以后的：" + str2);
        int[] intArray = new int[str2.length()];// 新建一个数组用来保存arr每一位的数字
        for (int i1 = 0; i1 < str2.length(); i1++) {
            // 遍历str将每一位数字添加如intArray
            Character ch = str2.charAt(i1);
            intArray[i1] = Integer.parseInt(ch.toString());
        }
        int[] arry = {1, 2, 3, 4, 5, 6, 7, 8};
        for (int i = 0; i < intArray.length; i++) {
            if (intArray[i] != 1) {
                arry[i] = 0;
            }
        }

        int[] a = cleanZero(arry);
        String s = Arrays.toString(a);
        // 将数组整合
        String str3 = s.replace("[", "").replace("]", "").replace(" ", "");

        System.out.println("数据处理" + str3);
        return str3;
    }

    // 判断数组中是否有0
    public static int[] cleanZero(int[] oldArr) {
        // 统计0的个数,变量的++ count++ count统计的变量
        int count = 0;
        // 如果使用，必须初始化，如果没有使用，不初始化不会报错
        for (int i = 0; i < oldArr.length; i++) {
            if (oldArr[i] == 0) {
                count++;
            }
        }
        int[] newArr = new int[oldArr.length - count];
        // 动态
        int index = 0;
        // 新数组使用的索引值
        // 遍历旧的数组，把非0的数组存储到新数组中
        for (int i = 0; i < oldArr.length; i++) {
            if (oldArr[i] != 0) {
                newArr[index++] = oldArr[i];
            }
        }
        return newArr;
    }

    // 高低位转换
    public static int[] getSortD_X(int[] a) {
        // new一个新数组。
        int[] newa = new int[a.length];
        for (int i = a.length - 1; i >= 0; i--) {
            // 将源数组从最后元素开始，一次复制给新数组。
            System.arraycopy(a, i, newa, a.length - i - 1, 1);
        }
        return newa;
    }

    // 数据转换 10进制转换2进制
    public static String binaryString2hexString(int i) {
        return Integer.toBinaryString(i);
    }

    /**
     * 将二进制转换为10进制
     *
     * @param bString
     * @return
     */
    public static String binaryString2hexString(String bString) {

        return Integer.valueOf(bString, 2).toString();
    }

    /**
     * 将字符串转化为int数组
     *
     * @param str 带解析的字符串
     * @return 转化而成的int数组
     */
    public static int[] parse(String str) {
        int length = str.length();
        int[] result = new int[length];
        // 依次取得字符串中的每一个字符，并将其转化为数字，放进int数组中
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            result[i] = Character.getNumericValue(c);
        }
        return result;
    }


    // 数据处理 获取分组类容
    public static String getGroups(String group) {

        // 将group转换成int数组
        String[] stringArr = group.split(",");
        int[] arry = new int[16];
        for (int i = 0; i < arry.length; i++) {
            arry[i] = i + 1;
            if (arry[i] == 1) {
                arry[i] = -1;
            }
            for (int j = 0; j < stringArr.length; j++) {
                String ids = stringArr[j];
                String str2 = ids.replaceAll(" ", "");
                int str3 = Integer.parseInt(str2);
                if (str3 == 1) {
                    str3 = -1;
                }
                // 设置选中的如果和16其中的数相等就把他替换成1
                if (arry[i] == str3) {
                    arry[i] = 1;
                }
            }
        }
        int[] a = getSortD_X(arry);
        for (int i = 0; i < a.length; i++) {
            if (a[i] > 1 || a[i] == -1) {
                a[i] = 0;
            }
        }
        String a1 = Arrays.toString(a);
        String a2 = a1.replace("[", "").replace("]", "").replace(" ", "").replace(",", "");
        System.out.println("二进制分组:" + a2);
        return binaryString2hexString(a2);
    }

    // 数据处理 获取分组类容
    public static String getLamps(String lamp) {
        // 将group转换成int数组
        String[] stringArr = lamp.split(",");
        int[] arry = new int[8];
        for (int i = 0; i < arry.length; i++) {
            arry[i] = i + 1;
            if (arry[i] == 1) {
                arry[i] = -1;
            }
            for (int j = 0; j < stringArr.length; j++) {
                String ids = stringArr[j];
                String str2 = ids.replaceAll(" ", "");
                int str3 = Integer.parseInt(str2);
                if (str3 == 1) {
                    str3 = -1;
                }
                // 设置选中的如果和16其中的数相等就把他替换成1
                if (arry[i] == str3) {
                    arry[i] = 1;
                }
            }
        }
        int[] a = getSortD_X(arry);
        for (int i = 0; i < a.length; i++) {
            if (a[i] > 1 || a[i] == -1) {
                a[i] = 0;
            }
        }
        String a1 = Arrays.toString(a);
        String a2 = a1.replace("[", "").replace("]", "").replace(" ", "").replace(",", "");
        System.out.println("二进制的回路:" + a2);
        return binaryString2hexString(a2);
    }

    //转换字符串
    public static String getLoop(String Loop) {
        System.out.println("回路:" + Loop);
        // 将group转换成String数组
        String[] stringArr = Loop.split(",");
        String loopString = "";
        //遍历stringArr数组
        for (int i = 0; i < stringArr.length; i++) {
            if (stringArr[i].equals("0")) {
                stringArr[i] = "关";
            } else if (stringArr[i].equals("1")) {
                stringArr[i] = "开";
            }
            loopString += stringArr[i] + ",";
        }
        // 去除最后一位的逗号
        loopString = loopString.substring(0, loopString.length() - 1);

        return loopString;
    }

    //耗能(主机/分控)
    public static float getHostFkNh(String lamp) {
        // 将group转换成int数组
        String[] stringArr = lamp.split(",");
        float number = (float) 0.0;
        for (int i = 0; i < stringArr.length; i++) {
            final int result = countInnerStr(stringArr[i], ".");
            if (result >2){
                String[] names =stringArr[i].split("\\.");
                stringArr[i] = names[0];
            }
            number += parseFloat(stringArr[i]);
        }
        return number;
    }
    public static int countInnerStr(final String str, final String patternStr) {
        int count = 0;
        final Pattern r = Pattern.compile(patternStr);
        final Matcher m = r.matcher(str);
        while (m.find()) {
            count++;
        }
        return count;
    }
    //排序
    public static String bubbleSort(int[] args) {
        String numb ="";
        for (int i = 0; i < args.length - 1; i++) {
            for (int j = i + 1; j < args.length; j++) {
                if (args[i] > args[j]) {
                    int temp = args[i];
                    args[i] = args[j];
                    args[j] = temp;
                }
            }
        }
        for (int i = 0; i <args.length ; i++) {
            numb +=args[i]+",";
        }
        return numb;
    }


}
