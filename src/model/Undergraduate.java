package model;

/**
 * 本科生类。
 */
public class Undergraduate extends Student {
    private String major; // 专业

    /**
     * 构造函数。
     * @param studentId 学号
     * @param name 姓名
     * @param age 年龄
     * @param className 班级
     * @param address 地址
     * @param major 专业
     * @throws IllegalArgumentException 输入无效时抛出
     */
    public Undergraduate(String studentId, String name, int age, String className, Address address, String major) {
        super(studentId, name, age, className, address);
         if (major == null || major.trim().isEmpty()) {
            throw new IllegalArgumentException("专业不能为空");
        }
        this.major = major;
    }

    // Getter
    public String getMajor() { return major; }

    // Setter
    public void setMajor(String major) {
         if (major == null || major.trim().isEmpty()) {
            throw new IllegalArgumentException("专业不能为空");
        }
        this.major = major;
    }

    /**
     * 返回本科生信息的字符串表示。
     * @return 格式化字符串
     */
    @Override
    public String toString() {
        return String.format("类型: 本科生, 专业: %s, %s",
                major,
                super.toString());
    }
}
