package club.kwcoder.book.service;

import club.kwcoder.book.dataobject.Role;
import club.kwcoder.book.dto.ResultDTO;

import java.util.List;

public interface RoleService {

    ResultDTO<List<Role>> roleList();

}
