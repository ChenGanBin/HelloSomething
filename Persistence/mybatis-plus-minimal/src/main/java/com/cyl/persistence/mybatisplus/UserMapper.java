package com.cyl.persistence.mybatisplus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cyl
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
