package view;

import service.StudentManagementSystem;
import model.*;
import utils.Input;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Map;

/**
 * 菜单类，处理用户交互。
 */
public class Menu {
    private final StudentManagementSystem sms;
    private final Scanner scanner;

    public Menu() {
        this.sms = new StudentManagementSystem();
        this.scanner = new Scanner(System.in, "UTF-8");
        addInitialData();
    }

    /**
     * 运行主菜单循环。
     */
    public void run() {
        int choice;
        do {
            printMainMenu();
            choice = Input.readIntInput(this.scanner, "请输入选项 (0-7): ", 0, 7);

            switch (choice) {
                case 1: addStudent(); break;
                case 2: modifyStudent(); break;
                case 3: deleteStudent(); break;
                case 4: browseStudents(); break;
                case 5: searchStudent(); break;
                case 6: sortStudents(); break;
                case 7: sms.printStudentCounts(); break;
                case 0: System.out.println("正在退出系统..."); break;
                default: System.out.println("无效选项，请重新输入。");
            }
            pauseExecution(choice);
        } while (choice != 0);

        scanner.close();
        System.out.println("系统已退出。感谢使用！");
    }

    /**
     * 暂停等待用户按 Enter。
     */
    private void pauseExecution(int choice) {
        if (choice != 0) {
            System.out.println("\n按 Enter 键继续...");
            scanner.nextLine();
        }
    }

    /**
     * 打印主菜单。
     */
    private void printMainMenu() {
        System.out.println("\n--- 学生信息管理系统 ---");
        System.out.println("1. 增加学生信息");
        System.out.println("2. 修改学生信息");
        System.out.println("3. 删除学生信息");
        System.out.println("4. 浏览学生信息");
        System.out.println("5. 查询学生信息");
        System.out.println("6. 排序学生信息");
        System.out.println("7. 统计学生人数");
        System.out.println("0. 退出系统");
        System.out.println("------------------------");
    }

    // --- 添加学生 ---
    private void addStudent() {
        System.out.println("\n--- 添加学生信息 ---");
        try {
            int type = readStudentType();
            String id = readStudentIdForAdd();
            if (id == null) return;

            String name = readStudentName();
            int age = readStudentAge();
            String className = readClassName();
            Address address = readAddressInfo();

            Student student;
            if (type == 1) {
                String major = readMajor();
                student = new Undergraduate(id, name, age, className, address, major);
            } else {
                String supervisor = readSupervisor();
                String researchDirection = readResearchDirection();
                student = new Graduate(id, name, age, className, address, supervisor, researchDirection);
            }

            readAndSetScores(student);
            sms.addStudent(student);

        } catch (IllegalArgumentException e) {
            System.out.println("添加失败：" + e.getMessage());
        } catch (Exception e) {
             System.out.println("添加时发生未知错误: " + e.getMessage());
        }
    }
    private int readStudentType() { /* ... */ return Input.readIntInput(this.scanner, "选择学生类型 (1: 本科生, 2: 研究生): ", 1, 2); }
    private String readStudentIdForAdd() { /* ... */
        System.out.print("学号: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("错误：学号不能为空！");
            return null;
        }
        if (sms.findStudentById(id).isPresent()) {
            System.out.println("错误：学号 " + id + " 已存在！无法添加。");
            return null;
        }
        return id;
     }
    private String readStudentName() { /* ... */ System.out.print("姓名: "); return scanner.nextLine().trim(); }
    private int readStudentAge() { /* ... */ return Input.readIntInput(this.scanner, "年龄 (正整数): ", 1, 120); }
    private String readClassName() { /* ... */ System.out.print("班级: "); return scanner.nextLine().trim(); }
    private Address readAddressInfo() { /* ... */
        System.out.println("--- 输入地址信息 ---");
        System.out.print("省份: "); String province = scanner.nextLine().trim();
        System.out.print("城市: "); String city = scanner.nextLine().trim();
        System.out.print("街道: "); String street = scanner.nextLine().trim();
        System.out.print("门牌号: "); String houseNumber = scanner.nextLine().trim();
        return new Address(province, city, street, houseNumber);
     }
    private String readMajor() { /* ... */ System.out.print("专业: "); return scanner.nextLine().trim(); }
    private String readSupervisor() { /* ... */ System.out.print("导师: "); return scanner.nextLine().trim(); }
    private String readResearchDirection() { /* ... */ System.out.print("研究方向: "); return scanner.nextLine().trim(); }
    private void readAndSetScores(Student student) { /* ... */
        System.out.println("--- 输入成绩 (输入课程名和分数，输入 'done' 结束) ---");
        String course;
        double score;
        while (true) {
            System.out.print("课程名 (或输入 'done' 结束): ");
            course = scanner.nextLine().trim();
            if (course.equalsIgnoreCase("done")) break;
            if (course.isEmpty()) {
                System.out.println("课程名不能为空，请重新输入。");
                continue;
            }
            score = Input.readDoubleInput(this.scanner, "成绩 (0-100): ", 0.0, 100.0);
            student.addOrUpdateScore(course, score);
        }
     }

