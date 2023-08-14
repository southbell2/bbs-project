package proj.bbs.user.controller.dto;

import lombok.Getter;
import lombok.Setter;
import proj.bbs.user.service.dto.PagedUserDTO;

import java.util.List;

@Getter
@Setter
public class PagedUserResponseDTO {
    List<PagedUserDTO> pagedUserDTOList;

    public PagedUserResponseDTO(List<PagedUserDTO> pagedUserDTOList) {
        this.pagedUserDTOList = pagedUserDTOList;
    }
}
