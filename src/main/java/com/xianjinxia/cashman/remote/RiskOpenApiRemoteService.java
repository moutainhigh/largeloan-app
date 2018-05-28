package com.xianjinxia.cashman.remote;

import com.xianjinxia.cashman.domain.RiskControlPushData;
import com.xianjinxia.cashman.enums.PlatformInterfaceEnum;
import com.xianjinxia.cashman.enums.ServiceCompanyEnum;
import com.xianjinxia.cashman.request.OpenApiRequest;
import com.xianjinxia.cashman.response.OpenApiBaseResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class RiskOpenApiRemoteService extends BaseRemoteService {

    private static final String XJX = "XJX";
    private static final String X_CLIENT_CHANNEL = "X-CLIENT-CHANNEL";
    private static final String OPEN_API_URL = "/loan-inner-gateway";

    @Override
    protected String getServiceName() {
        return "OPEN-API";
    }

    public OpenApiBaseResponse<Void> riskControlPushResponse(RiskControlPushData riskControlPushData, PlatformInterfaceEnum platformInterfaceEnum) {
        String url = super.buildUrl(OPEN_API_URL);
        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.add(X_CLIENT_CHANNEL, XJX);
        return myRestTemplate.httpPostAndHeaders(url, new OpenApiRequest(platformInterfaceEnum.getCode(), ServiceCompanyEnum.CASHMAN_APP_PUSH_RISK.getCode(), riskControlPushData), httpHeader, new ParameterizedTypeReference<OpenApiBaseResponse<Void>>() {
        });
    }

}