    // --- 修改学生 ---
    private void modifyStudent() {
        System.out.println("\n--- 修改学生信息 ---");
        System.out.print("请输入要修改的学生的学号: ");
        String id = scanner.nextLine().trim();

        Optional<Student> studentOpt = sms.findStudentById(id);
        if (!studentOpt.isPresent()) {
            System.out.println("错误：未找到学号为 " + id + " 的学生！");
            return;
        }
        Student oldStudent = studentOpt.get();
        System.out.println("找到学生，当前信息:");
        System.out.println(oldStudent.toString());
        System.out.println("--------------------");

        System.out.println("\n请输入新信息 (留空不改):");

        try {
            String newId = handleModifyStudentId(oldStudent);
            if (newId == null) return;
            String name = handleModifyName(oldStudent);
            int age = handleModifyAge(oldStudent);
            String className = handleModifyClassName(oldStudent);
            Address address = handleModifyAddress(oldStudent);
            Student tempStudentData = handleModifyTypeAndSpecifics(oldStudent, newId, name, age, className, address);
            if (tempStudentData == null) return;
            Student updatedStudent = handleModifyScores(oldStudent, tempStudentData);

            sms.modifyStudent(id, updatedStudent);

        } catch (IllegalArgumentException e) {
            System.out.println("修改失败：" + e.getMessage());
        } catch (Exception e) {
             System.out.println("修改时发生未知错误: " + e.getMessage());
        }
    }
    private String handleModifyStudentId(Student oldStudent) { /* ... */
        String oldId = oldStudent.getStudentId();
        String newId = oldId;
        System.out.print("是否修改学号? (y/n, 默认 n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
             System.out.print("新学号: ");
             newId = scanner.nextLine().trim();
             if (newId.isEmpty()) {
                 System.out.println("错误：新学号不能为空！修改失败。");
                 return null;
             }
             if (!oldId.equals(newId) && sms.findStudentById(newId).isPresent()) {
                 System.out.println("错误：新学号 " + newId + " 已被其他学生使用！修改失败。");
                 return null;
             }
        }
        return newId;
     }
    private String handleModifyName(Student oldStudent) { /* ... */
        System.out.print("新姓名 (当前: " + oldStudent.getName() + ", 留空不改): ");
        String nameInput = scanner.nextLine().trim();
        return nameInput.isEmpty() ? oldStudent.getName() : nameInput;
     }
    private int handleModifyAge(Student oldStudent) { /* ... */
        int currentAge = oldStudent.getAge();
        while (true) {
            System.out.print("新年龄 (当前: " + currentAge + ", 输入-1不改): ");
            String ageInputStr = scanner.nextLine().trim();
            if (ageInputStr.equals("-1")) {
                return currentAge;
            }
            if (ageInputStr.isEmpty()) {
                 return currentAge;
            }
            try {
                int ageInput = Integer.parseInt(ageInputStr);
                if (ageInput > 0 && ageInput <= 120) {
                    return ageInput;
                } else {
                    System.out.println("无效输入，请输入 1 到 120 之间的正整数或 -1。");
                }
            } catch (NumberFormatException e) {
                System.out.println("无效输入，请输入数字。");
            }
        }
     }
    private String handleModifyClassName(Student oldStudent) { /* ... */
        System.out.print("新班级 (当前: " + oldStudent.getClassName() + ", 留空不改): ");
        String classInput = scanner.nextLine().trim();
        return classInput.isEmpty() ? oldStudent.getClassName() : classInput;
     }
    private Address handleModifyAddress(Student oldStudent) { /* ... */
        Address address = oldStudent.getAddress();
        System.out.print("是否修改地址? (y/n, 默认 n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            address = readAddressInfo();
        }
        return address;
     }
    private Student handleModifyTypeAndSpecifics(Student oldStudent, String newId, String name, int age, String className, Address address) { /* ... */
        int currentType = (oldStudent instanceof Undergraduate) ? 1 : 2;
        int finalType = currentType;
        System.out.print("当前为" + (currentType == 1 ? "本科生" : "研究生") + "。是否修改类型? (1: 本科生, 2: 研究生, 其他输入表示不改): ");
        String typeInput = scanner.nextLine().trim();
        if (typeInput.equals("1")) finalType = 1;
        else if (typeInput.equals("2")) finalType = 2;

        Student tempStudent = null;
        if (finalType == 1) {
            String major;
            if (oldStudent instanceof Undergraduate && finalType == currentType) {
                 System.out.print("新专业 (当前: " + ((Undergraduate)oldStudent).getMajor() + ", 留空不改): ");
                 String majorInput = scanner.nextLine().trim();
                 major = majorInput.isEmpty() ? ((Undergraduate)oldStudent).getMajor() : majorInput;
            } else {
                 System.out.print("专业 (类型改变必须输入): ");
                 major = scanner.nextLine().trim();
                 if (major.isEmpty()) {
                     System.out.println("错误：改为本科生时，专业不能为空！修改失败。");
                     return null;
                 }
            }
            tempStudent = new Undergraduate(newId, name, age, className, address, major);
        } else {
            String supervisor, researchDirection;
             if (oldStudent instanceof Graduate && finalType == currentType) {
                 System.out.print("新导师 (当前: " + ((Graduate)oldStudent).getSupervisor() + ", 留空不改): ");
                 String supervisorInput = scanner.nextLine().trim();
                 supervisor = supervisorInput.isEmpty() ? ((Graduate)oldStudent).getSupervisor() : supervisorInput;
                 System.out.print("新研究方向 (当前: " + ((Graduate)oldStudent).getResearchDirection() + ", 留空不改): ");
                 String researchInput = scanner.nextLine().trim();
                 researchDirection = researchInput.isEmpty() ? ((Graduate)oldStudent).getResearchDirection() : researchInput;
            } else {
                 System.out.print("导师 (类型改变必须输入): ");
                 supervisor = scanner.nextLine().trim();
                 System.out.print("研究方向 (类型改变必须输入): ");
                 researchDirection = scanner.nextLine().trim();
                  if (supervisor.isEmpty() || researchDirection.isEmpty()) {
                     System.out.println("错误：改为研究生时，导师和研究方向不能为空！修改失败。");
                     return null;
                 }
            }
            tempStudent = new Graduate(newId, name, age, className, address, supervisor, researchDirection);
        }
        return tempStudent;
     }
    private Student handleModifyScores(Student oldStudent, Student updatedStudent) { /* ... */
        updatedStudent.getScores().putAll(oldStudent.getScores());
        System.out.print("是否修改成绩? (y/n, 默认 n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("--- 修改/添加成绩 (输入课程名和新分数, 输入 'done' 结束) ---");
            String course;
            double score;
            while (true) {
                System.out.print("课程名 (或输入 'done'): ");
                course = scanner.nextLine().trim();
                if (course.equalsIgnoreCase("done")) break;
                 if (course.isEmpty()) {
                    System.out.println("课程名不能为空，请重新输入。");
                    continue;
                }
                String prompt = updatedStudent.getScores().containsKey(course) ?
                                "修改 '" + course + "' 成绩为 (0-100): " :
                                "新增课程 '" + course + "' 成绩为 (0-100): ";
                score = Input.readDoubleInput(this.scanner, prompt, 0.0, 100.0);
                updatedStudent.addOrUpdateScore(course, score);
            }
        }
        return updatedStudent;
     }

