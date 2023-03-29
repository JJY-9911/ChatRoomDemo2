package Util;

import com.batis.User;
import org.apache.ibatis.annotations.Param;

//     <select id="getUser"
//             resultType="com.batis.User">
//             select *
//             from USER
//             where
//             USERNAME=#{userName} and PASSWORD=#{passWord} limit 1
//     </select>

public interface UserDao {
    //方法名和语句映射名一样
    User getUser(@Param("userName") String u, @Param("passWord") String p);
    boolean addUser(@Param("userName") String u, @Param("passWord") String p);
    boolean checkUser(@Param("userName") String u);
}
