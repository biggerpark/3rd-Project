package com.green.jobdone.business;

import com.green.jobdone.category.DetailTypeRepository;
import com.green.jobdone.common.MyFileUtils;
import com.green.jobdone.common.model.Domain;
import com.green.jobdone.config.security.AuthenticationFacade;
import com.green.jobdone.user.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BusinessServiceParentTest {
    @InjectMocks protected BusinessService businessService;
    @Mock protected  BusinessMapper businessMapper;
    @Mock protected  MyFileUtils myFileUtils;
    @Mock protected  AuthenticationFacade authenticationFacade; //인증받은 유저가 이용 할 수 있게.
    @Mock protected  BusinessRepository businessRepository;
    @Mock protected  BusinessPicRepository businessPicRepository;
    @Mock protected Domain domain;
    @Mock protected UserRepository userRepository;
    @Mock protected DetailTypeRepository detailTypeRepository;

    protected final long SIGNED_USER_ID = 2L;


}