    // --- 删除学生 ---
    private void deleteStudent() {
        System.out.println("\n--- 删除学生信息 ---");
        System.out.print("请输入要删除的学生的学号: ");
        String id = scanner.nextLine().trim();
        Optional<Student> studentOpt = sms.findStudentById(id);
        if (studentOpt.isPresent()) {
            Student studentToDelete = studentOpt.get();
            System.out.println("找到学生:");
            System.out.println(studentToDelete.toString());
            System.out.println("--------------------");
            System.out.print("确认删除该学生? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                sms.deleteStudent(id);
            } else {
                System.out.println("删除操作已取消。");
            }
        } else {
            System.out.println("错误：未找到学号为 " + id + " 的学生！");
        }
    }

    // --- 浏览学生 ---
    private void browseStudents() {
        System.out.println("\n--- 浏览学生信息 ---");
        System.out.println("1. 浏览所有学生");
        System.out.println("2. 浏览本科生");
        System.out.println("3. 浏览研究生");
        int choice = Input.readIntInput(this.scanner, "请选择浏览类型 (1-3): ", 1, 3);

        switch (choice) {
            case 1: sms.browseAllStudents(); break;
            case 2: sms.browseUndergraduates(); break;
            case 3: sms.browseGraduates(); break;
        }
    }

