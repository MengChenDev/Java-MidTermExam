package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 学生抽象类。
 */
public abstract class Student {
    protected String studentId; // 学号
    protected String name;      // 姓名
    protected int age;          // 年龄
    protected String className; // 班级
    protected Address address;  // 地址
    protected Map<String, Double> scores; // 成绩

    /**
     * 构造函数。
     * @param studentId 学号
     * @param name 姓名
     * @param age 年龄
     * @param className 班级
     * @param address 地址
     * @throws IllegalArgumentException 输入无效时抛出
     */
    public Student(String studentId, String name, int age, String className, Address address) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("学号不能为空");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("姓名不能为空");
        }
        if (age <= 0) {
            throw new IllegalArgumentException("年龄必须为正数");
        }
        if (className == null || className.trim().isEmpty()) {
            throw new IllegalArgumentException("班级不能为空");
        }
        if (address == null) {
            throw new IllegalArgumentException("地址不能为空");
        }
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.className = className;
        this.address = address;
        this.scores = new HashMap<>();
    }

    // --- Getters ---
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getClassName() { return className; }
    public Address getAddress() { return address; }
    public Map<String, Double> getScores() { return scores; }

    // --- Setters ---
    public void setStudentId(String studentId) {
         if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("学号不能为空");
        }
        this.studentId = studentId;
    }
    public void setName(String name) {
         if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("姓名不能为空");
        }
        this.name = name;
    }
    public void setAge(int age) {
         if (age <= 0) {
            throw new IllegalArgumentException("年龄必须为正数");
        }
        this.age = age;
    }
    public void setClassName(String className) {
         if (className == null || className.trim().isEmpty()) {
            throw new IllegalArgumentException("班级不能为空");
        }
        this.className = className;
    }
    public void setAddress(Address address) {
         if (address == null) {
            throw new IllegalArgumentException("地址不能为空");
        }
        this.address = address;
    }

    /**
     * 添加或更新成绩。
     * @param course 课程名
     * @param score 成绩 (0-100)
     */
    public void addOrUpdateScore(String course, double score) {
        if (course == null || course.trim().isEmpty()) {
             System.out.println("警告：课程名不能为空，成绩未添加/更新。");
             return;
        }
        if (score < 0 || score > 100) {
             System.out.println("警告：成绩 " + score + " 无效 (应在 0-100 之间)，课程 '" + course + "' 的成绩未添加/更新。");
             return;
        }
        this.scores.put(course.trim(), score);
    }

    /**
     * 计算总成绩。
     * @return 总成绩
     */
    public double calculateTotalScore() {
        return scores.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    /**
     * 获取指定课程成绩。
     * @param course 课程名
     * @return 成绩，不存在则为 0.0
     */
    public double getScore(String course) {
        return scores.getOrDefault(course, 0.0);
    }

    /**
     * 返回学生信息的字符串表示。
     * @return 格式化字符串
     */
    @Override
    public String toString() {
        String scoresString = scores.isEmpty() ? "无" :
                scores.entrySet().stream()
                      .map(entry -> entry.getKey() + ": " + String.format("%.1f", entry.getValue()))
                      .collect(Collectors.joining(", "));

        return String.format("学号: %s, 姓名: %s, 年龄: %d, 班级: %s, 地址: %s, 总分: %.2f, 成绩: {%s}",
               studentId, name, age, className, address, calculateTotalScore(), scoresString);
    }

    /**
     * 比较学生对象是否相等 (基于学号)。
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId);
    }

    /**
     * 返回哈希码 (基于学号)。
     */
    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
