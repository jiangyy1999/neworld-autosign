package com.tbs.jyy;


import com.tbs.jyy.tool.AutoSignTool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NeworldAutosignApplicationTests {
    @Autowired
    private AutoSignTool autoSignTool;


    @Test
    public void test1() {
        System.out.println(autoSignTool);
    }
}
