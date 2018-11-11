//package com.gloiot.chatsdk.utlis;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 作者：Ljy on 2017/7/13.
// * 功能：我的——我的资料
// */
//
//
//public class EmojiParse {
//    public String parseEmoji(String input) {
//        if (input == null || input.length() <= 0) {
//            return "";
//        }
//        StringBuilder result = new StringBuilder();
//        int[] codePoints = toCodePointArray(input);
//        List<Integer> key = null;
//        for (int i = 0; i < codePoints.length; i++) {
//            key = new ArrayList<Integer>();
//            if (i + 1 < codePoints.length) {
//                key.add(codePoints[i]);
//                key.add(codePoints[i + 1]);
//                if (convertMap.containsKey(key)) {
//                    String value = convertMap.get(key);
//                    if (value != null) {
//                        result.append("[e]" + value + "[/e]");
//                    }
//                    i++;
//                    continue;
//                }
//            }
//            key.clear();
//            key.add(codePoints[i]);
//            if (convertMap.containsKey(key)) {
//                String value = convertMap.get(key);
//                if (value != null) {
//                    result.append("[e]" + value + "[/e]");
//                }
//                continue;
//            }
//            result.append(Character.toChars(codePoints[i]));
//        }
//        return result.toString();
//    }
//}
