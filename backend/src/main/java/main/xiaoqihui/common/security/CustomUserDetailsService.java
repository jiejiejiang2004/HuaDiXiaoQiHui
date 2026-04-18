package main.xiaoqihui.common.security;

import main.xiaoqihui.recruit.RecruitmentMapper;
import main.xiaoqihui.recruit.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final RecruitmentMapper recruitmentMapper;

    public CustomUserDetailsService(RecruitmentMapper recruitmentMapper) {
        this.recruitmentMapper = recruitmentMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = recruitmentMapper.findUserByMobile(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new LoginUser(
            user.getUserId(),
            user.getMobile(),
            user.getPassword(),
            user.getUserType(),
            user.getRealName(),
            user.getStatus()
        );
    }
}
