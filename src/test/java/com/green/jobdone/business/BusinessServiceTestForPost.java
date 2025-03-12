package com.green.jobdone.business;

import com.green.jobdone.business.model.BusinessContentsPostReq;
import com.green.jobdone.business.model.BusinessPostSignUpReq;
import com.green.jobdone.business.model.get.BusinessGetRes;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;

public class BusinessServiceTestForPost extends BusinessServiceParentTest{

    @Test
    public void test() {
        given(authenticationFacade.getSignedUserId())
                .willReturn(SIGNED_USER_ID);

        BusinessPostSignUpReq req = new BusinessPostSignUpReq();


    }
}
