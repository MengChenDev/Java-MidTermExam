package service;

import model.Graduate;
import model.Student;
import model.Undergraduate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 学生管理系统服务类。
 */
public class StudentManagementSystem {
    private final Map<String, Student> students;

    public StudentManagementSystem() {
        this.students = new LinkedHashMap<>();
    }

    /**
     * 添加学生。
     * @param student 学生对象
     * @return 是否成功
     */
    public boolean addStudent(Student student) {
        if (student == null) {
             System.out.println("错误：不能添加空学生对象！");
             return false;
        }
        if (students.containsKey(student.getStudentId())) {
            System.out.println("错误：学号 " + student.getStudentId() + " 已存在，添加失败！");
            return false;
        }
        students.put(student.getStudentId(), student);
        System.out.println("学生 " + student.getName() + " 添加成功！");
        return true;
    }

    /**
     * 根据学号查找学生。
     * @param studentId 学号
     * @return Optional 包装的学生对象
     */
    public Optional<Student> findStudentById(String studentId) {
        return Optional.ofNullable(students.get(studentId));
    }

    /**
     * 修改学生信息。
     * @param oldStudentId 旧学号
     * @param updatedStudent 新学生信息对象
     * @return 是否成功
     */
    public boolean modifyStudent(String oldStudentId, Student updatedStudent) {
        if (updatedStudent == null) {
             System.out.println("错误：更新的学生信息不能为空！");
             return false;
        }
        Student existingStudent = students.get(oldStudentId);
        if (existingStudent == null) {
            System.out.println("错误：未找到学号为 " + oldStudentId + " 的学生，修改失败！");
            return false;
        }
        String newStudentId = updatedStudent.getStudentId();
        if (!oldStudentId.equals(newStudentId) && students.containsKey(newStudentId)) {
            System.out.println("错误：新学号 " + newStudentId + " 已被其他学生使用，修改失败！");
            return false;
        }
        if (!oldStudentId.equals(newStudentId)) {
            students.remove(oldStudentId);
        }
        students.put(newStudentId, updatedStudent);
        System.out.println("学号 " + oldStudentId + " 的学生信息已更新！" + (oldStudentId.equals(newStudentId) ? "" : " 新学号为 " + newStudentId));
        return true;
    }

    /**
     * 根据学号删除学生。
     * @param studentId 学号
     * @return 是否成功
     */
    public boolean deleteStudent(String studentId) {
        Student removedStudent = students.remove(studentId);
        if (removedStudent != null) {
            System.out.println("学号为 " + studentId + " 的学生 (" + removedStudent.getName() + ") 已被删除。");
            return true;
        } else {
            System.out.println("错误：未找到学号为 " + studentId + " 的学生，删除失败！");
            return false;
        }
    }

    /**
     * 显示学生列表。
     * @param studentList 学生列表
     * @param title 标题
     */
    public void displayStudentList(List<Student> studentList, String title) {
        System.out.println("\n--- " + title + " (" + studentList.size() + " 条) ---");
        if (studentList.isEmpty()) {
            System.out.println("没有找到符合条件的学生。");
            return;
        }
        studentList.forEach(System.out::println);
        System.out.println("--------------------");
    }

    // --- 浏览方法 ---
    public void browseAllStudents() {
        displayStudentList(new ArrayList<>(students.values()), "所有学生信息");
    }
    public void browseUndergraduates() {
        List<Student> undergraduates = students.values().stream()
                .filter(s -> s instanceof Undergraduate)
                .collect(Collectors.toList());
        displayStudentList(undergraduates, "所有本科生信息");
    }
    public void browseGraduates() {
         List<Student> graduates = students.values().stream()
                .filter(s -> s instanceof Graduate)
                .collect(Collectors.toList());
        displayStudentList(graduates, "所有研究生信息");
    }

    /**
     * 查询学生。
     * @param criteria 查询关键字
     * @param type 查询类型 ("id", "name", "class")
     * @return 学生列表
     */
    public List<Student> searchStudents(String criteria, String type) {
        if (criteria == null || criteria.trim().isEmpty() || type == null) {
            System.out.println("警告：查询条件或类型不能为空。");
            return Collections.emptyList();
        }
        String lowerCaseCriteria = criteria.toLowerCase().trim();

        return students.values().stream()
                .filter(student -> {
                    switch (type.toLowerCase()) {
                        case "id":
                            return student.getStudentId().equalsIgnoreCase(criteria.trim());
                        case "name":
                            return student.getName().toLowerCase().contains(lowerCaseCriteria);
                        case "class":
                            return student.getClassName().toLowerCase().contains(lowerCaseCriteria);
                        default:
                            System.out.println("警告：未知的查询类型 '" + type + "'。");
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 排序并显示学生。
     * @param sortBy 排序字段
     * @param ascending 是否升序
     */
    public void sortAndDisplayStudents(String sortBy, boolean ascending) {
        if (students.isEmpty()) {
            System.out.println("系统中没有学生信息可供排序。");
            return;
        }
        List<Student> sortedList = new ArrayList<>(students.values());
        Comparator<Student> comparator = null;
        String sortFieldDescription = "";
        String lowerSortBy = sortBy.toLowerCase();

        if (lowerSortBy.equals("id")) {
            comparator = Comparator.comparing(Student::getStudentId);
            sortFieldDescription = "学号";
        } else if (lowerSortBy.equals("totalscore")) {
            comparator = Comparator.comparingDouble(Student::calculateTotalScore);
            sortFieldDescription = "总成绩";
        } else if (lowerSortBy.startsWith("course:")) {
            String courseName = sortBy.substring("course:".length()).trim();
            if (courseName.isEmpty()) {
                 System.out.println("错误：未指定课程名称进行排序。");
                 return;
            }
            boolean courseExists = students.values().stream()
                                          .anyMatch(s -> s.getScores().containsKey(courseName));
            if (!courseExists) {
                 System.out.println("错误：系统中没有关于课程 '" + courseName + "' 的成绩记录，无法排序。");
                 return;
            }
            comparator = Comparator.comparingDouble(s -> s.getScore(courseName));
            sortFieldDescription = "课程 '" + courseName + "' 成绩";
        } else {
            System.out.println("错误：无效的排序依据 '" + sortBy + "'！");
            return;
        }
        if (!ascending) {
            comparator = comparator.reversed();
        }
        try {
            sortedList.sort(comparator);
        } catch (Exception e) {
            System.out.println("排序时发生错误: " + e.getMessage());
            return;
        }

        String sortOrder = ascending ? "升序" : "降序";
        displayStudentList(sortedList, "按 " + sortFieldDescription + " " + sortOrder + " 排序后的学生信息");
    }

    /**
     * 打印学生人数统计。
     */
    public void printStudentCounts() {
        long total = students.size();
        long undergraduateCount = students.values().stream().filter(s -> s instanceof Undergraduate).count();
        long graduateCount = total - undergraduateCount;

        System.out.println("\n--- 学生人数统计 ---");
        System.out.println("总人数: " + total);
        System.out.println("本科生: " + undergraduateCount);
        System.out.println("研究生: " + graduateCount);
        System.out.println("--------------------");
    }

    /**
     * 获取所有学生列表。
     * @return 学生列表
     */
    public List<Student> getAllStudentsList() {
        return new ArrayList<>(students.values());
    }
}
