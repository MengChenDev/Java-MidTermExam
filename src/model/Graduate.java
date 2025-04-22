package model;

/**
 * 研究生类。
 */
public class Graduate extends Student {
    private String supervisor;        // 导师
    private String researchDirection; // 研究方向

    /**
     * 构造函数。
     * @param studentId 学号
     * @param name 姓名
     * @param age 年龄
     * @param className 班级
     * @param address 地址
     * @param supervisor 导师
     * @param researchDirection 研究方向
     * @throws IllegalArgumentException 输入无效时抛出
     */
    public Graduate(String studentId, String name, int age, String className, Address address, String supervisor, String researchDirection) {
        super(studentId, name, age, className, address);
         if (supervisor == null || supervisor.trim().isEmpty()) {
            throw new IllegalArgumentException("导师不能为空");
        }
         if (researchDirection == null || researchDirection.trim().isEmpty()) {
            throw new IllegalArgumentException("研究方向不能为空");
        }
        this.supervisor = supervisor;
        this.researchDirection = researchDirection;
    }

    // Getters
    public String getSupervisor() { return supervisor; }
    public String getResearchDirection() { return researchDirection; }

    // Setters
    public void setSupervisor(String supervisor) {
         if (supervisor == null || supervisor.trim().isEmpty()) {
            throw new IllegalArgumentException("导师不能为空");
        }
        this.supervisor = supervisor;
    }
    public void setResearchDirection(String researchDirection) {
         if (researchDirection == null || researchDirection.trim().isEmpty()) {
            throw new IllegalArgumentException("研究方向不能为空");
        }
        this.researchDirection = researchDirection;
    }

    /**
     * 返回研究生信息的字符串表示。
     * @return 格式化字符串
     */
    @Override
    public String toString() {
        return String.format("类型: 研究生, 导师: %s, 研究方向: %s, %s",
                supervisor,
                researchDirection,
                super.toString());
    }
}
