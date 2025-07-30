package com.ezedin.auth_service.model.dto;

import com.ezedin.auth_service.model.enums.Role;
import com.ezedin.auth_service.model.enums.SectionName;

public interface userRegistrationRequest  {
        String getName();
        String getPassword();
        String getPhone_no();
        String getUserName();
        Role getRole();
}

