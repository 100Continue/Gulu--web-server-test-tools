package TestCase;

import junit.framework.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @Description A simple example using the testNG frame
 *
 * @author Ling Jian
 * @version 创建时间：2018-3-16 下午2:58:46
 */

public class TestSample  {

    @BeforeClass
    public static void before_test() throws Exception {

    }

    @Test
    public void test1(){

        System.out.println(1/1);
    }
    @Test
    public void test2(){

        long b = (long)(Math.random()*3);
        System.out.println(b);
        Assert.assertEquals(2, b);
        //System.out.println(1/0);

    }


}
