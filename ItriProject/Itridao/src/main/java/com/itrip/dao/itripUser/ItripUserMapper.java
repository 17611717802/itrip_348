package com.itrip.dao.itripUser;
import com.itrip.pojo.ItripUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ItripUserMapper {

	public int jh(@Param(value = "ph") String ph)throws Exception;

	public ItripUser getlogin(@Param(value = "name") String name,@Param(value = "ps") String ps)throws Exception;

	public ItripUser getItripUserById(@Param(value = "id") Long id)throws Exception;

	public List<ItripUser>	getItripUserListByMap(Map<String, Object> param)throws Exception;

	public Integer getItripUserCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItripUser(ItripUser itripUser)throws Exception;

	public Integer updateItripUser(ItripUser itripUser)throws Exception;

	public Integer deleteItripUserById(@Param(value = "id") Long id)throws Exception;

}