    // --- 查询学生 ---
    private void searchStudent() {
        System.out.println("\n--- 查询学生信息 ---");
        System.out.println("查询方式 (1: 按班级, 2: 按姓名, 3: 按学号): "); // 简化提示
        int typeChoice = Input.readIntInput(this.scanner, "请选择查询方式 (1-3): ", 1, 3);
        String criteria;
        String type;

        switch (typeChoice) {
            case 1: type = "class"; System.out.print("请输入班级关键字: "); break;
            case 2: type = "name"; System.out.print("请输入姓名关键字: "); break;
            case 3: type = "id"; System.out.print("请输入完整学号: "); break;
            default: return;
        }
        criteria = scanner.nextLine().trim();

        if (criteria.isEmpty()) {
             System.out.println("查询关键字不能为空！");
             return;
        }

        List<Student> results = sms.searchStudents(criteria, type);
        sms.displayStudentList(results, "查询结果");
    }

    // --- 排序学生 ---
    private void sortStudents() {
        System.out.println("\n--- 排序学生信息 ---");
        System.out.println("排序依据 (1: 学号, 2: 总成绩, 3: 课程成绩): "); // 简化提示
        int sortChoice = Input.readIntInput(this.scanner, "请选择排序依据 (1-3): ", 1, 3);

        String sortBy;
        switch (sortChoice) {
            case 1: sortBy = "id"; break;
            case 2: sortBy = "totalScore"; break;
            case 3:
                System.out.print("请输入课程名称: "); // 简化提示
                String courseName = scanner.nextLine().trim();
                 if (courseName.isEmpty()) {
                     System.out.println("课程名称不能为空！");
                     return;
                 }
                sortBy = "course:" + courseName;
                break;
            default: return;
        }

        System.out.print("排序方式 (1: 升序, 2: 降序): ");
        int orderChoice = Input.readIntInput(this.scanner, "请选择 (1 或 2): ", 1, 2); // 简化提示
        boolean ascending = (orderChoice == 1);

        sms.sortAndDisplayStudents(sortBy, ascending);
    }

     // --- 初始数据 ---
    private void addInitialData() {
        // ... (添加初始数据的代码保持不变) ...
        System.out.println("正在添加初始测试数据...");
        try {
            Address addr1 = new Address("广东", "深圳", "南山", "科技园路1号");
            Address addr2 = new Address("北京", "海淀", "中关村大街", "100号");
            Address addr3 = new Address("上海", "浦东", "世纪大道", "2001号");
            Address addr4 = new Address("广东", "广州", "天河", "软件路10号");

            // Updated initial data with age 24 for all students
            Undergraduate ug1 = new Undergraduate("U24001", "张三", 24, "计算机2401", addr1, "软件工程");
            ug1.addOrUpdateScore("Java", 85.5);
            ug1.addOrUpdateScore("数据结构", 90.0);
            sms.addStudent(ug1);

            Undergraduate ug2 = new Undergraduate("U24002", "李四", 24, "自动化2402", addr2, "控制理论");
            ug2.addOrUpdateScore("电路", 78.0);
            ug2.addOrUpdateScore("自控原理", 88.5);
            sms.addStudent(ug2);

            Graduate g1 = new Graduate("G24001", "王五", 24, "计算机研24", addr3, "赵教授", "人工智能");
            g1.addOrUpdateScore("机器学习", 92.0);
            g1.addOrUpdateScore("深度学习", 89.5);
            g1.addOrUpdateScore("Java", 75.0);
            sms.addStudent(g1);

            Graduate g2 = new Graduate("G24002", "马六", 24, "通信研24", addr1, "钱教授", "无线通信");
            g2.addOrUpdateScore("通信原理", 95.0);
            g2.addOrUpdateScore("信息论", 91.0);
            sms.addStudent(g2);

            Undergraduate ug3 = new Undergraduate("U24005", "陈七", 24, "软件工程2401", addr4, "软件测试");
            ug3.addOrUpdateScore("软件测试", 88.0);
            ug3.addOrUpdateScore("Java", 92.5);
            sms.addStudent(ug3);

            System.out.println("初始数据添加完毕。\n");
        } catch (IllegalArgumentException e) {
            System.err.println("错误：添加初始数据时发生错误 - " + e.getMessage());
        } catch (Exception e) {
             System.err.println("错误：添加初始数据时发生未知异常 - " + e.getMessage());
        }
    }
}
