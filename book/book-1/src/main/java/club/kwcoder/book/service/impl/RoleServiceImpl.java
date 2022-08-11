package club.kwcoder.book.service.impl;

import club.kwcoder.book.dataobject.Role;
import club.kwcoder.book.dto.ResultDTO;
import club.kwcoder.book.repository.RoleRepository;
import club.kwcoder.book.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ResultDTO<List<Role>> roleList() {
        List<Role> all = roleRepository.findAll();
        return ResultDTO.ok(
                "获取权限列表成功！",
                all
        );
    }
}
