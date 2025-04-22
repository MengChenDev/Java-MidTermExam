package utils;

import java.util.Scanner;

/**
 * 输入工具类，提供安全的输入读取方法
 */
public class Input {

    /**
     * 读取整数输入并处理异常，带重试提示。
     * @param scanner Scanner 对象
     * @param retryPrompt 重试时的提示信息
     * @return 读取到的整数
     */
    public static int readIntInput(Scanner scanner, String retryPrompt) {
        while (true) {
            try {
                String line = scanner.nextLine();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print(retryPrompt + " "); // 使用传入的提示信息
            }
        }
    }

    /**
     * 读取整数输入，带范围检查。
     * @param scanner Scanner 对象
     * @param retryPrompt 重试时的提示信息
     * @param min 允许的最小值
     * @param max 允许的最大值
     * @return 在范围内的整数
     */
    public static int readIntInput(Scanner scanner, String retryPrompt, int min, int max) {
        while (true) {
            try {
                String line = scanner.nextLine();
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                } else {
                     System.out.print("输入超出范围 (" + min + "-" + max + ")，请重新输入: ");
                }
            } catch (NumberFormatException e) {
                System.out.print(retryPrompt + " ");
            }
        }
    }

    /**
     * 读取整数输入，允许特定值（如 -1 表示不修改）。
     * @param scanner Scanner 对象
     * @param retryPrompt 重试时的提示信息
     * @param allowedValue 允许的特定值（通常用于表示不修改等特殊情况）
     * @return 读取到的正整数或允许的特定值
     */
    public static int readIntInputWithAllowedValue(Scanner scanner, String retryPrompt, int allowedValue) {
        while (true) {
            try {
                String line = scanner.nextLine();
                int value = Integer.parseInt(line);
                 if (value > 0 || value == allowedValue) { // 允许正数或特定值
                    return value;
                 } else {
                     // 修正提示信息，更清晰地说明允许的值
                     System.out.print("无效输入，请输入正整数或 " + allowedValue + ": ");
                 }
            } catch (NumberFormatException e) {
                System.out.print(retryPrompt + " ");
            }
        }
    }

    /**
     * 读取浮点数输入并处理异常，带重试提示。
     * @param scanner Scanner 对象
     * @param retryPrompt 重试时的提示信息
     * @return 读取到的浮点数
     */
    public static double readDoubleInput(Scanner scanner, String retryPrompt) {
         while (true) {
            try {
                String line = scanner.nextLine();
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.print(retryPrompt + " ");
            }
        }
    }

    /**
     * 读取浮点数输入，带范围检查。
     * @param scanner Scanner 对象
     * @param retryPrompt 重试时的提示信息
     * @param min 允许的最小值
     * @param max 允许的最大值
     * @return 在范围内的浮点数
     */
    public static double readDoubleInput(Scanner scanner, String retryPrompt, double min, double max) {
         while (true) {
            try {
                String line = scanner.nextLine();
                double value = Double.parseDouble(line);
                 if (value >= min && value <= max) {
                    return value;
                } else {
                     System.out.print("输入超出范围 (" + String.format("%.1f", min) + "-" + String.format("%.1f", max) + ")，请重新输入: ");
                }
            } catch (NumberFormatException e) {
                System.out.print(retryPrompt + " ");
            }
        }
    }
}
