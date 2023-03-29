package Util;

import com.batis.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MyBatis {

    public int Login(String u,String p){

        String resource = "com/batis/conf.xml";
        InputStream in = MyBatis.class.getClassLoader().getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        SqlSession session = sqlSessionFactory.openSession();

        UserDao userDao = session.getMapper(UserDao.class);
        User user = userDao.getUser(u, p);

        try{
            user.getId();
            return 1;//查询有账号
        }catch (Exception e){
            return 0;//查询为空
        }


    }

    public int Register(String u, String p){

        String resource = "com/batis/conf.xml";
        InputStream in = MyBatis.class.getClassLoader().getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        SqlSession session = sqlSessionFactory.openSession();

        UserDao userDao = session.getMapper(UserDao.class);

        //用户名查重
        boolean checkUser = userDao.checkUser(u);
        if (checkUser) {
            return 1;
        } else {
            boolean addUser = userDao.addUser(u, p);
            System.out.println(addUser);
            session.commit();
            return 0;
        }
    }
}
