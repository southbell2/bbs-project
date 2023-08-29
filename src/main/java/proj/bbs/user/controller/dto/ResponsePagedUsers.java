package proj.bbs.user.controller.dto;

import lombok.Getter;
import lombok.Setter;
import proj.bbs.user.service.dto.PagedUserDTO;

import java.util.List;

@Getter
@Setter
public class ResponsePagedUsers {
    List<PagedUserDTO> pagedUserDTOList;

    public ResponsePagedUsers(List<PagedUserDTO> pagedUserDTOList) {
        this.pagedUserDTOList = pagedUserDTOList;
    }
}
