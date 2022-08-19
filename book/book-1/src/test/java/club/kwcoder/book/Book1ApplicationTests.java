package club.kwcoder.book;

import club.kwcoder.book.dataobject.*;
import club.kwcoder.book.repository.*;
import club.kwcoder.book.service.UserService;
import club.kwcoder.book.util.RedisUtils;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@SpringBootTest
class Book1ApplicationTests {


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookClassifyRepository bookClassifyRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private ButtonRepository buttonRepository;

    @Autowired
    private RoleButtonRepository roleButtonRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void initBook() throws IOException {
        // 书名,作者,出版社,关键词,摘要,中国图书分类号,出版年月

        bookRepository.deleteAll();

        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("/Users/zhinushannan/code/weather/book/data/book.csv"))));
        String str = "";
        while ((str = reader.readLine()) != null) {
            if (str.startsWith("书名")) {
                continue;
            }
            String[] line = str.split(",");

            Book build = Book.builder()
                    .uuid(UUID.randomUUID().toString())
                    .name(line[0])
                    .author(line[1])
                    .publish(line[2])
                    .publishYear(Integer.parseInt(line[6].split("\\.")[0]))
                    .publishMonth(Integer.parseInt(line[6].split("\\.")[1]))
                    .keyWord(line[3])
                    .abstractOfBook(line[4])
                    .classifyId(line[5])
                    .total(5)
                    .remain(5)
                    .build();

            bookRepository.save(build);

        }


    }


    @Test
    void initClassify() {
        bookClassifyRepository.deleteAll();

        Map<String, String> classify = new HashMap<>();
        classify.put("A", "马克思主义、列宁主义、毛泽东思想、邓小平理论");
        classify.put("B", "哲学、宗教");
        classify.put("C", "社会科学总论");
        classify.put("D", "政治、法律");
        classify.put("E", "军事");
        classify.put("F", "经济");
        classify.put("G", "文化、科学、教育、体育");
        classify.put("H", "语言、文字");
        classify.put("I", "文学");
        classify.put("J", "艺术");
        classify.put("K", "历史、地理");
        classify.put("N", "自然科学总论");
        classify.put("O", "数理科学和化学");
        classify.put("P", "天文学、地球科学");
        classify.put("Q", "生物科学");
        classify.put("R", "医药、卫生");
        classify.put("S", "农业科学");
        classify.put("T", "工业技术");
        classify.put("U", "交通运输");
        classify.put("V", "航空、航天");
        classify.put("X", "环境科学、安全科学");
        classify.put("Z", "综合性图书");

        classify.forEach((key, value) -> bookClassifyRepository.save(new BookClassify(key, value)));
    }

    @Test
    void initMenu() {
        menuRepository.deleteAll();

        menuRepository.save(Menu.builder().index("/dashboard").title("系统首页").icon("el-icon-lx-home").sort(0).build());

        menuRepository.save(Menu.builder().index("/book").title("图书管理").icon("el-icon-lx-read").sort(1).build());
        menuRepository.save(Menu.builder().index("/book/list").title("查看图书").parentIndex("/book").sort(0).build());
        menuRepository.save(Menu.builder().index("/book/save").title("添加图书").parentIndex("/book").sort(1).build());

        menuRepository.save(Menu.builder().index("/user").title("用户管理").icon("el-icon-lx-people").sort(2).build());
        menuRepository.save(Menu.builder().index("/user/list").title("查看用户").parentIndex("/user").sort(0).build());
        menuRepository.save(Menu.builder().index("/user/save").title("添加用户").parentIndex("/user").sort(1).build());

        menuRepository.save(Menu.builder().index("/borrow").title("借阅管理").icon("el-icon-lx-vipcard").sort(3).build());
        menuRepository.save(Menu.builder().index("/borrow/return").title("归还图书").parentIndex("/borrow").sort(0).build());
        menuRepository.save(Menu.builder().index("/borrow/logs").title("借阅日志").parentIndex("/borrow").sort(1).build());
    }

    @Test
    void initRole() {
        roleRepository.deleteAll();

        roleRepository.save(Role.builder().name("ADMIN").remark("超级管理员").build());
        roleRepository.save(Role.builder().name("USER").remark("普通用户").build());
    }

    @Test
    void initRoleMenu() {
        roleMenuRepository.deleteAll();

        roleMenuRepository.save(RoleMenu.builder()._id(UUID.randomUUID().toString()).roleName("ADMIN").menuIndex("/dashboard").build());
        roleMenuRepository.save(RoleMenu.builder()._id(UUID.randomUUID().toString()).roleName("ADMIN").menuIndex("/book").build());
        roleMenuRepository.save(RoleMenu.builder()._id(UUID.randomUUID().toString()).roleName("ADMIN").menuIndex("/book/list").build());
        roleMenuRepository.save(RoleMenu.builder()._id(UUID.randomUUID().toString()).roleName("ADMIN").menuIndex("/book/save").build());
        roleMenuRepository.save(RoleMenu.builder()._id(UUID.randomUUID().toString()).roleName("ADMIN").menuIndex("/user").build());
        roleMenuRepository.save(RoleMenu.builder()._id(UUID.randomUUID().toString()).roleName("ADMIN").menuIndex("/user/list").build());
        roleMenuRepository.save(RoleMenu.builder()._id(UUID.randomUUID().toString()).roleName("ADMIN").menuIndex("/user/save").build());

        roleMenuRepository.save(RoleMenu.builder()._id(UUID.randomUUID().toString()).roleName("USER").menuIndex("/dashboard").build());
        roleMenuRepository.save(RoleMenu.builder()._id(UUID.randomUUID().toString()).roleName("USER").menuIndex("/book").build());
        roleMenuRepository.save(RoleMenu.builder()._id(UUID.randomUUID().toString()).roleName("USER").menuIndex("/book/list").build());
        roleMenuRepository.save(RoleMenu.builder()._id(UUID.randomUUID().toString()).roleName("USER").menuIndex("/borrow").build());
        roleMenuRepository.save(RoleMenu.builder()._id(UUID.randomUUID().toString()).roleName("USER").menuIndex("/borrow/return").build());
        roleMenuRepository.save(RoleMenu.builder()._id(UUID.randomUUID().toString()).roleName("USER").menuIndex("/borrow/logs").build());
    }

    @Test
    void initUserRole() {
        userRoleRepository.deleteAll();

        userRoleRepository.save(UserRole.builder()._id(UUID.randomUUID().toString()).email("admin@kwcoder.club").roleName("ADMIN").build());
        userRoleRepository.save(UserRole.builder()._id(UUID.randomUUID().toString()).email("user@kwcoder.club").roleName("USER").build());
    }

    @Test
    void initButton() {
        buttonRepository.deleteAll();

        buttonRepository.save(Button.builder().id(1).button("book-list-borrow").build());
        buttonRepository.save(Button.builder().id(2).button("book-list-delete").build());
        buttonRepository.save(Button.builder().id(3).button("book-list-edit").build());

        buttonRepository.save(Button.builder().id(4).button("borrow-logs-return").build());
        buttonRepository.save(Button.builder().id(5).button("borrow-return-return").build());

        buttonRepository.save(Button.builder().id(6).button("user-list-delete").build());
        buttonRepository.save(Button.builder().id(7).button("user-list-edit").build());
    }

    @Test
    void initButtonRole() {
        roleButtonRepository.deleteAll();

        roleButtonRepository.save(RoleButton.builder()._id(UUID.randomUUID().toString()).roleName("ADMIN").buttonId("book-list-delete").build());
        roleButtonRepository.save(RoleButton.builder()._id(UUID.randomUUID().toString()).roleName("ADMIN").buttonId("book-list-edit").build());
        roleButtonRepository.save(RoleButton.builder()._id(UUID.randomUUID().toString()).roleName("ADMIN").buttonId("user-list-delete").build());
        roleButtonRepository.save(RoleButton.builder()._id(UUID.randomUUID().toString()).roleName("ADMIN").buttonId("user-list-edit").build());

        roleButtonRepository.save(RoleButton.builder()._id(UUID.randomUUID().toString()).roleName("USER").buttonId("book-list-borrow").build());
        roleButtonRepository.save(RoleButton.builder()._id(UUID.randomUUID().toString()).roleName("USER").buttonId("borrow-logs-return").build());
        roleButtonRepository.save(RoleButton.builder()._id(UUID.randomUUID().toString()).roleName("USER").buttonId("borrow-return-return").build());
    }

    @Test
    void initUser() {
        userRepository.deleteAll();
        userRepository.save(User.builder().name("admin@kwcoder.club").email("admin@kwcoder.club").password(passwordEncoder.encode("123456")).remain(5).register(new Date()).lastLogin(new Date()).build());
        userRepository.save(User.builder().name("user@kwcoder.club").email("user@kwcoder.club").password(passwordEncoder.encode("123456")).remain(5).register(new Date()).lastLogin(new Date()).build());
    }

    @Test
    void test() {
        List<String> list = new ArrayList<>();
        list.add("123");
        list.add("1234234");

        System.out.println(list.toString().substring(1, list.toString().length() - 1));

    }

}
