package aq.common.oss;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;


public class OssAssumeRole {
    private static final ProtocolType PROTOCOL_TYPE = ProtocolType.HTTPS;
    private static final String VERSION = "2015-04-01";

    public static AssumeRoleResponse.Credentials createSTS(String accessKeyId,String accessKeySecret,
                                                           String roleArn,String roleSessionName,
                                                           String policy,String regionId,Long durationSeconds) throws Exception {
        final AssumeRoleResponse response =
                assumeRole(accessKeyId, accessKeySecret,
                        roleArn, roleSessionName,
                        policy,regionId,durationSeconds);
        AssumeRoleResponse.Credentials credentials = response.getCredentials();
        return credentials;
    }

    public static AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret,
                                                 String roleArn,
                                                 String roleSessionName, String policy,String regionId,Long durationSeconds) throws Exception {
            IClientProfile profile =
                    DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            //持续秒数 3600秒，即1小时
            request.setDurationSeconds(durationSeconds);
            request.setVersion(VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(PROTOCOL_TYPE);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);
            return client.getAcsResponse(request);
    }

    public static String getPutObjectPolicy(String backetName) {
        return String.format(
                "{\n" +
                        "    \"Version\": \"1\", \n" +
                        "    \"Statement\": [\n" +
                        "        {\n" +
                        "            \"Action\": [\n" +
                        "                \"oss:PutObject\" \n" +
                        "            ], \n" +
                        "            \"Resource\": [\n" +
                        "                \"acs:oss:*:*:%s/*\"\n" +
                        "            ], \n" +
                        "            \"Effect\": \"Allow\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}", backetName);
    }
}
